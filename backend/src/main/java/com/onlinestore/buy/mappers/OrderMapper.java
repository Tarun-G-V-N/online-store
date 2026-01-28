//package com.onlinestore.buy.mappers;
//
//import com.onlinestore.buy.dtos.OrderDto;
//import com.onlinestore.buy.dtos.OrderItemDto;
//import com.onlinestore.buy.entities.Order;
//import com.onlinestore.buy.entities.OrderItem;
//import org.modelmapper.ModelMapper;
//
//public class OrderMapper {
//    public static ModelMapper getModelMapper(){
//        ModelMapper modelMapper = new ModelMapper();
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
//        return modelMapper;
//    }
//}
