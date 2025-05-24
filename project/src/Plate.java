public class Plate {
    int keyX;
    int keyY;
    int targetX;
    int targetY;

    public Plate(int keyX, int keyY, int targetX, int targetY) {
        this.keyX = keyX;
        this.keyY = keyY;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public void openPath(int map[][]) {
        map[targetX][targetY] = 0; // Set tile jalan
    }

    public void setKey(int map[][]) {
        map[keyX][keyY] = 5; // Set tile kunci
        map[targetX][targetY] = 1; // Set tile dinding
    }

}
