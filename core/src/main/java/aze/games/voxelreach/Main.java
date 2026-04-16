package aze.games.voxelreach;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input;

import aze.games.voxelreach.player.Camera;
import aze.games.voxelreach.world.AvailableBlocks;
import aze.games.voxelreach.world.WorldEnvironment;
import aze.games.voxelreach.world.ChunkGeneration;
import aze.games.voxelreach.player.UserInterface;
import aze.games.voxelreach.player.Raycast;

public class Main extends ApplicationAdapter {
    SpriteBatch spriteBatch;
    BitmapFont font;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        font = new BitmapFont();
        Gdx.input.setCursorCatched(true);

        Camera.prepareCamera();
        Camera.prepareOverlayCamera();
        WorldEnvironment.prepareEnvironment();
        AvailableBlocks.loadBlocks();
        ChunkGeneration.makeChunk();
    }

    @Override
    public void render() {
        Camera.cameraMovement();
        Camera.playerMovement();
        Camera.getOverlay().update();
        UserInterface.update();

        Raycast.HitResult hit = Raycast.raycast(Camera.getCamera(),5f);

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.53f, 0.81f, 0.92f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
        Gdx.gl.glCullFace(GL20.GL_BACK);

        ChunkGeneration.renderChunks();
        UserInterface.drawDebug(spriteBatch, font);

        if (hit.hit) {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                ChunkGeneration.setBlock(hit.x, hit.y, hit.z, null);
                ChunkGeneration.rebuildChunk();
            }
            if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
                // this is an unholy amount of if statements but it's just temporary, will change later when I add more blocks
                if (UserInterface.selectedBlock == 0) {
                    ChunkGeneration.setBlock(hit.faceX, hit.faceY, hit.faceZ, AvailableBlocks.getBlock("grass"));
                }
                else if (UserInterface.selectedBlock == 1) {
                    ChunkGeneration.setBlock(hit.faceX, hit.faceY, hit.faceZ, AvailableBlocks.getBlock("dirt"));
                }
                else if (UserInterface.selectedBlock == 2) {
                    ChunkGeneration.setBlock(hit.faceX, hit.faceY, hit.faceZ, AvailableBlocks.getBlock("stone"));
                }
                else if (UserInterface.selectedBlock == 3) {
                    ChunkGeneration.setBlock(hit.faceX, hit.faceY, hit.faceZ, AvailableBlocks.getBlock("sand"));
                }
                ChunkGeneration.rebuildChunk();
            }
        }
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        font.dispose();
        ChunkGeneration.cleanup();
        AvailableBlocks.cleanupTextures();

        /*
         * release cursor just in case i need it later on
         * before i go insane debugging and realizing it's just the cursor
         */

        Gdx.input.setCursorCatched(false);
    }
}
