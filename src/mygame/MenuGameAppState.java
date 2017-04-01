/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;

import LemurProto.ActionButton;
import LemurProto.CallMethodAction;

import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;


/**
 *
 * @author Ivan
 */
public class MenuGameAppState extends AbstractAppState {
    private SimpleApplication app;
    private InputManager inputManager;
    private AssetManager assetManager;
    private ViewPort guiViewPort;
    
    private Container myWindow;
    private Button continueBtn;
    private boolean continueBtnRequired;
    
    
    public Boolean justInitialized;
    
    @Override
    public void update(float tpf) {
        this.inputManager.setCursorVisible(false);
    }
    @Override
    public void cleanup() {

    }
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        this.assetManager = app.getAssetManager();
        this.app = (SimpleApplication) app;
        this.inputManager = app.getInputManager();
        this.guiViewPort = app.getGuiViewPort();
        //not using nifty, but Lemur
        //Making Main Menu
         this.myWindow = new Container();
         // Add some elements
        myWindow.addChild(new Label("ROBOTFINDSKITTEN"));
        Button clickMe = myWindow.addChild(new Button("New Game"));
        clickMe.addClickCommands(new Command<Button>() {
            @Override
            public void execute( Button source ) {
                System.out.println("The world is yours.");
            }
        });
        
        Button optionsBtn = myWindow.addChild(new Button("Options"));
        optionsBtn.addClickCommands(new Command<Button>() {
            @Override
            public void execute( Button source ) {
                System.out.println("Options Button Clicked");
            }
        });
        Button quitBtn = myWindow.addChild(new ActionButton(new CallMethodAction("Exit Game", app, "stop")));
        /*
        quitBtn.addClickCommands(new Command<Button>() {
            @Override
            public void execute( Button source ) {
                System.out.println("Exit Button Clicked");
            }
        });
        */
        //surprisingly but resolution -- height and width of display is retrieved from camera properties defined 
        int winResolutionHeight = app.getCamera().getHeight();
        int winResolutionWidth = app.getCamera().getWidth();
        float topPercentPosition=0.75f;
        float leftPercentPosition=0.10f;
        int topLocation = Math.round(topPercentPosition*winResolutionHeight);
        int leftLocation = Math.round(leftPercentPosition*winResolutionWidth);
        myWindow.setLocalTranslation(leftLocation, topLocation, 0);
        
        this.continueBtnRequired = false;
        this.justInitialized = true;
    }
    @Override
    public void setEnabled(boolean enabled) {
        // Pause and unpause
        super.setEnabled(enabled);
        if(enabled){ 
            
            
                //append continue button, but only once. continue button is not shown on the first run of menu
                //it is being shown only on the second and further menu displayings
                //value justInitialized==true if not changed in outer routine
                if ((continueBtnRequired == true)) {
                    continueBtn = myWindow.addChild(new ActionButton(new CallMethodAction("Continue Game", app, "switchStateToMainGame")));
                    continueBtnRequired = false;
                }
            //this change change will take place on second run of setEnabled method. 
            //justInitialized == true only on the 1st run
            if (justInitialized == true) {
                continueBtnRequired = true;
            }
                
                
            justInitialized = false;    
            // init stuff that is in use while this state is RUNNING
            System.out.println("++++++Attach menu++++++");
            Node gui = app.getGuiNode();
            
            gui.attachChild(myWindow);
            GuiGlobals.getInstance().requestFocus(myWindow);
            
            
        } else {
            // take away everything not needed while this state is PAUSED
            if (justInitialized == false) {
                myWindow.removeFromParent(); 
                System.out.println("------Remove menu------");
            
            }
        }
    }
    
}
