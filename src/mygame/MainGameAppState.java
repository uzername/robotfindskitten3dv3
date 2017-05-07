package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.style.BaseStyles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import processing.AllParams;

/**
 * main game app state. see JMonkeyEngine Beginners Guide 3
 * https://jmonkeyengine.github.io/wiki/jme3/advanced/application_states.html
 * This item should also implement the listener's stuff. Just for completeness
 * @author Ivan
 */
public class MainGameAppState extends AbstractAppState implements AnalogListener, ActionListener {
    /*other important things are in AllGameResources*/
    private SimpleApplication app;
    private AssetManager assetManager;
    private Node rootNode;
    private Node shadowNode;
    
    private Container itemDescrWindow;
    private Label itemDescrLabel;
    private boolean itemDisplayed;
    
    //minor optimization for displaying messages. Surprisingly but optimization fails
    private String previouslyHitItem = null;
    
    //contains colliding boxes for all of the columns
    private Map<BoundingBox, String> CollidingBoxes;
    
    public AmbientLight al;
    public DirectionalLight sun;
    
    public Boolean justInitialized;
// Note that update is only called while the state is both attached and enabled.    
@Override
public void update(float tpf) {
    app.getInputManager().setCursorVisible(false);
}
@Override
public void cleanup() {

}
@Override
public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    this.app = (SimpleApplication) app;
    this.rootNode = this.app.getRootNode();
    this.assetManager = this.app.getAssetManager();
    
    this.shadowNode = new Node("ShadowNode");
    //this.rootNode.attachChild(shadowNode);
    
    //initialize interface (for displaying item description)
     GuiGlobals.initialize(this.app);
     BaseStyles.loadGlassStyle();
     GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
    this.itemDescrWindow = new Container();
    itemDescrLabel = new Label("ROBOTFINDSKITTEN");
    itemDescrWindow.addChild(itemDescrLabel);
        int winResolutionHeight = app.getCamera().getHeight();
        int winResolutionWidth = app.getCamera().getWidth();
        float topPercentPosition=0.99f;
        float leftPercentPosition=0.01f;
        int topLocation = Math.round(topPercentPosition*winResolutionHeight);
        int leftLocation = Math.round(leftPercentPosition*winResolutionHeight);
        itemDescrWindow.setLocalTranslation(leftLocation, topLocation, 0); 
    
    System.out.println("Initialize");
    this.basicSetLight();
    this.setScene();
    this.setCamera();
    this.registerInput();
    this.justInitialized = true;
    
    
    
} 
@Override
public void setEnabled(boolean enabled) {
    // Pause and unpause
    super.setEnabled(enabled);
    if(enabled){
        // init stuff that is in use while this state is RUNNING
        System.out.println("++++++Enabling scene++++++");
        if (enabled) {
            System.out.println("Attaching shadowNode");
            this.registerInput();
            this.rootNode.attachChild(shadowNode);
            System.out.println("Attaching sun");
            rootNode.addLight(this.sun); rootNode.addLight(this.al);
            
            System.out.println("Attaching interface");
            Node gui = app.getGuiNode();
            gui.attachChild(itemDescrWindow);
            
            justInitialized = false;
        } else {
            
        }
      } else {
        // take away everything not needed while this state is PAUSED
        
        System.out.println("------Shuting down scene. Removing Listeners------");
        rootNode.removeLight(al); rootNode.removeLight(sun);
        
        app.getInputManager().deleteMapping("moveForward");
        app.getInputManager().deleteMapping("moveBackward");
        app.getInputManager().deleteMapping("moveLeft");
        app.getInputManager().deleteMapping("moveRight");
        app.getInputManager().deleteMapping("rotateRight");
        app.getInputManager().deleteMapping("rotateLeft");
        app.getInputManager().deleteMapping("rotateUp");
        app.getInputManager().deleteMapping("rotateDown");
        
        if (justInitialized == false){
        System.out.println("------Shuting down scene. Removing shadowNode------");
        //rootNode.detachAllChildren();
        rootNode.detachChild(shadowNode);
        System.out.println("------Shuting down scene. Removing interface------");
        itemDescrWindow.removeFromParent(); 
        }
      }
}

 private void setCamera() {
        //flyCam.setMoveSpeed(10);
        app.getFlyByCamera().setEnabled(false);
        AllGameResources.myCam = new CameraNode("CamNode", app.getCamera());
        AllGameResources.myCam.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
        AllGameResources.playerNode.attachChild(AllGameResources.myCam);
        AllGameResources.myCam.setLocalTranslation(new Vector3f(-3.5f, 2.5f, 0));
    
        AllGameResources.myCam.lookAt(AllGameResources.playerNode.getLocalTranslation(), Vector3f.UNIT_Y);
        
        app.getInputManager().setCursorVisible(false);
    }
    private void basicSetLight() {
        this.sun = new DirectionalLight();
        this.sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        //rootNode.addLight(this.sun);
        this.al = new AmbientLight();
        this.al.setColor(ColorRGBA.White.mult(0.5f));
        //rootNode.addLight(this.al);
        
        //light is being added on scene activation, removed on deactivation
    }
    
    private void setScene() {
        //append player
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
        shadowNode.attachChild(AllGameResources.playerNode);
        
        //append floor
        attachFloor();
        
        placeObjects();
    }
    
   public void attachFloor() {
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
        shadowNode.attachChild(floor);
   }
   public void removeFloor() throws Exception {
       int res = shadowNode.detachChildNamed("floor");
       if (res==-1) {
           throw new Exception("floor not found");
       }
   }
   
   //removing objects from scene
   public void removeObjects() {
       
       for (Map.Entry<BoundingBox, String> entry : CollidingBoxes.entrySet()) {
           shadowNode.detachChildNamed(entry.getValue());
       }
   }
   
   public void placeObjects() {
       RenderHelpers.assetManager = assetManager;
       CollidingBoxes = new HashMap<>();
       int currentIndex=0;
       for (processing.GameFieldItem object : AllParams.GameLogicArray) {  
        Node addedNode = RenderHelpers.PreparedModel();
        
        addedNode.setLocalTranslation(object.fieldXPosition.floatValue(), 0.0f, -object.fieldYPosition.floatValue());
        BoundingBox collisionDetector = new BoundingBox(new Vector3f(addedNode.getLocalTranslation().x, 
                                                        addedNode.getLocalTranslation().y, 
                                                        addedNode.getLocalTranslation().z), 
                                             0.95f, 4.5f, 0.95f);
        /*
        Box collisionGeomBox = new Box(0.95f, 4.5f, 0.95f);
        Geometry collisionGeom = new Geometry("Collision_"+addedNode.getName(), collisionGeomBox);
        collisionGeom.setModelBound(collisionDetector);
        addedNode.attachChild(collisionGeom);
        */
        //addedNode.setModelBound(collisionDetector);
        
        
        CollidingBoxes.put(collisionDetector, addedNode.getName());
        
        System.out.println("the position: "+addedNode.getLocalTranslation().toString());
        shadowNode.attachChild(addedNode);
        AllParams.GameLogicArray.get(currentIndex).collisionID=addedNode.getName();
        currentIndex++;
       }
       /*
        addedNode.setLocalTranslation(processing.AllParams.allFieldDim1.floatValue()/2.0f, 0.0f, -processing.AllParams.allFieldDim2.floatValue()/2.0f);
        shadowNode.attachChild(addedNode);
        */
        /*
        Node addedNode2 = RenderHelpers.PreparedModel();
        addedNode2.setLocalTranslation(0.0f, 0.0f, -AllParams.allFieldDim2.floatValue());
        shadowNode.attachChild(addedNode2);
       
        Node addedNode3 = RenderHelpers.PreparedModel();
        addedNode3.setLocalTranslation(AllParams.allFieldDim1.floatValue(), 0.0f, -AllParams.allFieldDim2.floatValue());
        shadowNode.attachChild(addedNode3);
        
        Node addedNode4 = RenderHelpers.PreparedModel();
        addedNode4.setLocalTranslation(1.0f, 0.0f, -1.0f);
        shadowNode.attachChild(addedNode4);
        
        Node addedNode5 = RenderHelpers.PreparedModel();
        addedNode5.setLocalTranslation(AllParams.allFieldDim1.floatValue(), 0.0f, -0.0f);
        shadowNode.attachChild(addedNode5);
        */
        
        //place player on field
        AllGameResources.playerNode.setLocalTranslation(AllParams.mainactorPositionInsightX.floatValue(), 0.0f, -AllParams.mainactorPositionInsightY.floatValue());
        
   }

        public void onAnalog(String name, float value, float tpf) {
        //general movement algorithm
        //get the camera direction. It's OK to normalize it. 
        //Get the projection of this vector onto the base plane of scene (xOy for this scene). Normalize result
        //move along this vector
        
        AllGameResources.direction.set(app.getCamera().getDirection()).normalizeLocal();
        Vector3f planeNormal = new Vector3f(0, 1, 0);
        boolean kittenFound = false;
        
        if (name.equals("moveForward")) {
            Boolean collisionTestPassed = false;
                //direction.multLocal(5 * tpf);
                //playerNode.move(direction);
                // https://www.physicsforums.com/threads/projecting-a-vector-onto-a-plane.496184/
                Vector3f camProjectionVector = new Vector3f(AllGameResources.direction.subtract(planeNormal.mult(AllGameResources.direction.mult(planeNormal))));
                camProjectionVector.normalizeLocal();
                camProjectionVector.multLocal(5 * tpf);
                AllGameResources.direction.set(camProjectionVector);
                AllGameResources.playerNode.move(AllGameResources.direction);
            //test collision
            String value1="";
            for (Map.Entry<BoundingBox, String> entry : CollidingBoxes.entrySet()) {
                BoundingBox singleCollidingBox = entry.getKey();
                value1 = entry.getValue();
                
                CollisionResults results = new CollisionResults();
                //AllGameResources.playerNode.collideWith(singleCollidingBox, results);
                AllGameResources.playerNode.collideWith(singleCollidingBox, results);
                /*
                System.out.println("Number of Collisions between" +
                AllGameResources.playerNode.getName()+ " and " + singleCollidingBox.getType().name() + ": " + results.size());
                */
                // Use the results
                if (results.size() > 0) {
                  // how to react when a collision was detected
                  CollisionResult closest  = results.getClosestCollision();
                  /*
                  System.out.println("=====");
                  System.out.println("What was hit? " + closest.getGeometry().getName() );
                    System.out.println("identifier: "+value1);
                  */
                    //display item in interface
                    ItemSummaryResult r = RenderHelpers.getItemDescriptionClassById(value1);
                    String value2 = r.message;
                    kittenFound = r.isKitten;
                    if ( true || (previouslyHitItem == null) || (previouslyHitItem!=value1) ) {
                        System.out.println(value2);
                        itemDescrLabel.setText(value2);
                        //previouslyHitItem = value1;
                    }
                  /*  
                  System.out.println("Where was it hit? " + closest.getContactPoint() );
                  System.out.println("Distance? " + closest.getDistance() );
                  System.out.println("=====");
                  */
                  collisionTestPassed = true;
                } else {
                  // how to react when no collision occured
                  //collisionTestPassed = false;
                  //previouslyHitItem = null;
                }
            }
            if (collisionTestPassed == true) {
                //backtrace on collision. We have done movement before testing. 
                AllGameResources.direction.set(-AllGameResources.direction.x, -AllGameResources.direction.y, -AllGameResources.direction.z);
                AllGameResources.playerNode.move(AllGameResources.direction);
                //we have only one type of collidable entity; lookup for message text and display it somehow. Identifier is stored in value1
                
            }
        }
        if (name.equals("moveBackward")) {
            Boolean collisionTestPassed = false;
            
                //direction.multLocal(-5 * tpf);
                //playerNode.move(direction);
                Vector3f camProjectionVector = new Vector3f(AllGameResources.direction.subtract(planeNormal.mult(AllGameResources.direction.mult(planeNormal))));
                camProjectionVector.normalizeLocal();
                camProjectionVector.multLocal(-5 * tpf);
                AllGameResources.direction.set(camProjectionVector);
                AllGameResources.playerNode.move(AllGameResources.direction);
            
            String value1="";
            for (Map.Entry<BoundingBox, String> entry : CollidingBoxes.entrySet()) {
                BoundingBox singleCollidingBox = entry.getKey();
                value1 = entry.getValue();
                
                CollisionResults results = new CollisionResults();
                //AllGameResources.playerNode.collideWith(singleCollidingBox, results);
                AllGameResources.playerNode.collideWith(singleCollidingBox, results);
                /*
                System.out.println("Number of Collisions between" +
                AllGameResources.playerNode.getName()+ " and " + singleCollidingBox.getType().name() + ": " + results.size());
                */
                // Use the results
                if (results.size() > 0) {
                  // how to react when a collision was detected
                  CollisionResult closest  = results.getClosestCollision();
                  /*
                  System.out.println("=====");
                  System.out.println("What was hit? " + closest.getGeometry().getName() );
                    System.out.println("identifier: "+value1);
                  */
                  ItemSummaryResult r = RenderHelpers.getItemDescriptionClassById(value1);
                    String value2 = r.message;
                    kittenFound = r.isKitten;
                    
                    if ( true || (previouslyHitItem == null) || (previouslyHitItem!=value1) ) {
                        System.out.println(value2);
                        itemDescrLabel.setText(value2);
                        //previouslyHitItem = value1;
                    }
                  /*  
                  System.out.println("Where was it hit? " + closest.getContactPoint() );
                  System.out.println("Distance? " + closest.getDistance() );
                  System.out.println("=====");
                  */
                  collisionTestPassed = true;
                } else {
                  // how to react when no collision occured
                  //collisionTestPassed = false;
                  //previouslyHitItem = null;
                }
            }
            
            if (collisionTestPassed==true) {
                //backtrace on collision. We have done movement before testing. 
                AllGameResources.direction.set(-AllGameResources.direction.x, -AllGameResources.direction.y, -AllGameResources.direction.z);
                AllGameResources.playerNode.move(AllGameResources.direction);
                //here is the message
                
            }
        }
        if (name.equals("moveRight")) {
            Boolean collisionTestPassed = false;
            
            //direction.crossLocal(Vector3f.UNIT_X).multLocal(5 * tpf);
            //playerNode.move(direction);
            Vector3f camProjectionVector = new Vector3f(AllGameResources.direction.subtract(planeNormal.mult(AllGameResources.direction.mult(planeNormal))));
            camProjectionVector.normalizeLocal();
            camProjectionVector.crossLocal(Vector3f.UNIT_Y).multLocal(5 * tpf);
            AllGameResources.direction.set(camProjectionVector);
            AllGameResources.playerNode.move(AllGameResources.direction);
            
            String value1="";
            for (Map.Entry<BoundingBox, String> entry : CollidingBoxes.entrySet()) {
                BoundingBox singleCollidingBox = entry.getKey();
                value1 = entry.getValue();
                
                CollisionResults results = new CollisionResults();
                //AllGameResources.playerNode.collideWith(singleCollidingBox, results);
                AllGameResources.playerNode.collideWith(singleCollidingBox, results);
                /*
                System.out.println("Number of Collisions between" +
                AllGameResources.playerNode.getName()+ " and " + singleCollidingBox.getType().name() + ": " + results.size());
                */
                // Use the results
                if (results.size() > 0) {
                  // how to react when a collision was detected
                  CollisionResult closest  = results.getClosestCollision();
                  /*
                  System.out.println("=====");
                  System.out.println("What was hit? " + closest.getGeometry().getName() );
                    System.out.println("identifier: "+value1);
                  */  
                    //display here item in iface
                    //String value2 = RenderHelpers.getItemDescriptionById(value1);
                    ItemSummaryResult r = RenderHelpers.getItemDescriptionClassById(value1);
                    String value2 = r.message;
                    kittenFound = r.isKitten;
                    
                    if ( true || (previouslyHitItem == null) || (previouslyHitItem!=value1) ) {
                        System.out.println(value2);
                        itemDescrLabel.setText(value2);
                        //previouslyHitItem = value1;
                    }
                  /*  
                  System.out.println("Where was it hit? " + closest.getContactPoint() );
                  System.out.println("Distance? " + closest.getDistance() );
                  System.out.println("=====");
                  */
                  collisionTestPassed = true;
                } else {
                  // how to react when no collision occured
                  //collisionTestPassed = false;
                  
                  //previouslyHitItem = null;
                }
            }
            
            if (collisionTestPassed==true) {
                //backtrace on collision. We have done movement before testing. 
                AllGameResources.direction.set(-AllGameResources.direction.x, -AllGameResources.direction.y, -AllGameResources.direction.z);
                AllGameResources.playerNode.move(AllGameResources.direction);
                
            }
            
        }
        if (name.equals("moveLeft")) {
            Boolean collisionTestPassed = false;
            
            //direction.crossLocal(Vector3f.UNIT_X).multLocal(-5 * tpf);
            //playerNode.move(direction);
            Vector3f camProjectionVector = new Vector3f(AllGameResources.direction.subtract(planeNormal.mult(AllGameResources.direction.mult(planeNormal))));
            camProjectionVector.normalizeLocal();
            camProjectionVector.crossLocal(Vector3f.UNIT_Y).multLocal(-5 * tpf);
            AllGameResources.direction.set(camProjectionVector);
            AllGameResources.playerNode.move(AllGameResources.direction);
            
            String value1="";
            for (Map.Entry<BoundingBox, String> entry : CollidingBoxes.entrySet()) {
                BoundingBox singleCollidingBox = entry.getKey();
                value1 = entry.getValue();
                
                CollisionResults results = new CollisionResults();
                //AllGameResources.playerNode.collideWith(singleCollidingBox, results);
                AllGameResources.playerNode.collideWith(singleCollidingBox, results);
                /*
                System.out.println("Number of Collisions between" +
                AllGameResources.playerNode.getName()+ " and " + singleCollidingBox.getType().name() + ": " + results.size());
                */
                // Use the results
                if (results.size() > 0) {
                  // how to react when a collision was detected
                  CollisionResult closest  = results.getClosestCollision();
                  /*
                  System.out.println("=====");
                  System.out.println("What was hit? " + closest.getGeometry().getName() );
                    System.out.println("identifier: "+value1);
                  */
                    //String value2 = RenderHelpers.getItemDescriptionById(value1);                    
                    ItemSummaryResult r = RenderHelpers.getItemDescriptionClassById(value1);
                    String value2 = r.message;
                    kittenFound = r.isKitten;
                    
                    if ( true || (previouslyHitItem == null) || (previouslyHitItem!=value1) ) {
                        System.out.println(value2);
                        itemDescrLabel.setText(value2);
                        //previouslyHitItem = value1;
                    }
                  /*  
                  System.out.println("Where was it hit? " + closest.getContactPoint() );
                  System.out.println("Distance? " + closest.getDistance() );
                  System.out.println("=====");
                  */
                  collisionTestPassed = true;
                } else {
                  // how to react when no collision occured
                  //collisionTestPassed = false;
                  //previouslyHitItem = null;
                }
            }
            
            if (collisionTestPassed==true) {
                //backtrace on collision. We have done movement before testing. 
                AllGameResources.direction.set(-AllGameResources.direction.x, -AllGameResources.direction.y, -AllGameResources.direction.z);
                AllGameResources.playerNode.move(AllGameResources.direction);
                
            }
            
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
        
        if (kittenFound) {
            System.out.println("!!! KITTEN FOUND !!!");
        }
        
    }

    public void onAction(String name, boolean isPressed, float tpf) {

    }
    
    public void registerInput() {
        System.out.println("Registering Input");
    app.getInputManager().addMapping("moveForward", new KeyTrigger(KeyInput.KEY_UP), new KeyTrigger(KeyInput.KEY_W));
    app.getInputManager().addMapping("moveBackward", new KeyTrigger(KeyInput.KEY_DOWN), new KeyTrigger(KeyInput.KEY_S));
    app.getInputManager().addMapping("moveRight", new KeyTrigger(KeyInput.KEY_RIGHT), new KeyTrigger(KeyInput.KEY_D));
    app.getInputManager().addMapping("moveLeft", new KeyTrigger(KeyInput.KEY_LEFT), new KeyTrigger(KeyInput.KEY_A));
    
    app.getInputManager().addMapping("rotateRight", new MouseAxisTrigger(MouseInput.AXIS_X, true));
    app.getInputManager().addMapping("rotateLeft", new MouseAxisTrigger(MouseInput.AXIS_X, false));
    app.getInputManager().addMapping("rotateUp", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
    app.getInputManager().addMapping("rotateDown", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
    
    app.getInputManager().addListener(this, "moveForward", "moveBackward", "moveRight", "moveLeft");
    app.getInputManager().addListener(this, "rotateRight", "rotateLeft");
    app.getInputManager().addListener(this, "rotateUp", "rotateDown");
    
  }
    
    
}
