package it.epicode.gs_budgets.controller;



import it.epicode.gs_budgets.dto.UserDto;
import it.epicode.gs_budgets.dto.UserLoginDto;
import it.epicode.gs_budgets.exception.BadRequestException;
import it.epicode.gs_budgets.security.AuthResponse;
import it.epicode.gs_budgets.service.AuthService;
import it.epicode.gs_budgets.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/auth/register")
    public String register(@RequestBody @Validated UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors()
                    .stream().map(e -> e.getDefaultMessage()).reduce("", (s1, s2) -> s1 + s2));
        }
        return userService.saveUser(userDto);
    }

    @PostMapping("/auth/login")
    public AuthResponse login(@RequestBody @Validated UserLoginDto userLoginDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors()
                    .stream().map(e -> e.getDefaultMessage()).reduce("", (s1, s2) -> s1 + s2));
        }

        return authService.authenticateUserAndCreateToken(userLoginDto);
    }
}

