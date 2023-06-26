package com.bancrabs.villaticket.models.dtos.save;

import java.sql.Timestamp;
import java.util.Date;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SaveEventDTO {

    @NotEmpty
    private String title;
    
    @NotEmpty
    private String typeId;
    
    @NotEmpty
    private String locationId;

    @NotEmpty
    private String categoryId;

    @NotNull
    private Date date;

    @NotNull
    private Timestamp startTime;

    @NotNull
    private Timestamp endTime;
    
    @NotEmpty
    private String status;

    @NotNull
    private Boolean isVisible;
}
