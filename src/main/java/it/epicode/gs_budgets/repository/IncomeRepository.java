package it.epicode.gs_budgets.repository;

import it.epicode.gs_budgets.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Integer> {
    List<Income> findByAccountId(int accountId);
}
