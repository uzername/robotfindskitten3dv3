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
         this.myWindow = new Container();
         // Add some elements
        myWindow.addChild(new Label("Hello, World."));
        Button clickMe = myWindow.addChild(new Button("Click Me"));
        clickMe.addClickCommands(new Command<Button>() {
            @Override
            public void execute( Button source ) {
                System.out.println("The world is yours.");
            }
        });
        
        this.justInitialized = true;
    }
    @Override
    public void setEnabled(boolean enabled) {
        // Pause and unpause
        super.setEnabled(enabled);
        if(enabled){ 
            justInitialized = false;    
            // init stuff that is in use while this state is RUNNING
            System.out.println("Attach menu");
            Node gui = app.getGuiNode();
            myWindow.setLocalTranslation(300, 300, 0);
            gui.attachChild(myWindow);
            GuiGlobals.getInstance().requestFocus(myWindow);
        } else {
            // take away everything not needed while this state is PAUSED
            if (justInitialized == false) {
            myWindow.removeFromParent(); 
            }
        }
    }
    
}
