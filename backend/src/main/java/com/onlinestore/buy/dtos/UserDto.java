package com.onlinestore.buy.dtos;

import com.onlinestore.buy.entities.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private List<OrderDto> orders;
    private CartDto cart;
    private List<String> roles;
    private List<AddressDto> addresses;
}
