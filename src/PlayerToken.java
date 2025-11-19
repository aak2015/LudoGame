import java.awt.*;
import javax.swing.JComponent;


public class PlayerToken extends JComponent{
    private Color color;
    private BoardSquare currentPosition;
    private BoardSquare spawnSquare;

    public PlayerToken(Color tokenColor){
        this.color = tokenColor;
        setPreferredSize(new Dimension(30,30));
    }

    public void setCurrentBoardSquare(BoardSquare square){
        this.currentPosition = square;
    }

    public Color getColor(){
        return color;
    }

    public BoardSquare getCurrentPosition(){
        return currentPosition;
    }

    public void setSpawnSquare(BoardSquare spawn){
        this.spawnSquare = spawn;
    }

    public BoardSquare getSpawnSquare(){
        return this.spawnSquare;
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(color);
        g.fillOval(5, 5, getWidth() - 10, getHeight() - 10);
    }
}
