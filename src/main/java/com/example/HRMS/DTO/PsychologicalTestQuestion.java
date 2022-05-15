package com.example.HRMS.DTO;

import com.example.HRMS.enums.TypeQuestion;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonDeserialize(as = PsychologicalTestQuestion.class)
public class PsychologicalTestQuestion {



    private Long questionId;

    private String question;

    private String image;

    private String psychType;

    private Integer temp;

}
