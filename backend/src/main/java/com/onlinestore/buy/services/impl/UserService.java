package com.onlinestore.buy.services.impl;

import com.onlinestore.buy.dtos.UserDto;
import com.onlinestore.buy.entities.Role;
import com.onlinestore.buy.entities.User;
import com.onlinestore.buy.repositories.AddressRepository;
import com.onlinestore.buy.repositories.RoleRepository;
import com.onlinestore.buy.repositories.UserRepository;
import com.onlinestore.buy.requests.ChangePasswordRequest;
import com.onlinestore.buy.requests.CreateUserRequest;
import com.onlinestore.buy.requests.UpdateUserRequest;
import com.onlinestore.buy.services.IUserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final RoleRepository roleRepository;
//    private  final CartService cartService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(CreateUserRequest createUserRequest) {
        Role userRole = Optional.ofNullable(roleRepository.findByName("ROLE_USER"))
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        return Optional.of(createUserRequest)
                .filter(user -> !userRepository.existsByEmail(createUserRequest.getEmail()))
                .map(request -> {
                    User user = new User();
                    user.setFirstname(createUserRequest.getFirstname());
                    user.setLastname(createUserRequest.getLastname());
                    user.setEmail(createUserRequest.getEmail());
                    user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
                    user.setRoles(Set.of(userRole));
                    User savedUser = userRepository.save(user);
                    Optional.ofNullable(createUserRequest.getAddresses())
                            .ifPresent(addresses -> {
                                addresses.forEach(address -> {
                                    address.setUser(savedUser);
                                    addressRepository.save(address);
                                });
                            });
//                    cartService.initializeNewCartForUser(savedUser);
                    return savedUser;
                }).orElseThrow(() -> new EntityExistsException("User with email " + createUserRequest.getEmail() + " already exists"));
    }

    @Override
    public User updateUser(UpdateUserRequest updateUserRequest, Long id) {
        return userRepository.findById(id).map(user -> {
            user.setFirstname(updateUserRequest.getFirstname());
            user.setLastname(updateUserRequest.getLastname());
            user.setEmail(updateUserRequest.getEmail());
            user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
            return userRepository.save(user);
        }).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.findById(id).ifPresentOrElse(userRepository::delete, () -> {
            throw new EntityNotFoundException("User not found");
        });
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = Optional.ofNullable(userRepository.findByEmail(changePasswordRequest.getEmail()))
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
    }

    public UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return Optional.ofNullable(userRepository.findByEmail(email)).orElseThrow(() -> new EntityNotFoundException("Login required."));
    }
}
