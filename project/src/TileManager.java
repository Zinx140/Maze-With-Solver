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

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[30];
        mapTile = new int[gp.MAX_WORLD_COL][gp.MAX_WORLD_ROW];

        loadMap("project/src/maps/map1.txt");
        getTileImage();
    }

    public void changeMap(Player player) {
        gp.currentMap++;
        loadMap(gp.maps.get(gp.currentMap).path);
        player.playerX = gp.maps.get(gp.currentMap).PlayerStartX;
        player.playerY = gp.maps.get(gp.currentMap).PlayerStartY;
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

        } catch (IOException e) {
            e.getStackTrace();
        }
    }


    public void randomGold(int[][] map) {
       for(int i=0;i<gp.MAX_GOLD_PERMAP;i++){
            int x = (int) (Math.random() * gp.MAX_WORLD_COL);
            int y = (int) (Math.random() * gp.MAX_WORLD_ROW);
            if(map[x][y] == 0){
                map[x][y] = 11;
            }else{
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

        //pas load random posisi gold
        randomGold(mapTile);
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
