package com.example.HRMS.entity;

import com.example.HRMS.enums.Checking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Result result;

    @ManyToOne
    private Question questions;

    private String openAnswer;

    @Enumerated(EnumType.STRING)
    private Checking checking;


}