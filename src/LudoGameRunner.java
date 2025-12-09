import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 * Main class to run the Ludo game.
 * @author Aakash Krishnan
 * @version 1.0
 * Last Modified: December 8, 2025
 */
public class LudoGameRunner {
        public static void main(String[] args) {
        //Assemble the board.
        JFrame boardFrame = new JFrame("Ludo Board Test");
        boardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Display the start screen.
        StartScreen initialize = new StartScreen();
        boardFrame.add(initialize);
        boardFrame.setSize(500,400);
        boardFrame.setVisible(true);
        
        //Set up the listener for when the start screen input is processed.
        initialize.setListener(
            () -> {
                //Setup the board view on successful input processing.
                boardFrame.remove(initialize);
                boardFrame.setSize(800, 800);
                LudoBoardView testView = new LudoBoardView();
                boardFrame.setLayout(new BorderLayout());

                boardFrame.add(testView, BorderLayout.CENTER);
                boardFrame.add(testView.getGameStatusInfo(), BorderLayout.SOUTH);

                GameLogic logic = new GameLogic(testView.getSquares(), testView.getGameStatusInfo(), initialize.getPlayerTypes());
                logic.initializePlayerTokens(GameLogic.RED_KEY);
                logic.initializePlayerTokens(GameLogic.YELLOW_KEY);
                logic.initializePlayerTokens(GameLogic.GREEN_KEY);
                logic.initializePlayerTokens(GameLogic.BLUE_KEY);

                boardFrame.revalidate();
                boardFrame.repaint();

                logic.startTurn();
            }
        );

    }
}
