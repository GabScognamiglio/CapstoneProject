package it.epicode.gs_budgets.service;

import it.epicode.gs_budgets.dto.ExpenseDto;
import it.epicode.gs_budgets.dto.RecurringExpenseDto;
import it.epicode.gs_budgets.entity.Expense;
import it.epicode.gs_budgets.exception.NotFoundException;
import it.epicode.gs_budgets.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    public String saveExpense(ExpenseDto expenseDto) {
        Expense expense = new Expense();
        expense.setAccount(expenseDto.getAccount());
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
        expense.setAccount(expenseDto.getAccount());
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

//    public String createRecurringExpense(RecurringExpenseDto recurringExpenseDto){
//
//    }
}
