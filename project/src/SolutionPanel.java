import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class SolutionPanel extends JPanel {
    public int indexSolution = 0;
    GamePanel gp;
    int panelWidth;
    int panelHeight;
    int x;
    int y;
    int panelWidth1;
    int panelHeight1;
    int x1;
    int y1;
    int tileSize;
    ArrayList<Solution> currentSolution = new ArrayList<>();
    ArrayList<Solution> bestHP = new ArrayList<>();
    ArrayList<Solution> bestPath = new ArrayList<>();
    ArrayList<Solution> bestGold = new ArrayList<>();
    ArrayList<Solution> allSolution = new ArrayList<>();
    int bestMaxHP = 0;
    int bestMaxPath = 1000;
    int bestMaxGold = 0;
    
    ImageIcon closeIcon;
    ImageIcon prevIcon;
    ImageIcon nextIcon;

    public void categorize(){
        for (int i=0;i<gp.solutions.size();i++){
            allSolution.add(gp.solutions.get(i));
            if (gp.solutions.get(i).player.playerHp > bestMaxHP){
                bestMaxHP = gp.solutions.get(i).player.playerHp;
            }
            if (gp.solutions.get(i).path < bestMaxPath){
                bestMaxPath = gp.solutions.get(i).path;
            }
            if (gp.solutions.get(i).player.gold > bestMaxGold){
                bestMaxGold = gp.solutions.get(i).player.gold;
            }
        }


        for(Solution x : gp.solutions){
            if (x.player.playerHp == bestMaxHP){
                bestHP.add(x);
            }
            if (x.path == bestMaxPath){
                bestPath.add(x);
            }
            if (x.player.gold == bestMaxGold){
                bestGold.add(x);
            }
        }

    }

    public void setCurrentSolution(ArrayList<Solution> currentSolution) {
        this.currentSolution = currentSolution;
    }


    public SolutionPanel(GamePanel gp) {
        this.gp = gp;
        categorize();
        setCurrentSolution(allSolution);
        
        //ini buat overall panel
        panelWidth = (int)(gp.SCREEN_WIDTH * 0.7);
        panelHeight = (int)(gp.SCREEN_HEIGHT) + 50;
        x = (gp.SCREEN_WIDTH - panelWidth) / 2;
        y = (gp.SCREEN_HEIGHT - panelHeight) / 2 + 100;


        //ini buat yang posisi map nya
        panelWidth1 = (int)(panelWidth * 0.8);
        panelHeight1 = (int)(panelHeight * 0.8);
        x1 = (panelWidth - panelWidth1) / 2;
        y1 = (panelHeight - panelHeight1) / 2 + 150;
        tileSize = (int)(panelWidth1 / gp.MAX_WORLD_COL);
        
        setLayout(null);
        setBounds(x, y, panelWidth, panelHeight);
        setBackground(new Color(0, 0, 0, 200));

        // close button
        Button closeButton = new Button("Close");
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(new Color(50, 50, 50));
        closeButton.setBounds(panelWidth - 80, 10, 70, 25);
        closeButton.addActionListener(e -> {
            setVisible(false);
            gp.remove(this);
            gp.revalidate();
            gp.repaint();
            gp.requestFocusInWindow();
            gp.isSolving = false;
        });
        add(closeButton);

        
        // navigation buttons
        if (gp.solutions != null && gp.solutions.size() > 0) {
            
            int buttonWidth = 100;
            int buttonHeight = 30;
            int buttonGap = 75; // Gap antara button
            int totalWidth = (buttonWidth * 2) + buttonGap;
            
            // Position at the bottom of the panel
            int startX = (panelWidth - totalWidth) / 2; 
            int buttonY = panelHeight - 50; 
            
            // all button
            Button all = new Button("All");
            all.setForeground(Color.WHITE);
            all.setBackground(new Color(50, 50, 50));
            all.setBounds(x1, 60, 50, 25);
            all.addActionListener(e -> {
                setCurrentSolution(allSolution);
                indexSolution = 0;
                repaint();
            });
            add(all);
    
            // gold button
            Button gold = new Button("Gold");
            gold.setForeground(Color.WHITE);
            gold.setBackground(new Color(50, 50, 50));
            gold.setBounds(x1 + 70, 60, 50, 25);
            gold.addActionListener(e -> {
                setCurrentSolution(bestGold);
                indexSolution = 0;
                repaint();
            });
            add(gold);
    
            // path button
            Button path = new Button("Path");
            path.setForeground(Color.WHITE);
            path.setBackground(new Color(50, 50, 50));
            path.setBounds(x1 + 140, 60, 50, 25);
            path.addActionListener(e -> {
                setCurrentSolution(bestPath);
                indexSolution = 0;
                repaint();
            });
            add(path);
    
            // hp button
            Button hp = new Button("HP");
            hp.setForeground(Color.WHITE);
            hp.setBackground(new Color(50, 50, 50));
            hp.setBounds(x1 + 210, 60, 50, 25);
            hp.addActionListener(e -> {
                setCurrentSolution(bestHP);
                indexSolution = 0;
                repaint();
            });
            add(hp);
    
            // implement button
            Button implement = new Button("Implement");
            implement.setForeground(Color.WHITE);
            implement.setBackground(new Color(50, 50, 50));
            implement.setBounds(x1, y + 55, x1 + 250, 25);
            implement.addActionListener(e -> {
                //overwrite map di gamepanel
                gp.copyMap(gp.tileM.mapTile, currentSolution.get(indexSolution).map);
                
                // player di gamepanel di ganti ama solution
        
                gp.player = currentSolution.get(indexSolution).player.clone();
                
                // ini set biar player kelihatan di map
                gp.tileM.mapTile[gp.player.playerX][gp.player.playerY] = gp.player.playerTileNum; // Ensure player tile is marked
                
                // tutup panel
                setVisible(false);
                gp.remove(this);
                
                // Update UI hp dkk
                gp.playerHP.setValue(gp.player.playerHp);
                gp.playerHP.setString("Player HP: " + gp.player.playerHp);
                
                gp.isSolving = false;
                
                gp.revalidate();
                gp.repaint();
                gp.requestFocusInWindow();
                
                // Print debug info
                System.out.println("Solution implemented. Player position: (" + 
                                   gp.player.playerX + "," + gp.player.playerY + ")");
            });
            add(implement);
    
            JButton prevButton = new JButton("Previous");
            prevButton.setForeground(Color.WHITE);
            prevButton.setBackground(new Color(50, 50, 50));
            prevButton.setFocusPainted(false);
            prevButton.setBounds(startX, buttonY, buttonWidth, buttonHeight);
            prevButton.addActionListener(e -> {
                if (indexSolution > 0) {
                    indexSolution--;
                    repaint();
                }
            });
            add(prevButton);

            JButton nextButton = new JButton("Next");
            nextButton.setForeground(Color.WHITE);
            nextButton.setBackground(new Color(50, 50, 50));
            nextButton.setFocusPainted(false);
            nextButton.setBounds(startX + buttonWidth + buttonGap, buttonY, buttonWidth, buttonHeight);
            nextButton.addActionListener(e -> {
                if (indexSolution < currentSolution.size() - 1) {
                    indexSolution++;
                    repaint();
                }
            });
            add(nextButton);
            
        } 

        setVisible(true);
    }

    public void draw(Graphics2D g2) {
        // cek kalo ada solusi ga
        if (gp.solutions == null || gp.solutions.isEmpty() || indexSolution >= gp.solutions.size()) {
            Font fontBaru = new Font("Arial", Font.BOLD, 16);

            g2.setFont(fontBaru);
            g2.setColor(Color.WHITE);
            g2.drawString("Maze is impossible to solve", x1 + 47, y1 + 50);
            g2.drawString("There is no Solution", x1 + 70, y1 + 80);
            return;
        } 
        // current solution
        for (int worldRow = 0; worldRow < gp.MAX_WORLD_ROW; worldRow++) {
            for (int worldCol = 0; worldCol < gp.MAX_WORLD_COL; worldCol++) {

                int tileNum = currentSolution.get(indexSolution).map[worldCol][worldRow];
                
                int worldX = x1 + (worldCol * tileSize);
                int worldY = y1 + (worldRow * tileSize) - 10;
                
                if (gp.tileM.tile[tileNum].image != null) {
                    g2.drawImage(gp.tileM.tile[tileNum].image, worldX, worldY, tileSize, tileSize, null);
                } else {
                    g2.setColor(Color.RED);
                    g2.fillRect(worldX, worldY, tileSize, tileSize);
                }
            }
        }
        
        // jumlah solusi perpart
        g2.setColor(Color.WHITE);
        g2.drawString("Solution " + (indexSolution + 1) + " of " + currentSolution.size() , 
                    x1, y1 - 25);
        // solusi info
        g2.setColor(Color.WHITE);
        Font fontBaru = new Font("Arial", Font.PLAIN , 15);
        g2.setFont(fontBaru);
        g2.drawString("HP : " + currentSolution.get(indexSolution).player.playerHp , x1, y + 40);
        g2.drawString("Path : " + currentSolution.get(indexSolution).path , x1 + 100, y + 40);
        g2.drawString("Gold : " + currentSolution.get(indexSolution).player.gold, x1 + 200, y + 40);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // Actually call draw method
        draw(g2);
    }

}
