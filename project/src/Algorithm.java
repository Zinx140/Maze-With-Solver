import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;


public class Algorithm {
    int[][][] hasil;
    int mapY = 0;
    int mapX = 0;
    char[][][] map;  // 3d buat [map-ke?][y][x]
    boolean[][][] visited;  //buat ngekeep track path nya udah dilalui apa belum
    int[][][] path;  //buat nyimpen langkah path yang udah dilalui
    int pathCount = 1;  //buat nyimpen current path count
    String[] mapPath;  //buat nyimpen nama file map
    int playerX=0, playerY=0, playerMap=0;  //buat nyimpen posisi start
    int endX=14, endY=14, endMap=0;  //buat nyimpen posisi end
    boolean found = false;  //buat ngecek udah ketemu path apa belum
    int[][] map1 = new int[15][15];

    Scanner scanner = new Scanner(System.in);

    Algorithm(){
        loadMap("Maze-With-Solver/project/src/listMap.txt");  //baca listmap.txt

        BackTracking(this.visited, this.path, this.pathCount, this.playerX, this.playerY, this.playerMap);

    }

    void printHasil(int hasil[][][]){
        for(int i=0;i<mapPath.length;i++){
            System.out.println("Map " + mapPath[i] + ": ");
            for (int y=0;y<map[0].length;y++){
                for (int x=0;x<map[0][0].length;x++){
                    System.out.print(hasil[i][y][x] + " ");  //print hasil
                }
                System.out.println();
            }
        }
    }

    void BackTracking(boolean[][][] visited, int[][][] path, int pathCount, int playerX, int playerY, int playerMap){
        
        if(playerX == endX && playerY == endY && playerMap == endMap){  //base case -> kalau udah sampe tujuan
            System.out.println("=== Path found! ==="); 
            found = true; 

            visited[playerMap][playerY][playerX] = true;  //mark visited
            path[playerMap][playerY][playerX] = pathCount;  //kasih nomor urut ke arr

            for(int i=0;i<mapPath.length;i++){
                for (int y=0;y<map[0].length;y++){
                    for (int x=0;x<map[0][0].length;x++){
                        hasil[i][y][x] = path[i][y][x];  
                    }
                }
            }
            System.out.println("=========================");
            printHasil(hasil);
            System.out.println("=========================");
            pause();
            return;  
        }
        else{
            System.out .println("Player X: " + playerX + " Player Y: " + playerY + " Player Map: " + playerMap);  //print posisi player
            printHasil(path);
            for(int i=0;i<4;i++){  // 4 arah (atas, bawah, kiri, kanan)
                                    
                    
                    
                    //copy array visited ke array copy
                    boolean[][][] copyVisited = new boolean[visited.length][visited[0].length][visited[0][0].length];
                    int[][][] copyPath = new int[path.length][path[0].length][path[0][0].length];
                    for(int j=0;j<mapPath.length;j++){
                        for (int y=0;y<map[0].length;y++){
                            for (int x=0;x<map[0][0].length;x++){
                                copyPath[j][y][x] = path[j][y][x];  
                                copyVisited[j][y][x] = visited[j][y][x];  //copy visited
                            }
                        }
                    }
                    
                    //====================== update flag-flag buat part dibacktrack yang skarang
                    copyVisited[playerMap][playerY][playerX] = true;  //mark visited
                    copyPath[playerMap][playerY][playerX] = pathCount;  //kasih nomor urut ke arr
                    //====================
                    


                    //backtrack kalo valid
                    switch (i) {

                        case 0: //atas
                        System.out.println(">> Atas");
                        if(isValid(visited,path, pathCount+1, playerX, playerY-1, playerMap))
                            BackTracking(copyVisited,copyPath, pathCount+1, playerX, playerY-1, playerMap);
                        System.out.println("<< atas Back ============");
                        break;
                        case 1: //bawah
                        System.out.println(">> Bawah");
                        if(isValid(visited,path, pathCount+1, playerX, playerY+1, playerMap))
                            BackTracking(copyVisited,copyPath, pathCount+1, playerX, playerY+1, playerMap);
                        System.out.println("<< bawah Back ============");
                        break;
                        case 2: //kiri
                        System.out.println(">> Kiri");
                        if(isValid(visited,path, pathCount+1, playerX-1, playerY, playerMap))
                        BackTracking(copyVisited,copyPath, pathCount+1, playerX-1, playerY, playerMap);
                        System.out.println("<< kiri Back ============");
                        break;
                        case 3: //kanan
                        System.out.println(">> Kanan");
                        if(isValid(visited,path, pathCount+1, playerX+1, playerY, playerMap))
                        BackTracking(copyVisited,copyPath, pathCount+1, playerX+1, playerY, playerMap);
                        System.out.println("<< kanan Back ============");
                            break;
                       
                    }

                


            }
        }
    }

    boolean isValid(boolean[][][] visited,int[][][] path, int pathCount, int playerX, int playerY, int playerMap){

        //cek out of bounds ga
        if(playerX < 0 || playerX >= map[0][0].length || playerY < 0 || playerY >= map[0].length || playerMap < 0 || playerMap >= map.length) 
            {
                System.out.println("Out of bounds");
                return false;
            }
        //cek udah visited ga
        if(visited[playerMap][playerY][playerX] == true) {
            System.out.println("Udah visited");
            return false;
        }
        //tes iseng ada halangan
        if(map[playerMap][playerY][playerX] == '2') {
            System.out.println("Ada halangan");
            return false;

        }
        


        //kalo valid gas true
        return true; 
        
    }
    
    void pause(){
        System.out.println("Press Enter to continue...");   
        scanner.nextLine();  //wait for enter
    }

    // public void loadMap(String path) {
    //     try {
    //         File file = new File(path);
    //         if (!file.exists()) {
    //             System.err.println("File tidak ditemukan: " + path);
    //             return;
    //         }

    //         InputStream is = new FileInputStream(file);
    //         BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

    //         List<Integer> numbers = new ArrayList<>();

    //         String line;
    //         while ((line = br.readLine()) != null) {
    //             String[] tokens = line.trim().split(" ");
    //             for (String token : tokens) {
    //                 if (!token.trim().isEmpty()) { // Pastikan token tidak kosong
    //                     numbers.add(Integer.parseInt(token.trim()));
    //                 }
    //             }
    //         }

    //         br.close();

    //         int totalTiles = 15 * 15;
    //         if (numbers.size() != totalTiles) {
    //             throw new IllegalArgumentException("Jumlah angka dalam file tidak sesuai dengan ukuran peta (" 
    //                 + gp.MAX_WORLD_COL + "x" + gp.MAX_WORLD_COL + "). Dibutuhkan " + totalTiles + " angka, tetapi ditemukan " + numbers.size() + ".");
    //         }

    //         int col = 0;
    //         int row = 0;
    //         for (int num : numbers) {
    //             map1[col][row] = num;
    //             col++;
    //             if (col == gp.MAX_WORLD_COL) {
    //                 col = 0;
    //                 row++;
    //             }
    //         }

    //         System.out.println("Peta berhasil dimuat!");

    //     } catch (IOException e) {
    //         System.err.println("Terjadi kesalahan saat membaca file: " + e.getMessage());
    //     } catch (IllegalArgumentException e) {
    //         System.err.println(e.getMessage());
    //     }
    // }


    void loadMap(String file){
        //baca txt map
        try{
            InputStream is = new FileInputStream(file);
            BufferedReader read = new BufferedReader(new InputStreamReader(is, "UTF-8"));  //baca file listmap.txt
            int jumlahMap = Integer.parseInt(read.readLine());  //baca jumlah map
            mapY = Integer.parseInt(read.readLine()); // baca dimensi y map
            mapX = Integer.parseInt(read.readLine()); // dimensi x map
            map = new char[jumlahMap][mapY][mapX];  //inisialisasi map
            mapPath = new String[jumlahMap];  //inisialisasi mapPath
            visited = new boolean[jumlahMap][mapY][mapX];  //inisialisasi visited
            hasil = new int[jumlahMap][mapY][mapX];  //inisialisasi hasil
            path = new int[jumlahMap][mapY][mapX];  //inisialisasi path

            for(int i=0;i<jumlahMap;i++){
                mapPath[i] = read.readLine();  //baca nama file map
            }

            read.close();

        }catch (Exception e) {
            System.out.println("baca listmap, Error: " + e.getMessage());
        }

        //baca isi map
        try {
            for(int i=0;i<mapPath.length;i++){
                BufferedReader read = new BufferedReader(new FileReader(mapPath[i]));
                for (int y=0;y< mapY;y++){
                    String line = read.readLine();
                    for (int x=0;x < mapX;x++){
                        map[i][y][x] = line.charAt(x);  //baca isi map
                    }
                }
                read.close();
            }
        } catch (Exception e) {
            System.out.println("baca isi map(), Error: " + e.getMessage());
        }


        //sekalian inisialisasi visited map ke false semua
        for(int i = 0;i<map.length;i++){
            for(int j = 0;j<map[0].length;j++){
                for(int k = 0;k<map[0][0].length;k++){
                    visited[i][j][k] = false;  //inisialisasi visited
                }
            }
        }


        
    }
  

}
