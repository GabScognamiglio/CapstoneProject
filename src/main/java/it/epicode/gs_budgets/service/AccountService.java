package it.epicode.gs_budgets.service;

import it.epicode.gs_budgets.dto.AccountDto;
import it.epicode.gs_budgets.entity.Account;
import it.epicode.gs_budgets.exception.NotFoundException;
import it.epicode.gs_budgets.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserService userService;

    public String saveAccount(AccountDto accountDto) {
        Account account = new Account();
        account.setName(accountDto.getName());
        account.setDescription(accountDto.getDescription());
        account.setUser(userService.getUserById(accountDto.getUserId()));

        accountRepository.save(account);
        return "Account with id " + account.getId() + " correctly saved for user with id: " + account.getUser().getId();
    }

    public Page<Account> getAccounts(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return accountRepository.findAll(pageable);
    }

    public Account getAccountById(int id) {

        if (accountRepository.findById(id).isPresent()) {
            return accountRepository.findById(id).get();
        } else {
            throw new NotFoundException("Account with id: " + id + " not found");
        }
    }

    public Account updateAccount(int id, AccountDto accountDto) {
        Account account = getAccountById(id);
        account.setName(accountDto.getName());
        account.setDescription(accountDto.getDescription());
        account.setUser(userService.getUserById(accountDto.getUserId()));

        accountRepository.save(account);
        return account;
    }

    public String deleteAccount(int id) {
        accountRepository.delete(getAccountById(id));
        return "Account with id " + id + " correctly deleted";
    }

}
