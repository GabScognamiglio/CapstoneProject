package it.epicode.gs_budgets.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
public abstract class Transaction {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnoreProperties({"expenses","incomes"})
    private Account account;

    private double amount;
    private String tag;
    private String comment;
    private LocalDate date;
    private boolean isRecurring;
}
