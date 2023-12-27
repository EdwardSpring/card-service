package com.mintyn.cardservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mintyn.cardservice.client.BinWebClient;
import com.mintyn.cardservice.dto.Card;
import com.mintyn.cardservice.entity.CardLookup;
import com.mintyn.cardservice.model.Pagination;
import com.mintyn.cardservice.model.Stat;
import com.mintyn.cardservice.repository.CardLookupRepository;
import com.mintyn.cardservice.service.CardShemeService;
import com.mintyn.cardservice.service.mapping.CardMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardShemeServiceImpl implements CardShemeService{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final CardMapper cardMapper;
    private final BinWebClient binWebClient;
    private final CardLookupRepository cardLookupRepository;

    @Override
    @Transactional
    public Card verifyCard(String num) {
        log.info("Request to verify card: {}", num);
        CardLookup cardLookup = new CardLookup(num);
        cardLookupRepository.save(cardLookup);
        Card card = cardMapper.fromCardLookUp(binWebClient.doLookup(num));
        return card;
    }


    @Override
    public Page<Stat> stats(Pagination pagination) {
        log.info("Request to get a page of card lookup: {}", pagination);
        return cardLookupRepository.findAllGroupByNum(pagination);
    }

}
