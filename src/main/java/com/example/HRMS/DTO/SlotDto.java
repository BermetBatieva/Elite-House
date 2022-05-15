package com.example.HRMS.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class SlotDto {

    private String startDateAndTime;
    private String duration;
    private String meetingLink;
    private String description;
    private Long positionId;

}
