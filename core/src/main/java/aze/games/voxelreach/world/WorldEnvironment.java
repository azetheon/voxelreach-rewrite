package aze.games.voxelreach.world;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;

public class WorldEnvironment {
    private static Environment environment;

    public static void prepareEnvironment() {
        environment = new Environment();
        environment.add(new DirectionalLight().set(1f, 1f, 1f, -1f, -1f, -1f));
        environment.add(new DirectionalLight().set(0.3f, 0.3f, 0.35f, 1f, -0.5f, 1f));
    }

    public static Environment getEnvironment() {
        return environment;
    }
}

