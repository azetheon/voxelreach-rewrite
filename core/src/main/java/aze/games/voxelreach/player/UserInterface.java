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
    private static boolean showConsole = false;
    private static float screenCenterX = Gdx.graphics.getWidth() / 2f;
    private static float screenCenterY = Gdx.graphics.getHeight() / 2f;

    public static int selectedSlot = 0;

    private static String[] hotbarBlockNames = new String[4];
    private static int[] hotbarBlockCounts = new int[4];

    static StringBuilder consoleInput = new StringBuilder();

    public static String getSelectedBlockName() {
        int blockIndex = slotToBlockIndex(selectedSlot);
        if (blockIndex < 0 || blockIndex >= 4) return null;
        return hotbarBlockNames[blockIndex];
    }

    public static void consumeSelectedBlock() {
        int blockIndex = slotToBlockIndex(selectedSlot);
        if (blockIndex < 0 || blockIndex >= 4) return;
        if (hotbarBlockCounts[blockIndex] <= 0) return;
        hotbarBlockCounts[blockIndex]--;
        if (hotbarBlockCounts[blockIndex] == 0) {
            hotbarBlockNames[blockIndex] = null;
        }
    }

    public static boolean getChatStatus() {
        return showConsole;
    }

    public static boolean giveBlock(String blockName) {
        if (AvailableBlocks.getBlock(blockName) == null)
            return false;
        for (int i = 0; i < 4; i++) {
            if (blockName.equals(hotbarBlockNames[i]) && hotbarBlockCounts[i] < 99) {
                hotbarBlockCounts[i]++;
                return true;
            }
        }

        for (int i = 0; i < 4; i++) {
            if (hotbarBlockNames[i] == null) {
                hotbarBlockNames[i] = blockName;
                hotbarBlockCounts[i] = 1;
                return true;
            }
        }

        return false; // all slots full, don't do anything
    }

    // Slots 0 and 1 are tool slots, skip them
    // 2-3 are a visual gap, skip them
    private static int slotToBlockIndex(int slot) {
        return slot - 4;
    }

    public static void update() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            showDebug = !showDebug;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.T) && !showConsole) {
            showConsole = true;
            consoleInput.setLength(0);
            Gdx.input.setCursorCatched(false);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && showConsole) {
            showConsole = false;
            Gdx.input.setCursorCatched(true);
        }

        int[] slotMapping = {0, 1, 4, 5, 6, 7}; // 2 and 3 are empty space so skip those
        int[] numKeys = {
            Input.Keys.NUM_1, Input.Keys.NUM_2, Input.Keys.NUM_3,
            Input.Keys.NUM_4, Input.Keys.NUM_5, Input.Keys.NUM_6
        };   // i can add 7 8 9 later but currently I don't need them
        for (int i = 0; i < numKeys.length; i++) {
            if (Gdx.input.isKeyJustPressed(numKeys[i])) {
                selectedSlot = slotMapping[i];
            }
        }
    }

    public static void executeConsoleCommand() {
        String input = consoleInput.toString().trim();
        showConsole = false;
        consoleInput.setLength(0);
        Gdx.input.setCursorCatched(true);

        if (input.startsWith(".give ")) {
            String[] parts = input.split(" ");

            if (parts.length < 2) {
                System.out.println("[Console] Usage: .give <block> [amount]");
                return;
            }

            String blockName = parts[1].toLowerCase();
            int amount = 1;

            if (parts.length >= 3) {
                try {
                    amount = Integer.parseInt(parts[2]);
                } catch (NumberFormatException e) {
                    System.out.println("[Console] Invalid amount!");
                    return;
                }
            }

            boolean success = false;

            for (int i = 0; i < amount; i++) {
                if (giveBlock(blockName)) {
                    success = true;
                } else {
                    break; // stop if inventory is full
                }
            }

            if (!success) {
                System.out.println("[Console] Failed to give block!");
            } else {
                System.out.println("[Console] Added " + amount + " " + blockName + " to hotbar.");
            }
        }
        // more commands can go here later
    }

    public static String getSelectedBlockLabel() {
        String name = getSelectedBlockName();
        if (name == null) return "Tool slot";
        int blockIndex = slotToBlockIndex(selectedSlot);
        int count = (blockIndex >= 0 && blockIndex < 4) ? hotbarBlockCounts[blockIndex] : 0;
        return Character.toUpperCase(name.charAt(0)) + name.substring(1) + " x" + count;
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
        font.draw(spriteBatch, "Selected Block: " + getSelectedBlockLabel(), 10, y); y -= lineHeight;
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
        int totalSlots = Math.max(2, 8);

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

        for (int i = 0; i < totalSlots; i++) {
            if (i == 2 || i == 3) continue;
            float slotX = slotsStartX + i * (slotSize + slotMargin);
            float slotY = hotbarY + padding;
            boolean isSelected = (i == selectedSlot);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(isSelected
                ? new com.badlogic.gdx.graphics.Color(61/255f, 40/255f, 20/255f, 1f)
                : new com.badlogic.gdx.graphics.Color(42/255f, 26/255f, 12/255f, 1f));
            shapeRenderer.rect(slotX, slotY, slotSize, slotSize);
            shapeRenderer.end();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            Gdx.gl.glLineWidth(isSelected ? 2.5f : 1.5f);
            shapeRenderer.setColor(isSelected
                ? new com.badlogic.gdx.graphics.Color(200/255f, 147/255f, 74/255f, 1f)
                : new com.badlogic.gdx.graphics.Color(122/255f, 92/255f, 63/255f, 0.85f));
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

        // here we draw block icons cuz it looks fancy yeaaaaaaa
        spriteBatch.setProjectionMatrix(Camera.getOverlay().combined);
        spriteBatch.begin();

        float iconPadding = 5f;
        float iconSize = slotSize - (iconPadding * 2);

        for (int i = 4; i < totalSlots; i++) {
            int blockIndex = slotToBlockIndex(i);
            if (blockIndex < 0 || blockIndex >= 4) continue;
            if (hotbarBlockNames[blockIndex] == null) continue;

            AvailableBlocks.Block block = AvailableBlocks.getBlock(hotbarBlockNames[blockIndex]);
            if (block == null) continue;

            float slotX = slotsStartX + i * (slotSize + slotMargin);
            float slotY = hotbarY + padding;

            spriteBatch.draw(block.getSideTexture(),
                slotX + iconPadding,
                slotY + iconPadding,
                iconSize,
                iconSize);

            if (hotbarBlockCounts[blockIndex] > 1) {
                font.draw(spriteBatch, String.valueOf(hotbarBlockCounts[blockIndex]),
                    slotX + slotSize - iconPadding - 8,
                    slotY + iconPadding + 10);
            }
        }

        spriteBatch.end();

        Gdx.gl.glLineWidth(1f);
    }

    public static void drawConsole(ShapeRenderer shapeRenderer, BitmapFont font, SpriteBatch spriteBatch) {
        if (!showConsole) return;
        shapeRenderer.setProjectionMatrix(Camera.getOverlay().combined);

        float consoleX = 10.f;
        float consoleY = 20.f;
        float consoleWidth = 350.f;
        float consoleHeight = 20.f;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(26/255f, 16/255f, 8/255f, 0.92f);
        shapeRenderer.rect(consoleX, consoleY, consoleWidth, consoleHeight);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Gdx.gl.glLineWidth(2f);
        shapeRenderer.setColor(92/255f, 58/255f, 30/255f, 1f);
        shapeRenderer.rect(consoleX, consoleY, consoleWidth, consoleHeight);
        shapeRenderer.end();

        spriteBatch.setProjectionMatrix(Camera.getOverlay().combined);
        spriteBatch.begin();
        font.draw(spriteBatch, consoleInput.toString(), consoleX + 5, consoleY + 15);
        spriteBatch.end();
    }
}
