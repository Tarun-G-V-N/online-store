package com.onlinestore.buy.services.impl;

import com.onlinestore.buy.dtos.AddressDto;
import com.onlinestore.buy.entities.Address;
import com.onlinestore.buy.repositories.AddressRepository;
import com.onlinestore.buy.services.IAddressService;
import com.onlinestore.buy.services.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService implements IAddressService {
    private final AddressRepository addressRepository;
    private final IUserService userService;
    private final ModelMapper modelMapper;
    @Override
    public List<Address> createAddressList(List<Address> addresses, Long userId) {
        return Optional.ofNullable(userService.getUser(userId)).map(user -> {
            addresses.forEach(address -> address.setUser(user));
            return addressRepository.saveAll(addresses);
        }).orElse(Collections.emptyList());
    }

    @Override
    public List<Address> getUserAddresses(Long userId) {
        return addressRepository.findAllByUserId(userId);
    }

    @Override
    public Address getAddressById(Long id) {
        return addressRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Address not found"));
    }

    @Override
    public Address updateAddress(Long id, Address address) {
        return addressRepository.findById(id).map(
                existingAddress -> {
                    existingAddress.setPhoneNumber(address.getPhoneNumber());
                    existingAddress.setStreet(address.getStreet());
                    existingAddress.setCity(address.getCity());
                    existingAddress.setState(address.getState());
                    existingAddress.setCountry(address.getCountry());
                    existingAddress.setAddressType(address.getAddressType());
                    return addressRepository.save(existingAddress);
                }
        ).orElseThrow(() -> new EntityNotFoundException("Address not found"));
    }

    @Override
    public void deleteAddress(Long id) {
        addressRepository.findById(id).ifPresentOrElse(addressRepository::delete, () -> {
            throw new EntityNotFoundException("Address not found");
        });
    }

    @Override
    public AddressDto convertToDto(Address address){
        return modelMapper.map(address, AddressDto.class);
    }

    @Override
    public List<AddressDto> convertToDtoList(List<Address> addresses){
        return addresses.stream().map(this::convertToDto).toList();
    }
}
