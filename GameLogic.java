import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameLogic {
    private Map<Color, List<BoardSquare>> playPaths;
    private Map<Color, List<PlayerToken>> playTokens = new HashMap<>();
    private BoardSquare[][] squares;
    private int diceRoll = 1;

    public GameLogic(BoardSquare[][] squares){
        this.squares = squares;
        this.playPaths = readPlayPaths();
    }

    private Map<Color, List<BoardSquare>> readPlayPaths(){
        Map<Color, List<BoardSquare>> playPaths = new HashMap<Color, List<BoardSquare>>();
        Color c = Color.RED.darker().darker();
        playPaths.put(Color.RED.darker().darker(), new ArrayList<>());
        playPaths.get(Color.RED.darker().darker()).add(squares[2][3]);
        playPaths.get(Color.RED.darker().darker()).add(squares[1][6]);

        //TODO: Parse play paths
        return playPaths;
    }

    public void initializePlayerTokens(Color playerColor){
        playTokens.putIfAbsent(playerColor, new ArrayList<>());
        
        for(BoardSquare[] row : squares){
            for(BoardSquare square : row){
                if(square.getSquareClassification() == BoardSquare.SquareClassification.TOKEN_SPAWN && square.getSquareColor().equals(playerColor)){
                    if(playTokens.get(playerColor).size() == 4){
                        return;
                    }

                    PlayerToken token = new PlayerToken(playerColor.darker().darker());
                    attachListener(token);
                    square.placePlayerToken(token);
                    playTokens.get(playerColor).add(token);
                }
            }
        }
    }

    private void attachListener(PlayerToken token){
        token.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                System.out.println("Clicked on a " + token.getColor() + " at (" + token.getCurrentPosition().getRow() + ", " + token.getCurrentPosition().getColumn() + ")");
                moveToken(token, diceRoll);
            }
        });
    }


    public void moveToken(PlayerToken token, int distance){
        BoardSquare current = token.getCurrentPosition();
        BoardSquare next = getNextSquare(token, distance);

        if(next != null){
            current.removeToken(token);
            next.placePlayerToken(token);
        }else{
            System.out.println("Next is null, cannot move further");
        }
    }

    public BoardSquare getNextSquare(PlayerToken token, int distance){
        Color playerColor = token.getColor();
        List<BoardSquare> colorPath = playPaths.get(playerColor);
        if(colorPath == null){
            return null;
        }

        int currentPositionIndex = colorPath.indexOf(token.getCurrentPosition());
        
        int nextPositionIndex = currentPositionIndex + distance;

        if(nextPositionIndex >= colorPath.size()){
            return null;
        }

        return colorPath.get(nextPositionIndex);
    }
}
