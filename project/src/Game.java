import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game {
    
    BufferedImage background;
    BufferedImage img;
    ImageIcon startIcon;
    ImageIcon exitIcon;
    JFrame window;
    JPanel mainPanel;
    Sound sfxSound = new Sound();

    public Game() {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Maze");
        window.setPreferredSize(new Dimension(525, 725));
        window.setBackground(new Color(59, 69, 96));

        try {
            background = ImageIO.read(getClass().getResource("/img/mainMenu.png"));
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        try {
            img = ImageIO.read(getClass().getResource("/img/startMenu.png"));
            Image scaledImage = img.getScaledInstance(175, 64, Image.SCALE_SMOOTH);
            startIcon = new ImageIcon(scaledImage);

            img = ImageIO.read(getClass().getResource("/img/exitMenu.png"));
            scaledImage = img.getScaledInstance(175, 64, Image.SCALE_SMOOTH);
            exitIcon = new ImageIcon(scaledImage);

        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        JButton start = new JButton(startIcon);
        start.setBounds(180, 430, 175, 64);
        start.setContentAreaFilled(false);
        start.setBorderPainted(false);
        start.setFocusPainted(false);
        start.addActionListener(e -> {
            sfxSound.setFile(8); // assuming index 7 is click.wav
            sfxSound.playOnce();
            GamePanel gamePanel = new GamePanel(this);
            window.setPreferredSize(new Dimension(gamePanel.SCREEN_WIDTH, gamePanel.SCREEN_HEIGHT + 180));
            
            window.setContentPane(gamePanel); // Ganti mainPanel dengan gamePanel
            window.revalidate();
            window.repaint();

            gamePanel.requestFocusInWindow(); // agar keyboard terdeteksi
            gamePanel.startgameThread();
        });
        
        JButton exit = new JButton(exitIcon);
        exit.setBounds(180, 550, 175, 64);
        exit.setContentAreaFilled(false);
        exit.setBorderPainted(false);
        exit.setFocusPainted(false);
        exit.addActionListener(e -> {
            sfxSound.setFile(8); // assuming index 7 is click.wav
            sfxSound.playOnce();
            System.out.println("Exit button clicked");
            System.exit(0);
        });
        
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (background != null) {
                    g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        mainPanel.setLayout(null); // Supaya bisa pakai setBounds untuk tombol
        mainPanel.setPreferredSize(new Dimension(525, 725));

        // Tambahkan tombol ke panel
        mainPanel.add(start);
        mainPanel.add(exit);

        // Tambahkan panel ke window
        window.setContentPane(mainPanel);
        
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

    }

    public void returnToMenu() {
        window.setContentPane(mainPanel);
        window.revalidate();
        window.repaint();
    }
}
