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
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Axis;

import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.component.BorderLayout;
import com.simsilica.lemur.component.BoxLayout;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.core.GuiLayout;
import processing.AllParams;


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
    private Container optWindow;
    
    private com.simsilica.lemur.Label fieldDim1Text;
    private com.simsilica.lemur.Button dbutton1;
    private com.simsilica.lemur.Button ubutton1;
    private com.simsilica.lemur.Label fieldDim2Text;
    private com.simsilica.lemur.Button dbutton2;
    private com.simsilica.lemur.Button ubutton2;
    private com.simsilica.lemur.Label fieldItmNumbers;
    private com.simsilica.lemur.Button dbutton3;
    private com.simsilica.lemur.Button ubutton3;
    private com.simsilica.lemur.Button closeOptionsBtn;
    private com.simsilica.lemur.Button saveOptionsBtn;
    
    private Button continueBtn;
    private boolean continueBtnRequired;
    
    //backend of AllParams values
    private Double rawDim1Back;
    private Double rawDim2Back;
    private Integer rawItmNumberBack;
    
    public Boolean justInitialized;
    
    public Boolean optionsWndDisplayed;
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
        optionsWndDisplayed = false;
        int winResolutionHeight = app.getCamera().getHeight();
        int winResolutionWidth = app.getCamera().getWidth();
        float topPercentPosition=0.75f;
        float leftPercentPosition=0.10f;
        int topLocation = Math.round(topPercentPosition*winResolutionHeight);
        int leftLocation = Math.round(leftPercentPosition*winResolutionWidth);
        
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
                if (optionsWndDisplayed == false) {
                    ((SimpleApplication) app).getGuiNode().attachChild(optWindow);
                    optionsWndDisplayed = true;
                } else {
                    optWindow.removeFromParent();
                    optionsWndDisplayed = false;
                }
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
        
        myWindow.setLocalTranslation(leftLocation, topLocation, 0);
        
        int topLocationOptWindow = topLocation;
        int leftLocationOptWindow = Math.round(0.50f*winResolutionWidth);
        this.optWindow = new Container();
        //see https://hub.jmonkeyengine.org/t/lemur-box-layout-does-not-render-text-field-in-a-propper-way/38392/2
        optWindow.setLocalTranslation(leftLocationOptWindow, topLocationOptWindow, 0);
        optWindow.addChild(new Label("Options"));
        com.simsilica.lemur.Label fieldDim1Label = new com.simsilica.lemur.Label("Field Dim 1");
        fieldDim1Text = new com.simsilica.lemur.Label("");
        optWindow.addChild(fieldDim1Label);
        //number means a column in current row of gridlayout. addCHild call w/o number means start of new row.
        optWindow.addChild(fieldDim1Text, 1);
        //not working
        fieldDim1Text.setSize(new Vector3f(30f, fieldDim1Text.getSize().y, fieldDim1Text.getSize().z));
        ubutton1 = new com.simsilica.lemur.Button("");
        ubutton1.setIcon(new IconComponent("Textures/ubutton.gif"));
        ubutton1.addCommands(Button.ButtonAction.Down, new Command<Button>(){
            @Override
            public void execute(Button source) {
                //processing.AllParams.allFieldDim1 = (processing.AllParams.allFieldDim1/2.0+1.0)*2.0;
                Double rawDim1BackCopy = rawDim1Back;
                rawDim1Back = (rawDim1Back/2.0+1.0)*2.0;
                if (RenderHelpers.checkParamsConsistency(rawDim1Back, rawDim2Back, rawItmNumberBack) == false) {
                    rawDim1Back = rawDim1BackCopy;
                    return;
                }
                updateAllFieldDim1Fld();
            }            
        });
        dbutton1 = new com.simsilica.lemur.Button("");
        dbutton1.setIcon(new IconComponent("Textures/dbutton.gif"));
        dbutton1.addCommands(Button.ButtonAction.Down, new Command<Button>(){
            @Override
            public void execute(Button source) {
                //processing.AllParams.allFieldDim1 = (processing.AllParams.allFieldDim1/2.0-1.0)*2.0;
                Double rawDim1BackCopy = rawDim1Back;
                rawDim1Back = (rawDim1Back/2.0-1.0)*2.0;
                if (RenderHelpers.checkParamsConsistency(rawDim1Back, rawDim2Back, rawItmNumberBack) == false) {
                    rawDim1Back = rawDim1BackCopy;
                    return;
                }
                updateAllFieldDim1Fld();
            }            
        });
        
        optWindow.addChild(ubutton1, 2);
        optWindow.addChild(dbutton1, 3);
        
        com.simsilica.lemur.Label fieldDim2Label = new com.simsilica.lemur.Label("Field Dim 2");
        fieldDim2Text = new com.simsilica.lemur.Label("");
        optWindow.addChild(fieldDim2Label);
        optWindow.addChild(fieldDim2Text,1);
        //not working
        fieldDim2Text.setSize(new Vector3f(30f, fieldDim2Text.getSize().y, fieldDim2Text.getSize().z));
        ubutton2 = new com.simsilica.lemur.Button("");
        ubutton2.setIcon(new IconComponent("Textures/ubutton.gif"));
        ubutton2.addCommands(Button.ButtonAction.Down, new Command<Button>(){
            @Override
            public void execute(Button source) {
                Double rawDim2BackCopy = rawDim2Back;
                rawDim2Back = (rawDim2Back/2.0+1.0)*2.0;
                if (RenderHelpers.checkParamsConsistency(rawDim1Back, rawDim2Back, rawItmNumberBack) == false) {
                    rawDim2Back = rawDim2BackCopy;
                    return;
                }
                updateAllFieldDim2Fld();
            }            
        });
        dbutton2 = new com.simsilica.lemur.Button("");
        dbutton2.setIcon(new IconComponent("Textures/dbutton.gif"));
        dbutton2.addCommands(Button.ButtonAction.Down, new Command<Button>(){
            @Override
            public void execute(Button source) {
                Double rawDim2BackCopy = rawDim2Back;
                rawDim2Back = (rawDim2Back/2.0-1.0)*2.0;
                if (RenderHelpers.checkParamsConsistency(rawDim1Back, rawDim2Back, rawItmNumberBack) == false) {
                    rawDim2Back = rawDim2BackCopy;
                    return;
                }
                updateAllFieldDim2Fld();
            }            
        });
        optWindow.addChild(ubutton2, 2);
        optWindow.addChild(dbutton2, 3);
        
        com.simsilica.lemur.Label fieldItmNumberText = new com.simsilica.lemur.Label("Items:");
        fieldItmNumbers = new com.simsilica.lemur.Label("");
        optWindow.addChild(fieldItmNumberText);
        optWindow.addChild(fieldItmNumbers,1);
        ubutton3 = new com.simsilica.lemur.Button("");
        ubutton3.setIcon(new IconComponent("Textures/ubutton.gif"));
        ubutton3.addCommands(Button.ButtonAction.Down, new Command<Button>(){
            @Override
            public void execute(Button source) {
                //processing.AllParams.totalNumberOfItems = processing.AllParams.totalNumberOfItems+1;
                if (RenderHelpers.checkParamsConsistency(rawDim1Back, rawDim2Back, rawItmNumberBack+1) == false) {
                    return;
                }
                rawItmNumberBack+=1;
                updateItmNumberFld();
            }            
        });
        dbutton3 = new com.simsilica.lemur.Button("");
        dbutton3.setIcon(new IconComponent("Textures/dbutton.gif"));
        dbutton3.addCommands(Button.ButtonAction.Down, new Command<Button>(){
            @Override
            public void execute(Button source) {
                //processing.AllParams.totalNumberOfItems = processing.AllParams.totalNumberOfItems-1;
                if (RenderHelpers.checkParamsConsistency(rawDim1Back, rawDim2Back, rawItmNumberBack-1) == false) {
                    return;
                }
                rawItmNumberBack-=1;
                updateItmNumberFld();
            }            
        });
        optWindow.addChild(ubutton3, 2);
        optWindow.addChild(dbutton3, 3);
        
        closeOptionsBtn = new com.simsilica.lemur.Button("Close");
        closeOptionsBtn.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                  optWindow.removeFromParent();
                  optionsWndDisplayed = false;
            }
            
        });
        saveOptionsBtn = new com.simsilica.lemur.Button("Save");
        
        closeOptionsBtn.setPreferredSize(new Vector3f(70f, closeOptionsBtn.getPreferredSize().y, closeOptionsBtn.getPreferredSize().z));
        saveOptionsBtn.setPreferredSize(new Vector3f(70f, saveOptionsBtn.getPreferredSize().y, saveOptionsBtn.getPreferredSize().z));
        optWindow.addChild(closeOptionsBtn);
        optWindow.addChild(saveOptionsBtn,1);
        
        
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
            rawDim1Back = AllParams.allFieldDim1;
            rawDim2Back = AllParams.allFieldDim2;
            rawItmNumberBack = AllParams.totalNumberOfItems;
            
            updateAllFieldDim1Fld();
            updateAllFieldDim2Fld();
            updateItmNumberFld();
            
            gui.attachChild(myWindow);
            GuiGlobals.getInstance().requestFocus(myWindow);
            
            
        } else {
            // take away everything not needed while this state is PAUSED
            if (justInitialized == false) {
                myWindow.removeFromParent(); 
                if (optionsWndDisplayed) {
                    optWindow.removeFromParent();
                    optionsWndDisplayed = false;
                }
                System.out.println("------Remove menu------");
            
            }
        }
    }
    /**
     * activate display field for dim1
     */
    private void updateAllFieldDim1Fld() {
        //fieldDim1Text.setText( ( new Integer ( ((Double) (AllParams.allFieldDim1/2.0)).intValue() ) ).toString() );
        fieldDim1Text.setText( ( new Integer ( ((Double) (rawDim1Back/2.0)).intValue() ) ).toString() );
    }
    private void updateAllFieldDim2Fld() {
        //fieldDim2Text.setText( ( new Integer ( ((Double) (AllParams.allFieldDim2/2.0)).intValue() ) ).toString() );
        fieldDim2Text.setText( ( new Integer ( ((Double) (rawDim2Back/2.0)).intValue() ) ).toString() );
    }
    private void updateItmNumberFld() {
        fieldItmNumbers.setText(rawItmNumberBack.toString());
    }
}
