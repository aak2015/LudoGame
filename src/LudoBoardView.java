import java.awt.*;
import javax.swing.*;

/**
 * Represents the Ludo board view with all its squares and status information.
 * @author Aakash Krishnan
 * @version 1.0
 * Last Modified: December 8, 2025
 */
public class LudoBoardView extends JPanel{
    //Attributes
    private static final int BOARD_SIZE = 15;
    private BoardSquare[][] squares = new BoardSquare[BOARD_SIZE][BOARD_SIZE];
    private GameStatusInformation statusInfo;

    /**
     * Constructs the LudoBoardView with a grid of BoardSquares and status information.
     */
    public LudoBoardView(){
        setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE,1,1));
        statusInfo = new GameStatusInformation();
        buildBoard();
        
    }

    /**
     * Gets the 2D array of BoardSquares.
     * @return The 2D array of BoardSquares.
     */
    public BoardSquare[][] getSquares(){
        return squares;
    }

    //Private method to build the board by creating and adding BoardSquares.
    private void buildBoard(){
        for(int row = 0; row < BOARD_SIZE; row++){
            for(int column = 0; column < BOARD_SIZE; column++){
                BoardSquare square = createSquare(row, column);
                squares[row][column] = square;
                add(square);
            }
        }

    }


    //Private helper method to create a BoardSquare based on its position.
    private BoardSquare createSquare(int row, int column){
        Color color = Color.WHITE;
        BoardSquare.SquareClassification classification = BoardSquare.SquareClassification.NORMAL;

        // Define HOME_BASES
        if ((row < 6 && column < 6)) {
            color = Color.RED;
            if((row == 2 && (column == 2 || column == 3) || row == 3 && (column == 2 || column == 3))){
                classification = BoardSquare.SquareClassification.TOKEN_SPAWN;
            }
            else{
                classification = BoardSquare.SquareClassification.HOME_BASE;
            }
        } else if (row < 6 && column > 8) {
            color = Color.GREEN;

            if (row == 2 && (column == 11 || column == 12) || row == 3 && (column == 11 || column == 12)) {
                classification = BoardSquare.SquareClassification.TOKEN_SPAWN;
            }else{
                classification = BoardSquare.SquareClassification.HOME_BASE;
            }

        } else if (row > 8 && column < 6) {
            color = Color.YELLOW;
            if (row == 11 && (column == 2 || column == 3) || row == 12 && (column == 2 || column == 3)) {
                classification = BoardSquare.SquareClassification.TOKEN_SPAWN;
            }else{
                classification = BoardSquare.SquareClassification.HOME_BASE;
            }        
        } else if (row > 8 && column > 8) {
            color = Color.BLUE;
            if (row == 11 && (column == 11 || column == 12) || row == 12 && (column == 11 || column == 12)) {
                classification = BoardSquare.SquareClassification.TOKEN_SPAWN;
            }else{
                classification = BoardSquare.SquareClassification.HOME_BASE;
            }        
        }

        // Center 3x3 area
        else if (row >= 6 && row <= 8 && column >= 6 && column <= 8) {
            color = Color.LIGHT_GRAY;
            classification = BoardSquare.SquareClassification.CENTER;
        }

        // Red HOME_PATH (vertical, middle column)
        else if (column == 7 && row < 6) {
            color = Color.RED;
            classification = BoardSquare.SquareClassification.HOME_PATH;
            
        }

        // Green HOME_PATH (horizontal, middle row)
        else if (row == 7 && column > 8) {
            color = Color.GREEN;
            classification = BoardSquare.SquareClassification.HOME_PATH;
        }

        // Blue HOME_PATH (vertical, middle column)
        else if (column == 7 && row > 8) {
            color = Color.BLUE;
            classification = BoardSquare.SquareClassification.HOME_PATH;
        }

        // Yellow HOME_PATH (horizontal, middle row)
        else if (row == 7 && column < 6) {
            color = Color.YELLOW;
            classification = BoardSquare.SquareClassification.HOME_PATH;
        }

        //Reset squares to NORMAL for corners around the home.
        if((row == 6 && column == 6) || (row == 8 && column == 6) || (row == 6 && column == 8) || (row == 8 && column == 8)){
            classification = BoardSquare.SquareClassification.NORMAL;
        }

        //SAFE squares
        if((row == 1 && column == 6) || (row == 8 && column == 1) || (row == 13 && column == 8) || (row == 6 && column ==13) || (row == 2 && column == 8) || (row == 6 && column == 2) || (row == 12 && column == 6) || (row == 8 && column == 12)){
            classification = BoardSquare.SquareClassification.SAFE;
        }

        return new BoardSquare(color, classification, row, column);       
    }

    /**
     * Gets the GameStatusInformation panel.
     * @return The GameStatusInformation panel.
     */
    public GameStatusInformation getGameStatusInfo(){
        return statusInfo;
    }

}