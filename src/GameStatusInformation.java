
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameStatusInformation extends JPanel{
    private JLabel currentPlayer;
    private JLabel diceLabel;
    private JButton rollDiceButton;

    public GameStatusInformation(){
        setLayout(new FlowLayout());
        currentPlayer = new JLabel("Current Player: ");
        diceLabel = new JLabel("Dice Roll: - ");
        rollDiceButton = new JButton("Roll Dice");
        add(currentPlayer);
        add(diceLabel);
        add(rollDiceButton);
    }

    public JButton getRollDiceButton(){
        return rollDiceButton;
    }

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

    public void setDiceRoll(String roll){
        diceLabel.setText("Dice Roll: " + roll);
        repaint();
    }

     
}
