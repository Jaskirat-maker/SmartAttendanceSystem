package com.gfg.controller;

import com.gfg.model.FootfallLog;
import com.gfg.service.FootFallService;
import com.gfg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@Controller
public class AttendanceController {

    @Autowired
    private FootFallService footFallService;

    @Autowired
    private UserService userService;

    // Allowed attendance time window (8 AM to 10 AM)
    private final LocalTime allowedStart = LocalTime.of(8, 0);
    private final LocalTime allowedEnd = LocalTime.of(10, 0);

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List<FootfallLog> attendanceList = footFallService.getAllAttendanceLogs();
        model.addAttribute("attendanceList", attendanceList);
        return "dashboard";  // Thymeleaf template name (dashboard.html)
    }

    @PostMapping("/dashboard")
    public String markAttendance(
            @RequestParam Long userId,
            @RequestParam String name,
            @RequestParam String role,
            Model model) {

        String message;
        try {
            boolean marked = footFallService.markAttendanceWithDetails(userId, name, role, allowedStart, allowedEnd);
            if (marked) {
                message = "Attendance marked successfully for user " + name;
            } else {
                message = "Attendance already marked today for user " + name;
            }
        } catch (RuntimeException e) {
            message = "Error: " + e.getMessage();
        }

        // Reload attendance list and message
        List<FootfallLog> attendanceList = footFallService.getAllAttendanceLogs();
        model.addAttribute("attendanceList", attendanceList);
        model.addAttribute("message", message);

        return "dashboard";
    }
}
