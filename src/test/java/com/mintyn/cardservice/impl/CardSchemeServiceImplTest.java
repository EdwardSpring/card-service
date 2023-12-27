package com.mintyn.cardservice.impl;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.mintyn.cardservice.client.BinWebClient;
import com.mintyn.cardservice.dto.Card;
import com.mintyn.cardservice.model.CardLookUpResponse;
import com.mintyn.cardservice.model.Pagination;
import com.mintyn.cardservice.model.Stat;
import com.mintyn.cardservice.repository.CardLookupRepository;
import com.mintyn.cardservice.service.impl.CardShemeServiceImpl;
import com.mintyn.cardservice.service.mapping.CardMapper;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CardSchemeServiceImplTest {

    private final String CARD_TYPE = "mock type";
    private final String CARD_SCHEME = "mock scheme";
    private final String CARD_BANK = "mock bank";
    private final String NUM = "45717360";
    private final Long COUNT = 1L;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Mock
    private CardLookupRepository cardLookupRepository;

    @InjectMocks
    private CardShemeServiceImpl cardShemeService;

    @Mock
    private BinWebClient binWebClient;

    @Spy
    private CardMapper cardMapper = Mappers.getMapper(CardMapper.class);

    @BeforeEach
    public void init(TestInfo testInfo) {
        log.info("{}Starting {}{}", "-".repeat(10), testInfo.getDisplayName(), "-".repeat(10));
    }

    @Test
    public void verifyCardTest() throws Exception{

        // Mock response
        CardLookUpResponse cardLookUpResponse = new CardLookUpResponse();
        cardLookUpResponse.setScheme(CARD_SCHEME);
        cardLookUpResponse.setType(CARD_TYPE);
        cardLookUpResponse.setBank(new CardLookUpResponse.Bank(CARD_BANK));
        when(binWebClient.doLookup(any())).thenReturn(cardLookUpResponse);


        // actual mthod call
        Card result = cardShemeService.verifyCard(NUM);

        // Assertions
        assertThat(result).isNotNull();
        assertThat(result.getBank()).isEqualTo(CARD_BANK);
        assertThat(result.getScheme()).isEqualTo(CARD_SCHEME);
        assertThat(result.getType()).isEqualTo(CARD_TYPE);

        verify(cardLookupRepository, times(1)).save(any());
    }

    @Test
    public void statsTest() throws Exception{
        Pagination pagination = new Pagination("0", "0", null, "");

        // Mock response
        Stat stat = new Stat(NUM, COUNT);
        Page<Stat> page = new PageImpl<>(List.of(stat));
        when(cardLookupRepository.findAllGroupByNum(any())).thenReturn(page);

        // actual mthod call
        Page<Stat> result = cardShemeService.stats(pagination);

        // Assertions
        assertThat(result).isNotNull();
        assertThat(result.getContent()).startsWith(stat);
        assertThat(result.getTotalElements()).isEqualTo(page.getTotalElements());

        verify(cardLookupRepository, times(1)).findAllGroupByNum(any());
    }

}
