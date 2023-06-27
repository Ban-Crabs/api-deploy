package com.bancrabs.villaticket.models.dtos.save;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDTO {

    @NotEmpty
    private String username;
    
    @NotEmpty
    @Email
    private String email;
}
