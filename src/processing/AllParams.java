package processing;

import java.util.ArrayList;
import java.util.Arrays;

public class AllParams {
    //divide these constants by 2 to get the real dimension of playfield in tiles
    public static Double allFieldDim1 = 15.0*2.0;
    public static Double allFieldDim2 = 15.0*2.0;
    /**
     * total number of entities on field. The kitten is included here too.
     */
    public static Integer totalNumberOfItems = 10;
    public static String DataFilePath="itmbase2.xml";
    public static ArrayList<GameFieldItem> GameLogicArray;
    public static ArrayList<String> LoadedNKIs;
    //assume that all non-kitten items should be different. 
    //'true' means that there should not be a single NKI which is identified by different models on screen
    //in original RFK two entities with the same symbol and color may have different meaning!
    public static Boolean allDifferentNKI = true;
    public static ArrayList<String> allSymbols = new ArrayList<>(
        Arrays.asList("\"", "#", "%", "&", "\'", "(", ")", "*", "+", ",", "-", ".", "/", 
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ":", ";", "<", "=", ">", "?", "@", 
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", 
                "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "[", "\\", "]", "^", "_", "`", 
                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", 
                "r", "s", "t", "u", "v", "w", "x", "y", "z", "{", "|", "}", "~")
    );
    public static ArrayList<java.awt.Color> allColorNames = new ArrayList<>(
     Arrays.asList(
        java.awt.Color.RED, java.awt.Color.GREEN, java.awt.Color.BLUE, java.awt.Color.CYAN, 
        java.awt.Color.MAGENTA, java.awt.Color.YELLOW, 
        new java.awt.Color(128,0,128), new java.awt.Color(0,0,128), new java.awt.Color(128,0,0), 
        new java.awt.Color(255,192,203), new java.awt.Color(255,150,0),new java.awt.Color(0, 255, 0), 
        new java.awt.Color(169,169,169))
    );
    
    
}
