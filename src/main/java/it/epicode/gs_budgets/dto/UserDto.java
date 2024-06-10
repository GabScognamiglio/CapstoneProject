package it.epicode.gs_budgets.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    @NotBlank
    @Size(max = 30)
    private String firstName;
    @NotBlank
    @Size(max = 30)
    private String lastName;
    @Past
    private LocalDate dateOfBirth;
    private String phoneNumber;
    @Email
    @NotBlank(message = "Inserire l'email, non può essere vuota o mancante" )
    private String email;
    @NotBlank(message = "Inserire la password, non può essere vuota o mancante" )
    private String password;

}
