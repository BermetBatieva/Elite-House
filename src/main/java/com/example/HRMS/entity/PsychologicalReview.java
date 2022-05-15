package com.example.HRMS.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "psychological_review")
public class PsychologicalReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer temp;//1 2 3 4 5

    @ManyToOne
    private PsychologicalQuestions psychologicalQuestions;

    @ManyToOne
    private PsychologicalResults psychologicalResults;


}
