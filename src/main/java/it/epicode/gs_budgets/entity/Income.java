package it.epicode.gs_budgets.entity;

import it.epicode.gs_budgets.enums.IncomeCategory;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Income extends Transaction{
    @Enumerated(EnumType.STRING)
    private IncomeCategory category;
}
