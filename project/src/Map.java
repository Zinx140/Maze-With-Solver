public class Map {
    String path;
    int stage;
    static int mapQty = 0;
    int PlayerStartX;
    int PlayerStartY;

    public Map(String path, int PlayerStartX, int PlayerStartY) {
        this.path = path;
        this.PlayerStartX = PlayerStartX;
        this.PlayerStartY = PlayerStartY;
        stage = mapQty;
        mapQty++;
    }

}
