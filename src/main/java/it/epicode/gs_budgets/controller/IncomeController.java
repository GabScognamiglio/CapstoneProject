package it.epicode.gs_budgets.controller;

import it.epicode.gs_budgets.dto.IncomeDto;
import it.epicode.gs_budgets.dto.RecurringIncomeDto;
import it.epicode.gs_budgets.entity.Income;
import it.epicode.gs_budgets.exception.BadRequestException;
import it.epicode.gs_budgets.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/gs-budgets/incomes")
public class IncomeController {
    @Autowired
    private IncomeService incomeService;


    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<Income> getIncomes(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "id") String sortBy) {
        return incomeService.getIncomes(page, size, sortBy);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Income getIncomeById(@PathVariable int id) {
        return incomeService.getIncomeById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String saveIncome(@RequestBody @Validated IncomeDto incomeDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors()
                    .stream().map(e -> e.getDefaultMessage()).reduce("", (s, s2) -> s + " - " + s2));
        }
        return incomeService.saveIncome(incomeDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @ResponseStatus(HttpStatus.OK)
    public Income updateIncome(@PathVariable int id, @RequestBody @Validated IncomeDto incomeDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream().
                    map(objectError -> objectError.getDefaultMessage()).reduce("", (s, s2) -> s + " - " + s2));
        }
        return incomeService.updateIncome(id, incomeDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String deleteIncome(@PathVariable int id) {
        return incomeService.deleteIncome(id);
    }


    @PostMapping("/recurring")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String createRecurringIncomes(@RequestBody @Validated RecurringIncomeDto recurringIncomeDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream().
                    map(objectError -> objectError.getDefaultMessage()).reduce("", (s, s2) -> s + " - " + s2));
        }
        return incomeService.createRecurringIncomes(recurringIncomeDto);
    }

    @GetMapping("/account/{accountId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<Income> getIncomesByAccountId (@PathVariable int accountId) {
        return incomeService.getIncomesByAccountId(accountId);
    }

    @GetMapping("/account/recent/{accountId}")
    public List<Income> getRecentIncomes(@PathVariable int accountId) {
        return incomeService.getRecentIncomesByAccountId(accountId);
    }

}
