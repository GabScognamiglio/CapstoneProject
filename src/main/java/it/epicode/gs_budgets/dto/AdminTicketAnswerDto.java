package it.epicode.gs_budgets.dto;

import it.epicode.gs_budgets.enums.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminTicketAnswerDto {

    @NotBlank
    private String adminAnswer;
    @NotNull
    private TicketStatus status;
}
