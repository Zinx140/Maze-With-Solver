import java.util.ArrayList;

public class Player implements Cloneable {
    int playerX;
    int playerY;
    int playerHp;
    int playerAtk;
    int maxHp;
    GamePanel gp;
    int gold = 0;
    boolean isOpenChest = false;
    int trapDmg = 3;
    ArrayList<Trap> triggeredTraps = new ArrayList<>();
    Sound sound = new Sound();

    public Player(int playerX, int playerY, GamePanel gp) {
        this.playerX = playerX;
        this.playerY = playerY;
        this.gp = gp;
        this.playerHp = 100; // Set initial HP
        this.maxHp = 100; // Set maximum HP
        this.playerAtk = 100; // Set initial attack power
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
                if (map[i][j] == 4) {
                    map[i][j] = 6;
                }
            }
        }
    }

    public void move(int map[][], int direction, ArrayList<Plate> keys, ArrayList<Monster> monsters,
        boolean isSolving) {
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
                } else if (map[playerX][playerY - 1] == 7 || map[playerX][playerY - 1] == 8
                        || map[playerX][playerY - 1] == 9) { // lek ketemu monster gaiso lewat kecuali matek
                    Monster x = getId(monsters, playerX, playerY - 1);
                    if (x.hp > 0) {
                        x.hp -= (playerAtk - x.def); // nunggu atk nya bang
                        playerHp -= x.atk;
                    } else {
                        map[playerX][playerY] = trace; // kalo udah mati jadiin trace
                        playerY--;
                        map[playerX][playerY] = 3;
                        if (!isSolving) {
                            resetTraps(map);
                        }
                    }
                } else if (map[playerX][playerY - 1] == 11) {
                    map[playerX][playerY] = trace;
                    playerY--;
                    map[playerX][playerY] = 3;
                    gold++;
                    playMusic(1);
                    clearTrace(map);
                    resetTraps(map);
                } else if (map[playerX][playerY - 1] == 10) {
                    if (!isSolving) {
                        gp.tileM.changeMap(this);
                    } else {
                        map[playerX][playerY] = trace;
                        playerY--;
                    }
                } else if (map[playerX][playerY - 1] == 12) {
                    Trap triggered = getIdTrap(gp.traps, playerX, playerY - 1);
                    if (triggered != null && !triggeredTraps.contains(triggered)) {
                        triggeredTraps.add(triggered);
                    }
                    map[playerX][playerY] = trace;
                    playerY--;
                    map[playerX][playerY] = 3;
                    System.out.println("You stepped on a trap! " + trapDmg + " HP.");
                    playerHp -= trapDmg;
                    playMusic(2);
                } else if (map[playerX][playerY - 1] == 14) {
                    map[playerX][playerY] = trace;
                    playerY--;
                    map[playerX][playerY] = 3;
                    System.out.println("You found a chest ! ");
                    isOpenChest = true;
                    gp.tileM.transform();
                    clearTrace(map);
                    resetTraps(map);
                } else if (map[playerX][playerY - 1] != 1 && map[playerX][playerY - 1] != 4
                        && map[playerX][playerY - 1] != 2) {
                    map[playerX][playerY] = trace;
                    playerY--;
                    map[playerX][playerY] = 3;
                    if (!isSolving) {
                        resetTraps(map);
                    }
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
                } else if (map[playerX][playerY + 1] == 7 || map[playerX][playerY + 1] == 8
                        || map[playerX][playerY + 1] == 9) {
                    Monster x = getId(monsters, playerX, playerY + 1);
                    if (x.hp > 0) {
                        x.hp -= (playerAtk - x.def); // nunggu atk nya bang
                        playerHp -= x.atk;
                    } else {
                        map[playerX][playerY] = trace; // kalo udah mati jadiin trace
                        playerY++;
                        map[playerX][playerY] = 3;
                        if (!isSolving) {
                            resetTraps(map);
                        }
                    }
                } else if (map[playerX][playerY + 1] == 11) {
                    map[playerX][playerY] = trace;
                    playerY++;
                    map[playerX][playerY] = 3;
                    gold++;
                    playMusic(1);
                    resetTraps(map);
                    clearTrace(map);
                } else if (map[playerX][playerY + 1] == 10) {
                    if (!isSolving) {
                        gp.tileM.changeMap(this);
                    } else {
                        map[playerX][playerY] = trace;
                        playerY++;
                    }
                } else if (map[playerX][playerY + 1] == 12) {
                    Trap triggered = getIdTrap(gp.traps, playerX, playerY + 1);
                    if (triggered != null && !triggeredTraps.contains(triggered)) {
                        triggeredTraps.add(triggered);
                    }
                    map[playerX][playerY] = trace;
                    playerY++;
                    map[playerX][playerY] = 3;
                    System.out.println("You stepped on a trap! " + trapDmg + " HP.");
                    playerHp -= trapDmg;
                    playMusic(2);
                } else if (map[playerX][playerY + 1] == 14) {
                    map[playerX][playerY] = trace;
                    playerY++;
                    map[playerX][playerY] = 3;
                    System.out.println("You found a chest ! ");
                    gp.tileM.transform();
                    isOpenChest = true;
                    clearTrace(map);
                    resetTraps(map);
                } else if (map[playerX][playerY + 1] != 1 && map[playerX][playerY + 1] != 4
                        && map[playerX][playerY + 1] != 2) {
                    map[playerX][playerY] = trace;
                    playerY++;
                    map[playerX][playerY] = 3;
                    if (!isSolving) {
                        resetTraps(map);
                    }
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
                } else if (map[playerX - 1][playerY] == 7 || map[playerX - 1][playerY] == 8
                        || map[playerX - 1][playerY] == 9) {
                    Monster x = getId(monsters, playerX - 1, playerY);
                    if (x.hp > 0) {
                        x.hp -= (playerAtk - x.def); // nunggu atk nya bang
                        playerHp -= x.atk;
                    } else {
                        map[playerX][playerY] = trace; // kalo udah mati jadiin trace
                        playerX--;
                        map[playerX][playerY] = 3;
                        if (!isSolving) {
                            resetTraps(map);
                        }
                    }
                } else if (map[playerX - 1][playerY] == 11) {
                    map[playerX][playerY] = trace;
                    playerX--;
                    map[playerX][playerY] = 3;
                    gold++;
                    playMusic(1);
                    clearTrace(map);
                    resetTraps(map);
                } else if (map[playerX - 1][playerY] == 10) {
                    if (!isSolving) {
                        gp.tileM.changeMap(this);
                    } else {
                        map[playerX][playerY] = trace;
                        playerX--;
                    }
                } else if (map[playerX - 1][playerY] == 12) {
                    Trap triggered = getIdTrap(gp.traps, playerX - 1, playerY);
                    if (triggered != null && !triggeredTraps.contains(triggered)) {
                        triggeredTraps.add(triggered);
                    }
                    map[playerX][playerY] = trace;
                    playerX--;
                    map[playerX][playerY] = 3;
                    System.out.println("You stepped on a trap! " + trapDmg + " HP.");
                    playerHp -= trapDmg;
                    playMusic(2);
                } else if (map[playerX - 1][playerY] == 14) {
                    map[playerX][playerY] = trace;
                    playerX--;
                    map[playerX][playerY] = 3;
                    System.out.println("You found a chest ! ");
                    isOpenChest = true;
                    gp.tileM.transform();
                    resetTraps(map);
                    clearTrace(map);
                } else if (map[playerX - 1][playerY] != 1 && map[playerX - 1][playerY] != 4
                        && map[playerX - 1][playerY] != 2) {
                    map[playerX][playerY] = trace;
                    playerX--;
                    map[playerX][playerY] = 3;
                    if (!isSolving) {
                        resetTraps(map);
                    }
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
                } else if (map[playerX + 1][playerY] == 7 || map[playerX + 1][playerY] == 8
                        || map[playerX + 1][playerY] == 9) {
                    Monster x = getId(monsters, playerX + 1, playerY);

                    if (x.hp > 0) {
                        x.hp -= (playerAtk - x.def); // nunggu atk nya bang
                        playerHp -= x.atk;
                    } else {
                        map[playerX][playerY] = trace; // kalo udah mati jadiin trace
                        playerX++;
                        map[playerX][playerY] = 3;
                        if (!isSolving) {
                            resetTraps(map);
                        }
                    }
                } else if (map[playerX + 1][playerY] == 11) { // gold
                    map[playerX][playerY] = trace;
                    playerX++;
                    map[playerX][playerY] = 3;
                    gold++;
                    playMusic(1);
                    resetTraps(map);
                    clearTrace(map);
                } else if (map[playerX + 1][playerY] == 10 && !isSolving) {
                    if (!isSolving) {
                        gp.tileM.changeMap(this);
                    } else {
                        map[playerX][playerY] = trace;
                        playerX++;
                    }
                } else if (map[playerX + 1][playerY] == 12) {
                    Trap triggered = getIdTrap(gp.traps, playerX + 1, playerY);
                    if (triggered != null && !triggeredTraps.contains(triggered)) {
                        triggeredTraps.add(triggered);
                    }
                    map[playerX][playerY] = trace;
                    playerX++;
                    map[playerX][playerY] = 3;
                    System.out.println("You stepped on a trap! " + trapDmg + " HP.");
                    playerHp -= trapDmg;
                    playMusic(2);
                } else if (map[playerX + 1][playerY] == 14) {
                    map[playerX][playerY] = trace;
                    playerX++;
                    map[playerX][playerY] = 3;
                    System.out.println("You found a chest ! ");
                    isOpenChest = true;
                    resetTraps(map);
                    gp.tileM.transform();
                    clearTrace(map);
                } else if (map[playerX + 1][playerY] != 1 && map[playerX + 1][playerY] != 4
                        && map[playerX + 1][playerY] != 2) {
                    map[playerX][playerY] = trace;
                    playerX++;
                    map[playerX][playerY] = 3;
                    if (!isSolving) {
                        resetTraps(map);
                    }
                }
                break;
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
        sound.setFile(i);
        sound.play();
    }

    public Player clone() {
        try {
            return (Player) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Seharusnya tidak terjadi
        }
    }

}
