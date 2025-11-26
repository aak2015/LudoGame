
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
        currentPlayer.setText("Current Player: " + color.toString());
        repaint();
    }

    public void setDiceRoll(String roll){
        diceLabel.setText("Dice Roll: " + roll);
        repaint();
    }

     
}
