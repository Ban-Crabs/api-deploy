package com.bancrabs.villaticket.models.dtos.save;

import java.sql.Timestamp;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterOrderDTO {

    @NotNull
    private Timestamp purchaseDate;
    
    @NotEmpty
    private String purchaseMethod;
    
    @NotEmpty
    private String userId;
}
