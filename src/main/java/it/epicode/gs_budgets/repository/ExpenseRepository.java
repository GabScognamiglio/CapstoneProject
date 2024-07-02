package it.epicode.gs_budgets.repository;

import it.epicode.gs_budgets.entity.Expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    List<Expense> findByAccountId(int accountId);


    @Query("SELECT e FROM Expense e WHERE e.account.id = :accountId AND e.date <= :today ORDER BY e.date DESC")
    List<Expense> findRecentExpensesByAccountId(@Param("accountId") int accountId, @Param("today") LocalDate today);
}
