// name surname: Ibrahim Eren Bol
// student ID: 2023400093

import java.awt.*;

public class Map {

    // Instance variables
    private Stage stage;
    private Player player;
    private int[][] obstacles = {
            new int[]{0, 120, 120, 270}, new int[]{0, 270, 168, 330},
            new int[]{0, 330, 30, 480}, new int[]{0, 480, 180, 600},
            new int[]{180, 570, 680, 600}, new int[]{270, 540, 300, 570},
            new int[]{590, 540, 620, 570}, new int[]{680, 510, 800, 600},
            new int[]{710, 450, 800, 510}, new int[]{740, 420, 800, 450},
            new int[]{770, 300, 800, 420}, new int[]{680, 240, 800, 300},
            new int[]{680, 300, 710, 330}, new int[]{770, 180, 800, 240},
            new int[]{0, 120, 800, 150}, new int[]{560, 150, 800, 180},
            new int[]{530, 180, 590, 210}, new int[]{530, 210, 560, 240},
            new int[]{320, 150, 440, 210}, new int[]{350, 210, 440, 270},
            new int[]{220, 270, 310, 300}, new int[]{360, 360, 480, 390},
            new int[]{530, 310, 590, 340}, new int[]{560, 400, 620, 430}};

    private int[] button = new int[]{400, 390, 470, 410};

    private int[] buttonFloor = new int[]{400, 390, 470, 400};

    // Start Pipe Coordinates for Drawing
    private int[][] startPipe = {new int[]{115, 450, 145, 480},
            new int[]{110, 430, 150, 450}};

    // Exit Pipe Coordinates for Drawing
    private int[][] exitPipe = {new int[]{720, 175, 740, 215},
            new int[]{740, 180, 770, 210}};

    // Coordinates of spike areas
    private int[][] spikes = {
            new int[]{30, 333, 50, 423}, new int[]{121, 150, 207, 170},
            new int[]{441, 150, 557, 170}, new int[]{591, 180, 621, 200},
            new int[]{750, 301, 769, 419}, new int[]{680, 490, 710, 510},
            new int[]{401, 550, 521, 570}};

    // Door Coordinates
    private int[] door = new int[]{685, 180, 700, 240};

    // Door Animation Variables
    private int buttonPressNum;
    private boolean isButtonBeingPressed = false;
    private boolean isDoorOpen;
    private double doorCurrentHeight;
    // Final X and Y coordinates for player movement
    private double finalX;
    private double finalY;
    // Color Constants for objects
    final Color BUTTON_TOP = new Color(220, 20, 60);         // Red (Crimson-like)
    final Color BUTTON_FLOOR = new Color(80, 80, 80);        // Dark Gray
    final Color PIPE = new Color(255, 204, 0);               // Yellow-Orange
    final Color DOOR = new Color(70, 70, 70);                // Darker Gray


    // Constructor
    public Map(Stage stage, Player player) {
        this.stage = stage;
        this.player = player;
        this.buttonPressNum = 0;
        this.isDoorOpen = false;
        this.doorCurrentHeight = door[3] - door[1]; // Initialize with the height of the door

    }

    // Setters
    // Setting door current height for opening animation
    public void setDoorCurrentHeight(double doorCurrentHeight) {
        this.doorCurrentHeight = doorCurrentHeight;
    }
    
    // Getters
    public Stage getStage() {
        return stage;
    }  // Returns the current stage

    public Player getPlayer() {
        return player;
    } // Returns the player object

    public double getDoorCurrentHeight() {
        return doorCurrentHeight;
    } // Returns the current height of the door

    public boolean getIsDoorOpen() {return isDoorOpen;} // Returns if the door is open  
    
    // Instance methods
    
    // Method to move the player based on the direction
    public void movePlayer(char direction) {
        // Get the current position and velocity of the player
        double currentX = player.getX();
        double currentY = player.getY();
        double currentVelX = player.getVelocityX();
        double currentVelY = player.getVelocityY();
        // Calculate the next position based on the velocity 
        double nextX = currentX + currentVelX;
        double nextY = currentY + currentVelY;
        // Calculate the new position based on the direction
        switch(direction){
            case 'L':
                finalX = nextX;
                break;
            case 'R':
                finalX = nextX;
                break;
            case 'U':
                finalY = nextY;
                break;
        }
    }
    
    // Method to check collision with any objects
    private boolean checkCollision(double nextX, double nextY, int[][] objects) {
        double playerWidth = 20; 
        double playerHeight = 20; 
        double playerMinX = nextX - playerWidth / 2;
        double playerMaxX = nextX + playerWidth / 2;
        double playerMinY = nextY - playerHeight / 2;
        double playerMaxY = nextY + playerHeight / 2;

        // Check collision with each object
        for (int[] obj : objects) {
            double objMinX = obj[0];
            double objMinY = obj[1];
            double objMaxX = obj[2];
            double objMaxY = obj[3];
            if (playerMaxX > objMinX && playerMinX < objMaxX && playerMaxY > objMinY && playerMinY < objMaxY) {
                return true;
            }
        }
        return false;
    }
    // Method to check collision with spikes
    public boolean checkSpikeCollision(double playerX, double playerY) {
        return checkCollision(playerX, playerY, this.spikes);
    }
    // Method to check 
    public boolean checkPotentialDoorCollision(double potentialX, double potentialY) {
        double playerWidth = 20;
        double playerHeight = 20;
        // Calculate door boundaries/center
        int[] doorCoordinates = {door[0], door[1], door[2], (int) (door[1] + this.doorCurrentHeight)};
        double doorX = (doorCoordinates[0] + doorCoordinates[2]) / 2.0;
        double doorY = (doorCoordinates[1] + doorCoordinates[3]) / 2.0;
        double doorWidth = doorCoordinates[2] - doorCoordinates[0];
        double doorHeight = doorCoordinates[3] - doorCoordinates[1];

        // Use potentialX, potentialY instead of player.getX(), player.getY()
        boolean isOverlapping = (potentialX + playerWidth / 2 >= doorX - doorWidth / 2 &&
                potentialX - playerWidth / 2 <= doorX + doorWidth / 2 &&
                potentialY + playerHeight / 2 >= doorY - doorHeight / 2 &&
                potentialY - playerHeight / 2 <= doorY + doorHeight / 2);

        // Combine with the door open check
        return isOverlapping && !isDoorOpen;
    }
    // Method to check if the player is touching the exit pipe
    public boolean changeStage() {
        double playerX = player.getX();
        double playerY = player.getY();
        double playerWidth = 20;
        double playerHeight = 20;
        // check if player is touching the exit door considering the width and height of the player and door
        double exitPipeX = (exitPipe[1][0] + exitPipe[1][2]) / 2.0;
        double exitPipeY = (exitPipe[1][1] + exitPipe[1][3]) / 2.0;
        double exitPipeWidth = exitPipe[1][2] - exitPipe[1][0];
        double exitPipeHeight = exitPipe[1][3] - exitPipe[1][1];
        return playerX + playerWidth / 2 >= exitPipeX - exitPipeWidth / 2 &&
                playerX - playerWidth / 2 <= exitPipeX + exitPipeWidth / 2 &&
                playerY + playerHeight / 2 >= exitPipeY - exitPipeHeight / 2 &&
                playerY - playerHeight / 2 <= exitPipeY + exitPipeHeight / 2;
    }
    // Method to check if the player is touching the button momentarily
    public boolean checkIfButtonIsTouched() {
        double playerX = player.getX();
        double playerY = player.getY();
        double playerWidth = 20;
        double playerHeight = 20;
        double buttonX = (button[0] + button[2]) / 2.0;
        double buttonY = (button[1] + button[3]) / 2.0;
        double buttonWidth = button[2] - button[0];
        double buttonHeight = button[3] - button[1];

        boolean isTouching = playerX + playerWidth / 2 >= buttonX - buttonWidth / 2 &&
                playerX - playerWidth / 2 <= buttonX + buttonWidth / 2 &&
                playerY + playerHeight / 2 >= buttonY - buttonHeight / 2 &&
                playerY - playerHeight / 2 <= buttonY + buttonHeight / 2;

        return isTouching;
    }
    // Method to release the button so that it can be pressed again
    public void releaseButton() {
        isButtonBeingPressed = false; // Reset the flag when the button is released
    }
    // Method to press the button
    public void pressButton() {
        if (!isButtonBeingPressed && checkIfButtonIsTouched()) { // Condition to prevent button from being pressed multiple times
            buttonPressNum++;   // Increment the button press count
            isButtonBeingPressed = true; // Set the flag to true when the button is pressed
        }
    }
    // Method to update the door state based on the button press count
    public void updateDoorState() {
        int stageNumber = stage.getStageNumber();
        // Ensuring button press count is appropriate for the stage
        if (stageNumber == 3 && buttonPressNum >= 5) {
            isDoorOpen = true;
            buttonPressNum = 0;
        } else if (stageNumber != 3 && buttonPressNum >= 1) {
            isDoorOpen = true;
            buttonPressNum = 0;
        }
    }
    // Method to reset the stage (setting everything to initial state)
    public void restartStage() {
        player.respawn(new int[]{130,450});
        player.setVelocityX(0);
        player.setVelocityY(0);
        player.setOnGround(false);
        setDoorCurrentHeight(60);
        buttonPressNum = 0;
        isDoorOpen = false;

    }

    // Method for drawing all the rectangular objects
    private void drawRectangle(int[] rect) {
        StdDraw.filledRectangle(
                (rect[0] + rect[2]) / 2.0,  // center x
                (rect[1] + rect[3]) / 2.0,  // center y
                (rect[2] - rect[0]) / 2.0,  // half-width
                (rect[3] - rect[1]) / 2.0   // half-height
        );
    }
    public void draw() {
        // Drawing obstacles
        StdDraw.setPenColor(stage.getColor());
        for(int[] obstacle : obstacles) {
            drawRectangle(obstacle);
        }

        // Drawing button considering if it is being pressed
        if(!isButtonBeingPressed){
            StdDraw.setPenColor(BUTTON_TOP);
            drawRectangle(button);
        }
        StdDraw.setPenColor(BUTTON_FLOOR);
        drawRectangle(buttonFloor);

        // Drawing the pipes
        StdDraw.setPenColor(PIPE);
        drawRectangle(startPipe[0]);
        drawRectangle(startPipe[1]);
        drawRectangle(exitPipe[0]);
        drawRectangle(exitPipe[1]);

        // Drawing the door
        if (this.doorCurrentHeight > 0) {
            StdDraw.setPenColor(DOOR);
            drawRectangle(new int[]{door[0], door[1], door[2], (int) (door[1] + this.doorCurrentHeight)});
        }

        // Drawing the spikes
        for (int[] spike : spikes) {
            double centerX = (spike[0] + spike[2]) / 2.0;
            double centerY = (spike[1] + spike[3]) / 2.0;
            double width = spike[2] - spike[0];
            double height = spike[3] - spike[1];
            int[] s  = {spike[0], spike[1], spike[2], spike[3]};

            // Determine the orientation of the spike
            if (width > height) {
                // Horizontal spike
                if (spike[1] < 300) {
                    // Spike pointing down
                    StdDraw.picture(centerX, centerY, "Spikes.png", width, height, 180);
                }
                else {
                    // Spike pointing up
                    StdDraw.picture(centerX, centerY, "Spikes.png", width, height, 0);
                }
            }
            else {
                // Vertical spike
                if (spike[0] < 400) {
                    // Spike pointing right
                    StdDraw.picture(centerX, centerY, "Spikes.png", height, width, 90);
                }
                else {
                    // Spike pointing left
                    StdDraw.picture(centerX, centerY, "Spikes.png", height, width, 270);
                }
            }
        }
    }

    // Method for moving the player with respect to physics rules
    public void applyPhysicsAndUpdatePlayer() {
        Player player = this.player;
        double playerWidth = 20;
        double playerHeight = 20;

        // Applying gravity to player's velocity so it changes over time
        player.setVelocityY(player.getVelocityY() + stage.getGravity());

        // Get the current position of the player
        double currentX = player.getX();
        double currentY = player.getY();
        // Get the current velocity of the player
        double currentVelX = player.getVelocityX();
        double currentVelY = player.getVelocityY();
        // Calculate the next position based on the velocity
        double nextX = currentX + currentVelX;
        double nextY = currentY + currentVelY;

        // Determine if the player is colliding with an obstacle horizontally
        int[] collidingObstacleH = checkCollisionDetailed(nextX, currentY, playerWidth, playerHeight, this.obstacles);

        if (collidingObstacleH == null) {  // No horizontal collision detected
            if(checkPotentialDoorCollision(nextX,currentY)){ // Checking if the player is colliding with the door
                if(player.getDirection().equals("left"))  movePlayer('L'); // Letting player move left while touching to door
                else if(player.getDirection().equals("right") ) {     // Restricting player to move right while touching to door
                    finalX = currentX;
                    player.setVelocityX(0);
                }
            } else movePlayer('R');
        } else {  // Horizontal collision detected
            if (currentVelX > 0) { // Player was moving right so set player to the left side of the obstacle
                finalX = collidingObstacleH[0] - playerWidth / 2.0;
            } else if (currentVelX < 0) { // Player was moving left so set player to the right side of the obstacle
                finalX = collidingObstacleH[2] + playerWidth / 2.0;
            }
            player.setVelocityX(0);
        }

        // Determine if the player is colliding with an obstacle vertically
        player.setOnGround(false);
        int[] collidingObstacleV = checkCollisionDetailed(finalX, nextY, playerWidth, playerHeight, this.obstacles);

        if (collidingObstacleV == null) { // No vertical collision detected
            movePlayer('U'); // Move player vertically
        }
        else { // Vertical collision detected
            if (currentVelY < 0) {   // Player was moving down so set player to the top side of the obstacle
                finalY = collidingObstacleV[3] + playerHeight / 2.0;
                player.setOnGround(true); // Setting the player on ground after putting it onto the obstacle
            } else if (currentVelY > 0) { // Player was moving up so set player to the bottom side of the obstacle
                finalY = collidingObstacleV[1] - playerHeight / 2.0;
            }
            player.setVelocityY(0);
        }
        // Finally set the player's position to the final calculated position
        player.setX(finalX);
        player.setY(finalY);
    }

    // Method to get the coordinates of collided obstacle
    private int[] checkCollisionDetailed(double targetX, double targetY, double targetWidth, double targetHeight, int[][] objects) {
        double playerMinX = targetX - targetWidth / 2.0;
        double playerMaxX = targetX + targetWidth / 2.0;
        double playerMinY = targetY - targetHeight / 2.0;
        double playerMaxY = targetY + targetHeight / 2.0;

        for (int[] obj : objects) {
            double objMinX = obj[0];
            double objMinY = obj[1];
            double objMaxX = obj[2];
            double objMaxY = obj[3];

            if (playerMaxX > objMinX && playerMinX < objMaxX && playerMaxY > objMinY && playerMinY < objMaxY) {
                return obj;
            }
        }
        return null;
    }


}