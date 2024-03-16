package edu.school21;

public class Render {
    public void printMap(Cell[][] maze) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze.length; j++) {
                System.out.print(maze[i][j].getValue());
            }
            System.out.println();
        }
    }

}
