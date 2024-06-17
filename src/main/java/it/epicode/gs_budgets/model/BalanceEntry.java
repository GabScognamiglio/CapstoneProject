package it.epicode.gs_budgets.model;

import lombok.Data;

@Data
public class BalanceEntry {
    private double balance;
    private double totalIncome;
    private double totalExpense;


}