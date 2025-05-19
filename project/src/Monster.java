public class Monster {
    int monsterX, monsterY;
    int atk, def, hp;

    public Monster(int monsterX, int monsterY) {
        this.monsterX = monsterX;
        this.monsterY = monsterY;
    }

    public void setSpider(int map[][]) {
        hp = 5;
        atk = 3;
        def = 2;
        map[monsterX][monsterY] = 7; // set tile monster spider
    }

    public void setOgre(int map[][]) {
        hp = 10;
        atk = 1;
        def = 1;
        map[monsterX][monsterY] = 8; // set tile monster ogre
    }

    public void setDragon(int map[][]) {
        hp = 10;
        atk = 6;
        def = 3;
        map[monsterX][monsterY] = 9; // set tile monster dragon
    }
}
