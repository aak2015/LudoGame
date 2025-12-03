import java.awt.Color;
import java.util.List;
import java.util.Random;

public class ComputerizedPlayer {
    private Color computerColor;
    private Random random = new Random();

    public ComputerizedPlayer(Color color){
        this.computerColor = color;
    }

    public Color getColor(){
        return computerColor;
    }

    public PlayerToken makeMove(List<PlayerToken> validMoves){
        if(validMoves == null || validMoves.isEmpty()){
            return null;
        }
        int index = random.nextInt(validMoves.size());
        return validMoves.get(index);
    }
    
}
