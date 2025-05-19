public class Monster {
    int monsterX, monsterY;
    int atk, def, hp;

    public Monster(int monsterX, int monsterY) {
        this.monsterX = monsterX;
        this.monsterY = monsterY;
    }

    public void setSpider(int map[][]) {
        hp = 500;
        atk = 10;
        def = 10;
        map[monsterX][monsterY] = 7; // set tile monster spider
    }

    public void setOgre(int map[][]) {
        hp = 800;
        atk = 15;
        def = 10;
        map[monsterX][monsterY] = 8; // set tile monster ogre
    }

    public void setDragon(int map[][]) {
        hp = 1000;
        atk = 50;
        def = 30;
        map[monsterX][monsterY] = 9; // set tile monster dragon
    }
}
