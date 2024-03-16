package edu.school21;

import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.SGR;
import com.beust.jcommander.JCommander;

public class Game {
    private static Game instance;
    private int gameOver = 0;

    private void isGameOver(Map map) {
        if (map.isWin()) {
            gameOver = 1;
        } else if (map.isLose()) {
            gameOver = -1;
        }
    }

    private Game() {
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public void run(String[] args) {
        Args arguments = new Args();
        try {
            JCommander.newBuilder()
                    .addObject(arguments)
                    .build()
                    .parse(args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        arguments.checkArgs();

        Map map = new Map(arguments.getSize(), arguments.getWallsCount(),
                arguments.getEnemiesCount(),
                arguments.getProfile());
        map.generateStartMap();

        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        Terminal terminal = null;
        try {
            terminal = defaultTerminalFactory.createTerminal();
            terminal.enterPrivateMode();
            terminal.clearScreen();
            terminal.setCursorVisible(false);

            map.printMap();
            isGameOver(map);
            KeyStroke keyStroke;
            while (gameOver == 0) {
                keyStroke = terminal.readInput();
                if (keyStroke.getCharacter() == '9')
                    gameOver = -1;
                if (map.movePlayer(keyStroke.getCharacter())) {
                    isGameOver(map);
                    // map.moveEnemies();
                    terminal.clearScreen();
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
