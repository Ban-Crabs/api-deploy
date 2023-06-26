package com.bancrabs.villaticket.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDTO {
    
    @NotEmpty
    private String id;
    
    @NotEmpty
    private String password;
}
