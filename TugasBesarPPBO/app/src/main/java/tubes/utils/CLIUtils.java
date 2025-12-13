package tubes.utils;

import java.util.Random;

import javax.swing.JOptionPane;

public class CLIUtils {
    private static final Random random = new Random();

    public static String generateOTP() {
        int number = random.nextInt(900000) + 100000;
        return String.valueOf(number);
    }

    public static String showInputDialog(String message, String title) {
        return JOptionPane.showInputDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
    }

    public static void showInformationMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
