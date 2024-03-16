package edu.school21;

import com.diogonunes.jcdp.color.api.Ansi;
import com.diogonunes.jcdp.color.ColoredPrinter;
import java.util.ArrayList;
import java.util.List;

public class Map {
    private Integer size;
    private Integer wallsCount;
    private Integer enemiesCount;
    private Properties properties;
    private Cell[][] maze;

    public Map(Integer size, Integer wallsCount, Integer enemiesCount, String profile) {
        this.size = size;
        this.wallsCount = wallsCount;
        this.enemiesCount = enemiesCount;
        properties = new Properties("target/classes/application-" + profile + ".properties");
        this.maze = new Cell[size][size];
    }

    public void generateStartMap() {
        generateRandomMap();
        while (!isTargetReachable()) {
            generateRandomMap();
        }
        insertEnemies();
    }

    private void insertEnemies() {
        for (int i = 0; i < enemiesCount; i++) {
            int x = (int) (Math.random() * size);
            int y = (int) (Math.random() * size);
            if (maze[x][y].getValue() == properties.getEmpty()) {
                maze[x][y].setValue(properties.getEnemy());
            } else {
                i--;
            }
        }
    }

    private void generateRandomMap() {
        maze = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                maze[i][j] = new Cell(i, j, properties.getEmpty());
            }
        }
        int x = (int) (Math.random() * size);
        int y = (int) (Math.random() * size);
        while (maze[x][y].getValue() != properties.getEmpty()) {
            x = (int) (Math.random() * size);
            y = (int) (Math.random() * size);
        }
        maze[x][y].setValue(properties.getPlayer());
        x = (int) (Math.random() * size);
        y = (int) (Math.random() * size);
        while (maze[x][y].getValue() != properties.getEmpty()) {
            x = (int) (Math.random() * size);
            y = (int) (Math.random() * size);
        }
        maze[x][y].setValue(properties.getGoal());
        for (int i = 0; i < wallsCount; i++) {
            x = (int) (Math.random() * size);
            y = (int) (Math.random() * size);
            if (maze[x][y].getValue() == properties.getEmpty()) {
                maze[x][y].setValue(properties.getWall());
            } else {
                i--;
            }
        }
    }

    public boolean movePlayer(char direction) {
        boolean isMoved = false;
        int playerX = 0, playerY = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (maze[i][j].getValue() == properties.getPlayer()) {
                    playerX = i;
                    playerY = j;
                }
            }
        }
        switch (direction) {
            case 'w':
                if (playerX - 1 >= 0 && (maze[playerX - 1][playerY].getValue() == properties.getEmpty() ||
                        maze[playerX - 1][playerY].getValue() == properties.getGoal())) {
                    maze[playerX][playerY].setValue(properties.getEmpty());
                    maze[playerX - 1][playerY].setValue(properties.getPlayer());
                    isMoved = true;
                }
                break;
            case 's':
                if (playerX + 1 < size && (maze[playerX + 1][playerY].getValue() == properties.getEmpty() ||
                        maze[playerX + 1][playerY].getValue() == properties.getGoal())) {
                    maze[playerX][playerY].setValue(properties.getEmpty());
                    maze[playerX + 1][playerY].setValue(properties.getPlayer());
                    isMoved = true;
                }
                break;
            case 'a':
                if (playerY - 1 >= 0 && (maze[playerX][playerY - 1].getValue() == properties.getEmpty() ||
                        maze[playerX][playerY - 1].getValue() == properties.getGoal())) {
                    maze[playerX][playerY].setValue(properties.getEmpty());
                    maze[playerX][playerY - 1].setValue(properties.getPlayer());
                    isMoved = true;
                }
                break;
            case 'd':
                if (playerY + 1 < size && (maze[playerX][playerY + 1].getValue() == properties.getEmpty() ||
                        maze[playerX][playerY + 1].getValue() == properties.getGoal())) {
                    maze[playerX][playerY].setValue(properties.getEmpty());
                    maze[playerX][playerY + 1].setValue(properties.getPlayer());
                    isMoved = true;
                }
                break;
            default:
                break;
        }
        return isMoved;
    }

    public void moveEnemies() {
        List<int[]> enemies = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (maze[i][j].getValue() == properties.getEnemy()) {
                    enemies.add(new int[] { i, j });
                }
            }
        }
        for (int[] enemy : enemies) {
            moveEnemy(enemy[0], enemy[1]);
        }
    }

    private void moveEnemy(int x, int y) {
        if (canEnemyMove(x, y)) {
            ChaseLogic chaseLogic = new ChaseLogic(convertMap(x, y));
            switch (chaseLogic.makeMove()) {
                case 'w':
                    if (x - 1 >= 0 && (maze[x - 1][y].getValue() == properties.getEmpty() ||
                            maze[x - 1][y].getValue() == properties.getPlayer())) {
                        maze[x][y].setValue(properties.getEmpty());
                        maze[x - 1][y].setValue(properties.getEnemy());
                    }
                    break;
                case 's':
                    if (x + 1 < size && (maze[x + 1][y].getValue() == properties.getEmpty() ||
                            maze[x + 1][y].getValue() == properties.getPlayer())) {
                        maze[x][y].setValue(properties.getEmpty());
                        maze[x + 1][y].setValue(properties.getEnemy());
                    }
                    break;
                case 'a':
                    if (y - 1 >= 0 && (maze[x][y - 1].getValue() == properties.getEmpty() ||
                            maze[x][y - 1].getValue() == properties.getPlayer())) {
                        maze[x][y].setValue(properties.getEmpty());
                        maze[x][y - 1].setValue(properties.getEnemy());
                    }
                    break;
                case 'd':
                    if (y + 1 < size && (maze[x][y + 1].getValue() == properties.getEmpty() ||
                            maze[x][y + 1].getValue() == properties.getPlayer())) {
                        maze[x][y].setValue(properties.getEmpty());
                        maze[x][y + 1].setValue(properties.getEnemy());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private int[][] convertMap(int x, int y) {
        int[][] map = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (maze[i][j].getValue() == properties.getWall() ||
                        maze[i][j].getValue() == properties.getEnemy() ||
                        maze[i][j].getValue() == properties.getGoal()) {
                    map[i][j] = 1;
                } else if (maze[i][j].getValue() == properties.getPlayer()) {
                    map[i][j] = 2;
                } else {
                    map[i][j] = 0;
                }
            }
        }
        map[x][y] = -2;
        return map;
    }

    public boolean isWin() {
        int x = -1, y = -1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (maze[i][j].getValue() == properties.getGoal()) {
                    x = i;
                    y = j;
                }
            }
        }
        if (x == -1 || y == -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isLose() {
        int x = -1, y = -1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (maze[i][j].getValue() == properties.getPlayer()) {
                    x = i;
                    y = j;
                }
            }
        }
        if (x == -1 || y == -1 || !canPlayerMove(x, y)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean canEnemyMove(int x, int y) {
        if (x + 1 < size && maze[x + 1][y].getValue() == properties.getEmpty() ||
                x - 1 >= 0 && maze[x - 1][y].getValue() == properties.getEmpty() ||
                y + 1 < size && maze[x][y + 1].getValue() == properties.getEmpty() ||
                y - 1 >= 0 && maze[x][y - 1].getValue() == properties.getEmpty() ||
                x + 1 < size && maze[x + 1][y].getValue() == properties.getPlayer() ||
                x - 1 >= 0 && maze[x - 1][y].getValue() == properties.getPlayer() ||
                y + 1 < size && maze[x][y + 1].getValue() == properties.getPlayer() ||
                y - 1 >= 0 && maze[x][y - 1].getValue() == properties.getPlayer()) {
            return true;
        }
        return false;
    }

    private boolean canPlayerMove(int x, int y) {
        if (x + 1 < size && maze[x + 1][y].getValue() == properties.getEmpty() ||
                x - 1 >= 0 && maze[x - 1][y].getValue() == properties.getEmpty() ||
                y + 1 < size && maze[x][y + 1].getValue() == properties.getEmpty() ||
                y - 1 >= 0 && maze[x][y - 1].getValue() == properties.getEmpty() ||
                x + 1 < size && maze[x + 1][y].getValue() == properties.getGoal() ||
                x - 1 >= 0 && maze[x - 1][y].getValue() == properties.getGoal() ||
                y + 1 < size && maze[x][y + 1].getValue() == properties.getGoal() ||
                y - 1 >= 0 && maze[x][y - 1].getValue() == properties.getGoal()) {
            return true;
        }
        return false;
    }

    private boolean isTargetReachable() {
        boolean[][] visited = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                visited[i][j] = false;
            }
        }
        int x = -1, y = -1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (maze[i][j].getValue() == properties.getGoal()) {
                    x = i;
                    y = j;
                }
            }
        }
        if (x == -1 || y == -1) {
            return false;
        }
        return isReachable(visited, x, y);
    }

    private boolean isReachable(boolean[][] visited, int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size || visited[x][y]
                || maze[x][y].getValue() == properties.getWall()) {
            return false;
        }
        if (maze[x][y].getValue() == properties.getPlayer()) {
            return true;
        }
        visited[x][y] = true;
        return isReachable(visited, x + 1, y) || isReachable(visited, x - 1, y) || isReachable(visited, x, y + 1)
                || isReachable(visited, x, y - 1);
    }

    public void printMap() {
        ColoredPrinter cp = new ColoredPrinter.Builder(1, false).build();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cp.setBackgroundColor(Ansi.BColor.valueOf(properties.getColor(maze[i][j].getValue())));
                cp.print(maze[i][j].getValue());
            }
            System.out.println();
        }
    }

}
