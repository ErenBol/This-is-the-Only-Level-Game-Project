// name surname: Ibrahim Eren Bol
// student ID: 2023400093
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;


public class Game {
    private int stageIndex;  // Current stage index
    private ArrayList<Stage> stages; // List of stages
    private int deathNumber; // Number of deaths
    private double gameTime; // Current elapsed time
    private double resetTime;  // Time when the game was reset
    private boolean resetGame; // Flag to check if the game is reset
    private boolean showHelp; // Flag to indicate if help text should be shown
    private double totalPausedTime = 0;  // Total paused time during the game

    // Constructor(
    public Game(ArrayList<Stage> stages) {
        this.stages = stages;
        this.stageIndex = 0;
        this.deathNumber = 0;
        this.gameTime = 0;
        this.resetTime = 0.0;
        this.resetGame = false;
        this.showHelp = false;
    }
    // Method to reset the game status to initial values
    public void resetGameStatus(){
        this.stageIndex = 0;
        this.deathNumber = 0;
        this.gameTime = 0;
        this.resetTime = 0.0;
        this.resetGame = false;
        this.showHelp = false;
        this.totalPausedTime = 0;
    }

    // Getters

    public int getStageIndex() {  // Returns the current stage index
        return stageIndex;
    }

    public Stage getCurrentStage() {  // Returns the current stage itself
        return stages.get(stageIndex);
    }

    // Main method to start and run the game
    public void play() {
        // Initializing the game by creating a player and a map
        int[] spawnPoint = {130, 450};
        gameTime = System.currentTimeMillis();
        Player player = new Player(spawnPoint[0], spawnPoint[1]);
        Map map = new Map(stages.get(stageIndex), player);

       // Game loop
       while(true){
           // Firstly handling the user inputs
           handleInput(map);
           // Immediately after that, we check if the game is reset
           if(resetGame) {
                stageIndex = 0;
                deathNumber = 0;
                gameTime = System.currentTimeMillis();
                player.respawn(spawnPoint);  // Spawning the player at its initial position
                map = new Map(stages.get(stageIndex), player);
                resetGame = false;
           }
           // Secondly we move the player considering physics rules including gravity
           map.applyPhysicsAndUpdatePlayer();
           // Checking if the button is pressed or if the player is touching a spike after movement
           boolean isButtonPressed = map.checkIfButtonIsTouched();
           boolean isSpikeTouched = map.checkSpikeCollision(map.getPlayer().getX(), map.getPlayer().getY());

           // Drawing the whole game environment so the last movement is also visible
           StdDraw.clear(); // Clearing the screen
           drawUIBackground(); // Drawing the static UI background
           double elapsedTime = System.currentTimeMillis() - gameTime - totalPausedTime;
           String timerText = formatTime(elapsedTime);
           drawUIDynamicText(map, timerText, showHelp); // Drawing the dynamic UI text
           player.draw(); // Drawing the player
           map.draw(); // Drawing the game environment
           StdDraw.show();

           // Some checking operations

           // Checking if the player is touching a spike
           if (isSpikeTouched) {
               deathNumber++;
               showHelp = false;
               map.restartStage();
           }
           // Checking if the player is pressing the button and updating map variables
           if (isButtonPressed) {
               map.pressButton();
               map.updateDoorState();
           } else map.releaseButton();

           // Setting the door height while the door is opening
           double doorCurrentHeight = map.getDoorCurrentHeight();
           if(map.getIsDoorOpen() && doorCurrentHeight > 0) {
               doorCurrentHeight -= 2 ;
               map.setDoorCurrentHeight(doorCurrentHeight);
               // Condition to prevent the door height from going below 0
               if (doorCurrentHeight < 0) {
                   map.setDoorCurrentHeight(0);
               }
           }
           // Checking if the player has passed the stage
           if (map.changeStage()) {
               stageIndex++;
               if (stageIndex >= stages.size()) {  // checking if the player has passed all stages
                   displayGameOverBanner(timerText);
                   char order = ' ';
                   // Loop to wait for user input (until 'A' or 'Q' is pressed)
                   while(order != 'A' && order != 'Q') {
                       if (StdDraw.isKeyPressed(KeyEvent.VK_A)) {
                           order = 'A';

                       } else if (StdDraw.isKeyPressed(KeyEvent.VK_Q)) {
                           order = 'Q';
                       }
                   }
                   if (order == 'A') {   // If 'A' is pressed, restart the game
                       resetGameStatus();
                       play();
                   } else {  // If 'Q' is pressed, exit the game
                       System.exit(0);
                       break;
                   }
               }
               else displayStagePassingBanner();
               // Resetting the player position and creating a new map for the next stage
               player = new Player(130, 450);
               map = new Map(stages.get(stageIndex), player);
               showHelp = false;
           }
           StdDraw.pause(25); // Pause for a short time to control the frame rate
       }
    }

    // Method to handle user inputs
    private void handleInput(Map map) {
        Player player = map.getPlayer();
        // Making the player jump in stage 2 independent of player input
        if (getStageIndex() == 2) {
            player.jump(map.getStage().getVelocityY());
        }
        // Mouse input handling
        if (StdDraw.isMousePressed()) {
            double mouseX = StdDraw.mouseX();
            double mouseY = StdDraw.mouseY();
            // Help button
            if (mouseX >= 210 && mouseX <= 290 && mouseY >= 70 && mouseY <= 100) {
                showHelp = true;
            }
            // Restart button
            else if (mouseX >= 510 && mouseX <= 590 && mouseY >= 70 && mouseY <= 100) {
                map.restartStage();
                deathNumber++;
                showHelp = false;
                StdDraw.pause(200);
                totalPausedTime += 200;
            }
            // Reset button
            else if (mouseX> 320 && mouseX < 480 && mouseY > 5 && mouseY < 35) {
                displayResetGameBanner();
                resetGame = true;
            }
        }

        // Keyboard input handling
        int rightCode = getCurrentStage().getKeyCodes()[0];
        int leftCode = getCurrentStage().getKeyCodes()[1];
        int upCode = getCurrentStage().getKeyCodes()[2];

        // Detecting key presses for right, left, and up movements
        if (StdDraw.isKeyPressed(rightCode)){
            player.setDirection("right");  // Setting the direction of the player to right
            player.setVelocityX(getCurrentStage().getVelocityX()); // Setting the velocity to positive for right movement
        }
        else if (StdDraw.isKeyPressed(leftCode)) {
            player.setDirection("left"); // Setting the direction of the player to left
            player.setVelocityX(-getCurrentStage().getVelocityX()); // Setting the velocity to negative for left movement
        }
        else{   // Setting the player's current velocity to 0 if neither right nor left is pressed
            player.setVelocityX(0);
        }

        // Making the player jump if the up key is pressed and the player is on the ground
        if (StdDraw.isKeyPressed(upCode) && upCode != -1 && player.isOnGround()) {
            double jumpVelocity = getCurrentStage().getVelocityY();
            player.jump(jumpVelocity);
        }
    }

    // Method to format time in MM:SS:msms format
    private String formatTime(double millis) {
        long totalMillis = (long) millis;
        long seconds = (totalMillis / 1000) % 60;
        long minutes = (totalMillis / (1000 * 60)) % 60;
        long milliseconds = (totalMillis % 1000) / 10;
        return String.format("%02d:%02d:%02d", minutes, seconds, milliseconds);
    }

    // Method to display the stage passing banner when the player passes the stage
    private void displayStagePassingBanner() {
        StdDraw.setPenColor(StdDraw.GREEN); // Set the color banner area color
        StdDraw.filledRectangle(400, 300, 400, 60); // Banner area
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(new Font("Arial",Font.BOLD, 30));
        StdDraw.text(400, 320, "You passed the stage");
        StdDraw.text(400, 280, "But is the level over?!");
        StdDraw.setFont();
        StdDraw.show();
        StdDraw.pause(2000); // Pause for 2 seconds
        totalPausedTime += 2000;
        StdDraw.clear();
    }

    // Method to display the reset game banner when the player resets the game
    private void displayResetGameBanner() {
        StdDraw.setPenColor(StdDraw.GREEN); // Set the color banner area color
        StdDraw.filledRectangle(400, 300, 400, 60); // Banner area
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(new Font("Arial",Font.BOLD, 30));
        StdDraw.text(400, 300,  "RESETTING THE GAME...");
        StdDraw.setFont();
        StdDraw.show();
        StdDraw.pause(2000); // pause for 2 seconds
        totalPausedTime = 0 ;
        StdDraw.clear();
    }

    // Method to display the game over banner when the player finishes the level
    private void displayGameOverBanner(String finalTime) {
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.filledRectangle(400, 300, 400, 100);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(new Font("Arial",Font.BOLD, 30));
        StdDraw.text(400, 350, "CONGRATULATIONS YOU FINISHED THE LEVEL");
        StdDraw.text(400, 310, "PRESS A TO PLAY AGAIN");
        StdDraw.setFont(new Font("Arial",Font.PLAIN, 20));
        StdDraw.text(400, 270, "You finished with " + deathNumber + " deaths in " + finalTime);
        StdDraw.setFont();
        StdDraw.show();
    }

    // Method to draw the static UI background (does not change)
    private void drawUIBackground() {
        StdDraw.setPenColor(new Color(56, 93, 172));
        StdDraw.filledRectangle(400, 60, 400, 60); // Background for the UI
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(250, 85, "Help");
        StdDraw.rectangle(250, 85, 40, 15);  // Help button area
        StdDraw.text(550, 85, "Restart");
        StdDraw.rectangle(550, 85, 40, 15); // Restart button area
        StdDraw.text(400, 20, "RESET THE GAME");
        StdDraw.rectangle(400, 20, 80, 15); // Reset button area
        StdDraw.text(100, 75, "Level: 1"); // Level text
    }

    // Method to draw the dynamic UI text (changes during the game)
    private void drawUIDynamicText(Map map, String timerText, boolean showHelp) {
        StdDraw.setPenColor(StdDraw.WHITE);
        String supportText = showHelp ? "Help" : "Clue"; // Determining the text to show
        StdDraw.text(400, 85, supportText + ":");
        StdDraw.text(700, 75, "Deaths: " + deathNumber);
        StdDraw.text(700, 50, "Stage: " + (stageIndex + 1)); // Stage number
        StdDraw.text(100, 50, timerText);  // Timer text

        String message = showHelp ? map.getStage().getHelp() : map.getStage().getClue(); // Get the clue or help text
        StdDraw.setPenColor(new Color(56, 93, 172)); // Background color
        StdDraw.filledRectangle(400, 55, 150, 10); // Background for the text
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(400, 55, message); // Display the clue or help text
    }
}