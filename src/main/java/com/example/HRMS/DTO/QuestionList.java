package com.example.HRMS.DTO;

import com.example.HRMS.enums.TypeQuestion;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data

public class QuestionList {

    private Long questionId;

    private String question;

    private TypeQuestion type;

    private List<AnswerDTO> answers;
}
