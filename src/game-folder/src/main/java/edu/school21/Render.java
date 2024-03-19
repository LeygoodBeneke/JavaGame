package edu.school21;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.input.KeyStroke;

public class Render {

    private int gameOver = 0;

    private void isGameOver(Map map) {
        if (map.isWin()) {
            gameOver = 1;
        } else if (map.isLose()) {
            gameOver = -1;
        }
    }

    public void printMap(Cell[][] maze) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze.length; j++) {
                System.out.print(maze[i][j].getValue());
            }
            System.out.println();
        }
    }

    // public void printGameOver(int gameOver) {

    // try {
    // terminal.clearScreen();
    // final TextGraphics textGraphics = terminal.newTextGraphics();
    // if (gameOver > 0) {
    // textGraphics.setBackgroundColor(TextColor.ANSI.GREEN);
    // textGraphics.setForegroundColor(TextColor.ANSI.BLACK);
    // textGraphics.putString((terminal.getTerminalSize().getColumns() - 1) / 2,
    // (terminal.getTerminalSize().getRows() - 1) / 2, "You win!", SGR.BOLD);
    // Thread.sleep(2000);
    // } else {
    // textGraphics.setBackgroundColor(TextColor.ANSI.RED);
    // textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
    // textGraphics.putString((terminal.getTerminalSize().getColumns() - 1) / 2,
    // (terminal.getTerminalSize().getRows() - 1) / 2, "You lose!", SGR.BOLD);
    // Thread.sleep(2000);
    // }
    // } catch (Exception e) {
    // System.out.println(e.getMessage());
    // } finally {
    // try {
    // if (terminal != null) {
    // terminal.close();
    // }
    // } catch (Exception e) {
    // System.out.println(e.getMessage());
    // }

    // }
    // }

    public void productionMode(Map map) {
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        Terminal terminal = null;
        System.out.println("Production mode");
        try {
            terminal = defaultTerminalFactory.createTerminal();
            terminal.enterPrivateMode();
            terminal.clearScreen();
            terminal.setCursorVisible(false);

            map.printMap();
            KeyStroke keyStroke;
            while (gameOver == 0) {
                keyStroke = terminal.readInput();
                if (keyStroke.getCharacter() == '9')
                    gameOver = -1;
                if (map.movePlayer(keyStroke.getCharacter())) {
                    isGameOver(map);
                    terminal.clearScreen();
                    map.moveEnemies();
                    isGameOver(map);
                    map.printMap();
                    terminal.flush();
                }

            }
            terminal.clearScreen();
            final TextGraphics textGraphics = terminal.newTextGraphics();
            if (gameOver > 0) {
                textGraphics.setBackgroundColor(TextColor.ANSI.GREEN);
                textGraphics.setForegroundColor(TextColor.ANSI.BLACK);
                textGraphics.putString((terminal.getTerminalSize().getColumns() - 1) / 2,
                        (terminal.getTerminalSize().getRows() - 1) / 2, "You win!", SGR.BOLD);
                Thread.sleep(2000);
            } else {
                textGraphics.setBackgroundColor(TextColor.ANSI.RED);
                textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
                textGraphics.putString((terminal.getTerminalSize().getColumns() - 1) / 2,
                        (terminal.getTerminalSize().getRows() - 1) / 2, "You lose!", SGR.BOLD);
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (terminal != null) {
                    terminal.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
    }

}
