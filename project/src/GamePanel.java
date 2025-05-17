import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GamePanel {
    int[][] mapTile;  // 3d buat [map-ke?][y][x]
    final int MAX_WORLD_COL = 15; // Jumlah kolom peta
    final int MAX_WORLD_ROW = 15; // Jumlah baris peta

    Scanner getString = new Scanner(System.in);
    Scanner getInt = new Scanner(System.in);
    
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

    public void solve(int map[][], Player player, int path) {
        int[][] currentMap = new int[MAX_WORLD_COL][MAX_WORLD_ROW];
        if (map[player.playerX][player.playerY] == 2) { // Jika sudah sampai tujuan
            System.out.println("=== Path found! ===");
            System.out.println("Path: " + path);
            draw(map);
        } else {
            if (map[player.playerX][player.playerY - 1] != 1 && map[player.playerX][player.playerY - 1] != 4) { // Up
                Player playerClone = player.clone();
                copyMap(currentMap, map);
                playerClone.move(currentMap, 0);
                solve(currentMap, playerClone, path + 1);
            } 
            if (map[player.playerX][player.playerY + 1] != 1 && map[player.playerX][player.playerY + 1] != 4) { // Down
                Player playerClone = player.clone();
                copyMap(currentMap, map);
                playerClone.move(currentMap, 1);
                solve(currentMap, playerClone, path + 1);
            } 
            if (map[player.playerX - 1][player.playerY] != 1 && map[player.playerX - 1][player.playerY] != 4) { // Left
                Player playerClone = player.clone();
                copyMap(currentMap, map);
                playerClone.move(currentMap, 2);
                solve(currentMap, playerClone, path + 1);
            } 
            if (map[player.playerX + 1][player.playerY] != 1 && map[player.playerX + 1][player.playerY] != 4) { // Right
                Player playerClone = player.clone();
                copyMap(currentMap, map);
                playerClone.move(currentMap, 3);
                solve(currentMap, playerClone, path + 1);
            }
        }
    }

    public void Play() {
        mapTile = new int[MAX_WORLD_ROW][MAX_WORLD_COL]; // Inisialisasi peta
        loadMap("Maze-With-Solver/project/src/map1.txt"); // baca listmap.txt
        Player player = new Player(1, 1); // Inisialisasi posisi awal player
        mapTile[player.playerY][player.playerX] = 3; // Set tile player
        solve(mapTile, player, 0); // Panggil fungsi solve
        // while (mapTile[player.playerY][player.playerX] != 2) { // Selama player belum sampai tujuan
        //     draw(mapTile);
        //     System.out.print("Input move (w/a/s/d): ");
        //     String input = getString.nextLine();
        //     if (input.equals("w")) {
        //         player.move(mapTile, 0); // Up
        //     } else if (input.equals("s")) {
        //         player.move(mapTile, 1); // Down
        //     } else if (input.equals("a")) {
        //         player.move(mapTile, 2); // Left
        //     } else if (input.equals("d")) {
        //         player.move(mapTile, 3); // Right
        //     } else {
        //         System.out.println("Input tidak valid!");
        //     }           
        // }
    }
    
    public void loadMap(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                System.err.println("File tidak ditemukan: " + path);
                return;
            }

            InputStream is = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            List<Integer> numbers = new ArrayList<>();

            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.trim().split(" ");
                for (String token : tokens) {
                    if (!token.trim().isEmpty()) { // Pastikan token tidak kosong
                        numbers.add(Integer.parseInt(token.trim()));
                    }
                }
            }

            br.close();

            int totalTiles = MAX_WORLD_COL * MAX_WORLD_COL;
            if (numbers.size() != totalTiles) {
                throw new IllegalArgumentException("Jumlah angka dalam file tidak sesuai dengan ukuran peta (" 
                    + MAX_WORLD_COL + "x" + MAX_WORLD_COL + "). Dibutuhkan " + totalTiles + " angka, tetapi ditemukan " + numbers.size() + ".");
            }

            int col = 0;
            int row = 0;
            for (int num : numbers) {
                mapTile[col][row] = num;
                col++;
                if (col == MAX_WORLD_COL) {
                    col = 0;
                    row++;
                }
            }

            System.out.println("Peta berhasil dimuat!");

        } catch (IOException e) {
            System.err.println("Terjadi kesalahan saat membaca file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}
