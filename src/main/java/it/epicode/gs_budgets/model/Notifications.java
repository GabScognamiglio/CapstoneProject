package it.epicode.gs_budgets.model;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Notifications {
    private int count;
    private List<String> messages = new ArrayList<>();

    public Notifications(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }


    public void increment() {
        this.count++;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }
}
