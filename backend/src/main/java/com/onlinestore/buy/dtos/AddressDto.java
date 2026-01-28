package com.onlinestore.buy.dtos;

import lombok.Data;

@Data
public class AddressDto {
    private Long id;
    private String phoneNumber;
    private String street;
    private String city;
    private String state;
    private String country;
    private String addressType;
}
