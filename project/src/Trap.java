public class Trap {
    int trapX, trapY;

    public Trap(int trapX, int trapY) {
        this.trapX = trapX;
        this.trapY = trapY;
    }

    public void setTrap(int map[][]) {
        map[trapX][trapY] = 12; // Set tile trap
    }
}
