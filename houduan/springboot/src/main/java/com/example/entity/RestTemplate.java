package com.example.entity;

import lombok.Data;
@Data
public class RestTemplate {
    private String code;
    private data data;

    @Data
    private class data{
       private String generateUuid;
    };

}
