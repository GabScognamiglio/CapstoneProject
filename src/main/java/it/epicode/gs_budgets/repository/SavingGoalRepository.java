package it.epicode.gs_budgets.repository;

import it.epicode.gs_budgets.entity.SavingGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingGoalRepository extends JpaRepository<SavingGoal, Integer> {

    @Query("SELECT sg FROM SavingGoal sg WHERE sg.account.id = :accountId")
    List<SavingGoal> findByAccountId(@Param("accountId") int userId);
}