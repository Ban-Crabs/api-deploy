package com.bancrabs.villaticket.models.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyDTO {
    private Boolean result;
    private String message;
}
