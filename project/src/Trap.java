public class Trap {
    int trapX, trapY;
    int dmg;

    public Trap(int trapX, int trapY) {
        this.trapX = trapX;
        this.trapY = trapY;
        this.dmg = 1; // Default damage
    }

    public void setTrap(int map[][]) {
        map[trapX][trapY] = 12; // Set tile trap
    }
}
