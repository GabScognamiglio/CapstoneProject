package it.epicode.gs_budgets.dto;

import it.epicode.gs_budgets.entity.Account;
import it.epicode.gs_budgets.enums.ExpenseCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RecurringExpenseDto {

    @NotNull
    private int accountId;

    @Min(value = 0)
    private double amount;
    private String tag;
    private String comment;
    @NotNull
    private ExpenseCategory category;

    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @Min(value = 1)
    private int intervalDays;

    private boolean isRecurring;
}
