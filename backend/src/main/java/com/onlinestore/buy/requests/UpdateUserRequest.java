package com.onlinestore.buy.requests;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
}
