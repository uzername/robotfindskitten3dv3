/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.RenderState;
import com.jme3.math.Quaternion;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;

import java.util.UUID;
import processing.AllParams;
import processing.GameFieldItem;

/**
 *
 * @author Ivan
 */
public class RenderHelpers {
    public static AssetManager assetManager;
    /**
     * Generate cube, representing Game Item
     * @return final node, ready to be attached to scene graph
     */
    public static Node PreparedModel() {
            Random stringRandomizer = new Random();
        Node nodeReturn = new Node("ItemNode"+UUID.randomUUID().toString());
        Box b = new Box(0.95f, 0.250f, 0.95f);
        Geometry low = new Geometry("red cube", b);
        low.setLocalTranslation(0, 0.250f, 0);
        Material basemat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        basemat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/floor2.jpg"));
        low.setMaterial(basemat);
        Node lowNode = new Node("LowNode");
        lowNode.attachChild(low);
        nodeReturn.attachChild(lowNode);
        /*
        BitmapFont guiFont = assetManager.loadFont("Interface/verdanafont/SmapleBitmap2.fnt");
        BitmapText helloText = new BitmapText(guiFont, false);
        helloText.setSize(3);
        helloText.setText("A");
        helloText.setLocalTranslation(0, helloText.getLineHeight(), 0);        
        */
        //let's use some odd hints to render BufferedImage with transparency. Used to display the symbol
        /*choose color and symbol*/
            Random colorRandomizer = new Random();
        String randLine = "A";
        randLine = processing.AllParams.allSymbols.get(processing.NewClass.showRandomInteger(0, processing.AllParams.allSymbols.size()-1, stringRandomizer));
        java.awt.Color randColor = processing.AllParams.allColorNames.get(processing.NewClass.showRandomInteger(0, processing.AllParams.allColorNames.size()-1, colorRandomizer));
        /*==========*/
        AWTLoader loader = new AWTLoader();
        BufferedImage bufferedImage = new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
        g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, 99, 99);
        g.setComposite(AlphaComposite.Src);
        g.setColor(randColor);
        g.setFont(new java.awt.Font(java.awt.Font.MONOSPACED, java.awt.Font.BOLD, 80)); 
        g.drawString(randLine, 25,65);
        Image load = loader.load(bufferedImage, true);
        Texture texture = new Texture2D(load);
         Material matText = (assetManager.loadMaterial("Materials/plainMaterial.j3m"));
         //Material matText = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
         matText.setTexture("DiffuseMap", texture);
         matText.setTexture("SpecularMap", texture);
         //matText.setTexture("NormalMap", texture);
         float[] retColorCodes = randColor.getRGBColorComponents(null);
         matText.setColor("Ambient", new ColorRGBA(retColorCodes[0],retColorCodes[1],retColorCodes[2],1.0f) );
         //matText.setColor("Specular", ColorRGBA.Red);
         
         //System.out.println(Arrays.toString(retColorCodes));
         matText.setColor("Diffuse",new ColorRGBA(retColorCodes[0],retColorCodes[1],retColorCodes[2],1.0f) );
         
         matText.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
         matText.setTransparent(true);
         Quad plain = new Quad(2, 4);
        Geometry planeLetterGeometry = new Geometry("letterGeometry",plain);
        planeLetterGeometry.setQueueBucket(Bucket.Transparent);
        planeLetterGeometry.setMaterial(matText);
        //planeLetterGeometry.setMaterial(basemat);
        
        Geometry planeLetterGeometry2 = new Geometry("letterGeometry2",plain);
        planeLetterGeometry2.setMaterial(matText);
        
        Geometry planeLetterGeometry3 = new Geometry("letterGeometry3",plain);
        planeLetterGeometry3.setMaterial(matText);
        
        Geometry planeLetterGeometry4 = new Geometry("letterGeometry4",plain);
        planeLetterGeometry4.setMaterial(matText);
        
        Node helloText = new Node("DrawText");
        helloText.attachChild(planeLetterGeometry);
        helloText.attachChild(planeLetterGeometry2);
        helloText.attachChild(planeLetterGeometry3);
        helloText.attachChild(planeLetterGeometry4);
        
        planeLetterGeometry.setLocalTranslation(-1, 0.05f, 0.95f);
        
        planeLetterGeometry2.setLocalTranslation(-1, 0.05f, -0.95f);
        float[] ro2={0,(float)(-Math.PI/2.0),0};
        planeLetterGeometry2.rotate(new Quaternion(ro2));
        
        planeLetterGeometry3.setLocalTranslation(1, 0.05f, -0.95f);
        float[] ro3={0,(float)(Math.PI),0};
        planeLetterGeometry3.rotate(new Quaternion(ro3));
        
        planeLetterGeometry4.setLocalTranslation(1, 0.05f, 0.95f);
        float[] ro4={0,(float)(Math.PI/2.0f),0};
        planeLetterGeometry4.rotate(new Quaternion(ro4));
        
        nodeReturn.attachChild(helloText);
        //now drawing the main column
        Box column = new Box(0.65f, 2.0f, 0.65f);
        Geometry columnscreen = new Geometry("screen", column);
        Material columnmat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        columnmat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/ScreenFinal2Small.png"));
        columnscreen.setMaterial(columnmat);
        Node colNode = new Node("colNode");
        columnscreen.setLocalTranslation(0, 2.5f, 0);
        colNode.attachChild(columnscreen);
        nodeReturn.attachChild(colNode);
        
        Box b2 = new Box(0.95f, 0.250f, 0.95f);
        Geometry up = new Geometry("upper cube", b2);
        up.setMaterial(basemat);
        up.setLocalTranslation(0, 4.5f, 0);
        Node upNode = new Node("UpNode");
        upNode.attachChild(up);
        nodeReturn.attachChild(upNode);
        
        return nodeReturn;
    }
    /**
     * look through GameLogicArray and retrieve 'message' field which corresponds to collisionID
     * @param Id
     * @return 
     */
    public static String getItemDescriptionById(String Id) {
        String itmLine="";
        for (GameFieldItem object : AllParams.GameLogicArray) {
            if (object.collisionID.equals(Id)) {return object.message; }
        }
        return itmLine;
    }
}
