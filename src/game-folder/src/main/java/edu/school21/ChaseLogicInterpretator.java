package edu.school21;

import java.util.List;

public class ChaseLogicInterpretator {
    private int[][] map;
    private int size;
    private int startX;
    private int startY;
    private int destX;
    private int destY;

    public ChaseLogicInterpretator(int[][] map, int size) {
        this.map = map;
        this.size = size;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (map[i][j] == -2) {
                    startX = i;
                    startY = j;
                    map[i][j] = 0;
                } else if (map[i][j] == 2) {
                    destX = i;
                    destY = j;
                    map[i][j] = 0;
                }
            }
        }
    }

    public char returnStep() {
        AStarSearch aStarSearch = new AStarSearch();
        AStarSearch.Pair start = new AStarSearch.Pair(startX, startY);
        AStarSearch.Pair dest = new AStarSearch.Pair(destX, destY);
        List<AStarSearch.Pair> path = aStarSearch.buildPathList(map, size, dest, start);
        if (path != null) {

            AStarSearch.Pair next = path.get(1);
            if (next.first == start.first - 1) {
                return 'w';
            } else if (next.first == start.first + 1) {
                return 's';
            } else if (next.second == start.second - 1) {
                return 'a';
            } else if (next.second == start.second + 1) {
                return 'd';
            }
        }
        return 'r';
    }

}
