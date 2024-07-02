package it.epicode.gs_budgets.service;


import it.epicode.gs_budgets.dto.UserLoginDto;
import it.epicode.gs_budgets.entity.User;
import it.epicode.gs_budgets.exception.UnauthorizedException;
import it.epicode.gs_budgets.security.AuthResponse;
import it.epicode.gs_budgets.security.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTool jwtTool;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponse authenticateUserAndCreateToken(UserLoginDto userLoginDto) {
        User user = userService.getUserByEmail(userLoginDto.getEmail());

        if (passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            String token = jwtTool.createToken(user);
            return new AuthResponse(token,user);
        } else {
            throw new UnauthorizedException("Wrong password!");
        }
    }
}
