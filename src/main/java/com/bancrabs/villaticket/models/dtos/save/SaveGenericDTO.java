package com.bancrabs.villaticket.models.dtos.save;

import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SaveGenericDTO {
    
    @NotEmpty
    @Pattern(regexp = "[A-Z]{3}[0-9]{2}")
    private String code;

    @NotEmpty
    @Size(min = 3, max = 50)
    private String name;

    @NotNull
    private UUID eventId;
}
