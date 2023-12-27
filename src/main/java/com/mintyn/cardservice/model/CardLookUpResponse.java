package com.mintyn.cardservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CardLookUpResponse {
    private String scheme;
    private String type;
    private Bank bank;

    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Bank {
        private String name;
    }
}
