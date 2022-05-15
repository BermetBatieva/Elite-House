package com.example.HRMS.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class OpenAnswerCheck {

    private Long reviewId;

    private String question;

    private String answer;

    private Boolean checked;

}
