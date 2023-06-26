package com.bancrabs.villaticket.models.dtos.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponseDTO {
    private UUID id;
    private UUID tierId;
    private UserResponseDTO user;
    private Boolean result;
}
