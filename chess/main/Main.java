import javax.swing.JFrame;

import chessboard.GamePanel;

public class Main {
    
    public static void main(String[]args){
        
        JFrame window = new JFrame ("Chess");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //stops on window closing
        window.setResizable(false); //not resizable

        GamePanel gp = new GamePanel(); //importingthe gamepanel
        window.add(gp); //adding the gamepanel
        window.pack(); //packing to the screen?

        window.setLocationRelativeTo(null); //centers the window
        window.setVisible(true); //visibility

        gp.launchGame(); //runs backend
    }
}