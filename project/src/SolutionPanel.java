import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

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

    public SolutionPanel(GamePanel gp) {
        this.gp = gp;
        
        // Initialize all dimensions AFTER setting gp
        panelWidth = (int)(gp.SCREEN_WIDTH * 0.7);
        panelHeight = (int)(gp.SCREEN_HEIGHT * 0.7);
        x = (gp.SCREEN_WIDTH - panelWidth) / 2;
        y = (gp.SCREEN_HEIGHT - panelHeight) / 2;

        panelWidth1 = (int)(panelWidth * 0.8);
        panelHeight1 = (int)(panelHeight * 0.8);
        x1 = (panelWidth - panelWidth1) / 2;
        y1 = (panelHeight - panelHeight1) / 2;
        tileSize = (int)(panelWidth1 / gp.MAX_WORLD_COL);
        
        setLayout(null);
        setBounds(x, y, panelWidth, panelHeight);
        setBackground(new Color(0, 0, 0, 200));

        // Add close button
        Button closeButton = new Button("Close");
        closeButton.setForeground(Color.WHITE);
        closeButton.setBounds(panelWidth - 80, 10, 70, 25);
        closeButton.addActionListener(e -> {
            setVisible(false);
            gp.remove(this);
            gp.revalidate();
            gp.repaint();
            gp.requestFocusInWindow();
        });
        add(closeButton);

        // Add solution navigation buttons
        if (gp.solutions != null && gp.solutions.size() > 0) {
            Button prevButton = new Button("Previous");
            prevButton.setBounds(x1, panelHeight1 + y1 + 10, 100, 25);
            prevButton.addActionListener(e -> {
                if (indexSolution > 0) {
                    indexSolution--;
                    repaint();
                }
            });
            add(prevButton);

            Button nextButton = new Button("Next");
            nextButton.setBounds(panelWidth1 - 100 + x1, panelHeight1 + y1 + 10, 100, 25);
            nextButton.addActionListener(e -> {
                if (indexSolution < gp.solutions.size() - 1) {
                    indexSolution++;
                    repaint();
                }
            });
            add(nextButton);
        } else {
            // No solutions found
            Button noSolutionBtn = new Button("No solutions found");
            noSolutionBtn.setBounds(panelWidth/2 - 150, panelHeight/2 - 15, 300, 30);
            noSolutionBtn.setEnabled(false);
            add(noSolutionBtn);
        }

        setVisible(true);
    }

    public void draw(Graphics2D g2) {
        // Check if solutions exist
        if (gp.solutions == null || gp.solutions.isEmpty() || indexSolution >= gp.solutions.size()) {
            g2.setColor(Color.WHITE);
            g2.drawString("No solution available to display", x1 + 50, y1 + 50);
            return;
        }

        // Draw the current solution's map
        for (int worldRow = 0; worldRow < gp.MAX_WORLD_ROW; worldRow++) {
            for (int worldCol = 0; worldCol < gp.MAX_WORLD_COL; worldCol++) {
                // Get the tile number from the solution
                int tileNum = gp.solutions.get(indexSolution).map[worldCol][worldRow];
                
                // Calculate position within the solution panel
                int worldX = x1 + (worldCol * tileSize);
                int worldY = y1 + (worldRow * tileSize);
                
                // Draw the tile
                if (gp.tileM.tile[tileNum].image != null) {
                    g2.drawImage(gp.tileM.tile[tileNum].image, worldX, worldY, tileSize, tileSize, null);
                } else {
                    // Draw a placeholder if image is missing
                    g2.setColor(Color.RED);
                    g2.fillRect(worldX, worldY, tileSize, tileSize);
                }
            }
        }
        
        // Draw solution info
        g2.setColor(Color.WHITE);
        g2.drawString("Solution " + (indexSolution + 1) + " of " + gp.solutions.size() + 
                    " (Path length: " + gp.solutions.get(indexSolution).path + ")", 
                    x1, y1 - 10);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // Actually call draw method
        draw(g2);
    }
}
