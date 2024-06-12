package it.epicode.gs_budgets.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import it.epicode.gs_budgets.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue
    private int id;
    private String object;
    private String description;
    private LocalDate date = LocalDate.now();
    private String adminAnswer;
    @Enumerated(EnumType.STRING)
    private TicketStatus status = TicketStatus.OPEN;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIncludeProperties({"id"})
    private User user;
}
