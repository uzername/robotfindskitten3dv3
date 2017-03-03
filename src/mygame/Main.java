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
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.scene.CameraNode;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;
/**
 * test
 * @author normenhansen and jovan
 */
public class Main extends SimpleApplication implements AnalogListener, ActionListener {
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
     public MainGameAppState state;
     public MenuGameAppState state2;
     public Boolean FPSdisplayed;
     
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        //one cannot simply exit the app using the esc key. Instead you should display some sort of menu...
        //do not permit the user to exit app using alt+f4
        //switch FPS display by pressing F1. should work in any app state
        FPSdisplayed=false;
        this.setDisplayFps(FPSdisplayed);
        this.setDisplayStatView(FPSdisplayed);
        
        inputManager.deleteMapping(INPUT_MAPPING_EXIT);
        registerInput();
        state = new MainGameAppState();
        state.initialize(stateManager, this);
        
        //state is for main game
        //state2 is for main menu
        //state3 is for options
        
        GuiGlobals.initialize(this);
 
        GuiGlobals globals = GuiGlobals.getInstance();
        BaseStyles.loadGlassStyle();
        globals.getStyles().setDefaultStyle("glass");
        
        //stateManager.attach(state);
        state2 = new MenuGameAppState();
        state2.initialize(stateManager, this);
        //stateManager.attach(state2);
        state.setEnabled(true);
        state2.setEnabled(false);
        state.justInitialized = false;
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
        if (state.isEnabled()) {
        inputManager.setCursorVisible(false); 
        } 
        if (state2.isEnabled()) {
        inputManager.setCursorVisible(true); 
        } 
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    public void registerInput() { 
        inputManager.addMapping("switchMenuStates", new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addMapping("switchFPSDisplay", new KeyTrigger(KeyInput.KEY_F1));
        
        inputManager.addListener(this, "switchMenuStates");
        inputManager.addListener(this, "switchFPSDisplay");
    }
    public void onAnalog(String name, float value, float tpf) {
        
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("switchMenuStates") && isPressed) {
            Boolean mainStateActivity = state.isEnabled();
            Boolean menuStateActivity = state2.isEnabled();
            state.setEnabled(!(mainStateActivity.booleanValue()) );
            state2.setEnabled(!(menuStateActivity.booleanValue()) );
            System.out.println("State Changed");
        }
        if (name.equals("switchFPSDisplay")&&isPressed) {
            FPSdisplayed = !(FPSdisplayed);
            this.setDisplayFps(FPSdisplayed);
            this.setDisplayStatView(FPSdisplayed);
        }
    }
    
    public void switchStateToMainGame() {
        
            Boolean mainStateActivity = state.isEnabled();
            Boolean menuStateActivity = state2.isEnabled();
            state.setEnabled(!(mainStateActivity.booleanValue()) );
            state2.setEnabled(!(menuStateActivity.booleanValue()) );
            System.out.println("State Changed");
        
    }

    
   


}
