import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;



public class FlappyBird extends JPanel implements ActionListener,KeyListener   {
    int boardWidth = 360;
    int boardHeight = 640;

    // Images

    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    // Bird

    int birdX = boardWidth / 8;
    int birdY = boardHeight / 2;
    int birdWidth = 34;
    int birdHeight = 24;

    class Bird{
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img){
            this.img = img;
        }
    }

    //Pipes
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe{
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false; // this keeps track if the bird passed the pipe and keeps track of score
        
        Pipe(Image img){ // when you make a new object (Pike), this makes it need a picture no matter what
            this.img = img;
        }


    }




    // game logic
    Bird bird;
    int velocityX = -4; // this moves the pipes to the left speed
    int velocityY = 0; // game starts with bird going downwards
    int gravity = 1;


    ArrayList<Pipe> pipes;
    Random random = new Random();

    // control at what time an action happens

    Timer gameLoop; 
    Timer placePipesTimer; 

    boolean gameOver = false;
    double score = 0;




    FlappyBird (){
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        //setBackground(Color.blue);
        setFocusable(true);// this allows the object to wake up when someone is  typing or using it
        addKeyListener(this); // this is used to listen to the 3 key methods

        // load images
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        //bird
        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();

        // placePipesTimer 
        // Every 1.5 second, a new pipe appears
        placePipesTimer = new Timer(1500, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                placePipes();
            }

        });

        placePipesTimer.start();

        // game timer
        gameLoop = new Timer(1000/60,this); //1000/60 = 16.6
        gameLoop.start();

    }

    public void placePipes(){ // this method creates a new top pipe and stores it to the list
      
      int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random()* (pipeHeight/2));
      int openingSpace = boardHeight/4;
      Pipe topPipe = new Pipe(topPipeImg);
      topPipe.y = randomPipeY;
      pipes.add(topPipe); 

      // this creates the bottom pipe and positions it on top
      
      Pipe bottomPipe = new Pipe(bottomPipeImg); 
      bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
      pipes.add(bottomPipe);
    }





    // this method is used to update whenever the screen needs to be updated
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    
    public void draw(Graphics g){    
        // draws background
        g.drawImage(backgroundImg,0,0,boardWidth,boardHeight,null);
        // draws bird
        g.drawImage(bird.img,bird.x,bird.y,bird.width,bird.height,null);
        //draws pipes
        // draws all the pipes one by one, gets the image and position of each pipe, draws the image on the screen where the pipe should be 
        for(int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width,pipe.height,null);
        }

        // score
        g.setColor(Color.white);
        g.setFont(new Font("Arial",Font.PLAIN,32));
        if(gameOver){
            g.drawString("Game Over: " + String.valueOf((int)score),10,35);
        }
        else{
            g.drawString(String.valueOf((int) score),10,35);

        }   





    }

    public void move(){
        // bird
       velocityY = velocityY + gravity;
        bird.y= bird.y + velocityY; // update the bird's position based of speed
        birdY = Math.max(bird.y,0); // makes sure the bird does not go off screen

        // pipes
        for(int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.x = pipe.x + velocityX;

        if(!pipe.passed && bird.x > pipe.x + pipeWidth ){ // checks to see if the pipe has passed the pipes            pipe.passed = true;
            score = score + 0.5;
            pipe.passed = true;
        }


            if(collision(bird,pipe)){
                gameOver = true;
            }




        }

        if(bird.y > boardHeight){ // if the vertical height is greater than the board height, it collides, game over
            gameOver = true;
        }






    }

    public boolean collision(Bird a, Pipe b){
        return a.x < b.x + b.width &&
        a.x + a.width > b.x &&
        a.y < b.y + b.height &&
        a.y + a.height > b.y;

    }








    // whenever you click a button this method is triggred. this makes whatever on the screen move,
    // and the screen updates
    @Override
    public void actionPerformed(ActionEvent e) { // when you want an action to happen
        move(); // changes the position of an object
        repaint(); // updates the screen when the character is in a new postion 
        if(gameOver){
            placePipesTimer.stop(); // when bird falls, pipes stop apperaing
            gameLoop.stop(); // stop repainting and updating frames of game


        }
    }

    
   

    @Override

    // this makes the spacebar go upward
    public void keyPressed(KeyEvent e) { // this method is called when a key is pressed
        if(e.getKeyCode() == KeyEvent.VK_SPACE){ // checks to see what key was pressed
            velocityY = -9;

            if(gameOver){ // restart the game if you lose
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                gameOver = false;
                score = 0;
                gameLoop.start();
                placePipesTimer.start();
            }


        }
    }
    
    @Override
   
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

   




    













    
}
