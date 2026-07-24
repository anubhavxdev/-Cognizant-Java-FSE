package com.cognizant.userservice.service;

import com.cognizant.userservice.entity.User;
import com.cognizant.userservice.exception.UserNotFoundException;
import com.cognizant.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Business logic layer for User management.
 */
@Service
@Transactional
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /** Create a new user. Throws if email is already taken. */
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + user.getEmail());
        }
        User saved = userRepository.save(user);
        log.info("Created user id={}, email={}", saved.getId(), saved.getEmail());
        return saved;
    }

    /** Retrieve all users. */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /** Get a user by ID; throws {@link UserNotFoundException} if absent. */
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                             .orElseThrow(() -> new UserNotFoundException(id));
    }

    /** Get a user by email. */
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                             .orElseThrow(() -> new UserNotFoundException(email));
    }

    /** Update an existing user's name and phone. */
    public User updateUser(Long id, User updates) {
        User user = getUserById(id);
        user.setName(updates.getName());
        user.setPhone(updates.getPhone());
        User updated = userRepository.save(user);
        log.info("Updated user id={}", id);
        return updated;
    }

    /** Delete a user by ID. */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
        log.info("Deleted user id={}", id);
    }
}
