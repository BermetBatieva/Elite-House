package com.example.HRMS.entity;

import com.example.HRMS.enums.StatusTest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "psychological_results")
public class PsychologicalResults {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @Column(name = "attempt_date")
    private Date attemptDate;


    @Enumerated(EnumType.STRING)
    @Column(name = "status_test")
    private StatusTest statusTest;

}
