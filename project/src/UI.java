import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class UI extends JPanel {
    GamePanel gp;
    Graphics2D g2;
    BufferedImage img;

    public UI(GamePanel gp) {
        this.gp = gp;
        try {
            img = ImageIO.read(new File("Maze-With-Solver/project/img/header.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void draw(Graphics2D g2) {
        this.g2 = g2;
        
        if (gp.gamestate == gp.WIN_STATE) {
            g2.setColor(Color.white);
            g2.setFont(new Font("Arial", Font.PLAIN, 50));
            String text = "YOU WIN";
            int x = gp.SCREEN_WIDTH / 2 - g2.getFontMetrics().stringWidth(text) / 2;
            int y = gp.SCREEN_HEIGHT / 2 + 70;
            g2.drawString(text, x, y);
        } 
        if (gp.gamestate == gp.SOLVING_STATE) {

        }
        if (gp.gamestate == gp.PLAYER_STATE) {

        }
        if (gp.gamestate == gp.LOSE_STATE) {
            String text = "YOU LOSE";
            int x = gp.SCREEN_WIDTH / 2 - g2.getFontMetrics().stringWidth(text) / 2;
            int y = gp.SCREEN_HEIGHT / 2;
            g2.drawString(text, x, y);
        }
        
        
        g2.setColor(Color.white);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.drawImage(img, 0, 0, gp.SCREEN_WIDTH, 210, null);
        g2.drawString("Stage: 1", 50, 150);
    }
}
