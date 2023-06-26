package com.bancrabs.villaticket.models.dtos.save;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SaveTierDTO {

    @NotEmpty
    private String name;

    @NotNull
    private Float price;
    
    @NotNull
    private Integer quantity;
    
    @NotEmpty
    @Pattern(regexp = "[A-Z]{3}[0-9]{3}")
    private String localeId;
}
