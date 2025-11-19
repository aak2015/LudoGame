
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameStatusInformation extends JPanel{
    private JLabel currentPlayer;
    private JLabel diceLabel;

    public GameStatusInformation(){
        setLayout(new FlowLayout());
        currentPlayer = new JLabel("Current Player: ");
        diceLabel = new JLabel("Dice Roll: - ");
        add(currentPlayer);
        add(diceLabel);
    }

    public void setCurrentPlayer(Color color){
        currentPlayer.setText("Current Player: " + color.toString());
    }

    public void setDiceRoll(int roll){
        diceLabel.setText("Dice Roll: " + roll);
    }

     
}
