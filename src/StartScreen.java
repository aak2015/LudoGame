import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

enum PlayerType{
    HUMAN,
    COMPUTER
}

public class StartScreen extends JPanel{
    private ButtonGroup computerCountGroup;
    private Map<Color, PlayerType> playerTypes = new HashMap<>();
    private JRadioButton zeroComputers;
    private JRadioButton oneComputer;
    private JRadioButton twoComputers;
    private JRadioButton threeComputers;
    private JRadioButton fourComputers;
    private JCheckBox redCheckbox;
    private JCheckBox greenCheckbox;
    private JCheckBox yellowCheckbox;
    private JCheckBox blueCheckbox;

    private StartScreenListener listener;

    public StartScreen(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel computerCountPanel = new JPanel();

        //Welcome text

        JLabel welcomeText = new JLabel("Welcome to Ludo!");
        JLabel instructionText = new JLabel("Please select the options.");
        add(welcomeText);
        add(instructionText);


        //Initialize the radio buttons for the computer players.
        computerCountGroup = new ButtonGroup();
        zeroComputers = new JRadioButton("0");
        oneComputer = new JRadioButton("1");
        twoComputers = new JRadioButton("2");
        threeComputers = new JRadioButton("3");
        fourComputers = new JRadioButton("4");
        computerCountGroup.add(zeroComputers);
        computerCountGroup.add(oneComputer);
        computerCountGroup.add(twoComputers);
        computerCountGroup.add(threeComputers);
        computerCountGroup.add(fourComputers);

        JLabel computerCountLabel = new JLabel("Select Number of Computer Players:");
        computerCountPanel.add(computerCountLabel);

        computerCountPanel.add(zeroComputers);
        computerCountPanel.add(oneComputer);
        computerCountPanel.add(twoComputers);
        computerCountPanel.add(threeComputers);
        computerCountPanel.add(fourComputers);
   
        add(computerCountPanel);
        
        //Color checkbox selection panel
        JPanel colorSelectionPanel = new JPanel();
        JLabel colorSelectionLabel = new JLabel("Select Computer Player Colors:");
        colorSelectionPanel.add(colorSelectionLabel);
        redCheckbox = new JCheckBox("Red");
        greenCheckbox = new JCheckBox("Green");
        yellowCheckbox = new JCheckBox("Yellow");
        blueCheckbox = new JCheckBox("Blue");
   
        //Add to screen
        colorSelectionPanel.add(redCheckbox);
        colorSelectionPanel.add(greenCheckbox);
        colorSelectionPanel.add(yellowCheckbox);
        colorSelectionPanel.add(blueCheckbox);
        add(colorSelectionPanel);
        
        JButton submitButton = new JButton("Start Game");
        add(submitButton);

        submitButton.addActionListener(e -> {
            processInput();
        });

        setVisible(true);

    }

    private void processInput(){
        //Process computer count.
        int computerCount;
        if(zeroComputers.isSelected()){
            computerCount = 0;
        }
        else if(oneComputer.isSelected()){
            computerCount = 1;
        }
        else if(twoComputers.isSelected()){
            computerCount = 2;
        }
        else if(threeComputers.isSelected()){
            computerCount = 3;
        }
        else if(fourComputers.isSelected()){
            computerCount = 4;
        }else{
            showMessageDialog(null, "Please select number of computer players.");
            return;
        }
        
        //Process color selections.
        Set<Color> selectedComputerColors = new HashSet<>();
        if(redCheckbox.isSelected()){
            selectedComputerColors.add(GameLogic.RED_KEY);
        }
        if(greenCheckbox.isSelected()){
            selectedComputerColors.add(GameLogic.GREEN_KEY);
        }
        if(yellowCheckbox.isSelected()){
            selectedComputerColors.add(GameLogic.YELLOW_KEY);
        }
        if(blueCheckbox.isSelected()){
            selectedComputerColors.add(GameLogic.BLUE_KEY);
        }
        if(selectedComputerColors.size() < computerCount){
            showMessageDialog(null, "Please select enough colors for the computer players.");
            return;
        }
        if(selectedComputerColors.size() > computerCount){
            showMessageDialog(null, "Please select only enough colors for the computer players.");
            return;
        }

        //Assign player types based on selections.
        for(Color color : List.of(GameLogic.RED_KEY, GameLogic.GREEN_KEY, GameLogic.YELLOW_KEY, GameLogic.BLUE_KEY)){
            if(selectedComputerColors.contains(color)){
                playerTypes.put(color, PlayerType.COMPUTER);
            }else{
                playerTypes.put(color, PlayerType.HUMAN);
            }
        }
        
        listener.onStartScreenInputProcessed();
    }

    public Map<Color, PlayerType> getPlayerTypes(){
        return playerTypes;
    }

    public void setListener(StartScreenListener listener){
        this.listener = listener;
    }


}
