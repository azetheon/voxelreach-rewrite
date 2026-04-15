package aze.games.voxelreach.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.VertexAttributes;

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
        private String opacity;
        private Model model;
        private ModelInstance instance;

        public Block(String name, String top, String bottom, String side, String opacity) {
            this.name = name;
            this.top = getTexture(top);
            this.bottom = getTexture(bottom);
            this.side = getTexture(side);
            this.opacity = opacity;
            createModel();
        }

        private void createModel() {
            ModelBuilder modelBuilder = new ModelBuilder();
            modelBuilder.begin();
            float s = 1f;

            modelBuilder.part("front", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, new Material(TextureAttribute.createDiffuse(side)))
                .rect(
                    -s/2, -s/2, s/2,
                    s/2, -s/2, s/2,
                    s/2, s/2, s/2,
                    -s/2, s/2, s/2,
                    0, 0, 1);
            modelBuilder.part("back", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, new Material(TextureAttribute.createDiffuse(side)))
                .rect(
                    s/2, -s/2, -s/2,
                    -s/2, -s/2, -s/2,
                    -s/2, s/2, -s/2,
                    s/2, s/2, -s/2,
                    0, 0, -1);
            modelBuilder.part("left", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, new Material(TextureAttribute.createDiffuse(side)))
                .rect(
                    -s/2, -s/2, -s/2,
                    -s/2, -s/2, s/2,
                    -s/2, s/2, s/2,
                    -s/2, s/2, -s/2,
                    -1, 0, 0);
            modelBuilder.part("right", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, new Material(TextureAttribute.createDiffuse(side)))
                .rect(
                    s/2, -s/2, s/2,
                    s/2, -s/2, -s/2,
                    s/2, s/2, -s/2,
                    s/2, s/2, s/2,
                    1, 0, 0);
            modelBuilder.part("top", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, new Material(TextureAttribute.createDiffuse(top)))
                .rect(
                    -s/2, s/2, s/2,
                    s/2, s/2, s/2,
                    s/2, s/2, -s/2,
                    -s/2, s/2, -s/2,
                    0, 1, 0);
            modelBuilder.part("bottom", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, new Material(TextureAttribute.createDiffuse(bottom)))
                .rect(
                    -s/2, -s/2, -s/2,
                    s/2, -s/2, -s/2,
                    s/2, -s/2, s/2,
                    -s/2, -s/2, s/2,
                    0, -1, 0);

            model = modelBuilder.end();
            instance = new ModelInstance(model);
        }

        public Model getModel() {
            return model;
        }

        public ModelInstance getInstance() {
            return instance;
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
            "solid"));
        blocks.put("stone", new Block(
            "stone",
            "stone.png",
            "stone.png",
            "stone.png",
            "solid"));
        blocks.put("sand", new Block(
            "sand",
            "sand.png",
            "sand.png",
            "sand.png",
            "solid"));
        blocks.put("dirt", new Block(
            "dirt",
            "dirt.png",
            "dirt.png",
            "dirt.png",
            "solid"));
    }

    public static Block getBlock(String name) {
        return blocks.get(name);
    }

    public static Collection<Block> getAllBlocks() {
        return Collections.unmodifiableCollection(blocks.values());
    }
}
