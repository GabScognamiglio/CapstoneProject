package it.epicode.gs_budgets.entity;


import jakarta.persistence.*;
import lombok.Data;

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

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
