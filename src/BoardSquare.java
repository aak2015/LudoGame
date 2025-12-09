import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Represents a square on the Ludo board.
 * @author Aakash Krishnan
 * @version 1.0
 * Last Modified: December 8, 2025
 */
public class BoardSquare extends JPanel{

    /**
     * Enumeration for classifying different types of board squares.
     */
    public enum SquareClassification{
        NORMAL,
        SAFE,
        HOME_PATH,
        HOME_BASE,
        CENTER,
        TOKEN_SPAWN
        
    }

    //Attributes for the BoardSquare
    private final Color squareColor;
    private final SquareClassification squareClassification; 
    private ArrayList<PlayerToken> tokens = new ArrayList<>(); //Tokens in a square.
    private boolean isBlocked;
    private int row; 
    private int column;

    /**
     * Gets the classification of the square.
     * @return The square classification.
     */
    public SquareClassification getSquareClassification() {
        return squareClassification;
    }


    /**
     * Gets the color of the square.
     * @return The square color.
     */
    public Color getSquareColor(){
        return squareColor;
    }

    /**
     * Constructs a BoardSquare with specified color, classification, row, and column.
     * @param color The color of the square.
     * @param squareClassification The classification of the square.
     * @param row The row position of the square.
     * @param column The column position of the square.
     */
    public BoardSquare(Color color, SquareClassification squareClassification, int row, int column){
        this.squareColor = color;
        this.squareClassification = squareClassification;
        this.row = row;
        this.column = column;

        setPreferredSize(new Dimension(40,40));
        setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));

        if(squareClassification == SquareClassification.HOME_BASE || squareClassification == SquareClassification.HOME_PATH){
            setBackground(color.darker());
        }else if(squareClassification == SquareClassification.SAFE){
            setBackground(Color.LIGHT_GRAY);
            
        }else if(squareClassification == SquareClassification.CENTER){
            setBackground(Color.CYAN);
        }else if(squareClassification == SquareClassification.TOKEN_SPAWN){
            setBackground(color.brighter().brighter());
            
        }else{
            setBackground(Color.WHITE);
        }
    }

    /**
     * Places a player token on this board square.
     * @param token The player token to place.
     */
    public void placePlayerToken(PlayerToken token){
        
        tokens.add(token);
        token.setCurrentBoardSquare(this);
        if(this.getSquareClassification() == SquareClassification.TOKEN_SPAWN){
            token.setSpawnSquare(this);
        }

        removeAll();
        
        for(PlayerToken piece : tokens){
            add(piece);
        }

        repaint();
    }

    /**
     * Removes a player token from this board square.
     * @param token The player token to remove.
     */
    public void removeToken(PlayerToken token){
        tokens.remove(token);
        remove(token);
        repaint();
    }

    /**
     * Checks if the square is blocked.
     * @return true if the square is blocked, false otherwise.
     */
    public boolean isBlocked(){
       return this.isBlocked;
    }

    /**
     * Sets the blocked status of the square.
     * @param blocked true to block the square, false to unblock.
     */
    public void setBlocked(boolean blocked){
        this.isBlocked = blocked;
    }

    /**
     * Gets the row of the square.
     * @return The row index.
     */
    public int getRow(){
        return row;
    }

    /**
     * Gets the column of the square.
     * @return The column index.
     */
    public int getColumn(){
        return column;
    }

    /**
     * Gets the list of player tokens on this square.
     * @return The list of player tokens.
     */
    public ArrayList<PlayerToken> getTokens(){
        return this.tokens;
    }

    /**
     * Checks equality based on row and column.
     * @param obj The object to compare.
     * @return true if equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(!(obj instanceof BoardSquare)) return false;
        BoardSquare other = (BoardSquare) obj;
        return this.row == other.row && this.column == other.column;
    }

    /**
     * Generates a hash code based on row and column.
     * @return The hash code.
     */
    @Override
    public int hashCode(){
        return 31 * row + column;
    }

    /**
     * Returns a string representation of the BoardSquare.
     * @return The string representation.
     */
    @Override
    public String toString(){
        return "BoardSquare[row=" + row + ", column=" + column + ", classification=" + squareClassification + "]";
    }
}
