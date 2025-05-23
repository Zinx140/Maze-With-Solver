public class Potion {
    int potionX, potionY;

    public Potion(int potionX, int potionY) {
        this.potionX = potionX;
        this.potionY = potionY;
    }

    public void setPotion(int map[][]) {
        map[potionX][potionY] = 16; // Set tile potion
    }

    public void healPlayer(Player player) {
        player.playerHp = player.maxHp;
    }
}
