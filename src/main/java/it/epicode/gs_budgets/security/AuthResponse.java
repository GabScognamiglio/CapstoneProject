package it.epicode.gs_budgets.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.epicode.gs_budgets.entity.User;
import lombok.Data;

@Data
public class AuthResponse {

    private String token;

    @JsonIgnoreProperties(value = "password")
    private User user;

    public AuthResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }
}
