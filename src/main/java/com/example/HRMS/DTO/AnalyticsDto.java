package com.example.HRMS.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AnalyticsDto {

    private Long employeeId;

    private String photo;

    private String firsName;

    private String lastName;

    private String patronymic;

    private Integer generalScore;
}
