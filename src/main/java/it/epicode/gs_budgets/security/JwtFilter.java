package it.epicode.gs_budgets.security;

import it.epicode.gs_budgets.entity.User;
import it.epicode.gs_budgets.exception.Error;
import it.epicode.gs_budgets.exception.NotFoundException;
import it.epicode.gs_budgets.exception.UnauthorizedException;
import it.epicode.gs_budgets.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTool jwtTool;
    @Autowired
    UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
//        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
//        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
//        response.setHeader("Access-Control-Allow-Credentials", "true");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Error in authorization, token is null or Bearer is null");
        } else {
            String token = authHeader.substring(7);
            jwtTool.verifyToken(token);

            int userIdInsideToken = jwtTool.getIdFromToken(token);
            User user = userService.getUserById(userIdInsideToken);

            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
//            try {
//
//            } catch (Exception e) {
//                Error error = new Error();
//                error.setMessage(e.getMessage());
//                error.setDate(LocalDateTime.now());
//                error.setStatus(HttpStatus.UNAUTHORIZED); // Impostare lo stato appropriato (es. UNAUTHORIZED)
//
//                // Creare un ResponseEntity con l'oggetto Error e lo stato appropriato
//                ResponseEntity<Object> errorResponse = new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
//
//                // Inviare la risposta di errore al client
//                response.setStatus(errorResponse.getStatusCode().value());
//                response.getWriter().write(errorResponse.getBody().toString());
//            }
        }

    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }

}