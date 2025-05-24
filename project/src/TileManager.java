import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class TileManager {
    GamePanel gp;
    Tile[] tile;
    int[][] mapTile;
    Sound sound = new Sound();

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[30];
        mapTile = new int[gp.MAX_WORLD_COL][gp.MAX_WORLD_ROW];

        // Initialize plates list if it's null
        if (gp.plates == null) {
            gp.plates = new ArrayList<>();
        }

        loadMap("project/src/maps/map1.txt");
        getTileImage();
    }

    public void changeMap(Player player) {
        if (gp.currentMap < gp.maps.size() - 1) {
            gp.currentMap++;
            gp.goldTemp = player.gold;
            gp.hpTemp = player.playerHp;
            gp.solutions.clear();
        }
        loadMap(gp.maps.get(gp.currentMap).path);
        player.playerX = gp.maps.get(gp.currentMap).PlayerStartX;
        player.playerY = gp.maps.get(gp.currentMap).PlayerStartY;
        gp.playerXTemp = gp.maps.get(gp.currentMap).PlayerStartX;
        gp.playerYTemp = gp.maps.get(gp.currentMap).PlayerStartY;
        mapTile[player.playerX][player.playerY] = 3;
        gp.copyMap(gp.mapTemp, mapTile);
    }

    public void getTileImage() {
        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(new File("project/img/way.png"));
            tile[0].collison = false;

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(new File("project/img/wall.png"));
            tile[1].collison = false;

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(new File("project/img/princess.png"));
            tile[2].collison = false;

            tile[3] = new Tile();
            tile[3].image = ImageIO.read(new File("project/img/prince.png"));
            tile[3].collison = false;

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(new File("project/img/path.png"));
            tile[4].collison = false;

            tile[5] = new Tile();
            tile[5].image = ImageIO.read(new File("project/img/plate.png"));
            tile[5].collison = false;

            tile[6] = new Tile();
            tile[6].image = ImageIO.read(new File("project/img/path.png"));
            tile[6].collison = false;

            tile[7] = new Tile();
            tile[7].image = ImageIO.read(new File("project/img/spider.png"));
            tile[7].collison = false;

            tile[8] = new Tile();
            tile[8].image = ImageIO.read(new File("project/img/ogre.png"));
            tile[8].collison = false;

            tile[9] = new Tile();
            tile[9].image = ImageIO.read(new File("project/img/redDragon.png"));
            tile[9].collison = false;

            tile[10] = new Tile();
            tile[10].image = ImageIO.read(new File("project/img/stair.png"));
            tile[10].collison = false;

            tile[11] = new Tile();
            tile[11].image = ImageIO.read(new File("project/img/gold.png"));
            tile[11].collison = false;

            tile[12] = new Tile();
            tile[12].image = ImageIO.read(new File("project/img/trap.png"));
            tile[12].collison = false;

            tile[14] = new Tile();
            tile[14].image = ImageIO.read(new File("project/img/chest.png"));
            tile[14].collison = false;

            tile[15] = new Tile();
            tile[15].image = ImageIO.read(new File("project/img/armoredPrince.png"));
            tile[15].collison = false;

            tile[16] = new Tile();
            tile[16].image = ImageIO.read(new File("project/img/heal.png"));
            tile[16].collison = false;

            tile[17] = new Tile();
            tile[17].image = ImageIO.read(new File("project/img/phoenix.png"));
            tile[17].collison = false;

        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    public void transform(Player player) {
        player.playerTileNum = 15;
        player.playerHp = 2000;
        player.playerAtk = 2000;
        player.isArmored = true;
        if (!gp.isSolving) {
            sound.setFile(4);
            sound.playOnce();
        }
    }

    public void randomGold(int[][] map) {
        for (int i = 0; i < gp.MAX_GOLD_PERMAP; i++) {
            int x = (int) (Math.random() * gp.MAX_WORLD_COL);
            int y = (int) (Math.random() * gp.MAX_WORLD_ROW);
            if (map[x][y] == 0) {
                map[x][y] = 11;
            } else {
                i--;
            }
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

            int totalTiles = gp.MAX_WORLD_COL * gp.MAX_WORLD_COL;
            if (numbers.size() != totalTiles) {
                throw new IllegalArgumentException("Jumlah angka dalam file tidak sesuai dengan ukuran peta ("
                        + gp.MAX_WORLD_COL + "x" + gp.MAX_WORLD_COL + "). Dibutuhkan " + totalTiles
                        + " angka, tetapi ditemukan " + numbers.size() + ".");
            }

            int col = 0;
            int row = 0;
            for (int num : numbers) {
                mapTile[col][row] = num;
                col++;
                if (col == gp.MAX_WORLD_COL) {
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
        // set key
        setWall(mapTile, gp.currentMap);
        // set potion
        setPotionMap(mapTile, gp.currentMap);
        // set monster
        setMonsterMap(mapTile, gp.currentMap);
        // set trap
        setTrapMap(mapTile, gp.currentMap);
        // pas load random posisi gold
        randomGold(mapTile);
    }

    public void setPotionMap(int[][] map, int currentlvl) {
        if (currentlvl == 1) {
            gp.potions.add(new Potion(6, 1));
            for (int i = 0; i < gp.potions.size(); i++) {
                gp.potions.get(i).setPotion(map);
            }
        }
        if (currentlvl == 2) {
            gp.potions.clear();
            gp.potions.add(new Potion(11, 5));
            for (int i = 0; i < gp.potions.size(); i++) {
                gp.potions.get(i).setPotion(map);
            }
        }
        if (currentlvl == 3) {
            gp.potions.clear();
            gp.potions.add(new Potion(7, 7));
            gp.potions.add(new Potion(9, 2));
            for (int i = 0; i < gp.potions.size(); i++) {
                gp.potions.get(i).setPotion(map);
            }
        }

        if (currentlvl == 4) {
            gp.potions.clear();
            gp.potions.add(new Potion(2, 12));
            gp.potions.add(new Potion(4, 1));
            for (int i = 0; i < gp.potions.size(); i++) {
                gp.potions.get(i).setPotion(map);
            }
        }
    }

    public void setTrapMap(int[][] map, int currentlvl) {
        if (currentlvl == 1) {
            gp.traps.add(new Trap(3, 6));
            gp.traps.add(new Trap(5, 10));
            gp.traps.add(new Trap(13, 7));
            gp.traps.add(new Trap(11, 12));
            for (int i = 0; i < gp.traps.size(); i++) {
                gp.traps.get(i).setTrap(map);
            }
        }
        if (currentlvl == 2) {
            gp.traps.clear();
            gp.traps.add(new Trap(3, 13));
            gp.traps.add(new Trap(4, 3));
            gp.traps.add(new Trap(10, 13));
            for (int i = 0; i < gp.traps.size(); i++) {
                gp.traps.get(i).setTrap(map);
            }
        }
        if (currentlvl == 3) {
            gp.traps.clear();
            gp.traps.add(new Trap(5, 12));
            gp.traps.add(new Trap(7, 5));
            gp.traps.add(new Trap(12, 9));
            for (int i = 0; i < gp.traps.size(); i++) {
                gp.traps.get(i).setTrap(map);
            }
        }
        if (currentlvl == 4) {
            gp.traps.clear();
            gp.traps.add(new Trap(6, 3));
            gp.traps.add(new Trap(6, 11));
            for (int i = 0; i < gp.traps.size(); i++) {
                gp.traps.get(i).setTrap(map);
            }
        }
    }

    public void setMonsterMap(int[][] map, int currentlvl) {
        if (currentlvl == 2) {
            gp.monsters.add(new Monster(13, 12));
            gp.monsters.add(new Monster(6, 11));
            gp.monsters.add(new Monster(6, 13));
            for (int i = 0; i < gp.monsters.size(); i++) {
                gp.monsters.get(i).setSpider(map);
            }
        } else if (currentlvl == 3) {
            gp.monsters.clear();
            gp.monsters.add(new Monster(7, 4));
            gp.monsters.add(new Monster(6, 12));
            gp.monsters.add(new Monster(6, 1));
            for (int i = 0; i < gp.monsters.size(); i++) {
                gp.monsters.get(i).setSpider(map);
            }
            gp.monsters.add(new Monster(13, 5));
            gp.monsters.add(new Monster(9, 9));
            gp.monsters.get(3).setOgre(map);
            gp.monsters.get(4).setOgre(map);

        } else if (currentlvl == 4) {
            gp.monsters.clear();
            gp.monsters.add(new Monster(1, 5));
            gp.monsters.get(0).setPhx(map);(map);
            gp.monsters.add(new Monster(9, 12));
            gp.monsters.add(new Monster(9, 2));
            gp.monsters.get(1).setSpider(map);
            gp.monsters.get(2).setSpider(map);
            gp.monsters.add(new Monster(11, 5));
            gp.monsters.add(new Monster(1, 9));
            gp.monsters.add(new Monster(3, 7));
            gp.monsters.get(3).setOgre(map);
            gp.monsters.get(4).setOgre(map);
            gp.monsters.get(5).setDragon(map);

        }
    }

    public void setWall(int map[][], int currentlvl) {
        if (currentlvl == 1) {
            gp.plates.clear();
            gp.plates.add(new Plate(3, 11, 5, 12));
            gp.plates.add(new Plate(1, 13, 10, 3));
            gp.plates.add(new Plate(5, 1, 10, 13));
            setPlates(map, gp.plates);
        }
        if (currentlvl == 2) {
            gp.plates.clear();
            gp.plates.add(new Plate(7, 13, 3, 12));
            gp.plates.add(new Plate(7, 11, 13, 2));
            setPlates(map, gp.plates);
        }
        if (currentlvl == 3) {
            gp.plates.clear();
            gp.plates.add(new Plate(11, 3, 9, 8));
            gp.plates.add(new Plate(9, 3, 12, 1));
            gp.plates.add(new Plate(1, 13, 11, 10));
            gp.plates.add(new Plate(3, 5, 9, 4));
            setPlates(map, gp.plates);
        }
        if (currentlvl == 4) {
            gp.plates.clear();
            gp.plates.add(new Plate(8, 2, 1, 4));
            gp.plates.add(new Plate(8, 8, 12, 5));
            gp.plates.add(new Plate(8, 12, 1, 10));
            gp.plates.add(new Plate(6, 13, 5, 7));
            gp.plates.add(new Plate(11, 4, 6, 10));
            gp.plates.add(new Plate(11, 10, 6, 4));
            setPlates(map, gp.plates);
        }
    }

    public void setPlates(int map[][], ArrayList<Plate> Plates) {
        for (int i = 0; i < Plates.size(); i++) {
            Plates.get(i).setKey(map);
        }
    }

    public void draw(Graphics2D g2) {

        int worldCol = 0;
        int worldRow = 0;
        while (worldCol < gp.MAX_WORLD_COL && worldRow < gp.MAX_WORLD_ROW) {

            int tileNum = mapTile[worldCol][worldRow];

            int worldX = worldCol * gp.TILE_SIZE;
            int worldY = worldRow * gp.TILE_SIZE + 205;

            if (tile[tileNum].image != null) {
                g2.drawImage(tile[tileNum].image, worldX, worldY, gp.TILE_SIZE, gp.TILE_SIZE, null);
            } else {
                System.err.println("Gambar tile tidak ditemukan untuk tile number: " + tileNum);
            }

            worldCol++;

            if (worldCol == gp.MAX_WORLD_COL) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
