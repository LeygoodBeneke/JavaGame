package edu.school21;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import java.io.File;

@Parameters(separators = "=")
public class Args {
    @Parameter(names = "--enemiesCount", description = "Number of enemies", required = true)
    private Integer enemiesCount;

    @Parameter(names = "--wallsCount", description = "Number of walls", required = true)
    private Integer wallsCount;

    @Parameter(names = "--size", description = "Size of the map", required = true)
    private Integer size;

    @Parameter(names = "--profile", description = "Profile of the game", required = true)
    private String profile;

    public Integer getEnemiesCount() {
        return enemiesCount;
    }

    public Integer getWallsCount() {
        return wallsCount;
    }

    public Integer getSize() {
        return size;
    }

    public String getProfile() {
        return profile;
    }

    public void checkArgs() throws IllegalParametersException {
        if (enemiesCount < 1) {
            throw new IllegalParametersException("It can't be less than 1 enemy");
        }
        if (wallsCount < 0) {
            throw new IllegalParametersException("It can't be less than 0 walls");
        }
        if (size < 5) {
            throw new IllegalParametersException("It can't be less than 5 size");
        }
        if (wallsCount + enemiesCount > size * size - 2) {
            throw new IllegalParametersException("Too many enemies and walls");
        }
        File f = new File("target/classes/application-" + profile + ".properties");
        if (!f.exists()) {
            throw new IllegalParametersException("Profile doesn't exist");
        }
    }

}
