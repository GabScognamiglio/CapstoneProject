package it.epicode.gs_budgets.repository;

import it.epicode.gs_budgets.entity.Ticket;
import it.epicode.gs_budgets.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

}
