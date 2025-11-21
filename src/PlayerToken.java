import java.awt.*;
import javax.swing.JComponent;


public class PlayerToken extends JComponent{
    private Color displayColor;
    private Color canonicalColor;
    private BoardSquare currentPosition;
    private BoardSquare spawnSquare;

    public PlayerToken(Color tokenColor, Color canonicalColor){
        this.displayColor = tokenColor;
        this.canonicalColor = canonicalColor;
        setPreferredSize(new Dimension(30,30));
    }

    public void setCurrentBoardSquare(BoardSquare square){
        this.currentPosition = square;
    }

    public Color getDisplayColor(){
        return displayColor;
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

    public void setCanonicalColor(Color color){
        this.canonicalColor = color;
    }

    public Color getCanonicalColor(){
        return this.canonicalColor;
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(displayColor);
        g.fillOval(5, 5, getWidth() - 10, getHeight() - 10);
    }
}
