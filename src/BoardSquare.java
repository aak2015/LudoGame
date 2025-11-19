import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class BoardSquare extends JPanel{


    public enum SquareClassification{
        NORMAL,
        SAFE,
        HOME_PATH,
        HOME_BASE,
        CENTER,
        TOKEN_SPAWN
        
    }

    private final Color squareColor;
    private final SquareClassification squareClassification;
    private ArrayList<PlayerToken> tokens = new ArrayList<>();
    private boolean isBlocked;
    private int row;
    private int column;


    public SquareClassification getSquareClassification() {
        return squareClassification;
    }

    public Color getSquareColor(){
        return squareColor;
    }

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

    public void placePlayerToken(PlayerToken token){
        
        tokens.add(token);
        token.setCurrentBoardSquare(this);

        removeAll();
        
        for(PlayerToken piece : tokens){
            add(piece);
        }

        repaint();
    }

    public void removeToken(PlayerToken token){
        tokens.remove(token);
        remove(token);
        repaint();
    }

    public boolean isBlocked(){
       return this.isBlocked;
    }

    public void setBlocked(boolean blocked){
        this.isBlocked = blocked;
    }

    public int getRow(){
        return row;
    }

    public int getColumn(){
        return column;
    }

    public ArrayList<PlayerToken> getTokens(){
        return this.tokens;
    }
}
