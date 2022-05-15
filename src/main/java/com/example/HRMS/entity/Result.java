package com.example.HRMS.entity;

import com.example.HRMS.enums.StatusTest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "results")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Test test;

    @Column(name = "general_score")
    private Integer generalScore;

    @Column(name = "max_score")
    private Integer maxScore;

    @Column(name = "attempt_date")
    private Date attemptDate;

    @Enumerated(EnumType.STRING)
    private StatusTest statusTest;

}


