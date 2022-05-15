package com.example.HRMS.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "general_result")
public class GeneralResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "general_score")
    private Integer generalScore;


    @ManyToOne
    private PsychologicalType psychologicalType;

    @ManyToOne
    private PsychologicalResults psychologicalResults;
}
