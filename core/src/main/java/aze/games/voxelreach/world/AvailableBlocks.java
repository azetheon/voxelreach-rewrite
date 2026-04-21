package aze.games.voxelreach.world;

import com.badlogic.gdx.graphics.Texture;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AvailableBlocks {
    private static Map<String, Texture> textureCache = new HashMap<>();
    private static Map<String, Block> blocks = new HashMap<>();

    public static class Block {
        private String name;
        private Texture top, bottom, side;
        private String attribute;

        // i don't use attributes rn but it's for future block attrtibutes
        // like the opacity for water or interactability for workshop blocks

        public Block(String name, String top, String bottom, String side, String attribute) {
            this.name = name;
            this.top = getTexture(top);
            this.bottom = getTexture(bottom);
            this.side = getTexture(side);
            this.attribute = attribute;
        }

        // the fix for everything being stone bruh
        public Texture getTopTexture() {
            return top;
        }
        public Texture getBottomTexture() {
            return bottom;
        }
        public Texture getSideTexture() {
            return side;
        }
    }

    private static Texture getTexture(String path) {
        if (!textureCache.containsKey(path)) {
            textureCache.put(path, new Texture(path));
        }
        return textureCache.get(path);
    }

    public static void cleanupTextures() {
        for (Texture texture : textureCache.values()) {
            texture.dispose();
        }
        textureCache.clear();
    }

    public static void loadBlocks() {
        blocks.put("grass", new Block(
            "grass",
            "grass.png",
            "grass_bottom.png",
            "grass_side.png",
            "none"));
        blocks.put("stone", new Block(
            "stone",
            "stone.png",
            "stone.png",
            "stone.png",
            "none"));
        blocks.put("sand", new Block(
            "sand",
            "sand.png",
            "sand.png",
            "sand.png",
            "none"));
        blocks.put("dirt", new Block(
            "dirt",
            "dirt.png",
            "dirt.png",
            "dirt.png",
            "none"));
    }

    public static Block getBlock(String name) {
        return blocks.get(name);
    }

    public static Collection<Block> getAllBlocks() {
        return Collections.unmodifiableCollection(blocks.values());
    }

    // this will only be here until I make the block images be dynamic, for now since there's only 4 blocks I'll just do this
    public static String getBlockNameForSlot(int slotIndex) {
        switch (slotIndex) {
            case 4: return "grass";
            case 5: return "dirt";
            case 6: return "stone";
            case 7: return "sand";
            default: return null;
        }
    }
}
