package tubes.utils;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class UtilHashing {
    private static final String globalSalt = "rahasia123"; //Tentatif Belom Tentu Dibolehin

    // // Generate salt acak
    // public static String generateSalt() {
    // SecureRandom sr = new SecureRandom();
    // byte[] salt = new byte[16]; // 16 bytes = 128 bit
    // sr.nextBytes(salt);
    // return Base64.getEncoder().encodeToString(salt);
    // }

    // // Hash password dengan salt + iteration
    // public static String hashPassword(String password, String salt) {
    // try {
    // MessageDigest md = MessageDigest.getInstance("SHA-256");

    // // Gabungkan password + salt
    // String input = password + salt;
    // byte[] hashed = md.digest(input.getBytes());

    // // Lakukan hashing berulang (misal 1000 kali)
    // for (int i = 0; i < 1000; i++) {
    // md.update(hashed);
    // hashed = md.digest();
    // }

    // // Return hash dalam Base64
    // return Base64.getEncoder().encodeToString(hashed);

    // } catch (Exception e) {
    // throw new RuntimeException(e);
    // }

    // }
    // Hash password dengan salt + iteration
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Gabungkan password + salt
            String input = password + globalSalt;
            byte[] hashed = md.digest(input.getBytes());

            // Lakukan hashing berulang (misal 1000 kali)
            for (int i = 0; i < 1000; i++) {
                md.update(hashed);
                hashed = md.digest();
            }

            // Return hash dalam Base64
            return Base64.getEncoder().encodeToString(hashed);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Verifikasi password
    public static boolean verifyPassword(String password, String storedHash) {
        String hashedInput = hashPassword(password);
        return hashedInput.equals(storedHash);
    }

    // String password = "rahasia123";

    // // Generate salt untuk user baru
    // String salt = generateSalt();

    // // Hash password
    // String hash = hashPassword(password, salt);

    // System.out.println("Password: " + password);
    // System.out.println("Salt : " + salt);
    // System.out.println("Hash : " + hash);

    // // Verifikasi
    // boolean match = verifyPassword(password, salt, hash);
    // System.out.println("Cocok? : " + match);

}
