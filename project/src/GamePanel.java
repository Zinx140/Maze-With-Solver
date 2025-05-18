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
    ArrayList<SolvedRoute> hasil = new ArrayList<>();

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
                    case 5:
                        System.out.print("K "); // Tile kunci
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

    public void copyArrayList(ArrayList<Key> keysClone, ArrayList<Key> keys) {
        for (int i = 0; i < keys.size(); i++) {
            keysClone.add(keys.get(i));
        }
    }

    public void solve(int map[][], Player player, ArrayList<Key> keys, int path) {
        int[][] currentMap = new int[MAX_WORLD_COL][MAX_WORLD_ROW];
        if (map[player.playerX][player.playerY] == 2) { // Jika sudah sampai tujuan
            System.out.println("=== Path found! ===");
            System.out.println("Path: " + path);
            draw(map);
            int copyMap[][] = new int[MAX_WORLD_COL][MAX_WORLD_ROW];
            copyMap(copyMap, map);
            hasil.add(new SolvedRoute(copyMap,path));
        } else {
            if (map[player.playerX][player.playerY - 1] != 1 && map[player.playerX][player.playerY - 1] != 4) { // Up
                Player playerClone = player.clone();
                ArrayList<Key> keysClone = new ArrayList<>();
                copyArrayList(keysClone, keys);
                copyMap(currentMap, map);
                playerClone.move(currentMap, 0, keysClone);
                solve(currentMap, playerClone, keysClone, path + 1);
            } 
            if (map[player.playerX][player.playerY + 1] != 1 && map[player.playerX][player.playerY + 1] != 4) { // Down
                Player playerClone = player.clone();
                copyMap(currentMap, map);
                ArrayList<Key> keysClone = new ArrayList<>();
                copyArrayList(keysClone, keys);
                playerClone.move(currentMap, 1, keysClone);
                solve(currentMap, playerClone, keysClone, path + 1);
            } 
            if (map[player.playerX - 1][player.playerY] != 1 && map[player.playerX - 1][player.playerY] != 4) { // Left
                Player playerClone = player.clone();
                copyMap(currentMap, map);
                ArrayList<Key> keysClone = new ArrayList<>();
                copyArrayList(keysClone, keys);
                playerClone.move(currentMap, 2, keysClone);
                solve(currentMap, playerClone, keysClone, path + 1);
            } 
            if (map[player.playerX + 1][player.playerY] != 1 && map[player.playerX + 1][player.playerY] != 4) { // Right
                Player playerClone = player.clone();
                copyMap(currentMap, map);
                ArrayList<Key> keysClone = new ArrayList<>();
                copyArrayList(keysClone, keys);
                playerClone.move(currentMap, 3, keysClone);
                solve(currentMap, playerClone, keysClone, path + 1);
            }
        }
    }

    public void Play() {
        mapTile = new int[MAX_WORLD_ROW][MAX_WORLD_COL]; // Inisialisasi peta
        loadMap("project/src/map1.txt"); // baca listmap.txt
        Player player = new Player(1, 1, this); // Inisialisasi posisi awal player
        ArrayList<Key> keys = new ArrayList<>(); // Inisialisasi list kunci
        keys.add(new Key(3, 2, 7, 12)); // Tambahkan kunci ke list
        setKeys(mapTile, keys); // Set kunci di peta
        mapTile[player.playerX][player.playerY] = 3; // Set tile player
        solve(mapTile, player, keys, 0); // Panggil fungsi solve
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


        System.out.println("=== Hasil Solusi ===");
        for (int i = 0; i < hasil.size(); i++) {
            System.out.println("Path: " + hasil.get(i).path);
            draw(hasil.get(i).map);
        }
    }

    public void setKeys(int map[][], ArrayList<Key> keys) {
        for (int i = 0; i < keys.size(); i++) {
            keys.get(i).setKey(map);
        }
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
