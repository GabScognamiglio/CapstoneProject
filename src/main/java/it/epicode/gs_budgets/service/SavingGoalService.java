package it.epicode.gs_budgets.service;

import it.epicode.gs_budgets.dto.SavingGoalDto;
import it.epicode.gs_budgets.entity.Expense;
import it.epicode.gs_budgets.entity.SavingGoal;
import it.epicode.gs_budgets.enums.ExpenseCategory;
import it.epicode.gs_budgets.exception.NotFoundException;
import it.epicode.gs_budgets.repository.ExpenseRepository;
import it.epicode.gs_budgets.repository.SavingGoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SavingGoalService {
    @Autowired
    private SavingGoalRepository savingGoalRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ExpenseRepository expenseRepository;

    public List<SavingGoal> getSavingGoalsByUserId(int accountId) {
        return savingGoalRepository.findByAccountId(accountId);
    }

    public SavingGoal getSavingGoalById(int id) {
        if (savingGoalRepository.findById(id).isPresent()) {
            return savingGoalRepository.findById(id).get();
        }else {
            throw new NotFoundException("Saving goal with id: " + id + " not found");
        }
    }

    public String saveSavingGoal(SavingGoalDto savingGoalDto){
        SavingGoal savingGoal = new SavingGoal();

        savingGoal.setName(savingGoalDto.getName());
        savingGoal.setDescription(savingGoalDto.getDescription());
        savingGoal.setTargetAmount(savingGoalDto.getTargetAmount());
        savingGoal.setAccount(accountService.getAccountById(savingGoalDto.getAccountId()));

        savingGoalRepository.save(savingGoal);
        return "Saving goal correctly created";
    }


    public SavingGoal updateSavingGoal(int id, SavingGoalDto savingGoalDto) {
        SavingGoal savingGoal = getSavingGoalById(id);

        savingGoal.setName(savingGoalDto.getName());
        savingGoal.setDescription(savingGoalDto.getDescription());
        savingGoal.setTargetAmount(savingGoalDto.getTargetAmount());
        savingGoal.setAccount(accountService.getAccountById(savingGoalDto.getAccountId()));

        savingGoalRepository.save(savingGoal);
        return savingGoal;
    }

    public SavingGoal updateSavingGoalAndCreateExpense(int savingGoalId, double newSavedAmount) {
        SavingGoal savingGoal = savingGoalRepository.findById(savingGoalId)
                .orElseThrow(() -> new RuntimeException("SavingGoal not found with id: " + savingGoalId));

        double previousSavedAmount = savingGoal.getSavedAmount();
        double increaseAmount = newSavedAmount - previousSavedAmount;
        savingGoal.setSavedAmount(newSavedAmount);

        if (increaseAmount > 0) {
            Expense expense = new Expense();
            expense.setAccount(savingGoal.getAccount());
            expense.setAmount(increaseAmount);
            expense.setDate(LocalDate.now());
            expense.setTag("Obiettivi");
            expense.setComment("Deposito per obiettivo: " + savingGoal.getName() + " (goalID:" + savingGoal.getId() + ").");
            expense.setCategory(ExpenseCategory.OBIETTIVI);

            expenseRepository.save(expense);
        }

        return savingGoalRepository.save(savingGoal);
    }

    public String deleteSavingGoal(int id) {
        savingGoalRepository.delete(getSavingGoalById(id));
        return "Saving goal with id " + id + " correctly deleted";
    }

}
