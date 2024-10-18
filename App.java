import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        // dimensions of the size of the app
        int boardWidth = 360; //
        int boardHeight = 640;

        JFrame frame = new JFrame("Flappy Bird");
        //frame.setVisible(true);
        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        frame.pack();
        flappyBird.requestFocus(); // ready to use input from the keyboarc
        frame.setVisible(true);
        


        
    }
}
