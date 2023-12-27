package com.mintyn.cardservice.client;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.mintyn.cardservice.model.CardLookUpResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BinWebClient {

    private final WebClient binClient;

    @Cacheable("cards")
    public CardLookUpResponse doLookup(String num) {
        return binClient.get().uri("/".concat(num)).retrieve().bodyToMono(CardLookUpResponse.class).block();
    }

}
