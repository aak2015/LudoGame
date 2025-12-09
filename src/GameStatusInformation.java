
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * The panel that displays the current game status information such as current player and dice roll.
 * @author Aakash Krishnan
 * @version 1.0
 * Last Modified: December 8, 2025
 */
public class GameStatusInformation extends JPanel{
    //Attributes for the class
    private JLabel currentPlayer;
    private JLabel diceLabel;
    private JButton rollDiceButton;

    /**
     * Constructs the GameStatusInformation panel.
     */
    public GameStatusInformation(){
        setLayout(new FlowLayout());
        currentPlayer = new JLabel("Current Player: ");
        diceLabel = new JLabel("Dice Roll: - ");
        rollDiceButton = new JButton("Roll Dice");
        add(currentPlayer);
        add(diceLabel);
        add(rollDiceButton);
    }

    /**
     * Gets the roll dice button.
     * @return The roll dice button.
     */
    public JButton getRollDiceButton(){
        return rollDiceButton;
    }

    /**
     * Sets the current player display.
     * @param color The color of the current player.
     */
    public void setCurrentPlayer(Color color){
        String playerString = "";

        if(color == GameLogic.RED_KEY){
            playerString = "RED";
        }else if(color == GameLogic.YELLOW_KEY){
            playerString = "YELLOW";
        }else if(color == GameLogic.GREEN_KEY){
            playerString = "GREEN";
        }else if(color == GameLogic.BLUE_KEY){
            playerString = "BLUE";
        }

        currentPlayer.setText("Current Player: " + playerString);
        repaint();
    }

    /**
     * Sets the dice roll display.
     * @param roll The dice roll value.
     */
    public void setDiceRoll(String roll){
        diceLabel.setText("Dice Roll: " + roll);
        repaint();
    }

     
}
