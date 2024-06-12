package it.epicode.gs_budgets.service;

import it.epicode.gs_budgets.dto.AccountDto;
import it.epicode.gs_budgets.dto.AdminTicketAnswerDto;
import it.epicode.gs_budgets.dto.TicketDto;
import it.epicode.gs_budgets.entity.Account;
import it.epicode.gs_budgets.entity.Ticket;
import it.epicode.gs_budgets.entity.User;
import it.epicode.gs_budgets.exception.NotFoundException;
import it.epicode.gs_budgets.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserService userService;

    public String saveTicket(TicketDto ticketDto) {
        Ticket ticket = new Ticket();
        ticket.setObject(ticketDto.getObject());
        ticket.setDescription(ticketDto.getDescription());
        ticket.setUser(userService.getUserById(ticketDto.getUserId()));

        ticketRepository.save(ticket);
        return "Ticket with id " + ticket.getId() + " correctly saved for user with id: " + ticket.getUser().getId();
    }

    public Page<Ticket> getTickets(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ticketRepository.findAll(pageable);
    }

    public Ticket getTicketById(int id) {

        if (ticketRepository.findById(id).isPresent()) {
            return ticketRepository.findById(id).get();
        } else {
            throw new NotFoundException("Ticket with id: " + id + " not found");
        }
    }

    public Ticket updateTicket(int id, TicketDto ticketDto) {
        Ticket ticket = getTicketById(id);
        ticket.setObject(ticketDto.getObject());
        ticket.setDescription(ticketDto.getDescription());

        ticketRepository.save(ticket);
        return ticket;
    }

    public String deleteTicket(int id) {
        ticketRepository.delete(getTicketById(id));
        return "Ticket with id " + id + " correctly deleted";
    }

    public Ticket patchAdminAnswer (int id, AdminTicketAnswerDto adminTicketAnswerDto) {
       Ticket ticket = getTicketById(id);

       ticket.setAdminAnswer(adminTicketAnswerDto.getAdminAnswer());
       ticket.setStatus(adminTicketAnswerDto.getStatus());
       ticketRepository.save(ticket);

       return ticket;
    }

    public List<Ticket> getTicketsByUserId (int userId){
        return ticketRepository.findByUserId(userId);
    }

}
