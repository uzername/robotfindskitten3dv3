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
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;

/**
 * This app state is being performed when playing final video
 * @author Ivan
 */
public class FindingKittenGameAppState extends AbstractAppState{
    public AmbientLight al;
    public DirectionalLight sun;
    
    public Boolean justInitialized;
    
    private SimpleApplication app;
    private AssetManager assetManager;
    private Node rootNode;
    private Node shadowNode;
    private Spatial kittenNode;
    private CameraNode myCam;
    
      private AnimChannel channelAnim;
      private AnimControl controlAnim;
    
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
        
        justInitialized = false;
        }
    } else {
        if (justInitialized == false){
        //removing light separately
        rootNode.removeLight(al); rootNode.removeLight(sun);
            //should remove camera too
            rootNode.detachChild(shadowNode);
        }
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
       myCam.setLocalTranslation(new Vector3f(-3.5f*1.5f, 2.5f*1.5f, 0));
       myCam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
       //myCam.lookAt(AllGameResources.playerNode.getLocalTranslation(), Vector3f.UNIT_Y);
      
        app.getInputManager().setCursorVisible(false);
    }

 private void setScene() {
     kittenNode = this.assetManager.loadModel("Models/onlyKitty.j3o");
     
     //controlAnim.addListener(this);
     //channelAnim = controlAnim.createChannel();
     
             //controlAnim = kittenNode.getControl(AnimControl.class);        
        controlAnim = ((Node)(((Node) ( ((Node)(  ((Node) kittenNode).getChild(0) )).getChild(0) )).getChild(0))).getControl(AnimControl.class);
        for (String anim : controlAnim.getAnimationNames()) {
            System.out.println(anim);
        }
        channelAnim = controlAnim.createChannel();
        
     shadowNode.attachChild(kittenNode);
 }

}
