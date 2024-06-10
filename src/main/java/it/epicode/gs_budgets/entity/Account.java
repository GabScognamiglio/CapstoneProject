package it.epicode.gs_budgets.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToMany(mappedBy = "account")
    private List<Income> incomes;
    @OneToMany(mappedBy = "account")
    private List<Expense> expenses;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

}
