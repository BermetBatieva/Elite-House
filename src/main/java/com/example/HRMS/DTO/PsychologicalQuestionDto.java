package com.example.HRMS.DTO;

import com.example.HRMS.enums.PsychologicalQuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PsychologicalQuestionDto {

    private String question;

    private Long psychologicalTypeId;

    private PsychologicalQuestionType questionType;

}
