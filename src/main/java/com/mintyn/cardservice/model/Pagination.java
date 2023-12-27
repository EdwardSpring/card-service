package com.mintyn.cardservice.model;

import java.io.Serializable;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.Data;


@Data
public class Pagination implements Pageable, Serializable {

    private static final long serialVersionUID = -25822477129613575L;
    
    private Integer limit;
    private long start;
    private Sort sort;
    private String order;
    private String[] fields;
    
    public void setStart(long offset) {
        this.start = offset;
    }

    public Pagination(String offset, String limit, String order, String... fields) {
        validateLimit(limit);
        validateOffset(offset);
        validateSort(order, fields);
    }

    private void validateOffset(String offset){
        if ( offset == null  || offset.trim() == "" || !isNumeric(offset)) {
            this.start = 0;
        }
        else{
            this.start = Long.valueOf(offset);
        }
    }

    private void validateLimit(String limit){
        if ( limit == null || limit.trim() == ""  || !isNumeric(limit)) {
            this.limit = 20;
        }
        else{
            this.limit = Integer.valueOf(limit);
        }
    }

    private void validateSort(String order, String... fields){
        this.order = order;
        this.fields = fields;
        
        if (order == null || fields == null ) {
            this.sort = Sort.unsorted();
        }
        else{
            this.sort =  Sort.by(validateOrder(order), fields);
        }
    }

    private Sort.Direction validateOrder(String str) {
        switch (str.toLowerCase()) {
            case "asc":
                return Sort.Direction.ASC;
            case "desc":
                return Sort.Direction.DESC;
            default:
                return Sort.Direction.ASC;
        }
    }

    private boolean isNumeric(String str){
        return str.chars().allMatch( Character::isDigit );
    }


    @Override
    public int getPageNumber() {
        return 0;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return start;
    }

    @Override
    public Sort getSort() {
        return this.sort;
    }

    @Override
    public Pageable next() {
        return null;
    }

    @Override
    public Pageable previousOrFirst() {
        return this;
    }

    @Override
    public Pageable first() {
        return this;
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return null;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

}

