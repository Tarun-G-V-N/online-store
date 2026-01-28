package com.onlinestore.buy.services;

import com.onlinestore.buy.dtos.UserDto;
import com.onlinestore.buy.entities.User;
import com.onlinestore.buy.requests.ChangePasswordRequest;
import com.onlinestore.buy.requests.CreateUserRequest;
import com.onlinestore.buy.requests.UpdateUserRequest;

public interface IUserService {
    User createUser(CreateUserRequest createUserRequest);
    User updateUser(UpdateUserRequest updateUserRequest, Long id);
    User getUser(Long id);
    void deleteUser(Long id);
    User getAuthenticatedUser();
    void changePassword(ChangePasswordRequest changePasswordRequest);
    UserDto convertToDto(User user);
}
