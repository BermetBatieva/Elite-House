package com.example.HRMS.entity;

import com.example.HRMS.enums.Status;
import com.example.HRMS.enums.TypeQuestion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "questions")
public class Question{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    private String photo;

    private Integer score;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_question")
    private TypeQuestion  typeQuestion;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    private Test test;

}
