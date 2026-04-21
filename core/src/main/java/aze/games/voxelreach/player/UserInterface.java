package aze.games.voxelreach.player;

import aze.games.voxelreach.world.AvailableBlocks;
import aze.games.voxelreach.world.ChunkGeneration;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class UserInterface {
    private static boolean showDebug = true;
    private static float screenCenterX = Gdx.graphics.getWidth() / 2f;
    private static float screenCenterY = Gdx.graphics.getHeight() / 2f;

    public static int selectedBlock = 0;

    public static void update() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            showDebug = !showDebug;
        }

        int[] slots = {0, 1, 4, 5, 6, 7};

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1))
            selectedBlock = slots[0];
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2))
            selectedBlock = slots[1];
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3))
            selectedBlock = slots[2];
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4))
            selectedBlock = slots[3];
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5))
            selectedBlock = slots[4];
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6))
            selectedBlock = slots[5];
    }

    public static String getSelectedBlock() {
        if (selectedBlock == 4) {
            return "Grass";
        }
        if (selectedBlock == 5) {
            return "Dirt";
        }
        if (selectedBlock == 6) {
            return "Stone";
        }
        if (selectedBlock == 7) {
            return "Sand";
        }
        return "Tool slot";
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

    public static void drawHotbar(ShapeRenderer shapeRenderer, BitmapFont font, SpriteBatch spriteBatch) {
        shapeRenderer.setProjectionMatrix(Camera.getOverlay().combined);

        float slotSize = 42f;
        float slotMargin = 8f;
        float padding = 10f;
        int totalSlots = 8;

        float totalWidth = (totalSlots * slotSize) + ((totalSlots - 1) * slotMargin) + (padding * 2);
        float startX = (Gdx.graphics.getWidth() - totalWidth) / 2f;
        float hotbarY = 20f;
        float hotbarHeight = slotSize + (padding * 2);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(26/255f, 16/255f, 8/255f, 0.92f);
        shapeRenderer.rect(startX, hotbarY, totalWidth, hotbarHeight);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Gdx.gl.glLineWidth(2f);
        shapeRenderer.setColor(92/255f, 58/255f, 30/255f, 1f);
        shapeRenderer.rect(startX, hotbarY, totalWidth, hotbarHeight);
        shapeRenderer.end();

        float slotsStartX = startX + padding;

        // skip slot 2 and 3, I want to make it so that the hobar goes
        // Tool Tool                 Block Block Block Block
        // i think it'd be a cool gameplay feature
        for (int i = 0; i < totalSlots; i++) {
            if (i == 2 || i == 3) continue;

            float slotX = slotsStartX + i * (slotSize + slotMargin);
            float slotY = hotbarY + padding;
            boolean isSelected = (i == selectedBlock);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(isSelected ? new com.badlogic.gdx.graphics.Color(61/255f, 40/255f, 20/255f, 1f) : new com.badlogic.gdx.graphics.Color(42/255f, 26/255f, 12/255f, 1f));
            shapeRenderer.rect(slotX, slotY, slotSize, slotSize);
            shapeRenderer.end();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            Gdx.gl.glLineWidth(isSelected ? 2.5f : 1.5f);
            shapeRenderer.setColor(isSelected ? new com.badlogic.gdx.graphics.Color(200/255f, 147/255f, 74/255f, 1f) : new com.badlogic.gdx.graphics.Color(122/255f, 92/255f, 63/255f, 0.85f));
            shapeRenderer.rect(slotX, slotY, slotSize, slotSize);
            shapeRenderer.end();

            if (isSelected) {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(200/255f, 147/255f, 74/255f, 1f);
                float dotSize = 5f;
                shapeRenderer.rect(slotX + (slotSize / 2f) - (dotSize / 2f), hotbarY + 3f, dotSize, dotSize);
                shapeRenderer.end();
            }
        }

        spriteBatch.setProjectionMatrix(Camera.getOverlay().combined);
        spriteBatch.begin();

        float iconPadding = 5f;
        float iconSize = slotSize - (iconPadding * 2);

        // draw icon for the slots cuz looks better than text :)
        for (int i = 0; i < totalSlots; i++) {
            if (i == 2 || i == 3) continue;

            float slotX = slotsStartX + i * (slotSize + slotMargin);
            float slotY = hotbarY + padding;

            String blockName = AvailableBlocks.getBlockNameForSlot(i);
            if (blockName == null) continue;

            AvailableBlocks.Block block = AvailableBlocks.getBlock(blockName);
            if (block == null) continue;

            spriteBatch.draw(block.getSideTexture(),
                slotX + iconPadding,
                slotY + iconPadding,
                iconSize,
                iconSize);
        }

        spriteBatch.end();

        Gdx.gl.glLineWidth(1f);
    }
}
