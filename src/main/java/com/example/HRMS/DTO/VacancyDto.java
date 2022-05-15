package com.example.HRMS.DTO;

import com.example.HRMS.entity.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class VacancyDto {

    private Long id;

    private String title;

    private String text;

    private String wage;

    private Long positionId;
}
