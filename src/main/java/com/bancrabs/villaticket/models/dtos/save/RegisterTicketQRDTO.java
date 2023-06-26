package com.bancrabs.villaticket.models.dtos.save;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterTicketQRDTO {

    @NotNull
    private UUID ticketId;

    @NotNull
    private String qrCode;
}
