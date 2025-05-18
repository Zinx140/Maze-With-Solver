import java.awt.Dimension;

import javax.swing.JFrame;

public class Game {
    public Game() {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Maze");
        
        // MenuPanel menuPanel = new MenuPanel();
        // window.add(menuPanel);

        GamePanel gamePanel = new GamePanel();
        window.setPreferredSize(new Dimension(gamePanel.SCREEN_WIDTH, gamePanel.SCREEN_HEIGHT + 250));
        window.add(gamePanel);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startgameThread();
    }
}
