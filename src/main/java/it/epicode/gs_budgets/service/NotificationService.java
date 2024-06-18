package it.epicode.gs_budgets.service;

import it.epicode.gs_budgets.entity.Expense;
import it.epicode.gs_budgets.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Scheduled(fixedRate = 18000)
    public void sendNotifications() {
        List<Expense> expenses = expenseRepository.findAll();
        for (Expense expense : expenses) {
            if (isNearDueDate(expense.getDate())) {
                sendNotification(expense);
            }
        }
    }

    private boolean isNearDueDate(LocalDate date) {
        LocalDate today = LocalDate.now();
        return date.minusDays(1).isEqual(today);
    }

    private void sendNotification(Expense expense) {
        String message = "Spesa in arrivo nella categoria: " + expense.getCategory();
        template.convertAndSend("/topic/notifications", message);
    }
}

