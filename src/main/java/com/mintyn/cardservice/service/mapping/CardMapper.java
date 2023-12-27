package com.mintyn.cardservice.service.mapping;

import java.util.Optional;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.mintyn.cardservice.dto.Card;
import com.mintyn.cardservice.model.CardLookUpResponse;

@Mapper(componentModel = "spring")
public interface CardMapper{

    @Mapping(target = "bank", ignore = true)
    Card fromCardLookUp(CardLookUpResponse response);

    @AfterMapping
    default void setBankName(@MappingTarget Card card, CardLookUpResponse response){
        if (Optional.ofNullable(response).isPresent()) {
            
            if (Optional.ofNullable(response.getBank()).isPresent()) {
                card.setBank(response.getBank().getName());
            }
        }
    }
}
