package com.example.authentication.commons;

import com.fasterxml.jackson.databind.ObjectReader;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UniversalResponse {










    private int status;
    private Object data;
    private String message;
}
