package it.epicode.gs_budgets.controller;

import it.epicode.gs_budgets.dto.UserDto;
import it.epicode.gs_budgets.entity.User;
import it.epicode.gs_budgets.exception.BadRequestException;
import it.epicode.gs_budgets.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    //***Solo gli ADMIN possono manipolare gli utenti***

    @Autowired
    private UserService userService;

    @GetMapping("api/gs-budgets/users")
    @PreAuthorize("hasAuthority('ADMIN')") //solo chi è ADMIN è autorizzato
    public Page<User> getUsers(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "id") String sortBy) {
        return userService.getUsers(page, size, sortBy);
    }

    @GetMapping("api/gs-budgets/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @PutMapping("/api/gs-budgets/users/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@PathVariable int id, @RequestBody @Validated UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream().
                    map(objectError -> objectError.getDefaultMessage()).reduce("", (s, s2) -> s + " - " + s2));
            // " - " (riga sopra) serve per separare gli errori con un trattino tra uno e l'altro
        }
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/api/gs-budgets/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteUser(@PathVariable int id) {
        return userService.deleteUser(id);
    }

}
