package com.example.HRMS.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecruiterDto {

    private Long id;

    private  String firstname;

    private String lastname;

    private String department;

    private String position;

    private String email;

    private String phoneNumber;
}
