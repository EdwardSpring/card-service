package com.mintyn.cardservice.service;

import org.springframework.data.domain.Page;

import com.mintyn.cardservice.dto.Card;
import com.mintyn.cardservice.model.Pagination;
import com.mintyn.cardservice.model.Stat;

public interface CardShemeService {

    Card verifyCard(String num);
    Page<Stat> stats(Pagination pagination);

}
