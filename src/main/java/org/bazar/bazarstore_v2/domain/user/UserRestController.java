package org.bazar.bazarstore_v2.domain.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.bazar.bazarstore_v2.common.util.RestUtil.buildResponse;
import static org.bazar.bazarstore_v2.common.util.ValidationUtil.throwIfArgumentIsNull;

@Tag(name = "User API", description = "API for managing users")
@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {
    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create a new user", description = "Registers a new user in the system.")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    @PostMapping
    public ResponseEntity<Map<String, Object>> postUser(@RequestBody UserRequestDto userDto) {
        throwIfArgumentIsNull(userDto);
        return buildResponse(userService.createUser(userDto), HttpStatus.CREATED, "User has been created successfully.");
    }
}
