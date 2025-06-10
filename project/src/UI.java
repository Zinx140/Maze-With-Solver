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
    BufferedImage player;
    BufferedImage playerTransformed;
    BufferedImage spider;
    BufferedImage ogre;
    BufferedImage dragon;
    BufferedImage phx;
    int[][] dummyMap = new int[10][10];

    public UI(GamePanel gp) {
        this.gp = gp;
        try {
            img = ImageIO.read(new File("project/img/header.png"));
            player = ImageIO.read(new File("project/img/playerUI.png"));
            playerTransformed = ImageIO.read(new File("project/img/armoredUI.png"));
            spider = ImageIO.read(new File("project/img/spiderUI.png"));
            ogre = ImageIO.read(new File("project/img/ogreUI.png"));
            dragon = ImageIO.read(new File("project/img/dragonUI.png"));
            phx = ImageIO.read(new File("project/img/phoenixUI.png"));
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
        if (gp.gamestate == gp.PLAYER_STATE) {
            drawStats();
        }
        if (gp.gamestate == gp.LOSE_STATE) {
            g2.setColor(Color.white);
            g2.setFont(new Font("Arial", Font.PLAIN, 50));
            String text = "YOU LOSE";
            int x = gp.SCREEN_WIDTH / 2 - g2.getFontMetrics().stringWidth(text) / 2;
            int y = gp.SCREEN_HEIGHT / 2;
            g2.drawString(text, x, y);
        }

        g2.setColor(Color.white);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawImage(img, 0, 0, gp.SCREEN_WIDTH, 180, null);
        g2.drawString("Stage: " + (gp.currentMap + 1), 20, 125);
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.drawString("Gold: " + gp.player.gold, 20, 155);
    }

    public void drawStats() {
        int frameX = gp.TILE_SIZE * 1 + 20;
        int frameY = gp.TILE_SIZE * 6;
        int frameWidth = gp.TILE_SIZE * 12;
        int frameHeight = gp.TILE_SIZE * 13;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        g2.setFont(new Font("Sans Serif", Font.PLAIN, 20));
        g2.drawString("Player & Monsters Stats", gp.TILE_SIZE * 3 + 50, 250);
        
        g2.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        if (gp.player.isArmored) {
            g2.drawImage(playerTransformed, gp.TILE_SIZE * 3, 275, 32, 64, null);
        } else {
            g2.drawImage(player, gp.TILE_SIZE * 3, 275, 32, 64, null);
        }
        g2.drawString("Player", gp.TILE_SIZE * 4 + 20, 290);
        
        g2.drawImage(dragon, gp.TILE_SIZE * 7 + 20, 275, 64, 64, null);
        g2.drawString("Red Dragon", gp.TILE_SIZE * 9 + 20, 290);
        
        g2.drawImage(spider, gp.TILE_SIZE * 3 - 20, 400, 64, 64, null);
        g2.drawString("Spider", gp.TILE_SIZE * 4 + 30, 415);

        g2.drawImage(ogre, gp.TILE_SIZE * 7 + 20, 400, 64, 64, null);
        g2.drawString("Ogre", gp.TILE_SIZE * 9 + 20, 415);

        g2.drawImage(phx, gp.TILE_SIZE * 5 + 20, 525, 64, 64, null);
        g2.drawString("Phoenix", gp.TILE_SIZE * 7 + 20, 540);

        g2.setFont(new Font("Sans Serif", Font.PLAIN, 13));
        g2.drawString("HP: " + gp.player.playerHp, gp.TILE_SIZE * 4 + 20, 310);
        g2.drawString("ATK: " + gp.player.playerAtk, gp.TILE_SIZE * 4 + 20, 330);
        g2.drawString("Coin: " + gp.player.gold, gp.TILE_SIZE * 4 + 20, 350);

        Monster spider = new Monster(0, 0);
        spider.setSpider(dummyMap);
        Monster ogre = new Monster(0, 1);
        ogre.setOgre(dummyMap);
        Monster dragon = new Monster(0, 2);
        dragon.setDragon(dummyMap);
        Monster phoenix = new Monster(0, 3);
        phoenix.setPhx(dummyMap);
        
        g2.drawString("HP: 20", gp.TILE_SIZE * 9 + 20, 310);
        g2.drawString("ATK: 20", gp.TILE_SIZE * 9 + 20, 330);
        if (gp.player.clone().winBattle(dragon)) {
            g2.setColor(Color.green);
            g2.drawString("Can be killed", gp.TILE_SIZE * 9 + 20, 350);
        } else {
            g2.setColor(Color.red);
            g2.drawString("(Cannot be Killed)", gp.TILE_SIZE * 9 + 20, 350);
        }
        g2.setColor(Color.white);

        g2.drawString("HP: 30", gp.TILE_SIZE * 4 + 30, 435);
        g2.drawString("ATK: 30", gp.TILE_SIZE * 4 + 30, 455);
        if (gp.player.clone().winBattle(spider)) {
            g2.setColor(Color.green);
            g2.drawString("Can be killed", gp.TILE_SIZE * 4 + 30, 475);
        } else {
            g2.setColor(Color.red);
            g2.drawString("(Cannot be Killed)", gp.TILE_SIZE * 4 + 30, 475);
        }
        g2.setColor(Color.white);

        g2.drawString("HP: 30", gp.TILE_SIZE * 9 + 20, 435);
        g2.drawString("ATK: 30", gp.TILE_SIZE * 9 + 20, 455);
        if (gp.player.clone().winBattle(ogre)) {
            g2.setColor(Color.green);
            g2.drawString("Can be killed", gp.TILE_SIZE * 9 + 20, 475);
        } else {
            g2.setColor(Color.red);
            g2.drawString("(Cannot be Killed)", gp.TILE_SIZE * 9 + 20, 475);
        }
        g2.setColor(Color.white);

        g2.drawString("HP: 30", gp.TILE_SIZE * 7 + 20, 560);
        g2.drawString("ATK: 30", gp.TILE_SIZE * 7 + 20, 580);
        if (gp.player.clone().winBattle(phoenix)) {
            g2.setColor(Color.green);
            g2.drawString("Can be killed", gp.TILE_SIZE * 7 + 20, 600);
        } else {
            g2.setColor(Color.red);
            g2.drawString("(Cannot be Killed)", gp.TILE_SIZE * 7 + 20, 600);
        }
        g2.setColor(Color.white);

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
