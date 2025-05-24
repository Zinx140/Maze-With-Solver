public class Monster implements Cloneable {
    int monsterX, monsterY;
    String nama = "";
    int atk, def, hp, maxHP;

    public Monster(int monsterX, int monsterY) {
        this.monsterX = monsterX;
        this.monsterY = monsterY;
    }

    public void setSpider(int map[][]) {
        nama = "Spider";
        hp = 200;
        maxHP = 200;
        atk = 8;
        def = 5;
        map[monsterX][monsterY] = 7; // set tile monster spider
    }

    public void setOgre(int map[][]) {
        nama = "Ogre";
        hp = 350;
        maxHP = 350;
        atk = 12;
        def = 8;
        map[monsterX][monsterY] = 8; // set tile monster ogre
    }

    public void setPhx(int map[][]) {
        nama = "Phoenix";
        hp = 100;
        maxHP = 100;
        atk = 99;
        def = 0;
        map[monsterX][monsterY] = 17; // set tile monster pxh
    }

    public void setDragon(int map[][]) {
        nama = "Dragon";
        hp = 1000;
        maxHP = 1000;
        atk = 200;
        def = 35;
        map[monsterX][monsterY] = 9; // set tile monster dragon
    }

    public Monster clone() {
        try {
            return (Monster) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Seharusnya tidak terjadi
        }
    }
}
