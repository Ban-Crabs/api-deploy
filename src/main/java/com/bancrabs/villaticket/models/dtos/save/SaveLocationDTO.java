package com.bancrabs.villaticket.models.dtos.save;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SaveLocationDTO {

    @NotEmpty
    @Pattern(regexp = "[A-Z]{3}[0-9]{6}")
    private String id;

    @NotEmpty
    private String name;

    @Nullable
    private Boolean isAvailable;

}
