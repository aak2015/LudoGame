import java.awt.BorderLayout;
import javax.swing.JFrame;

public class LudoGameRunner {
        public static void main(String[] args) {
        JFrame boardFrame = new JFrame("Ludo Board Test");
        boardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        StartScreen initialize = new StartScreen();
        boardFrame.add(initialize);
        boardFrame.setSize(500,400);
        boardFrame.setVisible(true);
        
        initialize.setListener(
            () -> {
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
