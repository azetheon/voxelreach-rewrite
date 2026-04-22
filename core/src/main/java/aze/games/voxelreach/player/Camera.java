package aze.games.voxelreach.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import aze.games.voxelreach.player.UserInterface;

public class Camera {
    private static PerspectiveCamera camera;
    private static OrthographicCamera overlayCamera;

    private static int mouseX = Gdx.input.getX();
    private static int mouseY = Gdx.input.getY();
    private static int resolutionWidth = Gdx.graphics.getWidth();
    private static int resolutionHeight = Gdx.graphics.getHeight();

    public static void prepareCamera() {
        camera = new PerspectiveCamera(80, resolutionWidth, resolutionHeight);
        camera.position.set(3f, 3f, 3f); // spawn point
        camera.near = 0.1f;
        camera.far = 500f;
        camera.update(); // update where i look at
    }

    public static void cameraMovement() {
        if (UserInterface.getChatStatus()) return; // if chat is open i don't want the camera to move around

        int screenX = Gdx.input.getX();
        int screenY = Gdx.input.getY();


        // magnitude is needed for smoother moving instead of instant flicks
        int magnitudeX = Math.abs(mouseX - screenX);
        int magnitudeY = Math.abs(mouseY - screenY);

        /*
         * Explanation for myself cuz im still learning 3d game development:
         *
         * if mouseX goes over screenX move towards the right cuz if the mouse's x position is higher
         * than screen's x position we logically move right
         * same principle but opposite side for left
         *
         * same with y but camera has a maximum y rotation to not do barrel rolls
         * i set it as -0.999 and 0.999 instead of 1 cuz setting it to 1 gives weird results
         * i'll call it "half-barrel-rolls", it basically tries to roll but stops and glitches
         *
         * */

        float sensitivity = 0.1f;
        if (mouseX > screenX)
            camera.rotate(Vector3.Y, 1f * magnitudeX * sensitivity);
        if (mouseX < screenX)
            camera.rotate(Vector3.Y, -1f * magnitudeX * sensitivity);

        if (mouseY < screenY && camera.direction.y > -0.999)
            camera.rotate(camera.direction.cpy().crs(Vector3.Y), -1f * magnitudeY * sensitivity);
        if (mouseY > screenY && camera.direction.y < 0.999)
            camera.rotate(camera.direction.cpy().crs(Vector3.Y), 1f * magnitudeY * sensitivity);

        mouseX = screenX;
        mouseY = screenY;

        camera.update();
    }

    public static void prepareOverlayCamera() {
        overlayCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        overlayCamera.position.set(
            overlayCamera.viewportWidth / 2f,
            overlayCamera.viewportHeight / 2f,
            1f
        );
        overlayCamera.update();
    }

    public static void playerMovement() {
        if (UserInterface.getChatStatus()) return; // don't move while chat is open

        float delta = Gdx.graphics.getDeltaTime();
        float movementSpeed;

        /*
         * another note to myself:
         *
         * .nor() is "normalize"
         * normalizing the vector keeps only
         * the direction which lets me have consistent movement speeds
         *
         * .crs(something) is "cross product"
         * it gives a vector at a right angle to this and (something)
         * it allows me to go left and right
         *
         */

        Vector3 forward = new Vector3(camera.direction.x, 0, camera.direction.z).nor();
        Vector3 backward = new Vector3(-camera.direction.x, 0, -camera.direction.z).nor();
        Vector3 right = new Vector3(Vector3.Y).crs(forward).nor();
        Vector3 left = new Vector3(Vector3.Y).crs(backward).nor();

        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            movementSpeed = 20f;
        }
        else {
            movementSpeed = 10f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.position.add(forward.scl(movementSpeed * delta));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.position.add(backward.scl(movementSpeed * delta));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.position.add(right.scl(movementSpeed * delta));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.position.add(left.scl(movementSpeed * delta));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            camera.position.y += movementSpeed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            camera.position.y -= movementSpeed * delta;
        }
    }

    public static PerspectiveCamera getCamera() {
        return camera;
    }

    public static OrthographicCamera getOverlay() {
        return overlayCamera;
    }
}
