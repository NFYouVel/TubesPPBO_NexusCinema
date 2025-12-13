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

    public static JButton generateButton(String text, int width, int height) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width,height));
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

    public static JLabel getMovieIconLabel(String movieTitle) {
        String imagePath;
        switch (movieTitle) {
            case "Kimetsu No Yaiba: Infinity Castle Arc":
                imagePath = "/assets/images/kimetsu_1.png";
                break;
            case "Pabrik Gula":
                imagePath = "/assets/images/pabrik_gula.png";
                break;
            case "Top Gun: Maverick":
                imagePath = "/assets/images/top_gun_maverick.png";
                break;
            case "Mission: Impossible - Dead Reckoning":
                imagePath = "/assets/images/mission_impossible.png";
                break;
            default:
                return new JLabel("No Image");
        }
        return UtilJavaSwing.generateImage(imagePath, 300, 400);
    }
}
