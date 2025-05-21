public class Solution {
    public int[][] map;
    public int path;
    public Player player;

    public Solution(int[][] map, int path, Player player) {
        this.map = new int[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            System.arraycopy(map[i], 0, this.map[i], 0, map[i].length);
        }
        this.path = path;
        this.player = player;
    }
}
