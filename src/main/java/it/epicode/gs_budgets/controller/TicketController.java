package it.epicode.gs_budgets.controller;

import it.epicode.gs_budgets.dto.AccountDto;
import it.epicode.gs_budgets.dto.AdminTicketAnswerDto;
import it.epicode.gs_budgets.dto.TicketDto;
import it.epicode.gs_budgets.entity.Account;
import it.epicode.gs_budgets.entity.Ticket;
import it.epicode.gs_budgets.exception.BadRequestException;
import it.epicode.gs_budgets.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/gs-budgets/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<Ticket> getTickets(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "id") String sortBy) {
        return ticketService.getTickets(page, size, sortBy);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Ticket getTicketById(@PathVariable int id) {
        return ticketService.getTicketById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String saveTicket(@RequestBody @Validated TicketDto ticketDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors()
                    .stream().map(e -> e.getDefaultMessage()).reduce("", (s, s2) -> s + " - " + s2));
        }
        return ticketService.saveTicket(ticketDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @ResponseStatus(HttpStatus.OK)
    public Ticket updateTicket(@PathVariable int id, @RequestBody @Validated TicketDto ticketDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream().
                    map(objectError -> objectError.getDefaultMessage()).reduce("", (s, s2) -> s + " - " + s2));
            // " - " (riga sopra) serve per separare gli errori con un trattino tra uno e l'altro
        }
        return ticketService.updateTicket(id, ticketDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String deleteTicket(@PathVariable int id) {
        return ticketService.deleteTicket(id);
    }


    //Per la risposta di un admin a un ticket di supporto
    @PatchMapping("/{id}/admin-answer")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Ticket patchAdminAnswer(@PathVariable int id,@RequestBody @Validated AdminTicketAnswerDto adminTicketAnswerDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream().
                    map(objectError -> objectError.getDefaultMessage()).reduce("", (s, s2) -> s + " - " + s2));
        }
        return ticketService.patchAdminAnswer(id, adminTicketAnswerDto);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<Ticket> getTicketsByUserId(@PathVariable int userId){
        return ticketService.getTicketsByUserId(userId);
    }

}
