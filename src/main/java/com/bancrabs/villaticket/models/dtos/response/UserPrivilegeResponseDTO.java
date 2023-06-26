package com.bancrabs.villaticket.models.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPrivilegeResponseDTO {
    private String name;
    private String userId;
}
