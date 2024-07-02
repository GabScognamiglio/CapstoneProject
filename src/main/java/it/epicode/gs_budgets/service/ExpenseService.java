package it.epicode.gs_budgets.service;

import it.epicode.gs_budgets.dto.ExpenseDto;
import it.epicode.gs_budgets.dto.RecurringExpenseDto;
import it.epicode.gs_budgets.entity.Expense;
import it.epicode.gs_budgets.entity.Income;
import it.epicode.gs_budgets.exception.NotFoundException;
import it.epicode.gs_budgets.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private AccountService accountService;

    public String saveExpense(ExpenseDto expenseDto) {
        Expense expense = new Expense();
        expense.setAccount(accountService.getAccountById(expenseDto.getAccountId()));
        expense.setAmount(expenseDto.getAmount());
        expense.setTag(expenseDto.getTag());
        expense.setComment(expenseDto.getComment());
        expense.setDate(expenseDto.getDate());
        expense.setCategory(expenseDto.getCategory());
        expense.setRecurring(expenseDto.isRecurring());

        expenseRepository.save(expense);
        return "Expense with id " + expense.getId() + " correctly saved for account with id: " + expense.getAccount().getId();
    }

    public Page<Expense> getExpenses(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return expenseRepository.findAll(pageable);
    }

    public Expense getExpenseById(int id) {
        if (expenseRepository.findById(id).isPresent()) {
            return expenseRepository.findById(id).get();
        } else {
            throw new NotFoundException("Expense with id: " + id + " not found");
        }
    }

    public Expense updateExpense(int id, ExpenseDto expenseDto) {
        Expense expense = getExpenseById(id);
        expense.setAccount(accountService.getAccountById(expenseDto.getAccountId()));
        expense.setAmount(expenseDto.getAmount());
        expense.setTag(expenseDto.getTag());
        expense.setComment(expenseDto.getComment());
        expense.setDate(expenseDto.getDate());
        expense.setCategory(expenseDto.getCategory());
        expense.setRecurring(expenseDto.isRecurring());


        expenseRepository.save(expense);
        return expense;
    }

    public String deleteExpense(int id) {
        expenseRepository.delete(getExpenseById(id));
        return "Expense with id " + id + " correctly deleted";
    }

    //Creazione di spese ricorrenti

    public String createRecurringExpenses(RecurringExpenseDto recurringExpenseDto){
        LocalDate currentDate= recurringExpenseDto.getStartDate();

        do {
            Expense baseExpense = new Expense();
            baseExpense.setAccount(accountService.getAccountById(recurringExpenseDto.getAccountId()));
            baseExpense.setAmount(recurringExpenseDto.getAmount());
            baseExpense.setTag(recurringExpenseDto.getTag());
            baseExpense.setComment(recurringExpenseDto.getComment());
            baseExpense.setCategory(recurringExpenseDto.getCategory());
            baseExpense.setRecurring(recurringExpenseDto.isRecurring());
            baseExpense.setDate(currentDate);

            expenseRepository.save(baseExpense);
            currentDate = currentDate.plusDays(recurringExpenseDto.getIntervalDays());

        } while (!currentDate.isAfter(recurringExpenseDto.getEndDate()));

        return "Recurring expense correctly created";

    }

    public List<Expense> getExpenseByAccountId(int accountId){
        return expenseRepository.findByAccountId(accountId);
    }

    public List<Expense> getRecentExpensesByAccountId(int accountId) {
        LocalDate today = LocalDate.now();
        List<Expense> recentExpenses = expenseRepository.findRecentExpensesByAccountId(accountId, today);
        if (recentExpenses.size() > 10) {
            recentExpenses = recentExpenses.subList(0, 10);
        }
        return recentExpenses;
    }
}
