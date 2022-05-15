package com.example.HRMS.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDetailsDto {

    private Long userId;

    private String firstname;

    private String  lastname;

    private String urlPhoto;

    private String position;

    private String department;

    private String phoneNumber;

    private String email;

    private List<TestResultForTable> listTechTests;

    private List<PsychologicalListDto> listPsychTests;

    private Long resume;

    private List<Long> recommendations;

    private List<Long> additionalFiles;


}
