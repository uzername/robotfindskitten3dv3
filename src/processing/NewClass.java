package processing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * This class is used to fill the array with NKI
 * @author Ivan
 */
public class NewClass {
    private final String ROOT="root";
    private final String ITEM="item";
    /**
     * Load list of NKI from xml file
     * @param filePath
     */
    public void initNKIlistFromFile(String filePath) {
        
        String pathToLoad = filePath;
       if ("".equals(filePath)) {
           pathToLoad = AllParams.DataFilePath;
       }
       AllParams.LoadedNKIs = new ArrayList<String>();
       try {
           // First, create a new XMLInputFactory
           XMLInputFactory inputFactory = XMLInputFactory.newInstance();
           inputFactory.setProperty("javax.xml.stream.isCoalescing", true);
           // Setup a new eventReader
           InputStream in = new FileInputStream(pathToLoad);
           XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
           String item2append = null;
           Boolean itemFound = false;
           while (eventReader.hasNext()) { 
               XMLEvent event = eventReader.nextEvent();
               switch(event.getEventType()){
                   case XMLStreamConstants.START_ELEMENT:
                       StartElement startElement = event.asStartElement();
                       String qName = startElement.getName().getLocalPart();
                       if (qName.equalsIgnoreCase(ITEM))
                           itemFound = true;
                       break;
                   case XMLStreamConstants.CHARACTERS:
                       Characters characters = event.asCharacters();
                       if (itemFound) {
                           item2append = characters.getData();
                           //System.out.println(item2append);
                           AllParams.LoadedNKIs.add(item2append);
                           itemFound = false;
                       }
                       break;
                   case  XMLStreamConstants.END_ELEMENT:
                       
                       break;
               }
           }
        } catch (FileNotFoundException e) {
                        e.printStackTrace();
        } catch (XMLStreamException e) {
                        e.printStackTrace();
        }
    }
    
    /**
     * Randomly fill the GameLogicArray with values. 
     * @param inp_totalNumberOfItems - total number of entities on field. The kitten is included here too.
     */
    public void fillNKIField(Integer inp_totalNumberOfItems) {
        Random msgRandom = new Random();
        AllParams.GameLogicArray = new ArrayList<>();
        //get total size of field (dimensions)
        Integer FieldX = ((Double)(AllParams.allFieldDim1 / 1.0)).intValue();
        Integer FieldY = ((Double)(AllParams.allFieldDim1 / 1.0)).intValue();
        
        //fill the 2 arrays of available positions
        ArrayList<Integer> AvailPositionsX = new ArrayList<Integer>();
        /* surprisingly, but we need the odd numbers here */
        for (int i=0; i<FieldX; i++) { if (i % 2 != 0) { AvailPositionsX.add(i); } }
        ArrayList<Integer> AvailPositionsY = new ArrayList<Integer>();
        for (int i=0; i<FieldY; i++) { if (i % 2 != 0) AvailPositionsY.add(i); }
        //choose items from arrays (in random manner), create item in list and remove numbers from array
        Random aRandom = new Random();
        ArrayList<String> OperativeNKI_NamesList = new ArrayList<>(AllParams.LoadedNKIs);
        for (int i=0; i<inp_totalNumberOfItems-1; i++) {
            //choose name from NKI item list. Should be unique
            int randmsgIndex= showRandomInteger(0, OperativeNKI_NamesList.size()-1, aRandom);
            String NKI_name = OperativeNKI_NamesList.get( randmsgIndex );
            OperativeNKI_NamesList.remove(randmsgIndex);
            
            Integer choosenXIndex = showRandomInteger(0, AvailPositionsX.size()-1, aRandom);
            Integer choosenYIndex = showRandomInteger(0, AvailPositionsY.size()-1, aRandom);
            Integer choosenXPosition = AvailPositionsX.get(choosenXIndex);
            Integer choosenYPosition = AvailPositionsY.get(choosenYIndex);
            AllParams.GameLogicArray.add( new NKI(choosenXPosition, choosenYPosition, NKI_name) );
            AvailPositionsX.remove(choosenXIndex.intValue());
            AvailPositionsY.remove(choosenYIndex.intValue());
        }
            Integer choosenXIndex = showRandomInteger(0, AvailPositionsX.size()-1, aRandom);
            Integer choosenYIndex = showRandomInteger(0, AvailPositionsY.size()-1, aRandom);
            Integer choosenXPosition = AvailPositionsX.get(choosenXIndex);
            Integer choosenYPosition = AvailPositionsY.get(choosenYIndex);
            AllParams.GameLogicArray.add( new KI(choosenXPosition, choosenYPosition, "You have found kitten!") );
            AvailPositionsX.remove(choosenXPosition);
            AvailPositionsY.remove(choosenYPosition);
    }
    /* see http://www.javapractices.com/topic/TopicAction.do?Id=62 */
    public static int showRandomInteger(int aStart, int aEnd, Random aRandom){
        if (aStart > aEnd) {
          throw new IllegalArgumentException("Start cannot exceed End.");
        }
        //get the range, casting to long to avoid overflow problems
        long range = (long)aEnd - (long)aStart + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long)(range * aRandom.nextDouble());
        int randomNumber =  (int)(fraction + aStart);    
        return randomNumber;
    }
    
}
