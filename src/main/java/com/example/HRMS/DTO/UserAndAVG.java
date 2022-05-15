package com.example.HRMS.DTO;

import com.example.HRMS.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data

public class UserAndAVG {

    private User user;

    private Double avg;
}
