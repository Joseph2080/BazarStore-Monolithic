package org.bazar.bazarstore_v2.domain.user.integration;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AWSCognitoIdentityProviderException;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.amazonaws.services.cognitoidp.model.AdminDeleteUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminEnableUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminSetUserPasswordRequest;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.InvalidPasswordException;
import com.amazonaws.services.cognitoidp.model.ListUsersRequest;
import com.amazonaws.services.cognitoidp.model.ListUsersResult;
import com.amazonaws.services.cognitoidp.model.MessageActionType;
import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import com.amazonaws.services.cognitoidp.model.UserType;
import org.bazar.bazarstore_v2.common.exception.InvalidFieldException;
import org.bazar.bazarstore_v2.common.util.ServiceConstants;
import org.bazar.bazarstore_v2.domain.user.UserRequestDto;
import org.bazar.bazarstore_v2.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CognitoUserService implements UserService {

    private final AWSCognitoIdentityProvider cognitoClient;

    @Value("${aws.cognito.userPoolId}")
    private String userPoolId;
    final String COGNITO_REQUEST_ERROR = "error while processing request to Cognito";

    @Autowired
    public CognitoUserService(AWSCognitoIdentityProvider cognitoClient) {
        this.cognitoClient = cognitoClient;
    }

    @Override
    public String createUser(UserRequestDto userDto) {
        try {
            String email = userDto.getEmail();
            String password = userDto.getPassword();
            AttributeType emailAttribute = createAttribute("email", email);
            AttributeType phoneAttribute = createAttribute("phone_number", userDto.getPhoneNumber());
            AdminCreateUserRequest createUserRequest = new AdminCreateUserRequest()
                    .withUserPoolId(userPoolId)
                    .withUsername(email)
                    .withUserAttributes(emailAttribute, phoneAttribute)
                    .withTemporaryPassword(password)
                    .withMessageAction(MessageActionType.SUPPRESS);
            AdminCreateUserResult result = cognitoClient.adminCreateUser(createUserRequest);
            String userId = result.getUser().getUsername();
            if (userId != null) {
                enableUser(email);
                setPassword(email, password, true);
            }
            return userId;
        } catch (InvalidPasswordException ex) {
            throw new InvalidFieldException("password does not conform with the password policy.", ex);
        } catch (AWSCognitoIdentityProviderException ex) {
            throw new InvalidFieldException(COGNITO_REQUEST_ERROR, ex);
        }
    }

    private AttributeType createAttribute(String name, String value) {
        return new AttributeType().withName(name).withValue(value);
    }

    private void enableUser(String username) {
        try {
            AdminEnableUserRequest enableUserRequest = new AdminEnableUserRequest()
                    .withUserPoolId(userPoolId)
                    .withUsername(username);
            cognitoClient.adminEnableUser(enableUserRequest);
        } catch (Exception ex) {
            throw new RuntimeException(COGNITO_REQUEST_ERROR, ex);
        }
    }

    private void setPassword(String username, String password, boolean isPermanentPassword) {
        AdminSetUserPasswordRequest passwordRequest = new AdminSetUserPasswordRequest()
                .withUserPoolId(userPoolId)
                .withUsername(username)
                .withPassword(password)
                .withPermanent(isPermanentPassword);
        try {
            cognitoClient.adminSetUserPassword(passwordRequest);
        } catch (Exception ex) {
            throw new RuntimeException(COGNITO_REQUEST_ERROR, ex);
        }
    }

    @Override
    public void deleteUser(String username) {
        try {
            AdminDeleteUserRequest deleteUserRequest = new AdminDeleteUserRequest()
                    .withUserPoolId(userPoolId)
                    .withUsername(username);
            cognitoClient.adminDeleteUser(deleteUserRequest);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(ServiceConstants.INVALID_USER_ID);
        } catch (Exception ex) {
            throw new RuntimeException(COGNITO_REQUEST_ERROR, ex);
        }
    }

    @Override
    public void deleteAllUsers() {
        try {
            ListUsersRequest listUsersRequest = new ListUsersRequest().withUserPoolId(userPoolId);
            ListUsersResult listUsersResult = getListUserResults(listUsersRequest);
            do {
                for (UserType user : listUsersResult.getUsers()) {
                    String username = user.getUsername();
                    deleteUser(username);
                }
                listUsersRequest.setPaginationToken(listUsersResult.getPaginationToken());
            } while (listUsersResult.getPaginationToken() != null); // Continue until all users are deleted
        } catch (Exception ex) {
            throw new RuntimeException(COGNITO_REQUEST_ERROR, ex);
        }
    }

    private ListUsersResult getListUserResults(ListUsersRequest listUsersRequest) {
        return cognitoClient.listUsers(listUsersRequest);
    }

    @Override
    public boolean doesUserExistsById(String userId) {
        AdminGetUserRequest request = new AdminGetUserRequest()
                .withUserPoolId(userPoolId)
                .withUsername(userId);
        try {
            cognitoClient.adminGetUser(request);
            return true;
        } catch (UserNotFoundException e) {
            return false;
        }
    }
}
