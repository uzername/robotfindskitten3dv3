package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
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
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;
import java.util.logging.Level;
import java.util.logging.Logger;
import processing.AllParams;
import processing.NewClass;
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
     public NewClass mainGameAlgorithmRoutine;
     public MainGameAppState state;
     public MenuGameAppState state2;
     public FindingKittenGameAppState stateKitten;
     
     public Boolean FPSdisplayed;
     //external appstates may request appstate change, but switching states is performed only here
     //see line 111 of MenuGameAppstate. 
     public Boolean requestSwitchToNewGame=false;
     public Boolean escKeyAllowed = false; //is it ok to switch app states by esc key while we are in main menu?
                                           //also forbid appstate changing from main menu
     //final scene animation parameters
     private float rfk_heartAppearingDistance = 0.1f;
     private float rfk_robotDestinationZ = -0.5f;
    public static void main(String[] args) {
        Main app = new Main();
        
        com.jme3.system.AppSettings newSettings = new com.jme3.system.AppSettings(true);
        newSettings.setSettingsDialogImage("Textures/CoverFinal.png");
        app.setSettings(newSettings);
        
        app.start();
        
    }

    @Override
    public void simpleInitApp() {
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));
        
        //one cannot simply exit the app using the esc key. Instead you should display some sort of menu...
        //do not permit the user to exit app using alt+f4
        //switch FPS display by pressing F1. should work in any app state
        
        //initialize and precalculate all the resources here
        mainGameAlgorithmRoutine = new NewClass();
        mainGameAlgorithmRoutine.initNKIlistFromFile("");
        mainGameAlgorithmRoutine.fillNKIField(AllParams.totalNumberOfItems);
        
        System.out.println("GameLogicArray="+AllParams.GameLogicArray.toString());
        
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
        
        stateKitten = new FindingKittenGameAppState();
        stateKitten.initialize(stateManager, this);
        //stateManager.attach(state2);
        state.setEnabled(false);
        state2.setEnabled(true);
        stateKitten.setEnabled(false); stateKitten.justInitialized = false;
        //state2.justInitialized = true;
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
        if (stateKitten.isEnabled()) {
            inputManager.setCursorVisible(false);            
            //scale heart at some point
            if (stateKitten.robotNode.getWorldTranslation().z<=rfk_heartAppearingDistance) {
                //in case when 'heart' object is already shown do nothing
                stateKitten.showHeart();
                stateKitten.showRfkText();
                
            }
            //move robot node towards the kitten
            if (stateKitten.robotNode.getWorldTranslation().z>=rfk_robotDestinationZ) {
                stateKitten.robotNode.move(0.0f, 0.0f, -0.0004f); 
            }
            if (stateKitten.myCam.getLocalTranslation().z<=-7.5f) {
                stateKitten.myCam.move(0.0f, 0.0f, +0.0001f);
                //stateKitten.myCam.lookAt(stateKitten.kittenNode.getLocalTranslation(), Vector3f.ZERO);
            }
            
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
    //key binding processor. Seems like key bingings are global and they are shared among all the appstates
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("switchMenuStates") && isPressed ) {
            Boolean findingKittenStateActivity = stateKitten.isEnabled();
            Boolean mainStateActivity = state.isEnabled();
            Boolean menuStateActivity = state2.isEnabled();
            if (findingKittenStateActivity == false) {
                if (escKeyAllowed) {
                    state.setEnabled(!(mainStateActivity.booleanValue()) );
                    state2.setEnabled(!(menuStateActivity.booleanValue()) );
                    System.out.println("State Changed::no kitty"); 
                } else {
                    System.out.println("State NOT Changed::kitty");
                }
            } else {
                stateKitten.setEnabled(false);
                state2.setEnabled(true);
            }
        }
        if (name.equals("switchFPSDisplay")&&isPressed) {
            FPSdisplayed = !(FPSdisplayed);
            this.setDisplayFps(FPSdisplayed);
            this.setDisplayStatView(FPSdisplayed);
        }
    }
    //called from MenuGameAppState
    public void switchStateToMainGame() {
            if (escKeyAllowed == false) { 
                System.out.println( "State NOT Changed::kitty::(menu action)" );
                return; 
            
            }
            Boolean findingKittenStateActivity = stateKitten.isEnabled();
            Boolean mainStateActivity = state.isEnabled();
            Boolean menuStateActivity = state2.isEnabled();
            if (findingKittenStateActivity == false) { //kitten has not been found and displayed yet, switch menu
            state.setEnabled(!(mainStateActivity.booleanValue()) );
            state2.setEnabled(!(menuStateActivity.booleanValue()) );
            } else { //kitten has been found, show menu on ESC. It is assumed that main state is disabled
                
                stateKitten.setEnabled(false);
                state2.setEnabled(true);
            }
            System.out.println("State Changed");
            
    }
    
    public void activateKittenState() {
        this.escKeyAllowed = false;
        state.setEnabled(false);
        //state2.setEnabled(false);
        stateKitten.resetScenePositions();
        stateKitten.setEnabled(true);
        
    }
    
    public void startOverMainGame() {
        this.escKeyAllowed = true;
        System.out.println("Starting game from beginning");
        //clean up all of the collision shapes
        //remove data from structures
        applyNewParam();
        //and start everything from very beginning
    }
    public void applyNewParam() {
        System.out.println("...Applying parameters...");
        
         try {
             state.removeFloor();
         } catch (Exception ex) {
             Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
         }
        state.attachFloor();
        state.removeObjects();
        mainGameAlgorithmRoutine.fillNKIField(AllParams.totalNumberOfItems);
        System.out.println("GameLogicArray(after reinit)="+AllParams.GameLogicArray.toString());
        state.placeObjects();
        switchStateToMainGame();
    }


}
