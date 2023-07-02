package com.bancrabs.villaticket.models.dtos.response;

import java.sql.Timestamp;
import java.util.UUID;

import com.bancrabs.villaticket.models.entities.Event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceResponseDTO {
    private UUID id;
    private UserResponseDTO user;
    private Event event;
    private Timestamp timestamp;    
}
