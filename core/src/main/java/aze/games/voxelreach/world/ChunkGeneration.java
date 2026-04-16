package aze.games.voxelreach.world;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import aze.games.voxelreach.player.Camera;
import aze.games.voxelreach.world.AvailableBlocks.Block;

public class ChunkGeneration {

    private static final int chunkSize = 16;

    private static int voxelCounter = 0;
    private static int currentChunkCounter = 0;

    private static ModelBatch modelBatch;
    private static List<ModelInstance> chunks = new ArrayList<>();
    private static List<Model> chunkModels = new ArrayList<>();

    private static Block[][][] blocks = new Block[chunkSize][chunkSize][chunkSize];

    public static void makeChunk() {
        Block grass = AvailableBlocks.getBlock("grass");
        Block dirt = AvailableBlocks.getBlock("dirt");
        Block stone = AvailableBlocks.getBlock("stone");

        modelBatch = new ModelBatch();

        for (int x = 0; x < chunkSize; x++) {
            for (int z = 0; z < chunkSize; z++) {
                for (int y = 0; y < chunkSize; y++) {

                    voxelCounter++;

                    if (y == 15) {
                        blocks[x][y][z] = grass;
                    } else if (y >= 11) {
                        blocks[x][y][z] = dirt;
                    } else {
                        blocks[x][y][z] = stone;
                    }
                }
            }
        }

        ModelBuilder builder = new ModelBuilder();
        builder.begin();

        float s = 1f;

        for (int x = 0; x < chunkSize; x++) {
            for (int y = 0; y < chunkSize; y++) {
                for (int z = 0; z < chunkSize; z++) {

                    Block block = blocks[x][y][z];
                    if (block == null) continue;

                    if (isAir(x, y, z + 1)) {
                        builder.part("front", GL20.GL_TRIANGLES,
                                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates,
                                new Material(TextureAttribute.createDiffuse(block.getSideTexture())))
                            .rect(
                                x - s/2, y - s/2, z + s/2,
                                x + s/2, y - s/2, z + s/2,
                                x + s/2, y + s/2, z + s/2,
                                x - s/2, y + s/2, z + s/2,
                                0, 0, 1);
                    }

                    if (isAir(x, y, z - 1)) {
                        builder.part("back", GL20.GL_TRIANGLES,
                                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates,
                                new Material(TextureAttribute.createDiffuse(block.getSideTexture())))
                            .rect(
                                x + s/2, y - s/2, z - s/2,
                                x - s/2, y - s/2, z - s/2,
                                x - s/2, y + s/2, z - s/2,
                                x + s/2, y + s/2, z - s/2,
                                0, 0, -1);
                    }

                    if (isAir(x - 1, y, z)) {
                        builder.part("left", GL20.GL_TRIANGLES,
                                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates,
                                new Material(TextureAttribute.createDiffuse(block.getSideTexture())))
                            .rect(
                                x - s/2, y - s/2, z - s/2,
                                x - s/2, y - s/2, z + s/2,
                                x - s/2, y + s/2, z + s/2,
                                x - s/2, y + s/2, z - s/2,
                                -1, 0, 0);
                    }

                    if (isAir(x + 1, y, z)) {
                        builder.part("right", GL20.GL_TRIANGLES,
                                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates,
                                new Material(TextureAttribute.createDiffuse(block.getSideTexture())))
                            .rect(
                                x + s/2, y - s/2, z + s/2,
                                x + s/2, y - s/2, z - s/2,
                                x + s/2, y + s/2, z - s/2,
                                x + s/2, y + s/2, z + s/2,
                                1, 0, 0);
                    }

                    if (isAir(x, y + 1, z)) {
                        builder.part("top", GL20.GL_TRIANGLES,
                                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates,
                                new Material(TextureAttribute.createDiffuse(block.getTopTexture())))
                            .rect(
                                x - s/2, y + s/2, z + s/2,
                                x + s/2, y + s/2, z + s/2,
                                x + s/2, y + s/2, z - s/2,
                                x - s/2, y + s/2, z - s/2,
                                0, 1, 0);
                    }

                    if (isAir(x, y - 1, z)) {
                        builder.part("bottom", GL20.GL_TRIANGLES,
                                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates,
                                new Material(TextureAttribute.createDiffuse(block.getBottomTexture())))
                            .rect(
                                x - s/2, y - s/2, z - s/2,
                                x + s/2, y - s/2, z - s/2,
                                x + s/2, y - s/2, z + s/2,
                                x - s/2, y - s/2, z + s/2,
                                0, -1, 0);
                    }
                }
            }
        }

        Model chunkModel = builder.end();
        chunkModels.add(chunkModel);

        ModelInstance instance = new ModelInstance(chunkModel);
        chunks.add(instance);

        currentChunkCounter++;
    }

    private static boolean isAir(int x, int y, int z) {
        if (x < 0 || y < 0 || z < 0 || x >= chunkSize || y >= chunkSize || z >= chunkSize) {
            return true;
        }
        return blocks[x][y][z] == null;
    }

    public static void renderChunks() {
        modelBatch.begin(Camera.getCamera());
        for (ModelInstance instance : chunks) {
            modelBatch.render(instance, WorldEnvironment.getEnvironment());
        }
        modelBatch.end();
    }

    public static void cleanup() {
        modelBatch.dispose();
        for (Model model : chunkModels) {
            model.dispose();
        }
    }

    public static int getVoxelCount() {
        return voxelCounter;
    }

    public static int getChunkCount() {
        return currentChunkCounter;
    }
}
