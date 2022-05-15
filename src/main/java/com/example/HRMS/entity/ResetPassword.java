package com.example.HRMS.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "resetting")
public class ResetPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private String key;

    private String newEmail;

    public ResetPassword(User user, String key) {
        this.user = user;
        this.key = key;
    }

    public ResetPassword(User user_id, String key, String newEmail) {
        this.user = user;
        this.key = key;
        this.newEmail = newEmail;
    }
}
