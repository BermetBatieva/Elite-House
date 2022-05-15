package com.example.HRMS.DTO;

import com.example.HRMS.enums.TypeQuestion;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonDeserialize(as = TestQuestion.class)
public class TestQuestion {

    private Long reviewId;

    private Long questionId;

    private String question;

    private String image;

    private TypeQuestion typeQuestion;

    private String openAnswer;

    private List<AnswerDAO> answers;

}
