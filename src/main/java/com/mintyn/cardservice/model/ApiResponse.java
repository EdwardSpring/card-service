package com.mintyn.cardservice.model;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuppressWarnings("unchecked")
public class ApiResponse <T> {
    private boolean success;
    private String message;
    private Integer start;
    private Integer limit;
    private Long size;
    private T payload;

    public ApiResponse(boolean status) {
        setSuccess(status);
    }

    public ApiResponse(boolean status, String message) {
        setSuccess(status);
        setMessage(message);
    }

    
    public ApiResponse(boolean status, T load) {
        setSuccess(status);
        setPayload(load);

        if (Optional.ofNullable(load).isPresent()) {
            if (load instanceof Page) {
                Page<T> page = ((Page<T>)load);
                setSize(page.getTotalElements());
                setPayload((T) page.getContent());
            }
        }
    }
}
