package edu.school21;

import java.io.FileInputStream;

public class Properties {
    private String path;
    private char enemy;
    private char wall;
    private char player;
    private char empty = 32;
    private char goal;
    private String enemyColor;
    private String wallColor;
    private String playerColor;
    private String emptyColor;
    private String goalColor;

    public Properties(String path) {
        this.path = path;
        loadProfile();
    }

    private void loadProfile() {
        try (FileInputStream file = new FileInputStream(path)) {
            java.util.Properties properties = new java.util.Properties();
            properties.load(file);
            enemy = properties.getProperty("enemy.char").charAt(0);
            wall = properties.getProperty("wall.char").charAt(0);
            player = properties.getProperty("player.char").charAt(0);
            goal = properties.getProperty("goal.char").charAt(0);
            enemyColor = properties.getProperty("enemy.color");
            wallColor = properties.getProperty("wall.color");
            playerColor = properties.getProperty("player.color");
            emptyColor = properties.getProperty("empty.color");
            goalColor = properties.getProperty("goal.color");
            if (properties.getProperty("empty.char").isEmpty() == false) {
                empty = properties.getProperty("empty.char").charAt(0);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public char getEnemy() {
        return enemy;
    }

    public char getWall() {
        return wall;
    }

    public char getPlayer() {
        return player;
    }

    public char getEmpty() {
        return empty;
    }

    public char getGoal() {
        return goal;
    }

    public String getEnemyColor() {
        return enemyColor;
    }

    public String getWallColor() {
        return wallColor;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public String getEmptyColor() {
        return emptyColor;
    }

    public String getGoalColor() {
        return goalColor;
    }

    public String getColor(char c) {
        if (c == enemy) {
            return enemyColor;
        } else if (c == wall) {
            return wallColor;
        } else if (c == player) {
            return playerColor;
        } else if (c == empty) {
            return emptyColor;
        } else if (c == goal) {
            return goalColor;
        } else {
            return "NONE";
        }
    }

}
