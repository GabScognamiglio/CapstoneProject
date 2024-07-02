package it.epicode.gs_budgets.controller;

import it.epicode.gs_budgets.dto.AccountDto;
import it.epicode.gs_budgets.entity.Account;
import it.epicode.gs_budgets.entity.User;
import it.epicode.gs_budgets.exception.BadRequestException;
import it.epicode.gs_budgets.model.BalanceEntry;
import it.epicode.gs_budgets.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/gs-budgets/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<Account> getAccounts(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "id") String sortBy) {
        return accountService.getAccounts(page, size, sortBy);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Account getAccountById(@PathVariable int id) {
        return accountService.getAccountById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String saveAccount(@RequestBody @Validated AccountDto accountDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors()
                    .stream().map(e -> e.getDefaultMessage()).reduce("", (s, s2) -> s + " - " + s2));
        }
            return accountService.saveAccount(accountDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @ResponseStatus(HttpStatus.OK)
    public Account updateAccount(@PathVariable int id, @RequestBody @Validated AccountDto accountDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream().
                    map(objectError -> objectError.getDefaultMessage()).reduce("", (s, s2) -> s + " - " + s2));
            // " - " (riga sopra) serve per separare gli errori con un trattino tra uno e l'altro
        }
        return accountService.updateAccount(id, accountDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String deleteAccount(@PathVariable int id) {
        return accountService.deleteAccount(id);
    }

    @GetMapping("user/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Account getAccountByUserId(@PathVariable int userId){
        return accountService.getAccountByUserId(userId);
    }


    //BILANCI VARI

    @GetMapping("{accountId}/total-balance")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public BalanceEntry getAccountTotalBalance(@PathVariable int accountId){
        return accountService.getAccountTotalBalance(accountId);
    }

    @GetMapping("{accountId}/last-year-balance")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public BalanceEntry getAccountBalanceLast12Months(@PathVariable int accountId){
        return accountService.getAccountBalanceLast12Months(accountId);
    }

    @GetMapping("{accountId}/last-month-balance")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public BalanceEntry getAccountBalanceLastMonth(@PathVariable int accountId){
        return accountService.getAccountBalanceLastMonth(accountId);
    }

    @GetMapping("{accountId}/last-week-balance")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public BalanceEntry getAccountBalanceLastWeek(@PathVariable int accountId){
        return accountService.getAccountBalanceLastWeek(accountId);
    }

    @GetMapping("{accountId}/monthly-year-balance")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Map<String, BalanceEntry> getAccountBalancesMonthlyLast12Months(@PathVariable int accountId) {
        return accountService.getAccountBalancesMonthlyLast12Months(accountId);
    }

    @GetMapping("{accountId}/weekly-month-balance")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Map<String, BalanceEntry> getAccountBalancesWeeklyLast4Weeks(@PathVariable int accountId) {
        return accountService.getAccountBalancesWeeklyLast4Weeks(accountId);
    }








}
