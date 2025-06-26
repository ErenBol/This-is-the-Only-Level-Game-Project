// name surname: İbrahim Eren Bol
// student ID: 2023400093

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class IbrahimErenBol {
    public static void main(String[] args){

        int nullButton = -1;

        // Given Stages
        Stage s1 = new Stage(-0.45, 3.65,10,0,KeyEvent.VK_RIGHT,KeyEvent.VK_LEFT,KeyEvent.VK_UP,"Arrow keys are required","Arrow keys move player ,press button and enter the second pipe"); // normal game
        Stage s2 = new Stage(-0.45, 3.65,10,1,KeyEvent.VK_LEFT,KeyEvent.VK_RIGHT,KeyEvent.VK_UP,"Not always straight forward","Right and left buttons reversed"); // Reversed Buttons
        Stage s3 = new Stage(-2, 3.65, 24,2,KeyEvent.VK_RIGHT,KeyEvent.VK_LEFT,nullButton,"A bit bouncy here","You jump constantly"); // bouncing
        Stage s4 = new Stage(-0.45, 3.65,10,3,KeyEvent.VK_RIGHT,KeyEvent.VK_LEFT,KeyEvent.VK_UP,"Never gonna give you up","Press button 5 times "); //
        // Add a new stage here
        Stage s5 = new Stage(-0.10,3,10,4,KeyEvent.VK_RIGHT,KeyEvent.VK_LEFT,KeyEvent.VK_UP,"Floating!","You can jump and move in the air");

        // Add the stages to the arraylist
        ArrayList<Stage> stages = new ArrayList<Stage>();
        stages.add(s1);
        stages.add(s2);
        stages.add(s3);
        stages.add(s4);
        stages.add(s5);

        // Draw the game area
        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(800, 600);
        StdDraw.setXscale(0, 800);
        StdDraw.setYscale(0, 600);

        // Start the game
        Game game = new Game(stages);
        game.play();
    }
}
