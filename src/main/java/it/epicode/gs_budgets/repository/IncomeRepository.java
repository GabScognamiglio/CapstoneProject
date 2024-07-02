package it.epicode.gs_budgets.repository;

import it.epicode.gs_budgets.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Integer> {
    List<Income> findByAccountId(int accountId);

    @Query("SELECT e FROM Income e WHERE e.account.id = :accountId AND e.date <= :today ORDER BY e.date DESC")
    List<Income> findRecentIncomesByAccountId(@Param("accountId") int accountId, @Param("today") LocalDate today);
}
