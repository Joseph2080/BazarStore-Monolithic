package org.bazar.bazarstore_v2.domain.user;


public interface UserService {
    String createUser(UserRequestDto userDto);
    void deleteUser(String username);
    boolean doesUserExistsById(String userId);
    void deleteAllUsers();
    UserResponseDto findUserById(String userId);
}
