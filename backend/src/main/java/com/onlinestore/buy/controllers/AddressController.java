package com.onlinestore.buy.controllers;

import com.onlinestore.buy.dtos.AddressDto;
import com.onlinestore.buy.entities.Address;
import com.onlinestore.buy.responses.APIResponse;
import com.onlinestore.buy.services.IAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.version}/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final IAddressService addressService;

    @PostMapping("/{userId}/new")
    public ResponseEntity<APIResponse> createAddressList(@RequestBody List<Address> address, @PathVariable("userId") Long userId){
        List<Address> addressList = addressService.createAddressList(address, userId);
        List<AddressDto> addressDtoList = addressService.convertToDtoList(addressList);
        return ResponseEntity.ok(new APIResponse("Address created successfully", addressDtoList));
    }

    @GetMapping("/{userId}/address")
    public ResponseEntity<APIResponse> getUserAddresses(@PathVariable("userId") Long userId){
        List<Address> addressList = addressService.getUserAddresses(userId);
        List<AddressDto> addressDtoList = addressService.convertToDtoList(addressList);
        return ResponseEntity.ok(new APIResponse("Address fetched successfully", addressDtoList));
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<APIResponse> updateAddress(@PathVariable("id") Long id, @RequestBody Address address){
        Address updatedAddress = addressService.updateAddress(id, address);
        System.out.println(updatedAddress);
        return ResponseEntity.ok(new APIResponse("Address updated successfully", addressService.convertToDto(updatedAddress)));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<APIResponse> deleteAddress(@PathVariable("id") Long id){
        addressService.deleteAddress(id);
        return ResponseEntity.ok(new APIResponse("Address deleted successfully", null));
    }
}
