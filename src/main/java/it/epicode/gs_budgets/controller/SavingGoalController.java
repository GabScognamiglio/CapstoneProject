package it.epicode.gs_budgets.controller;


import it.epicode.gs_budgets.dto.SavingGoalDto;
import it.epicode.gs_budgets.dto.SavingGoalDto;
import it.epicode.gs_budgets.entity.SavingGoal;
import it.epicode.gs_budgets.entity.SavingGoal;
import it.epicode.gs_budgets.exception.BadRequestException;
import it.epicode.gs_budgets.service.SavingGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/gs-budgets/saving-goals")
public class SavingGoalController {

    @Autowired
    private SavingGoalService savingGoalService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String saveSavingGoal(@RequestBody @Validated SavingGoalDto savingGoalDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors()
                    .stream().map(e -> e.getDefaultMessage()).reduce("", (s, s2) -> s + " - " + s2));
        }
        return savingGoalService.saveSavingGoal(savingGoalDto);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<SavingGoal> getSavingGoalsByUserId(@PathVariable int userId){
        return savingGoalService.getSavingGoalsByUserId(userId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public SavingGoal getSavingGoalById(@PathVariable int id) {
        return savingGoalService.getSavingGoalById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @ResponseStatus(HttpStatus.OK)
    public SavingGoal updateSavingGoal(@PathVariable int id, @RequestBody @Validated SavingGoalDto savingGoalDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream().
                    map(objectError -> objectError.getDefaultMessage()).reduce("", (s, s2) -> s + " - " + s2));
            // " - " (riga sopra) serve per separare gli errori con un trattino tra uno e l'altro
        }
        return savingGoalService.updateSavingGoal(id, savingGoalDto);
    }

    @PutMapping("/{savingGoalId}/increase-saved-amount")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public SavingGoal increaseSavedAmount(
            @PathVariable int savingGoalId,
            @RequestParam int newSavedAmount) {
        return savingGoalService.updateSavingGoalAndCreateExpense(savingGoalId, newSavedAmount);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String deleteSavingGoal(@PathVariable int id) {
        return savingGoalService.deleteSavingGoal(id);
    }


}
