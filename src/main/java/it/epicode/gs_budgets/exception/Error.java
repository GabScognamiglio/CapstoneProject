package it.epicode.gs_budgets.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class Error {
    private String message;
    private LocalDateTime date;
    private HttpStatus status;
}
