package com.mintyn.cardservice.service.mapping;

import java.util.Optional;

import org.mapstruct.Mapper;

import com.mintyn.cardservice.dto.Card;
import com.mintyn.cardservice.model.CardLookUpResponse;

@Mapper(componentModel = "spring")
public interface CardMapper{

    Card fromCardLookUp(CardLookUpResponse response);

    default String map(CardLookUpResponse.Bank bank){
        if (Optional.ofNullable(bank).isEmpty()) {
            return null;
        }

        return bank.getName();
    }
}
