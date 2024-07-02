package it.epicode.gs_budgets.entity;

import it.epicode.gs_budgets.enums.ExpenseCategory;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Expense extends Transaction{
    @Enumerated(EnumType.STRING)
    private ExpenseCategory category;
}
