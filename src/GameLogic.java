import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 * Represents the core game logic for Ludo.
 * @author Aakash Krishnan
 * @version 1.0
 * Last Modified: December 8, 2025
 */

public class GameLogic {
    //Color constants for the canonical colors.
    public static final Color RED_KEY = Color.RED;
    public static final Color GREEN_KEY = Color.GREEN;
    public static final Color BLUE_KEY = Color.BLUE;
    public static final Color YELLOW_KEY = Color.YELLOW;

    //Color constants for the display colors.
    public static final Color RED_TOKEN = Color.RED.darker().darker();
    public static final Color GREEN_TOKEN = Color.GREEN.darker().darker();
    public static final Color BLUE_TOKEN = Color.BLUE.darker().darker();
    public static final Color YELLOW_TOKEN = Color.YELLOW.darker().darker();

    //Attributes for the Game Logic object.
    private Map<Color, List<BoardSquare>> playPaths; 
    private Map<Color, List<PlayerToken>> playTokens = new HashMap<>();
    private BoardSquare[][] squares; //Represents the board.
    private int diceRoll;
    private List<Color> playerOrder;
    private int currentPlayerIndex = 0;
    private Random random = new Random(); //For the dice.
    private boolean waitingForDiceRoll = false; 
    private GameStatusInformation statusInfo;
    private Map<Color, PlayerType> playerTypes = new HashMap<>();
    private Map<Color, ComputerizedPlayer> computerPlayers = new HashMap<>();

    /**
     * Constructor for the Game Logic.
     * @param squares The board squares.
     * @param statusInfo The game status information panel.
     * @param playerTypes The mapping of player colors to their types (human or computer).
     */
    public GameLogic(BoardSquare[][] squares, GameStatusInformation statusInfo, Map<Color, PlayerType> playerTypes){
        this.squares = squares;
        this.statusInfo = statusInfo;
        this.playPaths = readPlayPaths();
        this.playerTypes = playerTypes;

        //Generate a new computer player for each color whose key is 'COMPUTER'.
        for(Color color : playerTypes.keySet()){
            if(playerTypes.get(color) == PlayerType.COMPUTER){
                computerPlayers.put(color, new ComputerizedPlayer(color));
            }
        }

        //Get the turn order and set up the class.
        generatePlayerOrder();
        setUpRollButton();
    }

    //Add the listener to the roll button.
    private void setUpRollButton(){
        this.statusInfo.getRollDiceButton().addActionListener(e -> {
            if(waitingForDiceRoll){
                waitingForDiceRoll = false;
                processDiceRoll();
            }
        });
    }

    //Read the play paths from configuration files.
    private Map<Color, List<BoardSquare>> readPlayPaths(){
        Map<Color, List<BoardSquare>> playPaths = new HashMap<Color, List<BoardSquare>>();

        playPaths.put(RED_KEY, new ArrayList<BoardSquare>());
        playPaths.put(GREEN_KEY, new ArrayList<BoardSquare>()); 
        playPaths.put(BLUE_KEY, new ArrayList<BoardSquare>());  
        playPaths.put(YELLOW_KEY, new ArrayList<BoardSquare>());
        
        Map<Color, String> pathFiles = Map.of(
            RED_KEY, "src/pathFiles/redPath.cfg",
            GREEN_KEY, "src/pathFiles/greenPath.cfg",
            BLUE_KEY, "src/pathFiles/bluePath.cfg",
            YELLOW_KEY, "src/pathFiles/yellowPath.cfg"
        );

        for(Color color : pathFiles.keySet()){
            String filePath = pathFiles.get(color);
            try(BufferedReader br  = new BufferedReader(new FileReader(filePath))){
                String line;
                while((line = br.readLine()) != null){
                    String[] parts = line.split(",");
                    int row = Integer.parseInt(parts[0].trim());
                    int col = Integer.parseInt(parts[1].trim());
                    BoardSquare square = squares[row][col];
                    playPaths.get(color).add(square);
                }
            }catch(IOException e){
                System.err.println("Error reading the path configuration for color: " + color.toString());
                System.exit(1);
            }
        }

        return playPaths;
    }

    //Initialize player tokens on the board.
    public void initializePlayerTokens(Color playerColor){

        playTokens.putIfAbsent(playerColor, new ArrayList<>());
        
        for(BoardSquare[] row : squares){
            for(BoardSquare square : row){
                if(square.getSquareClassification() == BoardSquare.SquareClassification.TOKEN_SPAWN && square.getSquareColor().equals(playerColor)){
                    if(playTokens.get(playerColor).size() == 4){
                        return;
                    }

                    Color displayColor;
                    Color canonicalColor;
                    if(playerColor.equals(RED_KEY)){
                        displayColor = RED_TOKEN;
                        canonicalColor = RED_KEY;
                    }else if(playerColor.equals(GREEN_KEY)){
                        displayColor = GREEN_TOKEN;
                        canonicalColor = GREEN_KEY;
                    }else if(playerColor.equals(BLUE_KEY)){
                        displayColor = BLUE_TOKEN;
                        canonicalColor = BLUE_KEY;
                    }else{
                        displayColor = YELLOW_TOKEN;
                        canonicalColor = YELLOW_KEY;
                    }

                    PlayerToken token = new PlayerToken(displayColor, canonicalColor);
                    
                    attachListener(token);
                    square.placePlayerToken(token);
                    playTokens.get(playerColor).add(token);
                }
            }
        }
    }

    //Rolls the dice.
    private int rollDice(){
        diceRoll = random.nextInt(6) + 1;
        System.out.println("Dice Roll: " + diceRoll);
        return diceRoll;
    }

    /**
     * Gets the current dice roll.
     * @return The current dice roll.
     */
    public int getDiceRoll(){
        return diceRoll;
    }

    //Get all the valid moves for a particular player color.
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

    //Attach a mouse listener to a token.
    private void attachListener(PlayerToken token){
        token.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                System.out.println("Clicked on a " + token.getDisplayColor() + " at (" + token.getCurrentPosition().getRow() + ", " + token.getCurrentPosition().getColumn() + ")");
                moveToken(token, diceRoll);
            }
        });
    }

    //Remove all listeners from tokens when the game ends.
    private void removeListeners(){
        for(List<PlayerToken> tokens : playTokens.values()){
            for(PlayerToken token : tokens){
                for(java.awt.event.MouseListener listener : token.getMouseListeners()){
                    token.removeMouseListener(listener);
                }
            }
        }
    }

    //Move a token by a certain distance.
    private void moveToken(PlayerToken token, int distance){
        //Check if the move is valud.
        if(!validateMove(token, distance)){
            System.out.println("Invalid move for token of color " + token.getDisplayColor());
            return;
        }

        //Get the current and next squares.
        BoardSquare current = token.getCurrentPosition();
        BoardSquare next = getNextSquare(token, distance);

        //Ensure the destination is valid.
        if(next != null){
            current.removeToken(token);
            next.placePlayerToken(token);
        }else{
            System.out.println("Next is null, cannot move further");
            return;
        }

        //Ensure blocks are removed when a token leaves a square & the number of tokens drops to 1 or 0.
        ArrayList<PlayerToken> prevTokens = current.getTokens();
        if(prevTokens.size() <= 1){
            current.setBlocked(false);
        }

        //Handle capturing opponent tokens and blocking logic.
        ArrayList<PlayerToken> tokens = next.getTokens();
        if(tokens.size() > 1){
            boolean noOpponentTokens = tokens.stream().allMatch(t -> t.getCanonicalColor().equals(token.getCanonicalColor()));

            if(noOpponentTokens){
                next.setBlocked(true);
                System.out.println("Square is now blocked by: " + token.getCanonicalColor());
            }else{
                for(PlayerToken t : tokens){
                    if(!t.getCanonicalColor().equals(token.getCanonicalColor())){
                        System.out.println("Returning token of color " + t.getCanonicalColor() + " to start.");
                        returnToStart(t);
                    }
                }

                next.setBlocked(false);
            }

        }

        //Check for game over condition.
        if(!isGameOver()){
            advancePlayerTurn();
            startTurn();
        }
    }

    //Check if the game is over.
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
                String winner;
                if(color == GameLogic.RED_KEY){
                    winner = "Red";
                }else if(color == GameLogic.GREEN_KEY){
                    winner = "Green";
                }else if(color == GameLogic.BLUE_KEY){
                    winner = "Blue";
                }else{
                    winner = "Yellow";
                }
                JOptionPane.showMessageDialog(null, "Player color " + winner + " wins!");
                removeListeners();
                return true;
            }
        }

        return false;
    }

    //Validate if a move is possible for a token given a distance.
    private boolean validateMove(PlayerToken token, int distance){
        //Get the token's play path.
        List<BoardSquare> path = playPaths.get(token.getCanonicalColor());

        if(path == null){
            return false;
        }

        System.out.println(token.getSpawnSquare());
        System.out.println(token.getCurrentPosition());

        //If the token is at spawn, check for a 6.
        boolean isAtSpawn = token.getCurrentPosition().equals(token.getSpawnSquare());
        if(isAtSpawn){
            if(distance == 6){
                BoardSquare entry = path.get(0);
                boolean entryBlocked = entry.isBlocked() && !entry.getTokens().stream().anyMatch(t -> !t.getCanonicalColor().equals(token.getCanonicalColor()));
                return !entryBlocked;
            }
            return false;
        }

        //Get the current position index.
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
                boolean sameColor = path.get(i).getTokens().stream().allMatch(t -> t.getCanonicalColor().equals(token.getCanonicalColor()));
                if(!sameColor){
                    System.out.println("Square is blocked. Cannot move past blocked index: " + i);
                    return false;
                }
            }
        }

        //Ensure the destination is not blocked.
        BoardSquare endSquare = path.get(terminalIndex);
        if(endSquare.isBlocked()){
            boolean colorMatch = endSquare.getTokens().stream().allMatch(t -> t.getCanonicalColor().equals(token.getCanonicalColor()));
            if(!colorMatch){
                System.out.println("End square is blocked by opponent tokens.");
                return false;
            }
        }

        return true;
    }

    //Return a token to its start position.
    private void returnToStart(PlayerToken token){
        List<BoardSquare> path = playPaths.get(token.getCanonicalColor());
        if(path == null || path.isEmpty()){
            return;
        }

        BoardSquare spawn = token.getSpawnSquare();
        BoardSquare current = token.getCurrentPosition();

        current.removeToken(token);
        spawn.placePlayerToken(token);
    }

    /**
     * Gets the next square for a token given a distance.
     * @param token The player token.
     * @param distance The distance to move.
     * @return The next BoardSquare or null if invalid.
     */
    public BoardSquare getNextSquare(PlayerToken token, int distance){
        Color playerColor = token.getCanonicalColor();
        List<BoardSquare> colorPath = playPaths.get(playerColor);
        if(colorPath == null){
            return null;
        }

        BoardSquare current = token.getCurrentPosition();

        if(current == token.getSpawnSquare() && distance == 6){
            return colorPath.get(0);
        }

        int currentPositionIndex = colorPath.indexOf(current);
        
        if(currentPositionIndex == -1){
            return null;
        }

        int nextPositionIndex = currentPositionIndex + distance;

        if(nextPositionIndex >= colorPath.size()){
            return null;
        }

        return colorPath.get(nextPositionIndex);
    }

    //Handle the computer player's turn.
    private void computerPlayerTurn(Color computerColor){
        ComputerizedPlayer computer = computerPlayers.get(computerColor);
        if(computer == null){
            System.err.println("No computerized player found for color: " + computerColor);
            return;
        }

        rollDice();
        statusInfo.setDiceRoll(diceRoll + "");

        List<PlayerToken> validMoves = getValidMoves(computerColor);
        if(validMoves.isEmpty()){
            System.out.println("No valid moves for computer player: " + computerColor);
            advancePlayerTurn();
            javax.swing.SwingUtilities.invokeLater(() -> startTurn());
            return;
        }

        PlayerToken selectedToken = computer.makeMove(validMoves);
        if(selectedToken != null){
            moveToken(selectedToken, diceRoll);
        }
    }

    /**
     * Starts the turn for the current player.
     */
    public void startTurn(){
        Color currentPlayer = playerOrder.get(currentPlayerIndex);        
        statusInfo.setCurrentPlayer(currentPlayer);
        statusInfo.setDiceRoll("-");
        
        if (playerTypes.get(currentPlayer) == PlayerType.COMPUTER) {
            javax.swing.SwingUtilities.invokeLater(() -> computerPlayerTurn(currentPlayer));
        }else{
            waitingForDiceRoll = true;
        }
    }

    //Process the dice roll for the current player.
    private void processDiceRoll(){
        Color currentPlayer = playerOrder.get(currentPlayerIndex);
        rollDice();
        statusInfo.setDiceRoll(diceRoll + "");
        List<PlayerToken> validMoves = getValidMoves(currentPlayer);

        if(validMoves.isEmpty()){
            System.out.println("No valid moves for player: " + currentPlayer);
            advancePlayerTurn();
            javax.swing.SwingUtilities.invokeLater(() -> startTurn());
            return;
        }else{
            System.out.println("Waiting for token selection.");
        }
    }


    //Advance to the next player's turn.
    private void advancePlayerTurn(){
        currentPlayerIndex = (currentPlayerIndex + 1) % playerOrder.size();
        
    }

    //Generate the player order based on initial dice rolls.
    private void generatePlayerOrder(){
        Map<Color, Integer> results = new HashMap<>();
        results.put(RED_KEY, rollDice());
        results.put(GREEN_KEY, rollDice());
        results.put(BLUE_KEY, rollDice());
        results.put(YELLOW_KEY, rollDice());

        playerOrder = new ArrayList<>(results.keySet().stream()
            .sorted((c1, c2) -> results.get(c2) - results.get(c1))
            .toList()
        );
    }

}
