package mygame;

import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

/**
 * Mostly static class which contains all the game resources. 
 * used mostly to share the data between Main.java and gamestates
 * @author Ivan
 */
public class AllGameResources {
    public static Node playerNode;
    public static CameraNode myCam;
    public static Geometry player2;
    
    public static Vector3f direction = new Vector3f();
    public static boolean rotate = true;
}
