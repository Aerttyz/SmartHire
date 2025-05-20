package com.smarthire.resume.domain.DTO;

import lombok.*;

@Getter
@Setter
public class AcessDto {
    private String token;

    public AcessDto(String token){
        super();
        this.token = token;
    }
}
