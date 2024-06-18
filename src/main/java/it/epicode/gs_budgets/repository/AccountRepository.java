package it.epicode.gs_budgets.repository;

import it.epicode.gs_budgets.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
  Account findByUserId(int id);
}
