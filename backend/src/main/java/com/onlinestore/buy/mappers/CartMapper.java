//package com.onlinestore.buy.mappers;
//
//import com.onlinestore.buy.dtos.CartDto;
//import com.onlinestore.buy.dtos.CartItemDto;
//import com.onlinestore.buy.entities.Cart;
//import com.onlinestore.buy.entities.CartItem;
//import org.modelmapper.ModelMapper;
//
//public class CartMapper {
//    public static ModelMapper getModelMapper(){
//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.typeMap(CartItem.class, CartItemDto.class)
//                .addMappings(mapper -> {
//                    mapper.map(CartItem::getProduct, CartItemDto::setProduct);
//                    mapper.map(CartItem::getQuantity, CartItemDto::setQuantity);
//                    mapper.map(CartItem::getTotalPrice, CartItemDto::setTotalPrice);
//                });
//
//        modelMapper.typeMap(Cart.class, CartDto.class)
//                .addMappings(mapper -> {
//                    mapper.map(Cart::getId, CartDto::setId);
//                    mapper.map(cart1 -> cart1.getUser().getId(), CartDto::setUserId);
//                    mapper.map(Cart::getCartItems, CartDto::setCartItems);
//                    mapper.map(Cart::getTotalAmount, CartDto::setTotalAmount);
//                });
//        return modelMapper;
//    }
//}
