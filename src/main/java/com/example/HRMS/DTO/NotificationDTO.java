package com.example.HRMS.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data

public class NotificationDTO {

    private Long id;

    private String title;

    private String text;

    private String date;
}
