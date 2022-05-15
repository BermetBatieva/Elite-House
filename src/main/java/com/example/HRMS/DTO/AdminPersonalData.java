package com.example.HRMS.DTO;

import com.example.HRMS.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminPersonalData {

    private Long id;

    private String firstname;

    private String lastname;

    private String email;

    private Role role;

}
