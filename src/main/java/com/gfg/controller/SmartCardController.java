package com.gfg.controller;

import com.gfg.model.Attendancerequest;
import com.gfg.model.FootfallLog;
import com.gfg.model.User;
import com.gfg.service.FootFallService;
import com.gfg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SmartCardController {

    @Autowired
    private FootFallService footFallService;

    @Autowired
    private UserService userService;

    // POST to mark attendance - now matches updated service method signature
    @PostMapping("/mark")
    public ResponseEntity<String> markAttendance(@RequestBody Attendancerequest request) {
        boolean marked = footFallService.markAttendanceWithDetails(
                request.getUserId(),
                request.getName(),
                request.getRole()
        );
        return ResponseEntity.ok(marked ? "✅ Attendance marked!" : "⚠️ Already marked today.");
    }

    // Optional manual footfall log
    @PostMapping("/footfall")
    public FootfallLog logFootfall(@RequestBody FootfallLog log) {
        return footFallService.logFootfall(log);
    }

    // Get all footfall logs
    @GetMapping("/footfall")
    public List<FootfallLog> getAllFootfall() {
        return footFallService.getAll();
    }

    // Create new user
    @PostMapping("/user")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // Get all users
    @GetMapping("/user")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
