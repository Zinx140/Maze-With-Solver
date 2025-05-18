import java.awt.BasicStroke;
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
            img = ImageIO.read(new File("project/img/header.png"));
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
            drawStats();
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

    public void drawStats() {
        int frameX = gp.TILE_SIZE * 1 + 20;
        int frameY = gp.TILE_SIZE * 6;
        int frameWidth = gp.TILE_SIZE * 12;
        int frameHeight = gp.TILE_SIZE * 13;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

}
