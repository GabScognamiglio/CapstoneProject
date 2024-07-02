package it.epicode.gs_budgets.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SavingGoalDto {

    @NotBlank
    private String name;
    private String description;
    @Min(value = 0)
    private double targetAmount;
    @Min(value = 1)
    private int accountId;

}
