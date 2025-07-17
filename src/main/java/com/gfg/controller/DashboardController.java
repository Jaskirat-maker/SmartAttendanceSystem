package com.gfg.controller;

import com.gfg.model.FootfallLog;
import com.gfg.model.User;
import com.gfg.service.FootFallService;
import com.gfg.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/attendance")
public class DashboardController {

    @Autowired
    private FootFallService footFallService;

    @Autowired
    private UserService userService; // ✅ Needed to load users

    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("message", "");

        List<FootfallLog> attendanceList = footFallService.getAllAttendanceLogs();
        List<User> users = userService.getAllUsers(); // ✅ Fetch users

        model.addAttribute("attendanceList", attendanceList);
        model.addAttribute("users", users); // ✅ Add to model

        return "dashboard";
    }

    @PostMapping("/form")
    public String markAttendance(@RequestParam("userId") Long userId,
                                 @RequestParam("name") String name,
                                 @RequestParam("role") String role,
                                 Model model) {

        boolean marked = footFallService.markAttendanceWithDetails(userId, name, role);
        String message;

        int hour = LocalTime.now().getHour();
        if (marked) {
            message = (hour < 12)
                    ? "✅ Good morning, " + name + "! Attendance marked."
                    : "✅ Good evening, " + name + "! Attendance marked.";
        } else {
            message = "⚠️ Already marked today.";
        }

        List<FootfallLog> attendanceList = footFallService.getAllAttendanceLogs();
        List<User> users = userService.getAllUsers(); // ✅ Also re-fetch users

        model.addAttribute("attendanceList", attendanceList);
        model.addAttribute("users", users);
        model.addAttribute("message", message);

        return "dashboard";
    }
}
