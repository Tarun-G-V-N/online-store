package com.onlinestore.buy.security.config;

import com.onlinestore.buy.dtos.*;
import com.onlinestore.buy.entities.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        //Category Mappings
//        modelMapper.typeMap(Category.class, CategoryDto.class)
//        .addMappings(mapper -> {
//            mapper.map(Category::getId, CategoryDto::setId);
//            mapper.map(Category::getName, CategoryDto::setName);
//        });

        //Product Mappings
//        modelMapper.typeMap(Product.class, ProductDto.class)
//        .addMappings(mapper -> {
//            mapper.map(Product::getId, ProductDto::setId);
//            mapper.map(Product::getName, ProductDto::setName);
//            mapper.map(Product::getBrand, ProductDto::setBrand);
//            mapper.map(Product::getPrice, ProductDto::setPrice);
//            mapper.map(Product::getInventory, ProductDto::setInventory);
//            mapper.map(Product::getDescription, ProductDto::setDescription);
//            mapper.map(Product::getCategory, ProductDto::setCategory);
//            mapper.map(Product::getImages, ProductDto::setImages);
//        });

        //Cart Mappings
//        modelMapper.typeMap(CartItem.class, CartItemDto.class)
//        .addMappings(mapper -> {
//            mapper.map(CartItem::getProduct, CartItemDto::setProduct);
//            mapper.map(CartItem::getQuantity, CartItemDto::setQuantity);
//            mapper.map(CartItem::getTotalPrice, CartItemDto::setTotalPrice);
//        });

//        modelMapper.typeMap(Cart.class, CartDto.class)
//                .addMappings(mapper -> {
//                    mapper.map(Cart::getId, CartDto::setId);
//                    mapper.map(cart1 -> cart1.getUser().getId(), CartDto::setUserId);
//                    mapper.map(Cart::getCartItems, CartDto::setCartItems);
//                    mapper.map(Cart::getTotalAmount, CartDto::setTotalAmount);
//                });

        //Order Mappings
//        modelMapper.typeMap(OrderItem.class, OrderItemDto.class)
//                .addMappings(mapper -> {
//                    mapper.map(orderItem -> orderItem.getProduct().getId(), OrderItemDto::setProductId);
//                    mapper.map(orderItem -> orderItem.getProduct().getName(), OrderItemDto::setProductName);
//                    mapper.map(orderItem -> orderItem.getProduct().getBrand(), OrderItemDto::setProductBrand);
//                    mapper.map(OrderItem::getQuantity, OrderItemDto::setQuantity);
//                    mapper.map(OrderItem::getPrice, OrderItemDto::setPrice);
//                });
//        modelMapper.typeMap(Order.class, OrderDto.class)
//                .addMappings(mapper -> {
//                    mapper.map(Order::getId, OrderDto::setId);
//                    mapper.map(order1 -> order1.getUser().getId(), OrderDto::setUserId);
//                    mapper.map(Order::getStatus, OrderDto::setStatus);
//                    mapper.map(Order::getTotalAmount, OrderDto::setTotalAmount);
//                    mapper.map(Order::getOrderDate, OrderDto::setOrderDate);
//                    mapper.map(Order::getOrderItems, OrderDto::setItems);
//                });

        //User Mappings
//        modelMapper.typeMap(User.class, UserDto.class)
//                .addMappings(mapper -> {
//                    mapper.map(User::getId, UserDto::setId);
//                    mapper.map(User::getFirstname, UserDto::setFirstname);
//                    mapper.map(User::getLastname, UserDto::setLastname);
//                    mapper.map(User::getEmail, UserDto::setEmail);
//                    mapper.map(User::getOrders, UserDto::setOrders);
//                    mapper.map(User::getCart, UserDto::setCart);
//                    mapper.map(User::getAddresses, UserDto::setAddresses);
////                    mapper.map(user -> user.getRoles().stream().map(Role::getName).toList(), UserDto::setRoles);
//                });

        //Address Mappings
//        modelMapper.typeMap(Address.class, AddressDto.class)
//                .addMappings(mapper -> {
//                    mapper.map(Address::getId, AddressDto::setId);
//                    mapper.map(Address::getPhoneNumber, AddressDto::setPhoneNumber);
//                    mapper.map(Address::getStreet, AddressDto::setStreet);
//                    mapper.map(Address::getCity, AddressDto::setCity);
//                    mapper.map(Address::getState, AddressDto::setState);
//                    mapper.map(Address::getCountry, AddressDto::setCountry);
//                    mapper.map(Address::getAddressType, AddressDto::setAddressType);
//                });
        return modelMapper;
    }
}
