package it.epicode.gs_budgets.repository;

import it.epicode.gs_budgets.entity.Expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    List<Expense> findByAccountId(int accountId);
}
