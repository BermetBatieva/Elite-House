package com.example.HRMS.entity;

import com.example.HRMS.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "psychological_type")
public class PsychologicalType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "init_point")
    private Integer initPoint;//от скольки начинается для каждого типа

    @Enumerated(EnumType.STRING)
    private Status status;

}
