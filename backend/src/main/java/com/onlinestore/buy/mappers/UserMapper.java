//package com.onlinestore.buy.mappers;
//
//import com.onlinestore.buy.dtos.UserDto;
//import com.onlinestore.buy.entities.User;
//import org.modelmapper.ModelMapper;
//
//public class UserMapper {
//    public static ModelMapper getModelMapper(){
//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.typeMap(User.class, UserDto.class)
//                .addMappings(mapper -> {
//                    mapper.map(User::getId, UserDto::setId);
//                    mapper.map(User::getFirstname, UserDto::setFirstname);
//                    mapper.map(User::getLastname, UserDto::setLastname);
//                    mapper.map(User::getEmail, UserDto::setEmail);
////                    mapper.map(user -> user.getRoles().stream().map(Role::getName).toList(), UserDto::setRoles);
//                });
//        return modelMapper;
//    }
//}
