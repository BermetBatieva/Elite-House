package com.example.HRMS.DTO;

import com.example.HRMS.entity.InterviewTimeTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlotResponseMessage {

    private InterviewTimeTable slot;
    private String message;

}
