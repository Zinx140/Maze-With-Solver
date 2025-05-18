import java.util.ArrayList;

public class Player implements Cloneable {
    int playerX;
    int playerY;
    GamePanel gp;
    int gold=0;

    public Player(int playerX, int playerY, GamePanel gp) {
        this.playerX = playerX;
        this.playerY = playerY;
        this.gp = gp;
    }

    public int searchKey(ArrayList<Plate> keys) {
        System.out.println(keys.size());
        for (int i = 0; i < keys.size(); i++) {
            if (playerX == keys.get(i).keyX && playerY == keys.get(i).keyY) {
                return i; // Mengembalikan index kunci yang ditemukan
            }
        }
        return -1; // Tidak ada kunci yang ditemukan
    }

    public void clearTrace(int[][] map) {
        for (int i = 0; i < gp.MAX_WORLD_ROW; i++) {
            for (int j = 0; j < gp.MAX_WORLD_COL; j++) {
                if (map[i][j] == 4) {
                    map[i][j] = 6; 
                }
            }
        }
    }

    public void move(int map[][], int direction, ArrayList<Plate> keys, boolean isSolving) {
        int trace = (isSolving) ? 4 : 6; // Set trace tile based on solving state
        switch (direction) {
            case 0: // up
                if (map[playerX][playerY - 1] == 2) {
                    playerY--;  
                } else if (map[playerX][playerY - 1] == 5) { 
                    map[playerX][playerY] = trace; 
                    playerY--;
                    map[playerX][playerY] = 3; 
                    int keyIndex = searchKey(keys);
                    if (keyIndex != -1) {
                        keys.get(keyIndex).openPath(map);
                        keys.remove(keyIndex); 
                    }
                    clearTrace(map);
                } else if(map[playerX][playerY - 1] == 9) {
                    map[playerX][playerY] = trace; 
                    playerY--;
                    map[playerX][playerY] = 3; 
                    gold++;
                    System.out.println("+1 Gold");
                    System.out.println("Gold: " + gold);

                    clearTrace(map);
                }else if(map[playerX][playerY - 1] == 10) {
                    gp.tileM.changeMap(this);
                }else if (map[playerX][playerY - 1] != 1 && map[playerX][playerY - 1] != 4 && map[playerX][playerY - 1] != 2) {
                    map[playerX][playerY] = trace; 
                    playerY--;
                    map[playerX][playerY] = 3; 
                } 
                break;
            case 1: // down
                if (map[playerX][playerY + 1] == 2) {
                    playerY++;
                } else if (map[playerX][playerY + 1] == 5) { 
                    map[playerX][playerY] = trace; 
                    playerY++;
                    map[playerX][playerY] = 3; 
                    int keyIndex = searchKey(keys);
                    if (keyIndex != -1) {
                        keys.get(keyIndex).openPath(map);
                        keys.remove(keyIndex);
                    }
                    clearTrace(map);
                } else if(map[playerX][playerY + 1] == 9) {
                       map[playerX][playerY] = trace; 
                       playerY++;
                       map[playerX][playerY] = 3; 
                       gold++;
                       System.out.println("+1 Gold");
                       System.out.println("Gold: " + gold);
   
                       clearTrace(map);
                   }else if(map[playerX][playerY + 1] == 10) {
                    gp.tileM.changeMap(this);
                    }else if (map[playerX][playerY + 1] != 1 && map[playerX][playerY + 1] != 4 && map[playerX][playerY + 1] != 2) {
                    map[playerX][playerY] = trace; 
                    playerY++;
                    map[playerX][playerY] = 3; 
                }
                break;
            case 2: // left
                if (map[playerX - 1][playerY] == 2) {
                    playerX--;
                } else if (map[playerX - 1][playerY] == 5) { 
                    map[playerX][playerY] = trace; 
                    playerX--;
                    map[playerX][playerY] = 3; 
                    int keyIndex = searchKey(keys);
                    if (keyIndex != -1) {
                        keys.get(keyIndex).openPath(map);
                        keys.remove(keyIndex); 
                    }
                    clearTrace(map);
                } else if(map[playerX - 1][playerY] == 9) {
                    map[playerX][playerY] = trace; 
                    playerX--;
                    map[playerX][playerY] = 3; 
                    gold++;
                    System.out.println("+1 Gold");
                    System.out.println("Gold: " + gold);
                    clearTrace(map);
                }else if(map[playerX - 1][playerY] == 10) {
                    gp.tileM.changeMap(this);
                }else if (map[playerX - 1][playerY] != 1 && map[playerX - 1][playerY] != 4 && map[playerX - 1][playerY] != 2) {
                    map[playerX][playerY] = trace; 
                    playerX--;
                    map[playerX][playerY] = 3; 
                } 
                break;
            case 3: // right
                if (map[playerX + 1][playerY] == 2) {
                    playerX++;
                } else if (map[playerX + 1][playerY] == 5) { 
                    map[playerX][playerY] = trace;
                    playerX++;
                    map[playerX][playerY] = 3;
                    int keyIndex = searchKey(keys);
                    if (keyIndex != -1) {
                        keys.get(keyIndex).openPath(map);
                        keys.remove(keyIndex); 
                    }
                    clearTrace(map);
                } else if(map[playerX + 1][playerY] == 9) { //gold
                    map[playerX][playerY] = trace; 
                    playerX++;
                    map[playerX][playerY] = 3; 
                    gold++;
                    System.out.println("+1 Gold");
                    System.out.println("Gold: " + gold);
                    clearTrace(map);
                }else if(map[playerX + 1][playerY] == 10) {
                    gp.tileM.changeMap(this);
                }else if (map[playerX + 1][playerY] != 1 && map[playerX + 1][playerY] != 4 && map[playerX + 1][playerY] != 2) {
                    map[playerX][playerY] = trace; 
                    playerX++;
                    map[playerX][playerY] = 3;
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
