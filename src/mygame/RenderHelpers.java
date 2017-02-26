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

import java.util.UUID;

/**
 *
 * @author Ivan
 */
public class RenderHelpers {
    public static AssetManager assetManager;
    /**
     * 
     * @return final node, ready to be attached to scene graph
     */
    public static Node PreparedModel() {
        Node nodeReturn = new Node("ItemNode"+UUID.randomUUID().toString());
        Box b = new Box(0.95f, 0.50f, 0.95f);
        Geometry low = new Geometry("red cube", b);
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
        AWTLoader loader = new AWTLoader();
        BufferedImage bufferedImage = new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
        g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, 99, 99);
        g.setComposite(AlphaComposite.Src);
        g.setColor(java.awt.Color.RED);
        g.setFont(new java.awt.Font(/*g.getFont().getName()*/java.awt.Font.MONOSPACED, java.awt.Font.BOLD, 80)); 
        g.drawString("A", 25,65);
        Image load = loader.load(bufferedImage, true);
        Texture texture = new Texture2D(load);
         Material matText = (assetManager.loadMaterial("Materials/plainMaterial.j3m"));
         matText.setTexture("DiffuseMap", texture);
         matText.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
         matText.setTransparent(true);
         Quad plain = new Quad(2, 4);
        Geometry planeLetterGeometry = new Geometry("letterGeometry",plain);
        planeLetterGeometry.setQueueBucket(Bucket.Transparent);
        planeLetterGeometry.setMaterial(matText);
        //planeLetterGeometry.setMaterial(basemat);
        Node helloText = new Node("DrawText");
        helloText.attachChild(planeLetterGeometry);
        planeLetterGeometry.setLocalTranslation(-1, 0, 0.95f);
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
}
