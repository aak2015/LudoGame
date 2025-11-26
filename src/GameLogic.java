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

public class GameLogic {
    public static final Color RED_KEY = Color.RED;
    public static final Color GREEN_KEY = Color.GREEN;
    public static final Color BLUE_KEY = Color.BLUE;
    public static final Color YELLOW_KEY = Color.YELLOW;

    public static final Color RED_TOKEN = Color.RED.darker().darker();
    public static final Color GREEN_TOKEN = Color.GREEN.darker().darker();
    public static final Color BLUE_TOKEN = Color.BLUE.darker().darker();
    public static final Color YELLOW_TOKEN = Color.YELLOW.darker().darker();

    private Map<Color, List<BoardSquare>> playPaths;
    private Map<Color, List<PlayerToken>> playTokens = new HashMap<>();
    private BoardSquare[][] squares;
    private int diceRoll;
    private List<Color> playerOrder;
    private int currentPlayerIndex = 0;
    private Random random = new Random();
    private boolean waitingForDiceRoll = false;
    private GameStatusInformation statusInfo;

    public GameLogic(BoardSquare[][] squares, GameStatusInformation statusInfo){
        this.squares = squares;
        this.statusInfo = statusInfo;
        this.playPaths = readPlayPaths();
        generatePlayerOrder();
        setUpRollButton();
    }

    private void setUpRollButton(){
        this.statusInfo.getRollDiceButton().addActionListener(e -> {
            if(waitingForDiceRoll){
                waitingForDiceRoll = false;
                processDiceRoll();
            }
        });
    }

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
                System.out.println("Clicked on a " + token.getDisplayColor() + " at (" + token.getCurrentPosition().getRow() + ", " + token.getCurrentPosition().getColumn() + ")");
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
            System.out.println("Invalid move for token of color " + token.getDisplayColor());
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

        //Ensure blocks are removed when a token leaves a square & the number of tokens drops to 1 or 0.
        ArrayList<PlayerToken> prevTokens = current.getTokens();
        if(prevTokens.size() <= 1){
            current.setBlocked(false);
        }

        
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

        if(!isGameOver()){
            advancePlayerTurn();
            startTurn();
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
        List<BoardSquare> path = playPaths.get(token.getCanonicalColor());

        if(path == null){
            return false;
        }

        System.out.println(token.getSpawnSquare());
        System.out.println(token.getCurrentPosition());

        boolean isAtSpawn = token.getCurrentPosition().equals(token.getSpawnSquare());
        if(isAtSpawn){
            if(distance == 6){
                BoardSquare entry = path.get(0);
                boolean entryBlocked = entry.isBlocked() && !entry.getTokens().stream().anyMatch(t -> !t.getCanonicalColor().equals(token.getCanonicalColor()));
                return !entryBlocked;
            }
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
                boolean sameColor = path.get(i).getTokens().stream().allMatch(t -> t.getCanonicalColor().equals(token.getCanonicalColor()));
                if(!sameColor){
                    System.out.println("Square is blocked. Cannot move past blocked index: " + i);
                    return false;
                }
            }
        }

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

    /**
     * Turn Logic - Start the turn for a player
     */
    public void startTurn(){
        Color currentPlayer = playerOrder.get(currentPlayerIndex);
        
        statusInfo.setCurrentPlayer(currentPlayer);
        statusInfo.setDiceRoll("-");
        waitingForDiceRoll = true;
    }

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



    private void advancePlayerTurn(){
        currentPlayerIndex = (currentPlayerIndex + 1) % playerOrder.size();
        
    }

    public void generatePlayerOrder(){
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
