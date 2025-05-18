import java.util.ArrayList;

public class Player implements Cloneable {
    int playerX;
    int playerY;
    GamePanel gp;

    public Player(int playerX, int playerY, GamePanel gp) {
        this.playerX = playerX;
        this.playerY = playerY;
        this.gp = gp;
    }

    public int searchKey(ArrayList<Key> keys, Player player) {
        for (int i = 0; i < keys.size(); i++) {
            if (player.playerX == keys.get(i).keyX && player.playerY == keys.get(i).keyY) {
                return i; // Mengembalikan index kunci yang ditemukan
            }
        }
        return -1; // Tidak ada kunci yang ditemukan
    }

    public void clearTrace(int[][] map) {
        for (int i = 0; i < gp.MAX_WORLD_ROW; i++) {
            for (int j = 0; j < gp.MAX_WORLD_COL; j++) {
                if (map[i][j] == 4) {
                    map[i][j] = 6; // Set tile jalan
                }
            }
        }
    }

    public void move(int map[][], int direction, ArrayList<Key> keys) {
        switch (direction) {
            case 0: // up
                if (map[playerX][playerY - 1] == 2) {
                    playerY--;  
                } else if (map[playerX][playerY - 1] == 5) { 
                    map[playerX][playerY] = 4; // Set tile jalan
                    playerY--;
                    map[playerX][playerY] = 3; // Set tile player
                    int keyIndex = searchKey(keys, this);
                    if (keyIndex != -1) {
                        keys.get(keyIndex).openPath(map);
                        keys.remove(keyIndex); // Menghapus kunci setelah digunakan
                    }
                    clearTrace(map);
                } else if (map[playerX][playerY - 1] != 1 && map[playerX][playerY - 1] != 4 && map[playerX][playerY - 1] != 2) {
                    map[playerX][playerY] = 4; // Set tile jalan
                    playerY--;
                    map[playerX][playerY] = 3; // Set tile player
                }
                break;
            case 1: // down
                if (map[playerX][playerY + 1] == 2) {
                    playerY++;
                } else if (map[playerX][playerY + 1] == 5) { 
                    map[playerX][playerY] = 4; // Set tile jalan
                    playerY++;
                    map[playerX][playerY] = 3; // Set tile player
                    int keyIndex = searchKey(keys, this);
                    if (keyIndex != -1) {
                        keys.get(keyIndex).openPath(map);
                        keys.remove(keyIndex); // Menghapus kunci setelah digunakan
                    }
                    clearTrace(map);
                } else if (map[playerX][playerY + 1] != 1 && map[playerX][playerY + 1] != 4 && map[playerX][playerY + 1] != 2) {
                    map[playerX][playerY] = 4; // Set tile jalan
                    playerY++;
                    map[playerX][playerY] = 3; // Set tile player
                }
                break;
            case 2: // left
                if (map[playerX - 1][playerY] == 2) {
                    playerX--;
                } else if (map[playerX - 1][playerY] == 5) { 
                    map[playerX][playerY] = 4; // Set tile jalan
                    playerX--;
                    map[playerX][playerY] = 3; // Set tile player
                    int keyIndex = searchKey(keys, this);
                    if (keyIndex != -1) {
                        keys.get(keyIndex).openPath(map);
                        keys.remove(keyIndex); // Menghapus kunci setelah digunakan
                    }
                    clearTrace(map);
                } else if (map[playerX - 1][playerY] != 1 && map[playerX - 1][playerY] != 4 && map[playerX - 1][playerY] != 2) {
                    map[playerX][playerY] = 4; // Set tile jalan
                    playerX--;
                    map[playerX][playerY] = 3; // Set tile player
                }
                break;
            case 3: // right
                if (map[playerX + 1][playerY] == 2) {
                    playerX++;
                } else if (map[playerX + 1][playerY] == 5) { 
                    map[playerX][playerY] = 4; // Set tile jalan
                    playerX++;
                    map[playerX][playerY] = 3; // Set tile players
                    int keyIndex = searchKey(keys, this);
                    if (keyIndex != -1) {
                        keys.get(keyIndex).openPath(map);
                        keys.remove(keyIndex); // Menghapus kunci setelah digunakan
                    }
                    clearTrace(map);
                } else if (map[playerX + 1][playerY] != 1 && map[playerX + 1][playerY] != 4 && map[playerX + 1][playerY] != 2) {
                    map[playerX][playerY] = 4; // Set tile jalan
                    playerX++;
                    map[playerX][playerY] = 3; // Set tile player
                }
                break;
        }
    }

    public Player clone() {
        try {
            return (Player) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Seharusnya tidak terjadi
        }
    }
    
}
