package aze.games.voxelreach.world;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import aze.games.voxelreach.player.Camera;
import aze.games.voxelreach.world.AvailableBlocks.Block;

public class ChunkGeneration {
    /*
     * experimenting with different chunk sizes so I can check out
     * which one would be the most optimal for performance
     * */
    private static final int chunkSize = 16;

    private static int voxelCounter = 0;
    private static int currentChunkCounter = 0;

    private static ModelBatch modelBatch;
    private static List<ModelInstance> chunks = new ArrayList<>();
    private static Block[][][] blocks = new Block[chunkSize][chunkSize][chunkSize];

    public static void makeChunk() {
        Block grassBlock = AvailableBlocks.getBlock("grass");
        Block stoneBlock = AvailableBlocks.getBlock("stone");
        Block dirtBlock = AvailableBlocks.getBlock("dirt");

        modelBatch = new ModelBatch();

        for (int x = 0; x < chunkSize; x++) {
            for (int z = 0; z < chunkSize; z++) {
                for (int y = 0; y < chunkSize; y++) {
                    voxelCounter = voxelCounter + 1;

                    Block currentBlock;

                    if (y == 15) {
                        currentBlock = grassBlock;
                    }
                    else if (y >= 11) {
                        currentBlock = dirtBlock;
                    }
                    else {
                        currentBlock = stoneBlock;
                    }

                    blocks[x][y][z] = currentBlock;

                    ModelInstance instance = new ModelInstance(currentBlock.getModel());
                    instance.transform.setToTranslation(new Vector3(x,y,z));
                    chunks.add(instance);
                }
            }
        }

        currentChunkCounter = currentChunkCounter + 1;
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
    }

    public static int getVoxelCount() {
        return voxelCounter;
    }

    public static int getChunkCount() {
        return currentChunkCounter;
    }
}

