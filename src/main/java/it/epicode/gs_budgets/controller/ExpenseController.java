package it.epicode.gs_budgets.controller;


import it.epicode.gs_budgets.dto.ExpenseDto;
import it.epicode.gs_budgets.dto.RecurringExpenseDto;
import it.epicode.gs_budgets.entity.Expense;
import it.epicode.gs_budgets.exception.BadRequestException;
import it.epicode.gs_budgets.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/gs-budgets/expenses")
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;


    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<Expense> getExpenses(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "id") String sortBy) {
        return expenseService.getExpenses(page, size, sortBy);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Expense getExpenseById(@PathVariable int id) {
        return expenseService.getExpenseById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String saveExpense(@RequestBody @Validated ExpenseDto expenseDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors()
                    .stream().map(e -> e.getDefaultMessage()).reduce("", (s, s2) -> s + " - " + s2));
        }
        return expenseService.saveExpense(expenseDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @ResponseStatus(HttpStatus.OK)
    public Expense updateExpense(@PathVariable int id, @RequestBody @Validated ExpenseDto expenseDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream().
                    map(objectError -> objectError.getDefaultMessage()).reduce("", (s, s2) -> s + " - " + s2));
        }
        return expenseService.updateExpense(id, expenseDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String deleteExpense(@PathVariable int id) {
        return expenseService.deleteExpense(id);
    }


    @PostMapping("/recurring")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String createRecurringExpenses(@RequestBody @Validated RecurringExpenseDto recurringExpenseDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream().
                    map(objectError -> objectError.getDefaultMessage()).reduce("", (s, s2) -> s + " - " + s2));
        }
        return expenseService.createRecurringExpenses(recurringExpenseDto);
    }

    @GetMapping("/account/{accountId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<Expense> getExpensesByAccountId (@PathVariable int accountId) {
        return expenseService.getExpenseByAccountId(accountId);
    }

    @GetMapping("/account/recent/{accountId}")
    public List<Expense> getRecentExpenses(@PathVariable int accountId) {
        return expenseService.getRecentExpensesByAccountId(accountId);
    }
}
