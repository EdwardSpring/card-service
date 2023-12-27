package com.mintyn.cardservice.service.mapping;

import org.mapstruct.Mapper;

import com.mintyn.cardservice.entity.User;
import com.mintyn.cardservice.model.RegistrationModel;

@Mapper(componentModel = "spring")
public interface UserMapper{
    User fromRegistrationModel(RegistrationModel model);
}
