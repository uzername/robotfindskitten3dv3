package processing;

/**
 * The base class for item on game field. Would be OK to use the 
 * @author Ivan
 */
public class GameFieldItem {
    public Integer fieldXPosition;
    public Integer fieldYPosition;
    public String message;
    public GameFieldItem(Integer i_fieldXPosition, Integer i_fieldYPosition, String i_message) {
        this.fieldXPosition = i_fieldXPosition;
        this.fieldYPosition = i_fieldYPosition;
        this.message = i_message;
    }
    @Override
    public String toString() {
        return "["+fieldXPosition.toString()+" , "+fieldYPosition.toString()+" , \""+message+"\"]";
    }
}
