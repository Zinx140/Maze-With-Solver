import java.util.ArrayList;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Scanner;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_W) {
            player.move(tileM.mapTile, 0, keys, false); // Up
        } else if (keyCode == KeyEvent.VK_S) {
            player.move(tileM.mapTile, 1, keys, false); // Down
        } else if (keyCode == KeyEvent.VK_A) {
            player.move(tileM.mapTile, 2, keys, false); // Left
        } else if (keyCode == KeyEvent.VK_D) {
            player.move(tileM.mapTile, 3, keys, false); // Right
        } else if (keyCode == KeyEvent.VK_ENTER) {
            solve(tileM.mapTile, player, keys, 0); // Panggil fungsi solve
            isSolving = true;
            System.out.println("=== End of Path ===");
        } else if (keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    final int ORI_TILE_SIZE = 15;
    final int scale = 3;
    
    public final int TILE_SIZE = ORI_TILE_SIZE * scale;
    final int MAX_SCREEN_COL = 15;
    final int MAX_SCREEN_ROW = 15;
    public final int SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COL;
    public final int SCREEN_HEIGHT = TILE_SIZE * MAX_SCREEN_ROW;
    final int FPS = 60;
    
    public final int MAX_WORLD_COL = 15;
    public final int MAX_WORLD_ROW = 15;
    public final int WORLD_WIDTH = TILE_SIZE * MAX_SCREEN_COL;
    public final int WORLD_HEIGHT = TILE_SIZE * MAX_SCREEN_ROW;

    // gold di game
    public int MAX_GOLD_PERMAP = 5;    

    boolean isSolving = false;
    Thread gameThread;
    TileManager tileM = new TileManager(this);
    Player player = new Player(1, 1, this);

    ArrayList<Plate> keys = new ArrayList<>(); // Inisialisasi list kunci
    ArrayList<Solution> solutions = new ArrayList<>(); // Inisialisasi list solusi

    Scanner getString = new Scanner(System.in);
    Scanner getInt = new Scanner(System.in);

    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(this);
        this.setFocusable(true);
        this.setLayout(null);
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        keys.add(new Plate(3, 2, 7, 12)); // Tambahkan kunci ke list
        setKeys(tileM.mapTile, keys); // Set kunci di peta
        tileM.mapTile[player.playerX][player.playerY] = 3; // Set tile player

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            lastTime = currentTime;

            if (delta >= 1) {
                if (tileM.mapTile[player.playerX][player.playerY] == 2) { // Jika sudah sampai tujuan
                    System.out.println("You Win");
                    gameThread = null;
                } 
                repaint();
                delta--;
            }

            if (timer >= 1000000000) {
                timer = 0;
            }
        }
    }

    public void paintComponent(Graphics g) {
        // Update game logic here
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (isSolving) {
            for (int i = 0; i < solutions.size(); i++) {
                tileM.mapTile = solutions.get(i).map;
                tileM.draw(g2);
                try {
                    Thread.sleep(1000); // Delay 1 detik
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isSolving = false;
        } else {
            tileM.draw(g2);
        }
    }

    public void startgameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    public void draw(int map[][]) {
        for (int i = 0; i < MAX_WORLD_ROW; i++) {
            for (int j = 0; j < MAX_WORLD_COL; j++) {
                switch (map[j][i]) {
                    case 0:
                        System.out.print("  "); // Tile kosong
                        break;
                    case 1:
                        System.out.print("# "); // Tile dinding
                        break;
                    case 2:
                        System.out.print("F "); // Tile jalan
                        break;
                    case 3:
                        System.out.print("P "); // Tile player
                        break;
                    case 4:
                        System.out.print(". "); // Tile enemy
                        break;
                    case 5:
                        System.out.print("X "); // Tile kunci
                        break;
                    case 6:
                        System.out.print(". "); // Tile pintu
                        break;
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public void copyMap(int[][] map, int[][] mapTile) {
        for (int i = 0; i < MAX_WORLD_ROW; i++) {
            for (int j = 0; j < MAX_WORLD_COL; j++) {
                map[i][j] = mapTile[i][j];
            }
        }
    }

    public void copyArrayList(ArrayList<Plate> keysClone, ArrayList<Plate> keys) {
        for (int i = 0; i < keys.size(); i++) {
            keysClone.add(keys.get(i));
        }
    }

    public void solve(int map[][], Player player, ArrayList<Plate> keys, int path) {
        int[][] currentMap = new int[MAX_WORLD_COL][MAX_WORLD_ROW];
        if (map[player.playerX][player.playerY] == 2) { // Jika sudah sampai tujuan
            System.out.println("=== Path found! ===");
            System.out.println("Path: " + path);
            draw(map);
            solutions.add(new Solution(map, path, player.gold)); 
        } else {
            if (map[player.playerX][player.playerY - 1] != 1 && map[player.playerX][player.playerY - 1] != 4) { // Up
                Player playerClone = player.clone();
                ArrayList<Plate> keysClone = new ArrayList<>();
                copyArrayList(keysClone, keys);
                copyMap(currentMap, map);
                playerClone.move(currentMap, 0, keysClone, true);
                solve(currentMap, playerClone, keysClone, path + 1);
            } 
            if (map[player.playerX][player.playerY + 1] != 1 && map[player.playerX][player.playerY + 1] != 4) { // Down
                Player playerClone = player.clone();
                copyMap(currentMap, map);
                ArrayList<Plate> keysClone = new ArrayList<>();
                copyArrayList(keysClone, keys);
                playerClone.move(currentMap, 1, keysClone, true);
                solve(currentMap, playerClone, keysClone, path + 1);
            } 
            if (map[player.playerX - 1][player.playerY] != 1 && map[player.playerX - 1][player.playerY] != 4) { // Left
                Player playerClone = player.clone();
                copyMap(currentMap, map);
                ArrayList<Plate> keysClone = new ArrayList<>();
                copyArrayList(keysClone, keys);
                playerClone.move(currentMap, 2, keysClone, true);
                solve(currentMap, playerClone, keysClone, path + 1);
            } 
            if (map[player.playerX + 1][player.playerY] != 1 && map[player.playerX + 1][player.playerY] != 4) { // Right
                Player playerClone = player.clone();
                copyMap(currentMap, map);
                ArrayList<Plate> keysClone = new ArrayList<>();
                copyArrayList(keysClone, keys);
                playerClone.move(currentMap, 3, keysClone, true);
                solve(currentMap, playerClone, keysClone, path + 1);
            }
        }
    }

    // public void Play() {
    //     mapTile = new int[MAX_WORLD_ROW][MAX_WORLD_COL]; // Inisialisasi peta
    //     Player player = new Player(1, 1, this); // Inisialisasi posisi awal player
    //     ArrayList<Key> keys = new ArrayList<>(); // Inisialisasi list kunci
    //     keys.add(new Key(3, 2, 7, 12)); // Tambahkan kunci ke list
    //     setKeys(mapTile, keys); // Set kunci di peta
    //     mapTile[player.playerX][player.playerY] = 3; // Set tile player
    //     solve(mapTile, player, keys, 0); // Panggil fungsi solve
        // while (mapTile[player.playerX][player.playerY] != 2) { // Selama player belum sampai tujuan
        //     draw(mapTile);
        //     System.out.print("Input move (w/a/s/d): ");
        //     String input = getString.nextLine();
        //     if (input.equals("w")) {
        //         player.move(mapTile, 0, keys); // Up
        //     } else if (input.equals("s")) {
        //         player.move(mapTile, 1, keys); // Down
        //     } else if (input.equals("a")) {
        //         player.move(mapTile, 2, keys); // Left
        //     } else if (input.equals("d")) {
        //         player.move(mapTile, 3, keys); // Right
        //     } else {
        //         System.out.println("Input tidak valid!");
        //     }           
        // }
    // }

    public void setKeys(int map[][], ArrayList<Plate> keys) {
        for (int i = 0; i < keys.size(); i++) {
            keys.get(i).setKey(map);
        }
    }
    
}
