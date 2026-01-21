package com.auth_service.project.controller;

import com.auth_service.project.dto.MessageResponse;
import com.auth_service.project.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }


    @PostMapping("/users/{userId}/roles")
    public ResponseEntity<?> assignRole(
            @PathVariable int userId,
            @RequestParam String roleName
    ) {
        adminService.assignRoleToUser(userId, roleName);
        return ResponseEntity.ok(new MessageResponse("Rol asignado"));
    }


    @DeleteMapping("/users/{userId}/roles")
    public ResponseEntity<?> removeRole(
            @PathVariable int userId,
            @RequestParam String roleName
    ) {
        adminService.removeRoleFromUser(userId, roleName);
        return ResponseEntity.ok(new MessageResponse("Rol eliminado"));
    }
}
