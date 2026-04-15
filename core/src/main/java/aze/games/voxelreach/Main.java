package aze.games.voxelreach;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import aze.games.voxelreach.player.Camera;
import aze.games.voxelreach.world.AvailableBlocks;
import aze.games.voxelreach.world.WorldEnvironment;
import aze.games.voxelreach.world.ChunkGeneration;
import aze.games.voxelreach.player.UserInterface;

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

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.53f, 0.81f, 0.92f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        ChunkGeneration.renderChunks();
        UserInterface.drawDebug(spriteBatch, font);
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        font.dispose();
        ChunkGeneration.cleanup();
        for (AvailableBlocks.Block block : AvailableBlocks.getAllBlocks()) {
            block.getModel().dispose();
        }
        AvailableBlocks.cleanupTextures();

        /*
         * release cursor just in case i need it later on
         * before i go insane debugging and realizing it's just the cursor
         */

        Gdx.input.setCursorCatched(false);
    }
}
