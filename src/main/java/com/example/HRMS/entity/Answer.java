package com.example.HRMS.entity;

import com.example.HRMS.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "correct_answer")
    private boolean correctAnswer;

    @ManyToOne
    private Question question;

    @Enumerated(EnumType.STRING)
    private Status status;
}
