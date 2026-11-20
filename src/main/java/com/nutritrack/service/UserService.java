package com.nutritrack.service;

import com.nutritrack.model.User;
import com.nutritrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return "Username already taken.";
        }
        // Simple plain-text password for now (no BCrypt to keep it simple)
        userRepository.save(user);
        return "ok";
    }

    public Optional<User> login(String username, String password) {
        Optional<User> opt = userRepository.findByUsername(username);
        if (opt.isPresent() && opt.get().getPassword().equals(password)) {
            return opt;
        }
        return Optional.empty();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }
}
