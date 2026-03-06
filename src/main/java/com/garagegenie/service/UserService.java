package com.garagegenie.service;

import com.garagegenie.entity.User;
import com.garagegenie.exception.ResourceNotFoundException;
import com.garagegenie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepo;

    public List<User> getAll() {
        return userRepo.findAll();
    }

    public List<User> getCustomers() {
        return userRepo.findAll().stream()
                .filter(u -> u.getRole() == User.Role.CUSTOMER)
                .toList();
    }

    public User getById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    public User updateUser(Long id, String name, String phone) {
        User user = getById(id);
        if (name != null)
            user.setName(name);
        if (phone != null)
            user.setPhone(phone);
        return userRepo.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepo.existsById(id))
            throw new ResourceNotFoundException("User", id);
        userRepo.deleteById(id);
    }
}
