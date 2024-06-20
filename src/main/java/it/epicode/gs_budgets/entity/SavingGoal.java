package it.epicode.gs_budgets.entity;


import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "saving_goals")
@Data
public class SavingGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String description;
    private double targetAmount;
    private double savedAmount = 0;
    private LocalDate creationDate = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIncludeProperties("id")
    private Account account;
}
