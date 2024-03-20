package edu.school21;

import com.beust.jcommander.JCommander;

public class Game {
    private static Game instance;

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
        Render render = new Render(arguments.getProfile());

        render.run(map, arguments.getProfile());

    }
}
