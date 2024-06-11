package it.epicode.gs_budgets.dto;

import it.epicode.gs_budgets.entity.Account;
import it.epicode.gs_budgets.enums.IncomeCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class IncomeDto {

    @NotNull
    private int accountId;

    @Min(value = 0)
    private double amount;
    private String tag;
    private String comment;
    @NotNull
    private LocalDate date;
    @NotNull
    private IncomeCategory category;
    private boolean isRecurring;
}