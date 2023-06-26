package com.bancrabs.villaticket.models.dtos.save;

import java.util.UUID;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SaveTransferDTO {

    @NotNull
    private String senderId;
    
    @Nullable
    private String receiverId;

    @NotNull
    private UUID ticketId;
}
