package it.epicode.gs_budgets.service;

import it.epicode.gs_budgets.dto.IncomeDto;
import it.epicode.gs_budgets.dto.RecurringExpenseDto;
import it.epicode.gs_budgets.dto.RecurringIncomeDto;
import it.epicode.gs_budgets.entity.Expense;
import it.epicode.gs_budgets.entity.Income;
import it.epicode.gs_budgets.exception.NotFoundException;
import it.epicode.gs_budgets.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private AccountService accountService;

    public String saveIncome(IncomeDto incomeDto) {
        Income income = new Income();
        income.setAccount(accountService.getAccountById(incomeDto.getAccountId()));
        income.setAmount(incomeDto.getAmount());
        income.setTag(incomeDto.getTag());
        income.setComment(incomeDto.getComment());
        income.setDate(incomeDto.getDate());
        income.setCategory(incomeDto.getCategory());
        income.setRecurring(incomeDto.isRecurring());

        incomeRepository.save(income);
        return "Income with id " + income.getId() + " correctly saved for account with id: " + income.getAccount().getId();
    }

    public Page<Income> getIncomes(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return incomeRepository.findAll(pageable);
    }

    public Income getIncomeById(int id) {
        if (incomeRepository.findById(id).isPresent()) {
            return incomeRepository.findById(id).get();
        } else {
            throw new NotFoundException("Income with id: " + id + " not found");
        }
    }

    public Income updateIncome(int id, IncomeDto incomeDto) {
        Income income = getIncomeById(id);
        income.setAccount(accountService.getAccountById(incomeDto.getAccountId()));
        income.setAmount(incomeDto.getAmount());
        income.setTag(incomeDto.getTag());
        income.setComment(incomeDto.getComment());
        income.setDate(incomeDto.getDate());
        income.setCategory(incomeDto.getCategory());
        income.setRecurring(incomeDto.isRecurring());

        incomeRepository.save(income);
        return income;
    }

    public String deleteIncome(int id) {
        incomeRepository.delete(getIncomeById(id));
        return "Income with id " + id + " correctly deleted";
    }

    //entrate ricorrenti
    public String createRecurringIncomes(RecurringIncomeDto recurringIncomeDto) {
        LocalDate currentDate = recurringIncomeDto.getStartDate();

        do {
            Income baseIncome = new Income();
            baseIncome.setAccount(accountService.getAccountById(recurringIncomeDto.getAccountId()));
            baseIncome.setAmount(recurringIncomeDto.getAmount());
            baseIncome.setTag(recurringIncomeDto.getTag());
            baseIncome.setComment(recurringIncomeDto.getComment());
            baseIncome.setCategory(recurringIncomeDto.getCategory());
            baseIncome.setRecurring(recurringIncomeDto.isRecurring());
            baseIncome.setDate(currentDate);

            incomeRepository.save(baseIncome);
            currentDate = currentDate.plusDays(recurringIncomeDto.getIntervalDays());

        } while (!currentDate.isAfter(recurringIncomeDto.getEndDate()));

        return "Recurring income correctly created";

    }

    public List<Income> getIncomesByAccountId(int accountId) {
        return incomeRepository.findByAccountId(accountId);
    }

    public List<Income> getRecentIncomesByAccountId(int accountId) {
        LocalDate today = LocalDate.now();
        List<Income> recentIncomes = incomeRepository.findRecentIncomesByAccountId(accountId, today);
        if (recentIncomes.size() > 10) {
            recentIncomes = recentIncomes.subList(0, 10);
        }
        return recentIncomes;
    }
}
