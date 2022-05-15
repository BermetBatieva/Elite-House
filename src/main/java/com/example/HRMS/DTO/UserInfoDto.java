package com.example.HRMS.DTO;

import com.example.HRMS.entity.Department;
import com.example.HRMS.entity.Document;
import com.example.HRMS.entity.Position;
import com.example.HRMS.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {

    @JsonFormat(locale = "firstname")
    private String firstName;

    @JsonFormat(locale = "lastname")
    private String lastName;

    private String patronymic;

    private Gender gender;

    private Department department;

    private Position position;

    private String phoneNumber;

    private String email;

    private String photoUrl;

    private List<Document> resume;

    private List<Document> recommendations;

    private List<Document> certificates;

}
