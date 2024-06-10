package it.epicode.gs_budgets.entity;

import it.epicode.gs_budgets.enums.ExpenseCategory;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Expense extends Transaction{
    private ExpenseCategory category;
}
