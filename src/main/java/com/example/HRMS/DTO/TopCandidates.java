package com.example.HRMS.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data

public class TopCandidates {

    private Long candidateId;

    private String firstName;

    private String lastName;

    private String position;

    private int tests;

    private int completed;
}
