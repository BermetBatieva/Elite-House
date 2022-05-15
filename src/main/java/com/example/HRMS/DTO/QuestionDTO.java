package com.example.HRMS.DTO;

import com.example.HRMS.enums.TypeQuestion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class QuestionDTO {

    private Long testId;

    private String question;

    private Integer score;

    private TypeQuestion typeQuestion;

    List<AnswerDTO> answers;
}
