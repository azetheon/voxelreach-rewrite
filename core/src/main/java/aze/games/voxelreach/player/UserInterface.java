package aze.games.voxelreach.player;

import aze.games.voxelreach.world.ChunkGeneration;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class UserInterface {
    public static void drawDebug(SpriteBatch spriteBatch, BitmapFont font) {
        spriteBatch.setProjectionMatrix(Camera.getOverlay().combined);
        spriteBatch.begin();

        font.draw(spriteBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, Camera.getOverlay().viewportHeight - 10);
        font.draw(spriteBatch, "Voxel Count: " + ChunkGeneration.getVoxelCount(), 10, Camera.getOverlay().viewportHeight - 25);
        font.draw(spriteBatch, "Chunk Count: " + ChunkGeneration.getChunkCount(), 10, Camera.getOverlay().viewportHeight - 40);

        spriteBatch.end();
    }
}
