import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JOptionPane;

public class GameLogic {
    private Map<Color, List<BoardSquare>> playPaths;
    private Map<Color, List<PlayerToken>> playTokens = new HashMap<>();
    private BoardSquare[][] squares;
    private int diceRoll;
    private List<Color> playerOrder;
    private int currentPlayerIndex = 0;
    private Random random = new Random();

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

    private int rollDice(){
        diceRoll = random.nextInt(6) + 1;
        System.out.println("Dice Roll: " + diceRoll);
        return diceRoll;
    }

    public int getDiceRoll(){
        return diceRoll;
    }

    private List<PlayerToken> getValidMoves(Color playerColor){
        List<PlayerToken> validTokens = new ArrayList<>();
        List<PlayerToken> tokens = playTokens.get(playerColor);
        if(tokens == null){
            return validTokens;
        }

        for(PlayerToken token : tokens){
            if(validateMove(token, diceRoll)){
                validTokens.add(token);
            }
        }

        return validTokens;
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

    private void removeListeners(){
        for(List<PlayerToken> tokens : playTokens.values()){
            for(PlayerToken token : tokens){
                for(java.awt.event.MouseListener listener : token.getMouseListeners()){
                    token.removeMouseListener(listener);
                }
            }
        }
    }

    private void moveToken(PlayerToken token, int distance){
        if(!validateMove(token, distance)){
            System.out.println("Invalid move for token of color " + token.getColor());
            return;
        }

        BoardSquare current = token.getCurrentPosition();
        BoardSquare next = getNextSquare(token, distance);

        if(next != null){
            current.removeToken(token);
            next.placePlayerToken(token);
        }else{
            System.out.println("Next is null, cannot move further");
            return;
        }

        
        ArrayList<PlayerToken> tokens = next.getTokens();
        if(tokens.size() > 1){
            boolean noOpponentTokens = tokens.stream().allMatch(t -> t.getColor().equals(token.getColor()));

            if(noOpponentTokens){
                next.setBlocked(true);
                System.out.println("Square is now blocked by: " + token.getColor());
            }else{
                for(PlayerToken t : tokens){
                    if(!t.getColor().equals(token.getColor())){
                        System.out.println("Returning token of color " + t.getColor() + " to start.");
                        returnToStart(t);
                    }
                }

                next.setBlocked(false);
            }

        }
    }


    private boolean isGameOver(){
        for(Color color : playTokens.keySet()){
            List<PlayerToken> tokens = playTokens.get(color);
            if(tokens.size() != 4){
                continue;
            }
            List<BoardSquare> path = playPaths.get(color);
            BoardSquare home = path.get(path.size() - 1);
            boolean allAtHome = tokens.stream().allMatch(t -> t.getCurrentPosition() == home);
            if(allAtHome){
                JOptionPane.showMessageDialog(null, "Player color " + color + " wins!");
                removeListeners();
                return true;
            }
        }

        return false;
    }


    private boolean validateMove(PlayerToken token, int distance){
        List<BoardSquare> path = playPaths.get(token.getColor());

        if(path == null){
            return false;
        }

        BoardSquare current = token.getCurrentPosition();
        int startIndex = path.indexOf(current);

        if(startIndex == -1){
            return false;
        }

        int terminalIndex = startIndex + distance;

        if(terminalIndex >= path.size()){
            return false;
        }
        //Check if there is a blocked square in the path - amend logic is needed
        for(int i = startIndex; i < terminalIndex; i++){
            if(path.get(i).isBlocked()){
                System.out.println("Square is blocked. Cannot move past blocked index: " + i);
                return false;
            }
        }

        BoardSquare endSquare = path.get(terminalIndex);
        if(endSquare.isBlocked()){
            boolean colorMatch = endSquare.getTokens().stream().allMatch(t -> t.getColor().equals(token.getColor()));
            if(!colorMatch){
                System.out.println("End square is blocked by opponent tokens.");
                return false;
            }
        }

        return true;
    }


    private void returnToStart(PlayerToken token){
        List<BoardSquare> path = playPaths.get(token.getColor());
        if(path == null || path.isEmpty()){
            return;
        }

        BoardSquare spawn = path.get(0);
        BoardSquare current = token.getCurrentPosition();

        current.removeToken(token);
        spawn.placePlayerToken(token);
    }

    public BoardSquare getNextSquare(PlayerToken token, int distance){
        Color playerColor = token.getColor();
        List<BoardSquare> colorPath = playPaths.get(playerColor);
        if(colorPath == null){
            return null;
        }

        int currentPositionIndex = colorPath.indexOf(token.getCurrentPosition());
        
        if(currentPositionIndex == -1){
            return null;
        }

        int nextPositionIndex = currentPositionIndex + distance;

        if(nextPositionIndex >= colorPath.size()){
            return null;
        }

        return colorPath.get(nextPositionIndex);
    }

    /**
     * Turn Logic - Start the turn for a player
     */
    public void startTurn(){
        Color currentPlayer = playerOrder.get(currentPlayerIndex);
        
        int roll = rollDice();
        List<PlayerToken> validMoves = getValidMoves(currentPlayer);

        if(validMoves.isEmpty()){
            System.out.println("No valid moves for player: " + currentPlayer);
            advancePlayerTurn();
        }
    }



    private void advancePlayerTurn(){
        currentPlayerIndex = (currentPlayerIndex + 1) % playerOrder.size();
        if(!isGameOver()){
            
            startTurn();
        }
    }

    public void generatePlayerOrder(){
        Map<Color, Integer> results = new HashMap<>();
        results.put(Color.RED, rollDice());
        results.put(Color.GREEN, rollDice());
        results.put(Color.BLUE, rollDice());
        results.put(Color.YELLOW, rollDice());

        playerOrder = new ArrayList<>(results.keySet().stream()
            .sorted((c1, c2) -> results.get(c2) - results.get(c1))
            .toList()
        );
    }

}
