public class Player implements Cloneable {
    int playerX;
    int playerY;

    public Player(int playerX, int playerY) {
        this.playerX = playerX;
        this.playerY = playerY;
    }

    public void move(int map[][], int direction) {
        switch (direction) {
            case 0: // up
                if (map[playerX][playerY - 1] == 2) {
                    playerY--;  
                } else if (map[playerX][playerY - 1] != 1 && map[playerX][playerY - 1] != 4 && map[playerX][playerY - 1] != 2) {
                    map[playerX][playerY] = 4; // Set tile jalan
                    playerY--;
                    map[playerX][playerY] = 3; // Set tile player
                }
                break;
            case 1: // down
                if (map[playerX][playerY + 1] == 2) {
                    playerY++;
                } else if (map[playerX][playerY + 1] != 1 && map[playerX][playerY + 1] != 4 && map[playerX][playerY + 1] != 2) {
                    map[playerX][playerY] = 4; // Set tile jalan
                    playerY++;
                    map[playerX][playerY] = 3; // Set tile player
                }
                break;
            case 2: // left
                if (map[playerX - 1][playerY] == 2) {
                    playerX--;
                } else if (map[playerX - 1][playerY] != 1 && map[playerX - 1][playerY] != 4 && map[playerX - 1][playerY] != 2) {
                    map[playerX][playerY] = 4; // Set tile jalan
                    playerX--;
                    map[playerX][playerY] = 3; // Set tile player
                }
                break;
            case 3: // right
                if (map[playerX + 1][playerY] == 2) {
                    playerX++;
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
