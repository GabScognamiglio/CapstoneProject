package it.epicode.gs_budgets.service;

import it.epicode.gs_budgets.dto.AccountDto;
import it.epicode.gs_budgets.entity.Account;
import it.epicode.gs_budgets.entity.Expense;
import it.epicode.gs_budgets.entity.Income;
import it.epicode.gs_budgets.exception.NotFoundException;
import it.epicode.gs_budgets.model.BalanceEntry;
import it.epicode.gs_budgets.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserService userService;

    public String saveAccount(AccountDto accountDto) {
        Account account = new Account();
        account.setName(accountDto.getName());
        account.setDescription(accountDto.getDescription());
        account.setUser(userService.getUserById(accountDto.getUserId()));

        accountRepository.save(account);
        return "Account with id " + account.getId() + " correctly saved for user with id: " + account.getUser().getId();
    }

    public Page<Account> getAccounts(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return accountRepository.findAll(pageable);
    }

    public Account getAccountById(int id) {

        if (accountRepository.findById(id).isPresent()) {
            return accountRepository.findById(id).get();
        } else {
            throw new NotFoundException("Account with id: " + id + " not found");
        }
    }

    public Account updateAccount(int id, AccountDto accountDto) {
        Account account = getAccountById(id);
        account.setName(accountDto.getName());
        account.setDescription(accountDto.getDescription());
        account.setUser(userService.getUserById(accountDto.getUserId()));

        accountRepository.save(account);
        return account;
    }

    public String deleteAccount(int id) {
        accountRepository.delete(getAccountById(id));
        return "Account with id " + id + " correctly deleted";
    }


    //PER AVERE GLI ACCOUNT DELL'UTENTE LOGGATO
    public Account getAccountByUserId(int userId) {
        return accountRepository.findByUserId(userId);
    }


    //PER AVERE I BILANCI DETTAGLIATI, ANNUALI, MENSILI, SETTIMANALI...

    public BalanceEntry getAccountTotalBalance(int accountId) {
        Account account = getAccountById(accountId);
        return calculateTotalBalance(account);
    }

    public BalanceEntry getAccountBalanceLast12Months(int accountId) {
        Account account = getAccountById(accountId);
        return calculateBalanceLastMonths(account, 12);
    }

    public BalanceEntry getAccountBalanceLastMonth(int accountId) {
        Account account = getAccountById(accountId);
        return calculateBalanceCurrentMonth(account);
    }

    public BalanceEntry getAccountBalanceLastWeek(int accountId) {
        Account account = getAccountById(accountId);
        return calculateBalanceLastWeek(account);
    }


    //BILANCIO + SPESE + ENTRATE MESE PER MESE ULTIMI 12
//    public Map<String, BalanceEntry> getAccountBalancesMonthlyLast12Months(int accountId) {
//        Account account = getAccountById(accountId);
//        Map<String, BalanceEntry> balances = new LinkedHashMap<>();
//
//        LocalDate today = LocalDate.now();
//        LocalDate startDate = today.minusMonths(12).withDayOfMonth(1);
//        LocalDate endDate = today.withDayOfMonth(1).minusDays(1);
//
//        LocalDate tempDate = startDate;
//        while (!tempDate.isAfter(endDate)) {
//            LocalDate finalTempDate = tempDate;
//
//            double totalIncome = account.getIncomes().stream()
//                    .filter(income -> income.getDate().getMonth() == finalTempDate.getMonth() && income.getDate().getYear() == finalTempDate.getYear())
//                    .mapToDouble(Income::getAmount)
//                    .sum();
//
//            double totalExpense = account.getExpenses().stream()
//                    .filter(expense -> expense.getDate().getMonth() == finalTempDate.getMonth() && expense.getDate().getYear() == finalTempDate.getYear())
//                    .mapToDouble(Expense::getAmount)
//                    .sum();
//
//            double balance = totalIncome - totalExpense;
//
//            BalanceEntry entry = new BalanceEntry();
//            entry.setBalance(balance);
//            entry.setTotalIncome(totalIncome);
//            entry.setTotalExpense(totalExpense);
//
//            balances.put(finalTempDate.getMonth().toString() + " " + finalTempDate.getYear(), entry);
//            tempDate = tempDate.plusMonths(1);
//        }
//
//        return balances;
//    }

    public Map<String, BalanceEntry> getAccountBalancesMonthlyLast12Months(int accountId) {
        Account account = getAccountById(accountId);
        Map<String, BalanceEntry> balances = new LinkedHashMap<>();

        LocalDate today = LocalDate.now();
        // Calcola la data di inizio 12 mesi fa da oggi
        LocalDate startDate = today.minusMonths(12).withDayOfMonth(1);
        // Calcola la data di fine come il mese precedente rispetto ad oggi
        LocalDate endDate = today.withDayOfMonth(1).minusDays(1);

        // Parti da luglio dell'anno precedente rispetto all'anno corrente
        LocalDate tempDate = startDate.plusMonths(1).withMonth(7);

        while (!tempDate.isAfter(endDate)) {
            calculateBalanceForDate(account, tempDate, balances);

            // Avanza al mese successivo
            tempDate = tempDate.plusMonths(1);
        }

        // Calcola il bilancio anche per il mese corrente (giugno 2024)
        if (today.getMonthValue() == 6) { // Se siamo a giugno
            calculateBalanceForDate(account, today, balances);
        }

        return balances;
    }

    private void calculateBalanceForDate(Account account, LocalDate date, Map<String, BalanceEntry> balances) {
        double totalIncome = account.getIncomes().stream()
                .filter(income -> income.getDate().getMonth() == date.getMonth() && income.getDate().getYear() == date.getYear())
                .mapToDouble(Income::getAmount)
                .sum();

        double totalExpense = account.getExpenses().stream()
                .filter(expense -> expense.getDate().getMonth() == date.getMonth() && expense.getDate().getYear() == date.getYear())
                .mapToDouble(Expense::getAmount)
                .sum();

        double balance = totalIncome - totalExpense;

        BalanceEntry entry = new BalanceEntry();
        entry.setBalance(balance);
        entry.setTotalIncome(totalIncome);
        entry.setTotalExpense(totalExpense);

        balances.put(date.getMonth().toString() + " " + date.getYear(), entry);
    }



    //BILANCIO + SPESE + ENTRATE SETTIMANA PER SETTIMANA ULTIME 4
    public Map<String, BalanceEntry> getAccountBalancesWeeklyLast4Weeks(int accountId) {
        Account account = getAccountById(accountId);
        Map<String, BalanceEntry> balances = new LinkedHashMap<>();

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusWeeks(4).with(DayOfWeek.MONDAY);
        LocalDate endDate = today.with(DayOfWeek.SUNDAY);

        LocalDate tempDate = startDate;
        while (!tempDate.isAfter(endDate)) {
            LocalDate finalTempDate = tempDate;

            double totalIncome = account.getIncomes().stream()
                    .filter(income -> income.getDate().isAfter(finalTempDate.minusDays(1)) && income.getDate().isBefore(finalTempDate.plusDays(7)))
                    .mapToDouble(Income::getAmount)
                    .sum();

            double totalExpense = account.getExpenses().stream()
                    .filter(expense -> expense.getDate().isAfter(finalTempDate.minusDays(1)) && expense.getDate().isBefore(finalTempDate.plusDays(7)))
                    .mapToDouble(Expense::getAmount)
                    .sum();

            double balance = totalIncome - totalExpense;

            BalanceEntry entry = new BalanceEntry();
            entry.setBalance(balance);
            entry.setTotalIncome(totalIncome);
            entry.setTotalExpense(totalExpense);

            balances.put(finalTempDate.toString(), entry);
            tempDate = tempDate.plusWeeks(1);
        }

        return balances;
    }



    // Metodi privati per calcolare i bilanci

    //BILANCIO TOTALE

    private BalanceEntry calculateTotalBalance(Account account) {
        LocalDate today = LocalDate.now();

        double totalIncome = account.getIncomes().stream()
                .filter(income -> !income.getDate().isAfter(today)) // Filtra le entrate future
                .mapToDouble(Income::getAmount)
                .sum();

        double totalExpense = account.getExpenses().stream()
                .filter(expense -> !expense.getDate().isAfter(today)) // Filtra le spese future
                .mapToDouble(Expense::getAmount)
                .sum();

        double balance = totalIncome - totalExpense;

        BalanceEntry entry = new BalanceEntry();
        entry.setBalance(balance);
        entry.setTotalIncome(totalIncome);
        entry.setTotalExpense(totalExpense);

        return entry;
    }

    //BILANCIO ULTIMI MESI (A DISCREZIONE UTENTE)
    private BalanceEntry calculateBalanceLastMonths(Account account, int months) {
        LocalDate today = LocalDate.now();
        LocalDate monthsAgo = today.minusMonths(months);

        // Per evitare operazioni future, definiamo una data massima pari a oggi.
        LocalDate maxDate = today;

        double totalIncome = account.getIncomes().stream()
                .filter(income -> !income.getDate().isAfter(maxDate) && income.getDate().isAfter(monthsAgo))
                .mapToDouble(Income::getAmount)
                .sum();

        double totalExpense = account.getExpenses().stream()
                .filter(expense -> !expense.getDate().isAfter(maxDate) && expense.getDate().isAfter(monthsAgo))
                .mapToDouble(Expense::getAmount)
                .sum();

        double balance = totalIncome - totalExpense;

        BalanceEntry entry = new BalanceEntry();
        entry.setBalance(balance);
        entry.setTotalIncome(totalIncome);
        entry.setTotalExpense(totalExpense);

        return entry;
    }


    //BILANCIO ULTIMO MESE
    private BalanceEntry calculateBalanceCurrentMonth(Account account) {
        LocalDate today = LocalDate.now();
        LocalDate currentMonthStart = today.withDayOfMonth(1); // Primo giorno del mese corrente
        LocalDate currentMonthEnd = today.withDayOfMonth(today.lengthOfMonth()); // Ultimo giorno del mese corrente

        double totalIncome = account.getIncomes().stream()
                .filter(income -> income.getDate().isAfter(currentMonthStart.minusDays(1)) && income.getDate().isBefore(currentMonthEnd.plusDays(1)))
                .mapToDouble(Income::getAmount)
                .sum();

        double totalExpense = account.getExpenses().stream()
                .filter(expense -> expense.getDate().isAfter(currentMonthStart.minusDays(1)) && expense.getDate().isBefore(currentMonthEnd.plusDays(1)))
                .mapToDouble(Expense::getAmount)
                .sum();

        double balance = totalIncome - totalExpense;

        BalanceEntry entry = new BalanceEntry();
        entry.setBalance(balance);
        entry.setTotalIncome(totalIncome);
        entry.setTotalExpense(totalExpense);

        return entry;
    }


    //BILANCIO ULTIMA SETTIMANA
    private BalanceEntry calculateBalanceLastWeek(Account account) {
        LocalDate today = LocalDate.now();
        LocalDate lastWeekStart = today.minusWeeks(1).with(DayOfWeek.MONDAY);
        LocalDate lastWeekEnd = today.minusWeeks(1).with(DayOfWeek.SUNDAY);

        double totalIncome = account.getIncomes().stream()
                .filter(income -> income.getDate().isAfter(lastWeekStart) && income.getDate().isBefore(lastWeekEnd.plusDays(1)))
                .mapToDouble(Income::getAmount)
                .sum();

        double totalExpense = account.getExpenses().stream()
                .filter(expense -> expense.getDate().isAfter(lastWeekStart) && expense.getDate().isBefore(lastWeekEnd.plusDays(1)))
                .mapToDouble(Expense::getAmount)
                .sum();

        double balance = totalIncome - totalExpense;

        BalanceEntry entry = new BalanceEntry();
        entry.setBalance(balance);
        entry.setTotalIncome(totalIncome);
        entry.setTotalExpense(totalExpense);

        return entry;
    }



}
