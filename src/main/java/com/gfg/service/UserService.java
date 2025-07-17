package com.gfg.service;

import com.gfg.model.User;
import com.gfg.repository.UserRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Transparent 1x1 dummy image (replace with real Base64 later)
    private final String dummyImageBase64 =
            "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAACklEQVR42mP4//8/AAX+Av5B4ialAAAAAElFTkSuQmCC";

    public User createUser(User user) {
        if (user.getQrCodeData() == null) {
            user.setQrCodeData(generateQRCodeBase64("ID: " + user.getId() + ", Name: " + user.getName() + ", Role: " + user.getRole()));
        }

        if (user.getImageBase64() == null) {
            user.setImageBase64(dummyImageBase64);
        }

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    public User getOrCreateUser(Long id, String name, String role) {
        return userRepository.findById(id).orElseGet(() -> {
            User user = new User();
            user.setId(id);
            user.setName(name);
            user.setRole(role);
            user.setQrCodeData(generateQRCodeBase64("ID: " + id + ", Name: " + name + ", Role: " + role));
            user.setImageBase64(dummyImageBase64);
            user.setLeavesTaken(5);
            user.setLeavesLeft(15);
            return userRepository.save(user);
        });
    }

    public String generateQRCodeBase64(String text) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 150, 150);

            BufferedImage bufferedImage = new BufferedImage(150, 150, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < 150; x++) {
                for (int y = 0; y < 150; y++) {
                    int color = bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
                    bufferedImage.setRGB(x, y, color);
                }
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            return Base64.getEncoder().encodeToString(baos.toByteArray());

        } catch (WriterException | java.io.IOException e) {
            throw new RuntimeException("Error generating QR Code", e);
        }
    }

    // 🧪 Create dummy teachers on app startup
    @PostConstruct
    public void initDummyTeachers() {
        if (userRepository.count() == 0) {
            createUser(new User(null, "Anjali Sharma", "Math Teacher", null, dummyImageBase64, 3, 17));
            createUser(new User(null, "Ravi Kumar", "Science Teacher", null, dummyImageBase64, 5, 15));
            createUser(new User(null, "Preeti Kaur", "English Teacher", null, dummyImageBase64, 2, 18));
        }
    }
}
