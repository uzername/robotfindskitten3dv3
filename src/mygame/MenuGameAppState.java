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
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author Ivan
 */
public class MenuGameAppState extends AbstractAppState {
    public NiftyJmeDisplay niftyDisplay;
    private SimpleApplication app;
    private InputManager inputManager;
    private AssetManager assetManager;
    private ViewPort guiViewPort;
    
    
    @Override
    public void update(float tpf) {
    
    }
    @Override
    public void cleanup() {

    }
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.assetManager = app.getAssetManager();
        this.app = (SimpleApplication) app;
        this.inputManager = app.getInputManager();
        this.guiViewPort = app.getGuiViewPort();
        //not using nifty, but Lemur
         
    }
    @Override
    public void setEnabled(boolean enabled) {
        // Pause and unpause
        super.setEnabled(enabled);
        if(enabled){ 
            // init stuff that is in use while this state is RUNNING
            
        } else {
            // take away everything not needed while this state is PAUSED
            
        }
    }
    
}
