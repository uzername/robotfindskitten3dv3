package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;

import com.jme3.input.ChaseCamera;
import com.jme3.input.MouseInput;
import com.jme3.scene.CameraNode;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.scene.Node;
/**
 * test
 * @author normenhansen and jovan
 */
public class Main extends SimpleApplication implements AnalogListener, ActionListener{
    /*
    private DirectionalLight sun;
    private AmbientLight al;
    private Geometry player2;
    private CameraNode myCam;
    private Node playerNode;
    */
    //direction of camera
    /*
    Vector3f direction = new Vector3f();    
    boolean rotate = true;
    */
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        this.basicSetLight();
        this.setScene();
        this.setCamera();
        this.registerInput();
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
        inputManager.setCursorVisible(false);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    private void setCamera() {
        //flyCam.setMoveSpeed(10);
        flyCam.setEnabled(false);
        AllGameResources.myCam = new CameraNode("CamNode", cam);
        AllGameResources.myCam.setControlDir(ControlDirection.SpatialToCamera);
        AllGameResources.playerNode.attachChild(AllGameResources.myCam);
        AllGameResources.myCam.setLocalTranslation(new Vector3f(-7, 5, 0));
    
        AllGameResources.myCam.lookAt(AllGameResources.playerNode.getLocalTranslation(), Vector3f.UNIT_Y);
        
        inputManager.setCursorVisible(false);
    }
    private void basicSetLight() {
        AllGameResources.sun = new DirectionalLight();
        AllGameResources.sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        rootNode.addLight(AllGameResources.sun);
        AllGameResources.al = new AmbientLight();
        AllGameResources.al.setColor(ColorRGBA.White.mult(0.5f));
        rootNode.addLight(AllGameResources.al);
    }
    
    private void setScene() {
        Box b = new Box(0.5f, 0.5f, 0.5f);
        
        AllGameResources.player2 = new Geometry("green cube", b);
        AllGameResources.player2.setLocalTranslation(0.0f, 0.5f, 0.0f);
        
        Material mat2 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat2.setBoolean("UseMaterialColors",true);
        mat2.setColor("Diffuse",  ColorRGBA.Green);
        mat2.setColor("Ambient",  ColorRGBA.Green);
        mat2.setColor("Specular", ColorRGBA.Blue); 
        AllGameResources.player2.setMaterial(mat2);
        AllGameResources.playerNode = new Node("player");
        AllGameResources.playerNode.attachChild(AllGameResources.player2);
        rootNode.attachChild(AllGameResources.playerNode);
        
        Quad plain = new Quad(processing.AllParams.allFieldDim1.floatValue(), processing.AllParams.allFieldDim2.floatValue());
        plain.scaleTextureCoordinates(new Vector2f(processing.AllParams.allFieldDim1.floatValue()/2.0f, processing.AllParams.allFieldDim2.floatValue()/2.0f));
        Geometry floor = new Geometry("floor", plain);
        Material matFloor = (assetManager.loadMaterial("Materials/plainMaterial.j3m"));
        //Material matFloor = new Material(assetManager, "Common/MatDefs/Misc/SimpleTextured.j3md");
        Texture floortexture = assetManager.loadTexture("Textures/cul_door.jpg");
        floortexture.setWrap(Texture.WrapMode.Repeat);
        matFloor.setTexture("DiffuseMap", floortexture);
        
        floor.setMaterial(matFloor);
        floor.rotate(-new Double(Math.PI/2.0).floatValue(), 0.0f, 0.0f);
        
        rootNode.attachChild(floor);
        
        placeObjects();
    }
   public void placeObjects() {
       RenderHelpers.assetManager = assetManager;
       Node addedNode = RenderHelpers.PreparedModel();
       addedNode.setLocalTranslation(processing.AllParams.allFieldDim1.floatValue()/2.0f, 0.0f, -processing.AllParams.allFieldDim2.floatValue()/2.0f);
       rootNode.attachChild(addedNode);
   }
    
   public void registerInput() {
    inputManager.addMapping("moveForward", new KeyTrigger(keyInput.KEY_UP), new KeyTrigger(keyInput.KEY_W));
    inputManager.addMapping("moveBackward", new KeyTrigger(keyInput.KEY_DOWN), new KeyTrigger(keyInput.KEY_S));
    inputManager.addMapping("moveRight", new KeyTrigger(keyInput.KEY_RIGHT), new KeyTrigger(keyInput.KEY_D));
    inputManager.addMapping("moveLeft", new KeyTrigger(keyInput.KEY_LEFT), new KeyTrigger(keyInput.KEY_A));
    
    inputManager.addMapping("rotateRight", new MouseAxisTrigger(MouseInput.AXIS_X, true));
    inputManager.addMapping("rotateLeft", new MouseAxisTrigger(MouseInput.AXIS_X, false));
    inputManager.addMapping("rotateUp", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
    inputManager.addMapping("rotateDown", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
    
    inputManager.addListener(this, "moveForward", "moveBackward", "moveRight", "moveLeft");
    inputManager.addListener(this, "rotateRight", "rotateLeft");
    inputManager.addListener(this, "rotateUp", "rotateDown");
    
  }

    public void onAnalog(String name, float value, float tpf) {
        //general movement algorithm
        //get the camera direction. It's OK to normalize it. 
        //Get the projection of this vector onto the base plane of scene (xOy for this scene). Normalize result
        //move along this vector
        AllGameResources.direction.set(cam.getDirection()).normalizeLocal();
        Vector3f planeNormal = new Vector3f(0, 1, 0);
        if (name.equals("moveForward")) {
            //direction.multLocal(5 * tpf);
            //playerNode.move(direction);
            // https://www.physicsforums.com/threads/projecting-a-vector-onto-a-plane.496184/
            Vector3f camProjectionVector = new Vector3f(AllGameResources.direction.subtract(planeNormal.mult(AllGameResources.direction.mult(planeNormal))));
            camProjectionVector.normalizeLocal();
            camProjectionVector.multLocal(5 * tpf);
            AllGameResources.direction.set(camProjectionVector);
            AllGameResources.playerNode.move(AllGameResources.direction);
        }
        if (name.equals("moveBackward")) {
            //direction.multLocal(-5 * tpf);
            //playerNode.move(direction);
            Vector3f camProjectionVector = new Vector3f(AllGameResources.direction.subtract(planeNormal.mult(AllGameResources.direction.mult(planeNormal))));
            camProjectionVector.normalizeLocal();
            camProjectionVector.multLocal(-5 * tpf);
            AllGameResources.direction.set(camProjectionVector);
            AllGameResources.playerNode.move(AllGameResources.direction);
        }
        if (name.equals("moveRight")) {
            //direction.crossLocal(Vector3f.UNIT_X).multLocal(5 * tpf);
            //playerNode.move(direction);
            Vector3f camProjectionVector = new Vector3f(AllGameResources.direction.subtract(planeNormal.mult(AllGameResources.direction.mult(planeNormal))));
            camProjectionVector.normalizeLocal();
            camProjectionVector.crossLocal(Vector3f.UNIT_Y).multLocal(5 * tpf);
            AllGameResources.direction.set(camProjectionVector);
            AllGameResources.playerNode.move(AllGameResources.direction);
        }
        if (name.equals("moveLeft")) {
            //direction.crossLocal(Vector3f.UNIT_X).multLocal(-5 * tpf);
            //playerNode.move(direction);
            Vector3f camProjectionVector = new Vector3f(AllGameResources.direction.subtract(planeNormal.mult(AllGameResources.direction.mult(planeNormal))));
            camProjectionVector.normalizeLocal();
            camProjectionVector.crossLocal(Vector3f.UNIT_Y).multLocal(-5 * tpf);
            AllGameResources.direction.set(camProjectionVector);
            AllGameResources.playerNode.move(AllGameResources.direction);
        }
        if (name.equals("rotateRight") && AllGameResources.rotate) {
            AllGameResources.playerNode.rotate(0, 0.5f*value, 0);
        }
        if (name.equals("rotateLeft") && AllGameResources.rotate) {
            AllGameResources.playerNode.rotate(0, -0.5f*value, 0);
        }
        if (name.equals("rotateUp") && AllGameResources.rotate) {
            // Do not rotate camera node here, instead rotate camera node by value along the axis, 
            // which goes through playernode
            AllGameResources.myCam.rotate(0.5f*value, 0, 0);
        }
        if (name.equals("rotateDown") && AllGameResources.rotate) {
            //playerNode.rotate(-0.5f*value, 0, 0);
            AllGameResources.myCam.rotate(-0.5f*value, 0, 0);
        }
    }

    public void onAction(String name, boolean isPressed, float tpf) {

    }
}
