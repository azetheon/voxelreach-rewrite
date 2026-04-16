package aze.games.voxelreach.player;

import aze.games.voxelreach.world.ChunkGeneration;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.Camera;

public class Raycast {
    public static class HitResult {
        public int x, y, z;
        public int faceX, faceY, faceZ; // face of the block i wanna place block on (very good english ik)
        public boolean hit;
    }

    public static HitResult raycast(Camera camera, float maxDistance) {
        Vector3 origin = new Vector3(camera.position);
        Vector3 direction = new Vector3(camera.direction).nor();
        HitResult result = new HitResult();

        float step = 0.1f;
        Vector3 position = new Vector3(origin);

        for (float i = 0; i < maxDistance; i += step) {
            position.add(direction.x * step, direction.y * step, direction.z * step);

            int x = (int)Math.floor(position.x + 0.5f);
            int y = (int)Math.floor(position.y + 0.5f);
            int z = (int)Math.floor(position.z + 0.5f);

            if (isSolid(x, y, z)) {
                result.hit = true;
                result.x = x;
                result.y = y;
                result.z = z;

                result.faceX = (int)Math.floor(position.x - direction.x * step + 0.5f);
                result.faceY = (int)Math.floor(position.y - direction.y * step + 0.5f);
                result.faceZ = (int)Math.floor(position.z - direction.z * step + 0.5f);

                return result;
            }
        }

        result.hit = false;
        return result;
    }

    private static boolean isSolid(int x, int y, int z) {
        return ChunkGeneration.getBlock(x, y, z) != null;
    }
}
