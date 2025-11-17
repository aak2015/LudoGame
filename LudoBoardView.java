import java.awt.*;
import javax.swing.*;

public class LudoBoardView extends JPanel{
    private static final int BOARD_SIZE = 15;
    private BoardSquare[][] squares = new BoardSquare[BOARD_SIZE][BOARD_SIZE];

    public LudoBoardView(){
        setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE,1,1));
        buildBoard();
        
    }

    public BoardSquare[][] getSquares(){
        return squares;
    }

    private void buildBoard(){
        for(int row = 0; row < BOARD_SIZE; row++){
            for(int column = 0; column < BOARD_SIZE; column++){
                BoardSquare square = createSquare(row, column);
                squares[row][column] = square;
                add(square);
            }
        }

    }



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
            
            if (column == 7 && row == 0) {
                classification = BoardSquare.SquareClassification.SAFE;
                
            }else{
                classification = BoardSquare.SquareClassification.HOME_PATH;
            }
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

        return new BoardSquare(color, classification, row, column);       
    }




    public static void main(String[] args) {
        JFrame boardFrame = new JFrame("Ludo Board Test");
        boardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        boardFrame.setSize(800, 800);
        LudoBoardView testView = new LudoBoardView();


        boardFrame.add(testView);

        GameLogic logic = new GameLogic(testView.getSquares());
        logic.initializePlayerTokens(Color.RED);
        logic.initializePlayerTokens(Color.YELLOW);
        logic.initializePlayerTokens(Color.GREEN);
        logic.initializePlayerTokens(Color.BLUE);
        boardFrame.setVisible(true);

    }

}