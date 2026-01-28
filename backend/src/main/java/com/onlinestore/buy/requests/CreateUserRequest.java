package com.onlinestore.buy.requests;

import com.onlinestore.buy.entities.Address;
import lombok.Data;

import java.util.List;

@Data
public class CreateUserRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private List<Address> addresses;
}
