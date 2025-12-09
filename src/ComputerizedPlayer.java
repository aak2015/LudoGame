import java.awt.Color;
import java.util.List;
import java.util.Random;

/**
* Represents a computerized player in the Ludo game.
* @author Aakash Krishnan
* @version 1.0
* Last Modified: December 8, 2025
*/
public class ComputerizedPlayer {
    //Attributes
    private Color computerColor;
    private Random random = new Random();

    /**
     * Constructs a ComputerizedPlayer with the specified color.
     * @param color The color of the computerized player.
     */
    public ComputerizedPlayer(Color color){
        this.computerColor = color;
    }

    /**
     * Gets the color of the computerized player.
     * @return The color of the computerized player.
     */
    public Color getColor(){
        return computerColor;
    }

    /**
     * Makes a move by selecting a token from the list of valid moves.
     * @param validMoves The list of valid tokens that can be moved.
     * @return The selected PlayerToken to move, or null if no valid moves.
     */
    public PlayerToken makeMove(List<PlayerToken> validMoves){
        if(validMoves == null || validMoves.isEmpty()){
            return null;
        }
        int index = random.nextInt(validMoves.size());
        return validMoves.get(index);
    }
    
}
