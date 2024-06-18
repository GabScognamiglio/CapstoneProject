package it.epicode.gs_budgets.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "accounts")
@Data
public class Account {
    @Id
    @GeneratedValue
    private int id;

    private String name;
    private String description;
    private LocalDate creationDate = LocalDate.now();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Income> incomes;
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Expense> expenses;

    @OneToMany(mappedBy = "account")
    private List<SavingGoal> savingGoals;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIncludeProperties({"id", "email", "firstName", "lastName"})
    private User user;

}
