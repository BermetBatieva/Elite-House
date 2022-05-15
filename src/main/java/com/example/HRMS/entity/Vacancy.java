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
@Table(name = "vacancies")
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    @Column(name="text", length=1000)
    private String text;

    private String wage;

    @ManyToOne
    private Position position;

    @Enumerated(EnumType.STRING)
    private Status status;
}
