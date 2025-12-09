import java.awt.*;
import javax.swing.JComponent;

/**
 * Represents the player token in the Ludo game.
 * @author Aakash Krishnan
 * @version 1.0
 * Last Modified: December 8, 2025
 */
public class PlayerToken extends JComponent{
    //Attributes
    private Color displayColor;
    private Color canonicalColor;
    private BoardSquare currentPosition;
    private BoardSquare spawnSquare;

    /**
     * Constructor for Player Token.
     * @param tokenColor The display color of the token.
     * @param canonicalColor The canonical color representing the player.  Used for maps and internal identifications.
     */
    public PlayerToken(Color tokenColor, Color canonicalColor){
        this.displayColor = tokenColor;
        this.canonicalColor = canonicalColor;
        setPreferredSize(new Dimension(30,30));
    }

    /**
     * Set the current board square of the token.
     * @param square
     */
    public void setCurrentBoardSquare(BoardSquare square){
        this.currentPosition = square;
    }

    /**
     * Get the display color of the token.
     * @return Color of the token.
     */
    public Color getDisplayColor(){
        return displayColor;
    }

    /**
     * Get the current position of the token.
     * @return Current BoardSquare of the token.
     */
    public BoardSquare getCurrentPosition(){
        return currentPosition;
    }

    /**
     * Set the spawn square of the token.
     * @param spawn The BoardSquare representing the spawn location.
     */
    public void setSpawnSquare(BoardSquare spawn){
        this.spawnSquare = spawn;
    }

    /**
     * Get the spawn square of the token.
     * @return The BoardSquare representing the spawn location.
     */
    public BoardSquare getSpawnSquare(){
        return this.spawnSquare;
    }

    /**
     * Set the canonical color of the token.
     * @param color The canonical Color to set.
     */
    public void setCanonicalColor(Color color){
        this.canonicalColor = color;
    }

    /**
     * Get the canonical color of the token.
     * @return The canonical Color of the token.
     */
    public Color getCanonicalColor(){
        return this.canonicalColor;
    }

    //Paint the token.
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(displayColor);
        g.fillOval(5, 5, getWidth() - 10, getHeight() - 10);
    }
}
