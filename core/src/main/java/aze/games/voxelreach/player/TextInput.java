package aze.games.voxelreach.player;

import com.badlogic.gdx.InputProcessor;

public class TextInput implements InputProcessor {

    @Override
    public boolean keyTyped(char character) {
        if (!UserInterface.getChatStatus()) return false;

        if (character == '\b' && UserInterface.consoleInput.length() > 0) {
            UserInterface.consoleInput.deleteCharAt(UserInterface.consoleInput.length() - 1);
            return true;
        }

        if (character == '\n') { // when enter is pressed remove the characters i'll implement the command execution here
            UserInterface.consoleInput.setLength(0);
            return true;
        }

        if (character >= 32) { // settings this to only 32 and above prevents some weird characters from appearing
            UserInterface.consoleInput.append(character);
        }

        return true;
    }

    // taken from https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/InputProcessor.java
    // just returns false cuz i don't use it
    @Override public boolean keyDown(int keycode) { return false; }
    @Override public boolean keyUp(int keycode) { return false; }
    @Override public boolean touchDown(int x, int y, int pointer, int button) { return false; }
    @Override public boolean touchUp(int x, int y, int pointer, int button) { return false; }
    @Override public boolean touchDragged(int x, int y, int pointer) { return false; }
    @Override public boolean mouseMoved(int x, int y) { return false; }
    @Override public boolean scrolled(float amountX, float amountY) { return false; }

    // return false i don't use it but it errors otherwise
    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }
}
