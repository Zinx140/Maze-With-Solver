import java.util.ArrayList;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_W) {
            player.move(tileM.mapTile, player, 0, plates, monsters, false); // Up
        } else if (keyCode == KeyEvent.VK_S) {
            player.move(tileM.mapTile, player, 1, plates, monsters, false); // Down
        } else if (keyCode == KeyEvent.VK_A) {
            player.move(tileM.mapTile, player, 2, plates, monsters, false); // Left
        } else if (keyCode == KeyEvent.VK_D) {
            player.move(tileM.mapTile, player, 3, plates, monsters, false); // Right
        }
        if (keyCode == KeyEvent.VK_X && gamestate == PLAYER_STATE) {
            gamestate = PLAY_STATE;
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

    public final int PLAY_STATE = 1;
    public final int PLAYER_STATE = 2;
    public final int SOLVING_STATE = 3;
    public final int WIN_STATE = 4;
    public final int LOSE_STATE = 5;

    ImageIcon solveIcon;
    ImageIcon resetIcon;
    ImageIcon playerStatIcon;
    ImageIcon nextStageIcon;
    ImageIcon exitIcon;
    public int currentMap = 0; // Map yang sedang dimainkan

    // gold di game
    public int MAX_GOLD_PERMAP = 5;

    int playerXTemp = 1;
    int playerYTemp = 1;
    int hpTemp = 0;
    boolean isSolving = false;
    Image img;
    int goldTemp = 0;
    int gamestate;
    int[][] mapTemp;

    Thread gameThread;
    JProgressBar playerHP;
    TileManager tileM = new TileManager(this);
    Player player = new Player(1, 1, this);
    UI ui = new UI(this);
    Game game;
    Sound bgmSound = new Sound();  // for looping music
    Sound sfxSound = new Sound();  // for one-time effects

    ArrayList<Plate> plates = new ArrayList<>(); // Inisialisasi list kunci
    ArrayList<Solution> solutions = new ArrayList<>(); // Inisialisasi list solusi
    ArrayList<Monster> monsters = new ArrayList<>(); // Inisialisasi list monster
    ArrayList<Trap> traps = new ArrayList<>(); // Inisialisasi list trap

    ArrayList<Map> maps = new ArrayList<>();

    Scanner getString = new Scanner(System.in);
    Scanner getInt = new Scanner(System.in);

    public GamePanel(Game game) {
        this.game = game;
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(this);
        this.setFocusable(true);
        this.setLayout(null);
        bgmSound.setFile(0);  // assuming index 0 is mainTheme.wav
        bgmSound.play();
        bgmSound.loop();
        mapTemp = new int[MAX_WORLD_ROW][MAX_WORLD_COL];
        try {
            img = ImageIO.read(new File("project/img/solveBtn.png"));
            Image scaledImage = img.getScaledInstance(100, 43, Image.SCALE_SMOOTH);
            solveIcon = new ImageIcon(scaledImage);

            img = ImageIO.read(new File("project/img/resetBtn.png"));
            scaledImage = img.getScaledInstance(100, 43, Image.SCALE_SMOOTH);
            resetIcon = new ImageIcon(scaledImage);

            img = ImageIO.read(new File("project/img/statusBtn.png"));
            scaledImage = img.getScaledInstance(100, 43, Image.SCALE_SMOOTH);
            playerStatIcon = new ImageIcon(scaledImage);

            img = ImageIO.read(new File("project/img/nextBtn.png"));
            scaledImage = img.getScaledInstance(43, 43, Image.SCALE_SMOOTH);
            nextStageIcon = new ImageIcon(scaledImage);

            img = ImageIO.read(new File("project/img/exit.png"));
            scaledImage = img.getScaledInstance(43, 43, Image.SCALE_SMOOTH);
            exitIcon = new ImageIcon(scaledImage);
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        initMenuBtn();

        // isi list map
        maps.add(new Map("project/src/maps/map1.txt", 1, 1));
        maps.add(new Map("project/src/maps/map2.txt", 1, 1));
        maps.add(new Map("project/src/maps/map3.txt", 9, 1));
        maps.add(new Map("project/src/maps/map4.txt", 1, 1));
        maps.add(new Map("project/src/maps/map5.txt", 13, 1));
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        plates.add(new Plate(3, 2, 7, 12)); // Tambahkan kunci ke list
        setPlates(tileM.mapTile, plates); // Set kunci di peta
        tileM.mapTile[player.playerX][player.playerY] = 3; // Set tile player
        copyMap(mapTemp, tileM.mapTile); // Copy map ke mapTemp

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            lastTime = currentTime;
            
            if (delta >= 1) {
                if (player.playerHp <= 0) { // Jika player mati
                    player.playerHp = 0;
                    playerHP.setValue(player.playerHp);
                    playerHP.setString("Player HP: " + player.playerHp);
                    System.out.println("You Lose");
                    gamestate = LOSE_STATE;
                    bgmSound.stop();
                    sfxSound.setFile(3); // assuming index 3 is death.wav
                    sfxSound.playOnce();
                    gameThread = null;
                    try {
                        repaint();
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    SwingUtilities.invokeLater(() -> game.returnToMenu());
                }
                if (tileM.mapTile[player.playerX][player.playerY] == 2) { // Jika sudah sampai tujuan
                    System.out.println("You Win");
                    playerHP.setValue(player.playerHp);
                    playerHP.setString("Player HP: " + player.playerHp);
                    gamestate = WIN_STATE;
                    bgmSound.stop();
                    sfxSound.setFile(5); // assuming index 5 is won.wav
                    sfxSound.playOnce();
                    gameThread = null;
                    try {
                        repaint();
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    SwingUtilities.invokeLater(() -> game.returnToMenu());
                }
                playerHP.setValue(player.playerHp);
                playerHP.setString("Player HP: " + player.playerHp);
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

        tileM.draw(g2);
        ui.draw(g2);
        
    }

    public void startgameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void initMenuBtn() {
        goldTemp = player.gold;
        hpTemp = player.playerHp;
        playerXTemp = player.playerX;
        playerYTemp = player.playerY;
        gamestate = PLAY_STATE;

        playerHP = new JProgressBar(0 , player.maxHp);
        playerHP.setValue(player.playerHp);
        playerHP.setStringPainted(true);
        playerHP.setForeground(Color.red);
        playerHP.setBounds(30, 20, 600, 70);
        playerHP.setFont(new Font("Arial", Font.PLAIN, 20));
        playerHP.setString("Player HP: " + player.playerHp);
        playerHP.setBackground(Color.black);
        this.add(playerHP);

        JButton solution = new JButton(solveIcon);
        solution.setIcon(solveIcon);
        solution.setBorderPainted(false);
        solution.setContentAreaFilled(false);
        solution.setFocusPainted(false);
        solution.setBounds(170, 120, 100, 43);
        solution.addActionListener(e -> {
            sfxSound.setFile(8); // assuming index 7 is click.wav
            sfxSound.playOnce();
            if (!isSolving) {
                isSolving = true;
                int [][] solutionMap = new int[MAX_WORLD_ROW][MAX_WORLD_COL];
                copyMap(solutionMap, mapTemp);
                draw(solutionMap);
                new Thread(() -> {
                    solve(solutionMap, new Player(player.playerX, player.playerY, this), plates, monsters, 0, player.gold);
                    isSolving = false;
                    SwingUtilities.invokeLater(() -> this.requestFocusInWindow());
                }).start();
            }
        });
        this.add(solution);

        JButton playerstat = new JButton();
        playerstat.setBorderPainted(false);
        playerstat.setContentAreaFilled(false);
        playerstat.setFocusPainted(false);
        playerstat.setFocusable(false);
        playerstat.setIcon(playerStatIcon);
        playerstat.setBounds(290, 120, 100, 43);
        playerstat.addActionListener(e -> {
            sfxSound.setFile(8); // assuming index 7 is click.wav
            sfxSound.playOnce();
            if (gamestate == PLAYER_STATE) {
                gamestate = PLAY_STATE;
            } else if (gamestate == PLAY_STATE) {
                gamestate = PLAYER_STATE;
            }
        });
        this.add(playerstat);

        JButton reset = new JButton();
        reset.setBorderPainted(false);
        reset.setContentAreaFilled(false);
        reset.setFocusPainted(false);
        reset.setFocusable(false);
        reset.setIcon(resetIcon);
        reset.setBounds(400, 120, 100, 43);
        reset.addActionListener(e -> {
            sfxSound.setFile(8); // assuming index 7 is click.wav
            sfxSound.playOnce();
            reset();
        });
        this.add(reset);

        JButton nextStage = new JButton();
        nextStage.setBorderPainted(false);
        nextStage.setContentAreaFilled(false);
        nextStage.setFocusPainted(false);
        nextStage.setFocusable(false);
        nextStage.setIcon(nextStageIcon);
        nextStage.setBounds(510, 120, 43, 43);
        nextStage.addActionListener(e -> {
            sfxSound.setFile(8); // assuming index 7 is click.wav
            sfxSound.playOnce();
            tileM.changeMap(player);
        });
        this.add(nextStage);

        JButton exit = new JButton();
        exit.setBorderPainted(false);
        exit.setContentAreaFilled(false);
        exit.setFocusPainted(false);
        exit.setFocusable(false);
        exit.setIcon(exitIcon);
        exit.setBounds(570, 120, 43, 43);
        exit.addActionListener(e -> {
            bgmSound.stop();
            sfxSound.setFile(8); // assuming index 7 is click.wav
            sfxSound.playOnce();
            SwingUtilities.invokeLater(() -> game.returnToMenu());
        });
        this.add(exit);
    }

    public void copyMap(int[][] map, int[][] mapTile) {
        for (int i = 0; i < MAX_WORLD_ROW; i++) {
            for (int j = 0; j < MAX_WORLD_COL; j++) {
                map[i][j] = mapTile[i][j];
            }
        }
    }

    public void copyArrayListPlate(ArrayList<Plate> PlatesClone, ArrayList<Plate> Plates) {
        for (int i = 0; i < Plates.size(); i++) {
            PlatesClone.add(Plates.get(i));
        }
    }

    public void copyArrayListMonster(ArrayList<Monster> MonstersClone, ArrayList<Monster> Monsters) {
        for (int i = 0; i < Monsters.size(); i++) {
            MonstersClone.add(Monsters.get(i).clone());
        }
    }

    public void solve(int map[][], Player player, ArrayList<Plate> Plates, ArrayList<Monster> monsters, int path, int gold) {
        int[][] currentMapArr = new int[MAX_WORLD_COL][MAX_WORLD_ROW];
        if (map[player.playerX][player.playerY] == 10 || map[player.playerX][player.playerY] == 2) { // Jika sudah sampai tujuan
            System.out.println("=== Path found! ===");
            System.out.println("Current Map: " + currentMap);
            System.out.println("Player HP: " + player.playerHp);
            System.out.println("Player Gold: " + player.gold);
            System.out.println("Path: " + path);
            draw(map);
            solutions.add(new Solution(map, path, gold));
        } else {
            if (map[player.playerX][player.playerY - 1] != 1 && map[player.playerX][player.playerY - 1] != 4 && player.playerHp > 0) { // Up
                Player playerClone = player.clone();
                ArrayList<Plate> PlatesClone = new ArrayList<>();
                ArrayList<Monster> monstersClone = new ArrayList<>();
                copyArrayListPlate(PlatesClone, Plates);
                copyArrayListMonster(monstersClone, monsters);
                copyMap(currentMapArr, map);
                playerClone.move(currentMapArr, playerClone, 0, PlatesClone, monstersClone, true);
                solve(currentMapArr, playerClone, PlatesClone, monstersClone, path + 1, playerClone.gold);
            }
            if (map[player.playerX][player.playerY + 1] != 1 && map[player.playerX][player.playerY + 1] != 4 && player.playerHp > 0) { // Down
                Player playerClone = player.clone();
                copyMap(currentMapArr, map);
                ArrayList<Plate> PlatesClone = new ArrayList<>();
                ArrayList<Monster> monstersClone = new ArrayList<>();
                copyArrayListPlate(PlatesClone, Plates);
                copyArrayListMonster(monstersClone, monsters);
                playerClone.move(currentMapArr, playerClone, 1, PlatesClone, monstersClone, true);
                solve(currentMapArr, playerClone, PlatesClone, monstersClone, path + 1, playerClone.gold);
            }
            if (map[player.playerX - 1][player.playerY] != 1 && map[player.playerX - 1][player.playerY] != 4 && player.playerHp > 0) { // Left
                Player playerClone = player.clone();
                copyMap(currentMapArr, map);
                ArrayList<Plate> PlatesClone = new ArrayList<>();
                ArrayList<Monster> monstersClone = new ArrayList<>();
                copyArrayListPlate(PlatesClone, Plates);
                copyArrayListMonster(monstersClone, monsters);
                playerClone.move(currentMapArr, playerClone, 2, PlatesClone, monstersClone, true);
                solve(currentMapArr, playerClone, PlatesClone, monstersClone, path + 1, playerClone.gold);
            }
            if (map[player.playerX + 1][player.playerY] != 1 && map[player.playerX + 1][player.playerY] != 4 && player.playerHp > 0) { // Right
                Player playerClone = player.clone();
                copyMap(currentMapArr, map);
                ArrayList<Plate> PlatesClone = new ArrayList<>();
                ArrayList<Monster> monstersClone = new ArrayList<>();
                copyArrayListPlate(PlatesClone, Plates);
                copyArrayListMonster(monstersClone, monsters);
                playerClone.move(currentMapArr, playerClone, 3, PlatesClone, monstersClone, true);
                solve(currentMapArr, playerClone, PlatesClone, monstersClone, path + 1, playerClone.gold);
            }
        }
    }

    public void reset() {
        player.playerX = playerXTemp;
        player.playerY = playerYTemp;
        player.playerHp = player.maxHp;
        player.gold = goldTemp;
        copyMap(tileM.mapTile, mapTemp);
        tileM.mapTile[player.playerX][player.playerY] = 3;
        try {
            tileM.tile[3].image = ImageIO.read(new File("project/img/prince.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setPlates(int map[][], ArrayList<Plate> Plates) {
        for (int i = 0; i < Plates.size(); i++) {
            Plates.get(i).setKey(map);
        }
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
                        System.out.print("F "); // Tile princess
                        break;
                    case 3:
                        System.out.print("P "); // Tile player
                        break;
                    case 4:
                        System.out.print(". "); // Tile path
                        break;
                    case 5:
                        System.out.print("_ "); // Tile plate
                        break;
                    case 6:
                        System.out.print(". "); // Tile paath
                        break;
                    case 7:
                        System.out.print("S "); // Tile monster spider
                        break;
                    case 8:
                        System.out.print("O "); // Tile monster ogre
                        break;
                    case 9:
                        System.out.print("D "); // Tile monster dragon
                        break;
                    case 10:
                        System.out.print("G "); // Tile exit
                        break;
                    case 11:
                        System.out.print("g "); // Tile gold
                        break;
                    case 12:
                        System.out.print("T "); // Tile trap
                        break;
                    // case 13:
                    //     System.out.print("E "); // Tile enemy
                    //     break;
                    case 14:
                        System.out.print("C "); // Tile chest
                        break;
                }
            }
            System.out.println();
        }
        System.out.println();
    }

}
