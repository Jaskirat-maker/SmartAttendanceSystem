package com.gfg.service;

import com.gfg.model.FootfallLog;
import com.gfg.model.User;
import com.gfg.repository.FootFallServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class FootFallService {

    @Autowired
    private FootFallServiceRepository footFallRepo;

    @Autowired
    private UserService userService;

    // Default attendance time window (8 AM to 10 AM)
    private final LocalTime defaultStart = LocalTime.of(8, 0);
    private final LocalTime defaultEnd = LocalTime.of(10, 0);

    public List<FootfallLog> getAllAttendanceLogs() {
        return footFallRepo.findAll();
    }

    /**
     * Marks attendance using default allowed time window.
     *
     * @param userId User ID
     * @param name   User name
     * @param role   User role
     * @return true if attendance marked, false if already marked
     */
    public boolean markAttendanceWithDetails(Long userId, String name, String role) {
        return markAttendanceInternal(userId, name, role, defaultStart, defaultEnd);
    }

    /**
     * Marks attendance using custom time window.
     *
     * @param userId       User ID
     * @param name         User name
     * @param role         User role
     * @param allowedStart Start of allowed time
     * @param allowedEnd   End of allowed time
     * @return true if attendance marked, false if already marked
     */
    public boolean markAttendanceWithDetails(Long userId, String name, String role, LocalTime allowedStart, LocalTime allowedEnd) {
        return markAttendanceInternal(userId, name, role, allowedStart, allowedEnd);
    }

    /**
     * Internal reusable logic for marking attendance.
     */
    private boolean markAttendanceInternal(Long userId, String name, String role, LocalTime allowedStart, LocalTime allowedEnd) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        // Validate attendance time window
        if (now.isBefore(allowedStart) || now.isAfter(allowedEnd)) {
            throw new RuntimeException("Attendance not allowed outside the allowed time slot.");
        }

        // Check if already marked today
        boolean alreadyMarked = footFallRepo.existsByUser_IdAndDate(userId, today);
        if (alreadyMarked) return false;

        // Get or create user
        User user = userService.getOrCreateUser(userId, name, role);

        // Create and save attendance log
        FootfallLog log = new FootfallLog();
        log.setUser(user);
        log.setDate(today);
        log.setTimestamp(LocalDateTime.now());
        footFallRepo.save(log);

        return true;
    }

    /**
     * Generic method to manually log any footfall entry.
     */
    public FootfallLog logFootfall(FootfallLog log) {
        return footFallRepo.save(log);
    }

    /**
     * Fetches all footfall logs (alias of getAllAttendanceLogs).
     */
    public List<FootfallLog> getAll() {
        return footFallRepo.findAll();
    }
}
