package com.auth_service.project.service;

import com.auth_service.project.persistence.model.User;
import com.auth_service.project.persistence.model.Role;
import com.auth_service.project.persistence.repository.RoleRepository;
import com.auth_service.project.persistence.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void assignRoleToUser(int userId, String roleName) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        user.getRoles().add(role);
        userRepository.save(user);
    }

    public void removeRoleFromUser(int userId, String roleName) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        user.getRoles().remove(role);
        userRepository.save(user);
    }
}
