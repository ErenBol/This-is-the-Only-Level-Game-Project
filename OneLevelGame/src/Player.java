// name surname: Ibrahim Eren Bol
// student ID: 2023400093

public class Player {

    // Instance variables
    private double x; // Player's x-coordinate
    private double y; // Player's y-coordinate
    private double width = 20; // Player's width
    private double height = 20; // Player's height
    private double velocityY; // Player's vertical velocity
    private double velocityX; // Player's horizontal velocity
    private boolean isOnGround = false; // Flag to check if player is on the ground
    private String direction= "right"; // Player's direction

    // Constructor
    public Player(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /// Setters
    public void setX(double x) {
        this.x = x;
    } // Set the x-coordinate of the player

    public void setY(double y) {
        this.y = y;
    } // Set the y-coordinate of the player

    public void setVelocityX(double velocityX) {this.velocityX = velocityX;} // Set the horizontal velocity of the player

    public void setVelocityY(double velocityY) {this.velocityY = velocityY;} // Set the vertical velocity of the player

    public void setDirection(String direction) {
        this.direction = direction;
    } // Set the direction of the player

    public void setOnGround(boolean onGround) { this.isOnGround = onGround; } // Set the onGround status of the player


    // Getters
    public double getX() {
        return x;
    } // Get the x-coordinate of the player

    public double getY() {
        return y;
    } // Get the y-coordinate of the player

    public double getVelocityY() {
        return velocityY;
    } // Get the vertical velocity of the player

    public double getVelocityX() {return velocityX;} // Get the horizontal velocity of the player

    public String getDirection() {
        return direction;
    } // Get the direction of the player

    public boolean isOnGround() { return isOnGround; } // Check if the player is on the ground


    // Instance methods

    // Method to respawn the player at a given spawn point and reset the velocity
    public void respawn(int[] spawnPoint) {
        if (spawnPoint.length == 2) {
            this.x = spawnPoint[0];
            this.y = spawnPoint[1];
            setVelocityX(0);
            setVelocityY(0);
            setOnGround(false);
        }
    }

    // Method to jump if the player is on the ground
    public void jump(double jumpVelocity) {
        if (isOnGround) {
            this.velocityY = jumpVelocity;
            isOnGround = false;
        }
    }

    // Method to update the player according to the direction
    public void draw(){
        if (direction.equals("left")) {
            StdDraw.picture(x, y, "ElephantLeft.png", width, height);
        } else if (direction.equals("right")) {
            StdDraw.picture(x, y, "ElephantRight.png", width, height);
        }
    }

}