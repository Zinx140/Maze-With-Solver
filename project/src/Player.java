import java.util.ArrayList;

public class Player implements Cloneable {
    boolean isOpenChest = false;
    boolean isArmored = false;
    GamePanel gp;
    int gold = 0;
    int maxHp;
    int playerX;
    int playerY;
    int playerHp;
    int playerAtk;
    Sound sound = new Sound();
    int trapDmg = 3;
    ArrayList<Trap> triggeredTraps = new ArrayList<>();
    boolean solved = false;
    int playerTileNum;

    public Player(int playerX, int playerY, GamePanel gp) {
        this.gp = gp;
        this.maxHp = 100; // Set maximum HP
        this.playerX = playerX;
        this.playerY = playerY;
        this.playerHp = 100; // Set initial HP
        this.playerAtk = 100; // Set initial attack power
        this.playerTileNum = 3; // Set initial tile number
    }

    public int searchKey(ArrayList<Plate> keys) {
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
                if (map[i][j] == 4 || map[i][j] == 3) {
                    map[i][j] = 6;
                }
            }
        }
    }

    public void move(int map[][], Player player, int direction, ArrayList<Plate> keys, ArrayList<Monster> monsters, boolean isSolving) {
        switch (direction) {
            case 0: // up
                movePlayer(map, player, keys, monsters, isSolving, 0, -1);
                break;
            case 1: // down
                movePlayer(map, player, keys, monsters, isSolving, 0, 1);
                break;
            case 2: // left
                movePlayer(map, player, keys, monsters, isSolving, -1, 0);
                break;
            case 3: // right
                movePlayer(map, player, keys, monsters, isSolving, 1, 0);
                break;
        }
    }

    public void movePlayer(int[][] map, Player player, ArrayList<Plate> keys, ArrayList<Monster> monsters, boolean isSolving, int dx, int dy) {
        int trace = (isSolving) ? 4 : 6; // Set trace tile based on solving state
        if (map[player.playerX + dx][player.playerY + dy] == 2) {
            if (!isSolving) {
                gp.hpTemp = player.playerHp;
                player.playerX += dx;
                player.playerY += dy;
            } else {
                map[player.playerX][player.playerY] = player.playerTileNum;
                player.solved = true;
            }
        } else if (map[player.playerX + dx][player.playerY + dy] == 5) {
            map[player.playerX][player.playerY] = trace;
            player.playerX += dx;
            player.playerY += dy;
            map[player.playerX][player.playerY] = player.playerTileNum;
            int keyIndex = searchKey(keys);
            playMusic(7);
            if (keyIndex != -1) {
                keys.get(keyIndex).openPath(map);
                keys.remove(keyIndex);
            }
            clearTrace(map);
        } else if (map[player.playerX + dx][player.playerY + dy] == 7 || map[player.playerX + dx][player.playerY + dy] == 8
                || map[player.playerX + dx][player.playerY + dy] == 9) {
            Monster x = getId(monsters, player.playerX + dx, player.playerY + dy);
            System.out.println("You encountered a monster!" + x);
            boolean win = winBattle(x);
            if (win) {
                map[player.playerX][player.playerY] = trace;
                player.playerX += dx;
                player.playerY += dy;
                map[player.playerX][player.playerY] = player.playerTileNum;
                if (!isSolving) {
                    resetTraps(map);
                }
            } 
            if (player.playerHp < 0) {
                player.playerHp = 0;
            }
        } else if (map[player.playerX + dx][player.playerY + dy] == 11) {
            map[player.playerX][player.playerY] = trace;
            player.playerX += dx;
            player.playerY += dy;
            map[player.playerX][player.playerY] = player.playerTileNum;
            gold++;
            playMusic(1);
            if (isSolving) {
                clearTrace(map);
            }
            resetTraps(map);
        } else if (map[player.playerX + dx][player.playerY + dy] == 10) {
            if (!isSolving) {
                gp.hpTemp = player.playerHp;
                gp.tileM.changeMap(player);
                playMusic(6);
            } else {
                map[player.playerX][player.playerY] = player.playerTileNum;
                player.solved = true;
            }
        } else if (map[player.playerX + dx][player.playerY + dy] == 12) {
            Trap triggered = getIdTrap(gp.traps, player.playerX + dx, player.playerY + dy);
            if (triggered != null && !triggeredTraps.contains(triggered)) {
                triggeredTraps.add(triggered);
            }
            map[player.playerX][player.playerY] = trace;
            player.playerX += dx;
            player.playerY += dy;
            map[player.playerX][player.playerY] = player.playerTileNum;
            System.out.println("You stepped on a trap! " + trapDmg + " HP.");
            player.playerHp -= trapDmg;
            playMusic(2);
        } else if (map[player.playerX + dx][player.playerY + dy] == 14) {
            map[player.playerX][player.playerY] = trace;
            map[player.playerX + dx][player.playerY + dy] = 15;
            player.playerX += dx;
            player.playerY += dy;
            System.out.println("You found a chest ! ");
            isOpenChest = true;
            gp.tileM.transform(player);
            resetTraps(map);
            clearTrace(map);
        } else if (map[player.playerX + dx][player.playerY + dy] != 1 && map[player.playerX + dx][player.playerY + dy] != 4
                && map[player.playerX + dx][player.playerY + dy] != 2) {
            map[player.playerX][player.playerY] = trace;
            player.playerX += dx;
            player.playerY += dy;
            map[player.playerX][player.playerY] = player.playerTileNum;
            if (!isSolving) {
                resetTraps(map);
            }
        }
    }

    public Monster getId(ArrayList<Monster> monster, int x, int y) {
        for (Monster monster2 : monster) {
            if (monster2.monsterX == x && monster2.monsterY == y) {
                return monster2;
            }
        }
        return null;
    }

    public Trap getIdTrap(ArrayList<Trap> trap, int x, int y) {
        for (Trap trap2 : trap) {
            if (trap2.trapX == x && trap2.trapY == y) {
                return trap2;
            }
        }
        return null;
    }

    public void resetTraps(int[][] map) { // reset trap
        for (Trap t : triggeredTraps) {
            map[t.trapX][t.trapY] = 12;
        }
        triggeredTraps.clear();
    }

    public void playMusic(int i) {
        if (!gp.isSolving) {
            sound.setFile(i);
            sound.play();
        }
    }
    
    public boolean winBattle(Monster x) {
        while (playerHp > 0 && x.hp > 0) {
            x.hp -= (playerAtk - x.def);
            playerHp -= x.atk;
            if (playerHp < 0) {
                playerHp = 0;
            }
        }

        if (x.hp <= 0) {
            return true; // Player menang
        } else {
            return false; // Player kalah
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
