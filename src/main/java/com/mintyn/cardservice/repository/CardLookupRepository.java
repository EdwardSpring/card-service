package com.mintyn.cardservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mintyn.cardservice.entity.CardLookup;
import com.mintyn.cardservice.model.Pagination;
import com.mintyn.cardservice.model.Stat;

public interface CardLookupRepository extends JpaRepository<CardLookup, Long>{

    @Query(value = "SELECT new com.mintyn.cardservice.model.Stat(num, count(c)) FROM CardLookup c GROUP BY c.num")
    Page<Stat> findAllGroupByNum(Pagination pagination);

}
