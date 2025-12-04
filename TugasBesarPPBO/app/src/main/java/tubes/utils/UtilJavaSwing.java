package tubes.utils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class UtilJavaSwing {

    public static JFrame generateFrame(String title, int width, int height) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);
        return frame;
    }

    public static JButton generateButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150,50));
        return button;
    }

    public static JLabel generateImage(String path, int width, int height) {
        try {
            URL url = UtilJavaSwing.class.getResource(path);

            if (url == null) {
                System.out.println("Image not found!");
                return null;
            }

            ImageIcon bgImage = new ImageIcon(url);
            Image image = bgImage.getImage();
            Image newImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);

            return new JLabel(new ImageIcon(newImage));

        } catch (Exception e) {
            System.out.println("Image load error: " + e.getMessage());
            return null;
        }
    }
}
