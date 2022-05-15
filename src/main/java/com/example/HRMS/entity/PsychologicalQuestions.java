package com.example.HRMS.entity;

import com.example.HRMS.enums.PsychologicalQuestionType;
import com.example.HRMS.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "psychological_questions")
public class PsychologicalQuestions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    private String photo;

    @Enumerated(EnumType.STRING)
    private PsychologicalQuestionType sign;

    @ManyToOne
    private PsychologicalType psychologicalType;

    @Enumerated(EnumType.STRING)
    private Status status;

}
