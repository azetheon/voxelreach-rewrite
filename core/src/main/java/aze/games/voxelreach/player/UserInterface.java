package aze.games.voxelreach.player;

import aze.games.voxelreach.world.ChunkGeneration;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input;

public class UserInterface {
    private static boolean showDebug = true;
    private static float screenCenterX = Gdx.graphics.getWidth() / 2f;
    private static float screenCenterY = Gdx.graphics.getHeight() / 2f;

    public static int selectedBlock = 0;

    public static void update() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            showDebug = !showDebug;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            selectedBlock = 0;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            selectedBlock = 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            selectedBlock = 2;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            selectedBlock = 3;
        }
    }

    public static String getSelectedBlock() {
        if (selectedBlock == 0) {
            return "Grass";
        }
        if (selectedBlock == 1) {
            return "Dirt";
        }
        if (selectedBlock == 2) {
            return "Stone";
        }
        if (selectedBlock == 3) {
            return "Sand";
        }
        return "How did you even get this??";
    }

    public static void drawDebug(SpriteBatch spriteBatch, BitmapFont font) {
        if (!showDebug) return;

        spriteBatch.setProjectionMatrix(Camera.getOverlay().combined);
        spriteBatch.begin();

        // after extensive testing by which I mean changing the number up by 5, I have decided that 15 is a good amount of spacing between the lines
        float y = Camera.getOverlay().viewportHeight - 10;
        float lineHeight = 15;

        long usedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
        long maxMemory = Runtime.getRuntime().maxMemory() / (1024 * 1024);

        font.draw(spriteBatch, "VoxelReach Pre-Alpha v0.1.0", 10, y); y -= lineHeight;
        font.draw(spriteBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, y); y -= lineHeight;
        font.draw(spriteBatch, "Selected Block: " + getSelectedBlock(), 10, y); y -= lineHeight;
        font.draw(spriteBatch, "Voxel Count: " + ChunkGeneration.getVoxelCount(), 10, y); y -= lineHeight;
        font.draw(spriteBatch, "Chunk Count: " + ChunkGeneration.getChunkCount(), 10, y); y -= lineHeight;
        font.draw(spriteBatch, "Chunk Rebuilds: " + ChunkGeneration.getChunkRebuildsCounter(), 10, y); y -= lineHeight;

        font.draw(spriteBatch,
            "Coordinates: " +
                (int)Camera.getCamera().position.x + ", " +
                (int)Camera.getCamera().position.y + ", " +
                (int)Camera.getCamera().position.z,
            10, y); y -= lineHeight;

        font.draw(spriteBatch,
            "Camera Direction: " +
                String.format("%.2f, %.2f, %.2f",
                    Camera.getCamera().direction.x,
                    Camera.getCamera().direction.y,
                    Camera.getCamera().direction.z),
            10, y); y -= lineHeight;

        font.draw(spriteBatch,"Memory: " + usedMemory + "MB / " + maxMemory + "MB",10, y); y -= lineHeight;
        font.draw(spriteBatch, "+", screenCenterX - 3, screenCenterY + 5);

        spriteBatch.end();
    }
}
