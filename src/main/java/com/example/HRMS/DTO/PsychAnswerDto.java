package com.example.HRMS.DTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonDeserialize(as = PsychAnswerDto.class)
public class PsychAnswerDto {

    private Long questionId;

    private Integer temp;

}
