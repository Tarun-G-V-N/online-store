package com.onlinestore.buy.controllers;

import com.onlinestore.buy.entities.User;
import com.onlinestore.buy.requests.ChangePasswordRequest;
import com.onlinestore.buy.requests.CreateUserRequest;
import com.onlinestore.buy.requests.UpdateUserRequest;
import com.onlinestore.buy.responses.APIResponse;
import com.onlinestore.buy.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.version}/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse> createUser(@RequestBody CreateUserRequest createUserRequest) {
        User user = userService.createUser(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse("Created user successfully", userService.convertToDto(user)));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<APIResponse> getUser(@PathVariable("id") Long id) {
        User user = userService.getUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse("User fetched successfully", userService.convertToDto(user)));
    }

    @PutMapping("/user/{id}/update")
    public ResponseEntity<APIResponse> updateUser(@PathVariable("id") Long id, @RequestBody UpdateUserRequest updateUserRequest) {
        User user = userService.updateUser(updateUserRequest, id);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse("User updated successfully", userService.convertToDto(user)));
    }

    @DeleteMapping("/user/{id}/delete")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
    }

    @PostMapping("/user/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            userService.changePassword(changePasswordRequest);
            return ResponseEntity.status(HttpStatus.OK).body(new APIResponse("Password updated successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
