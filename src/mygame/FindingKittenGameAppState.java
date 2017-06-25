/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.shape.Box;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.style.BaseStyles;
import com.simsilica.lemur.style.ElementId;

/**
 * This app state is being performed when playing final video
 * @author Ivan
 */
public class FindingKittenGameAppState extends AbstractAppState{
    public AmbientLight al;
    public DirectionalLight sun;
    
    public Boolean justInitialized;
    
    public Boolean rfk_heartShown = false;
    
    private SimpleApplication app;
    private AssetManager assetManager;
    private Node rootNode;
    private Node shadowNode;
    public Spatial kittenNode;
    
    public Node robotNode;
    
    public Spatial heartNode;
    
    public CameraNode myCam;
    
      private AnimChannel channelAnim;
      private AnimControl controlAnim;
    
      public String rfk_message = "You found kitten!  Way to go, robot!";
        private Container rfkFoundLineWindow;
        private Label rfkFoundLineLabel;
        private Boolean rfkTextDisplayed = false;
      
      
    @Override
public void update(float tpf) {
    super.update(tpf);
    app.getInputManager().setCursorVisible(false);
        //unfortunatelly this code is not working from here. But it works from Main.java class
        //robotNode.move(0.0f, 0.5f, 1.3f); 
    
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
    
    this.shadowNode = new Node("ShadowNodeKitten");
    
    System.out.println("Initialize");
    this.basicSetLight();
    this.setScene();
    this.setCamera();
    
    this.justInitialized = true;
}
@Override
public void setEnabled(boolean enabled) {
    // Pause and unpause
    super.setEnabled(enabled);
    if (enabled) {
        if (justInitialized == false) {
        rootNode.addLight(this.sun); rootNode.addLight(this.al);
        this.rootNode.attachChild(shadowNode);
        
        //run kitten animation
        channelAnim.setAnim("Action");
        channelAnim.setLoopMode(LoopMode.Cycle);
        channelAnim.setSpeed(1f);
        
        System.out.println(kittenNode.getWorldTranslation().toString());
        System.out.println(robotNode.getWorldTranslation().toString());
        
        justInitialized = false;
        }
    } else {
        if (justInitialized == false){
        //removing light separately
        rootNode.removeLight(al); rootNode.removeLight(sun);
            //should remove camera too
            //detach also heart if applicable
            if (this.rfk_heartShown) {
                this.heartNode.removeFromParent();
            }
            this.hideRfkText();
            rootNode.detachChild(shadowNode);
            
        }
        this.rfk_heartShown = false;
        this.rfkTextDisplayed = false;
    }
}
public void showRfkText() {
    if (this.rfkTextDisplayed==false) {
            Node gui = app.getGuiNode();
            gui.attachChild(rfkFoundLineWindow);
        this.rfkTextDisplayed = true;
    }
}

public void hideRfkText() {
    if (this.rfkTextDisplayed==true) {
            rfkFoundLineWindow.removeFromParent();
        this.rfkTextDisplayed = false;
    }
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
 private void setCamera() {
        //flyCam.setMoveSpeed(10);
        app.getFlyByCamera().setEnabled(false);
       myCam = new CameraNode("CamNode", app.getCamera());
        //AllGameResources.playerNode.attachChild(AllGameResources.myCam);
        //why not to add cam directly to shadownode?
       this.shadowNode.attachChild(myCam);
       myCam.setLocalTranslation(new Vector3f(-15.5f, 6.5f, -8.5f));
       myCam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
       //myCam.lookAt(AllGameResources.playerNode.getLocalTranslation(), Vector3f.UNIT_Y);
      
        app.getInputManager().setCursorVisible(false);
    }

 private void setScene() {
     /*
     GuiGlobals.initialize(this.app);
     BaseStyles.loadGlassStyle();
     GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
     */
     //assigning ElementId causes NullPointerException
     this.rfkFoundLineWindow = new Container(/* new ElementId("rfkFoundNewLineWindow") */);
     rfkFoundLineLabel = new Label(rfk_message/*, new ElementId("rfkFoundLineLabel") */);
     rfkFoundLineWindow.addChild(rfkFoundLineLabel);
     
     
        float topPercentPosition=0.50f;
        float leftPercentPosition=0.35f;
        int winResolutionHeight = app.getCamera().getHeight();
        int winResolutionWidth = app.getCamera().getWidth();
        int topLocation = Math.round(topPercentPosition*winResolutionHeight);
        int leftLocation = Math.round(leftPercentPosition*winResolutionWidth);
        rfkFoundLineWindow.setLocalTranslation(leftLocation, topLocation, 0); 
     
     kittenNode = this.assetManager.loadModel("Models/onlyKitty.j3o");
     kittenNode.setLocalTranslation(5.0f, -0.05f, -3.5f);
     
     Box robotBox = new Box(0.5f, 0.5f, 0.5f);       
     Geometry robotGeometry = new Geometry("robot actor", robotBox);
     //robotGeometry.setLocalTranslation(2.0f, 0, 0);
     
     heartNode = this.assetManager.loadModel("Models/heart_lowpoly.j3o");
     heartNode.rotate(0.0f, new Double(Math.PI/2.0).floatValue(), 0.0f);
     heartNode.setLocalTranslation(10.5f, -0.05f, -1.25f);
     Material matRed = new Material(this.assetManager, "Common/MatDefs/Light/Lighting.j3md");
        matRed.setBoolean("UseMaterialColors",true);
        matRed.setColor("Diffuse",  ColorRGBA.Red);
        matRed.setColor("Ambient",  ColorRGBA.Red);
        matRed.setColor("Specular", ColorRGBA.Pink); 
     heartNode.setMaterial(matRed);
     //heartNode.scale(0.05f);
     
     Material mat2 = new Material(this.assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat2.setBoolean("UseMaterialColors",true);
        mat2.setColor("Diffuse",  ColorRGBA.Green);
        mat2.setColor("Ambient",  ColorRGBA.Green);
        mat2.setColor("Specular", ColorRGBA.Blue); 
    
     
        robotNode = new Node("robot scene node");
        robotGeometry.setMaterial(mat2);
        robotNode.attachChild(robotGeometry);
        robotNode.setLocalTranslation(5.5f,0.0f,10.0f);
        shadowNode.attachChild(robotNode);
     
     //controlAnim.addListener(this);
     //channelAnim = controlAnim.createChannel();
     
             //controlAnim = kittenNode.getControl(AnimControl.class);        
        controlAnim = ((Node)(((Node) ( ((Node)(  ((Node) kittenNode).getChild(0) )).getChild(0) )).getChild(0))).getControl(AnimControl.class);
        for (String anim : controlAnim.getAnimationNames()) {
            System.out.println(anim);
        }
        channelAnim = controlAnim.createChannel();
        
     shadowNode.attachChild(kittenNode);
     //shadowNode.attachChild(heartNode);
 }
 /**
  * put all actors back to their places. 
  * It is assumed that SetScene method has been called before, otherwise we'll get a lot of errors
  */
 public void resetScenePositions() {
     myCam.setLocalTranslation(new Vector3f(-15.5f, 6.5f, -8.5f));
     this.hideRfkText();
     kittenNode.setLocalTranslation(5.0f, -0.05f, -3.5f);
     heartNode.setLocalTranslation(10.5f, -0.05f, -1.25f);
     robotNode.setLocalTranslation(5.5f,0.0f,10.0f);
 }
 
 public void showHeart() {
     if (this.rfk_heartShown == false) {
        shadowNode.attachChild(heartNode);
        this.rfk_heartShown=true;
     }
 }
}
