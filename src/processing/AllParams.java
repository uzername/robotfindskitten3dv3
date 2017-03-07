package processing;

import java.util.ArrayList;

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
}
