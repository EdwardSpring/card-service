package com.mintyn.cardservice.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mintyn.cardservice.dto.Card;
import com.mintyn.cardservice.model.ApiResponse;
import com.mintyn.cardservice.model.Pagination;
import com.mintyn.cardservice.model.Stat;
import com.mintyn.cardservice.service.CardShemeService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CardSchemeResource {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final CardShemeService cardShemeServie;

    @GetMapping("/card-scheme/verify/{num}")
    public ResponseEntity<ApiResponse<?>> verifyCard(@PathVariable String num) {
        log.info("Rest request to verify card: {}", num);

        if (num.length() < 6) {
            throw new IllegalArgumentException("Number must be at least 6 figures");
        }
        
        Card card = cardShemeServie.verifyCard(num);
        ApiResponse<Card> response = new ApiResponse<>(true, card);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/card-scheme/stats")
    public ResponseEntity<ApiResponse<?>> stats(Pagination pagination) {
        log.info("Rest request to get stats for card lookups: {}", pagination);


        Page<Stat> lookups = cardShemeServie.stats(pagination);
        ApiResponse<Page<Stat>> response = new ApiResponse<>(true, lookups);
        response.setLimit(pagination.getLimit());
        response.setStart((int) pagination.getStart());
        return ResponseEntity.ok(response);
    }
    
    

}
