package edu.school21;

import java.io.IOException;
import java.util.List;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;
import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.input.KeyStroke;

public class Render {
    private int gameOver = 0;
    private Properties properties;

    public Render(String profile) {
        properties = new Properties("target/classes/application-" + profile + ".properties");
    }

    public void run(Map map, String mode) {
        if (mode.equals("development")) {
            developmentMode(map);
        } else {
            productionMode(map);
        }
    }

    private void isGameOver(Map map) {
        if (map.isWin()) {
            gameOver = 1;
        } else if (map.isLose()) {
            gameOver = -1;
        }
    }

    private void printMap(Map map, Properties properties) {
        ColoredPrinter cp = new ColoredPrinter.Builder(1, false).build();
        Cell[][] maze = map.getMaze();
        int size = map.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cp.setBackgroundColor(Ansi.BColor.valueOf(properties.getColor(maze[i][j].getValue())));
                cp.print(maze[i][j].getValue());
            }
            System.out.println();
        }
    }

    private void gameIsOver(Terminal terminal) throws IOException, InterruptedException {
        final TextGraphics textGraphics = terminal.newTextGraphics();
        terminal.clearScreen();
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
        textGraphics.setBackgroundColor(TextColor.ANSI.DEFAULT);
        textGraphics.setForegroundColor(TextColor.ANSI.DEFAULT);
    }

    private void developmentMode(Map map) {
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        Terminal terminal = null;
        try {
            terminal = defaultTerminalFactory.createTerminal();
            terminal.setCursorVisible(false);
            printMap(map, properties);
            KeyStroke keyStroke;
            while (gameOver == 0) {
                keyStroke = terminal.readInput();
                if (keyStroke.getCharacter() == '9')
                    gameOver = -1;
                if (map.movePlayer(keyStroke.getCharacter())) {
                    isGameOver(map);
                    printMap(map, properties);
                    System.out.println("Now it's time for the enemies to move.");
                    System.out.println();
                    List<int[]> enemies = map.getEnemies();
                    for (int[] enemy : enemies) {
                        map.moveSingleEnemy(enemy[0], enemy[1]);
                        isGameOver(map);
                        printMap(map, properties);
                        System.out.println("To confirm the movement of the enemy, press 8");
                        keyStroke = terminal.readInput();
                        while (true) {
                            if (keyStroke.getCharacter() == '8') {
                                System.out.println("Enemy movement confirmed.");
                                break;
                            }
                            keyStroke = terminal.readInput();
                        }
                    }
                    System.out.println("Now it's time for the player to move.");
                }
            }
            gameIsOver(terminal);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                terminal.setCursorVisible(true);
                if (terminal != null) {
                    terminal.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void productionMode(Map map) {
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        Terminal terminal = null;
        try {
            terminal = defaultTerminalFactory.createTerminal();
            terminal.enterPrivateMode();
            terminal.clearScreen();
            terminal.setCursorVisible(false);
            printMap(map, properties);
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
                    printMap(map, properties);
                    terminal.flush();
                }
            }
            terminal.clearScreen();
            gameIsOver(terminal);
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
