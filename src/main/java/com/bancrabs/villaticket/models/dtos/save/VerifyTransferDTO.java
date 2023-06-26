package com.bancrabs.villaticket.models.dtos.save;

import java.sql.Timestamp;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyTransferDTO {
    
    @NotNull
    private String receiverId;

    @NotNull
    private Timestamp timestamp;
}
