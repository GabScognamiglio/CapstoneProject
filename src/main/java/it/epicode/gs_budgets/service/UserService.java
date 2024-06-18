package it.epicode.gs_budgets.service;

import it.epicode.gs_budgets.dto.UserDto;
import it.epicode.gs_budgets.entity.Account;
import it.epicode.gs_budgets.entity.User;
import it.epicode.gs_budgets.exception.NotFoundException;
import it.epicode.gs_budgets.repository.AccountRepository;
import it.epicode.gs_budgets.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String saveUser(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setDateOfBirth(userDto.getDateOfBirth());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Account account =new Account();
        account.setName("Conto di " + user.getFirstName());
        account.setDescription("Conto personale");
        account.setUser(user);
        user.setAccount(account);
        userRepository.save(user);
        accountRepository.save(account);

        return "User with id " + user.getId() + " correctly saved";
    }

    public Page<User> getUsers(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userRepository.findAll(pageable);
    }

    public User getUserById(int id) {

        if (userRepository.findById(id).isPresent()) {
            return userRepository.findById(id).get();
        } else {
            throw new NotFoundException("User with id: " + id + " not found");
        }
    }

    public User updateUser(int id, UserDto userDto) {
        User user = getUserById(id);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setDateOfBirth(userDto.getDateOfBirth());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        userRepository.save(user);
        return user;
    }

    public String deleteUser(int id) {
            userRepository.delete(getUserById(id));
            return "User with id " + id + " correctly deleted";
    }

    public User getUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if(userOptional.isPresent()) {
            return userOptional.get();
        }
        else {
            throw new NotFoundException("User with email " + email + " not found.");
        }
    }
}
