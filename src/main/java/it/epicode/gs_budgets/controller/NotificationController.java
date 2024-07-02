package it.epicode.gs_budgets.controller;

import it.epicode.gs_budgets.model.Notifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {
    @Autowired
    private SimpMessagingTemplate template;

    private Notifications notifications = new Notifications(0);

    @GetMapping("/notify")
    public String getNotification(@RequestParam("message") String message) {
        notifications.increment();
        notifications.addMessage(message);
        template.convertAndSend("/topic/notification", notifications);
        return "Notifications successfully sent to Angular!";
    }
}