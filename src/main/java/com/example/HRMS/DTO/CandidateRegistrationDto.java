package com.example.HRMS.DTO;

import com.example.HRMS.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CandidateRegistrationDto {

    private String firstName;

    private String lastName;

    private String patronymic;

    private String phoneNumber;

    private Long positionId;

    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

}
