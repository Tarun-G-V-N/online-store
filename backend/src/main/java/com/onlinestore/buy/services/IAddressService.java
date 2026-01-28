package com.onlinestore.buy.services;

import com.onlinestore.buy.dtos.AddressDto;
import com.onlinestore.buy.entities.Address;

import java.util.List;

public interface IAddressService {
    List<Address> createAddressList(List<Address> addresses, Long userId);
    List<Address> getUserAddresses(Long userId);
    Address getAddressById(Long id);
    Address updateAddress(Long id, Address address);
    void deleteAddress(Long id);
    List<AddressDto> convertToDtoList(List<Address> addresses);
    AddressDto convertToDto(Address address);
}
