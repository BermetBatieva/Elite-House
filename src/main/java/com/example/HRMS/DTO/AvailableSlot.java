package com.example.HRMS.DTO;

import com.example.HRMS.entity.Position;
import com.example.HRMS.entity.User;
import com.example.HRMS.enums.ReservationStatus;
import com.example.HRMS.enums.Status;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

public class AvailableSlot {

    private Long id;

    private Date startDateTime;

    private Date endDateAndTime;

    private String meetingLink;

    private String description;

    private List<Position> positions;

    private User candidate;
}
