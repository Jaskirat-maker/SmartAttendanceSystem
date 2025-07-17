package com.gfg.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor // ✅ This adds the required constructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String role;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String qrCodeData; // base64 QR code string

    @Lob
    @Column(columnDefinition = "TEXT")
    private String imageBase64; // base64 profile image

    private int leavesTaken;

    private int leavesLeft;
}
