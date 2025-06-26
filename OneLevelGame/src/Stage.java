// name surname: Ibrahim Eren Bol
// student ID: 2023400093

import java.awt.*;
import java.util.Random;

public class Stage {

    // Instance variables
    private int stageNumber;  // Stage number
    private double gravity;  // Gravity value
    private double velocityX; // Horizontal velocity of stage
    private double velocityY;  // Vertical velocity of stage
    private int rightCode;     // Key code for right movement
    private int leftCode;      // Key code for left movement
    private int upCode;        // Key code for up movement
    private String clue;       // Clue text for the stage
    private String help;      // Help text for the stage
    private Color color;      // Color of the stage (randomly generated for each stage)

    // Constructor
    public Stage(double gravity, double velocityX, double velocityY, int stageNumber, int rightCode, int leftCode, int upCode, String clue, String help) {
        this.gravity = gravity;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.stageNumber = stageNumber;
        this.rightCode = rightCode;
        this.leftCode = leftCode;
        this.upCode = upCode;
        this.clue = clue;
        this.help = help;
        // set a random color
        Random random = new Random();
        this.color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    // Getters only.
    public int getStageNumber() {return stageNumber;}  // Returns the stage number

    public double getGravity() {return gravity;}       // Returns the gravity value

    public double getVelocityX() {return velocityX;}   // Returns the horizontal velocity of the stage

    public double getVelocityY() {return velocityY;}  // Returns the vertical velocity of the stage

    public int[] getKeyCodes() {return new int[]{rightCode, leftCode, upCode};} // Return the key codes for right, left, and up movement

    public String getClue() {return clue;}         // Returns the clue text for the stage

    public String getHelp(){return help;}           // Returns the help text for the stage

    public Color getColor(){return color;}      // Returns the color of the stage
}