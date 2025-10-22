package chessboard;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JPanel;
import piece.piece;
import piece.pawn;
import piece.king;
import piece.queen;
import piece.knight;
import piece.rook;
import piece.bishop;


public class GamePanel extends JPanel implements Runnable{
    public static final int Width = 1080;
    public static final int Height = 1024;
    final int FPS = 60;
    Thread gameThread; //initialize thread
    chessboard board = new chessboard();
    mouse mouse = new mouse();
    engine engine = new engine();
    engine.Move suggestedMove;

//show pieces
    public static ArrayList<piece> pieces = new ArrayList<>();
    public static ArrayList<piece> simpieces = new ArrayList<>();
    ArrayList<piece> promoPieces = new ArrayList<> ();
    piece activeP, checkingP;
    public static piece castlingP;
//colors
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    int currentcolor = WHITE;
//bools
    boolean canMove = false;
    boolean validSquare = false;
    boolean promotion;
    boolean gameover;
    boolean stalemate;
    boolean positionwasanalyzed = false;

    public GamePanel() { //gamepanel
        setPreferredSize(new Dimension(Width, Height));
        setBackground(Color.BLACK);
        setPieces();
        copyPieces(pieces, simpieces);
        addMouseMotionListener(mouse);
        addMouseListener(mouse);
    }
    public void launchGame(){
        gameThread = new Thread(this); //call on thread
        gameThread.start();

    // Test for white rook at (3, 3)
    // List<int[]> moves = lm.allLegalMoves(Type.KING, GamePanel.WHITE, 4, 7);

    // System.out.println("Legal moves for white king at (0,4):");
    // if (moves.isEmpty()) {
    // System.out.println("No legal moves found for: " + Type.ROOK);
    // }
    // for (int[] move : moves) {
    //     System.out.println(" @ " + move[0] + ", " + move[1]);
    //  }
    }
    public void setPieces(){
        pieces.add(new pawn(WHITE, 0, 6));
        pieces.add(new pawn(WHITE, 1, 6));
        pieces.add(new pawn(WHITE, 2, 6));
        pieces.add(new pawn(WHITE, 3, 6));
        pieces.add(new pawn(WHITE, 4, 6));
        pieces.add(new pawn(WHITE, 5, 6));
        pieces.add(new pawn(WHITE, 6, 6));
        pieces.add(new pawn(WHITE, 7, 6));
        pieces.add(new rook(WHITE, 0, 7));
        pieces.add(new rook(WHITE, 7, 7));
        pieces.add(new knight(WHITE, 1, 7));
        pieces.add(new knight(WHITE, 6, 7));
        pieces.add(new bishop(WHITE, 2, 7));
        pieces.add(new bishop(WHITE, 5, 7));
        pieces.add(new king(WHITE, 4, 7));
        pieces.add(new queen(WHITE, 3, 7));
         
        pieces.add(new pawn(BLACK, 0, 1));
        pieces.add(new pawn(BLACK, 1, 1));
        pieces.add(new pawn(BLACK, 2, 1));
        pieces.add(new pawn(BLACK, 3, 1));
        pieces.add(new pawn(BLACK, 4, 1));
        pieces.add(new pawn(BLACK, 5, 1));
        pieces.add(new pawn(BLACK, 6, 1));
        pieces.add(new pawn(BLACK, 7, 1));
        pieces.add(new rook(BLACK, 0, 0));
        pieces.add(new rook(BLACK, 7, 0));
        pieces.add(new knight(BLACK, 1, 0));
        pieces.add(new knight(BLACK, 6, 0));
        pieces.add(new bishop(BLACK, 2, 0));
        pieces.add(new bishop(BLACK, 5, 0));
        pieces.add(new king(BLACK, 4, 0));
        pieces.add(new queen(BLACK, 3, 0));
     
    }

    private void copyPieces(ArrayList<piece> source, ArrayList<piece> target){
        target.clear();
        for (int i=0; i < source.size(); i++){
            target.add(source.get(i));
        }

    }
    private void update(){ //updates pieces

        if(promotion){
            promoting(); 
        } else if (gameover == false && stalemate == false){

        if(mouse.pressed){ //if pressed
            if(activeP == null){ //check if piece is picked  up
                    for(piece piece : simpieces) {
                        if (piece.color == currentcolor && //make sure its the right color
                            piece.col == mouse.x/chessboard.sq_size &&
                            piece.row == mouse.y/chessboard.sq_size){
                                activeP = piece;
                        }
                    }
            }
            else { //if piece is picked up , follow mouse
                simulate();
            }
        }
        if(mouse.pressed == false) {
            if(activeP != null){
                if(validSquare){
                //update piece list when one is deleted
                    copyPieces(simpieces, pieces);
                    activeP.updatePosition();
                    if(castlingP != null){
                        castlingP.updatePosition();
                        
                    }

                    if(isKingInCheck()&& isCheckmate()){
                       gameover=true; 
                    }
                    else if (isStalemate()&&isKingInCheck()==false){
                        stalemate = true;
                    }
                    else {
                   if (canPromote()){
                        promotion = true;
                        positionwasanalyzed = false;
                    } else {
                        changePlayer();
                        positionwasanalyzed = false;
                    }
                    }

                }
                else {
                    copyPieces(simpieces, pieces);
                    activeP.resetPosition();
                    activeP = null;
            }
        }
        }
    }
    if (!positionwasanalyzed){
    suggestedMove = engine.findBestMove(simpieces, currentcolor);//ts will always be running :skull:
    positionwasanalyzed = true;}
}
    private void simulate() {
        canMove = false;
        validSquare = false;

        copyPieces(pieces, simpieces);
        //check if piece is being held and obtain the active position

        if(castlingP != null){
            castlingP.col = castlingP.preCol;
            castlingP.x = castlingP.getX(castlingP.col);
            castlingP = null;
        }

       activeP.x = mouse.x - chessboard.hf_sq_size; //centers piece
       activeP.y = mouse.y - chessboard.hf_sq_size;
       activeP.col = activeP.getCol(activeP.x);
        activeP.row = activeP.getCol(activeP.y);
        //check for legal mvoe
        if(activeP.canMove(activeP.col, activeP.row)){
            canMove = true;

            if (activeP.occupiedsquare != null){
                simpieces.remove(activeP.occupiedsquare.getIndex());
            }
            checkCastling();
            if (isIllegal(activeP)== false && opponentCanCaptureKing()== false){
            validSquare = true;
            }

        }
    }
    private boolean isIllegal(piece king){
        if (king.type == Type.KING){
            for (piece piece : simpieces){
                if(piece != king && piece.color != king.color && piece.canMove(king.col, king.row)){
                   return true; 
                }
            }
        }

        return false;
    }

    private boolean opponentCanCaptureKing(){
        piece king = getKing(false);
        for (piece piece : simpieces){
            if(piece.color != king.color && piece.canMove(king.col, king.row)){
                return true;
            }
        }

        return false;
    }

    public boolean isKingInCheck(){
        piece king = getKing(true);
        
        if(activeP.canMove(king.col, king.row)){
            checkingP = activeP;
            return true;
        } else {
            checkingP = null;
        }

        return false;
    }
    private piece getKing(boolean opponent){
        piece king = null;
        for(piece piece : simpieces){
            if(opponent){
                if(piece.type == Type.KING && piece.color != currentcolor){
                    king= piece;
                }
            }
            else {
                if(piece.type == Type.KING && piece.color == currentcolor){
                    king = piece;
                }
            }
        }
        return king;
    }
    private boolean isCheckmate(){
        piece king = getKing(true);
        if (kingCanMove(king)){
            return false;
        } else {
            //check for blocks
            int colDiff = Math.abs(checkingP.col - king.col);//check the position of checking piece vs king
            int rowDiff = Math.abs(checkingP.row - king.row);

            if (colDiff == 0){//vertical attack
                if(checkingP.row<king.row){
                    for(int row = checkingP.row; row<king.row; row++){
                        for(piece piece : simpieces){
                            if(piece != king && piece.color != currentcolor && piece.canMove(checkingP.col, row)){
                                return false;
                            }
                        }
                    }
                }
                if(checkingP.row>king.row){
                    for(int row = checkingP.row; row>king.row; row--){
                        for(piece piece : simpieces){
                            if(piece != king && piece.color != currentcolor && piece.canMove(checkingP.col, row)){
                                return false;
                            }
                        }
                    }
                }
                
            } else if (rowDiff ==0){//horizontal attack
                if(checkingP.col<king.col){
                    for(int col = checkingP.col; col<king.col; col++){
                        for(piece piece : simpieces){
                            if(piece != king && piece.color != currentcolor && piece.canMove(col, checkingP.row)){
                                return false;
                            }
                        }
                    }
                }
                if(checkingP.col>king.col){
                    for(int col = checkingP.col; col>king.col; col--){
                        for(piece piece : simpieces){
                            if(piece != king && piece.color != currentcolor && piece.canMove(col, checkingP.row)){
                                return false;
                            }
                        }
                    }
                }
            } else if (colDiff == rowDiff){//diagonal attack 
                if (checkingP.row < king.row){
                    if (checkingP.col < king.col){
                        for(int col = checkingP.col, row = checkingP.row; col<king.col; col++, row++){
                        for(piece piece : simpieces){
                            if(piece != king && piece.color != currentcolor && piece.canMove(col, row)){
                                return false;
                            }
                        }
                    }
                    }
                    if (checkingP.col > king.col){
                        for(int col = checkingP.col, row = checkingP.row; col>king.col; col--, row++){
                        for(piece piece : simpieces){
                            if(piece != king && piece.color != currentcolor && piece.canMove(col, row)){
                                return false;
                            }
                        }
                    }
                    }
                }
                if (checkingP.row > king.row){
                    if (checkingP.col < king.col){
                        for(int col = checkingP.col, row = checkingP.row; col>king.col; col++, row--){
                        for(piece piece : simpieces){
                            if(piece != king && piece.color != currentcolor && piece.canMove(col, row)){
                                return false;
                            }
                        }
                    }
                    }
                    if (checkingP.col > king.col){
                        for(int col = checkingP.col, row = checkingP.row; col<king.col; col--, row--){
                        for(piece piece : simpieces){
                            if(piece != king && piece.color != currentcolor && piece.canMove(col, row)){
                                return false;
                            }
                        }
                    }
                    }
                }
            } else {//knight
            
            
            }
            }
        
        return true;
    }
    private boolean kingCanMove(piece king){
        if(isValidMove(king, -1, -1)) {return true;}
         if(isValidMove(king, 0, -1)) {return true;}
          if(isValidMove(king, 1, -1)) {return true;}
           if(isValidMove(king, -1, 1)) {return true;}
            if(isValidMove(king, -1, 0)) {return true;}
             if(isValidMove(king, 0, 1)) {return true;}
              if(isValidMove(king, 1, 0)) {return true;}
               if(isValidMove(king, 1, 1)) {return true;}
               
               return false;
    }
    private boolean isValidMove(piece king, int colPlus, int rowPlus){
        boolean isValidMove = false;

        king.col += colPlus;
        king.row += rowPlus;
        if (king.canMove(king.col, king.row)){
            if (king.occupiedsquare != null ){
                simpieces.remove(king.occupiedsquare.getIndex());
            }
            if (isIllegal(king) == false){
                isValidMove = true;
            }
        }
        king.resetPosition();
        copyPieces(pieces, simpieces);
        return isValidMove;
    }
    private boolean isStalemate(){//shitty ass stalemate checker
        int count = 0;
        for (piece piece : simpieces){

            if(piece.color != currentcolor){
                count++;
            }
        }
        if (count == 1) {
            if(kingCanMove(getKing(true))==false){
                return true;
            }
        }
        return false;
    }


    private void checkCastling() {
        if (castlingP != null) {
            if (castlingP.col == 0){
                castlingP.col += 3;
            } else if (castlingP.col ==7 ){
                castlingP.col -= 2;
            }
            castlingP.x = castlingP.getX(castlingP.col);
        }
    }
    private void changePlayer(){
        if (currentcolor == WHITE){
            currentcolor = BLACK;

            for (piece piece : pieces){
                if(piece.color == WHITE){
                    piece.twoStepped = false;
                }
            }

        } else {
            currentcolor = WHITE;
            for (piece piece : pieces){
                if(piece.color == WHITE){
                    piece.twoStepped = false;
                }
            }
        }
        activeP = null;
    }
    private boolean canPromote() {
        if (activeP.type == Type.PAWN) {
            if (currentcolor == WHITE && activeP.row == 0 || currentcolor == BLACK && activeP.row ==7){
                promoPieces.clear();
                promoPieces.add(new rook(currentcolor, 9, 2));
                  promoPieces.add(new bishop(currentcolor, 9, 3));
                  promoPieces.add(new knight(currentcolor, 9, 4));
                  promoPieces.add(new queen(currentcolor, 9,5));
                  return true;
            }
        }
        return false;
    }
    private void promoting(){
        if(mouse.pressed){
            for(piece piece : promoPieces){
                if(piece.col == mouse.x/chessboard.sq_size && piece.row==mouse.y/chessboard.sq_size){
                    switch(piece.type){
                        case ROOK: simpieces.add(new rook(currentcolor, activeP.col, activeP.row)); break;
                        case KNIGHT: simpieces.add(new knight(currentcolor, activeP.col, activeP.row)); break;
                        case BISHOP: simpieces.add(new bishop(currentcolor, activeP.col, activeP.row)); break;
                        case QUEEN: simpieces.add(new queen(currentcolor, activeP.col, activeP.row)); break;
                        default: break;
                    }
                    simpieces.remove(activeP.getIndex());
                    copyPieces(simpieces, pieces);
                    activeP = null;
                    promotion = false;
                    changePlayer();
                }
            }
        }
    }

    public void paintComponent(Graphics g) { //paint the screen
        super.paintComponent(g); 

        Graphics2D g2 = (Graphics2D) g;
        board.draw(g2);

        for (piece p : simpieces){
            p.draw(g2);
        }
        if (activeP != null){
            if (canMove){
                if(isIllegal(activeP)||opponentCanCaptureKing()){
            g2.setColor(Color.red);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            g2.fillRect(activeP.col*chessboard.sq_size, activeP.row*chessboard.sq_size, chessboard.sq_size, chessboard.sq_size);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                } else {
            g2.setColor(Color.BLACK);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            g2.fillRect(activeP.col*chessboard.sq_size, activeP.row*chessboard.sq_size, chessboard.sq_size, chessboard.sq_size);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }
        }
            activeP.draw(g2);
        }
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.white);
        g2.setFont(new Font(null, Font.PLAIN, 40));

        if (promotion){
            g2.drawString("Promote to:", 820, 150);
            for (piece piece : promoPieces){
                g2.drawImage(piece.image, piece.getX(piece.col), piece.getY(piece.row),
                chessboard.sq_size, chessboard.sq_size, null);
            }
        } else {
            if (currentcolor == WHITE){
            g2.drawString("White's Move", 820,500);
        //    System.out.println(suggestedMove);
            if (suggestedMove != null) {
                String moveText = suggestedMove.piece.type + "@" + 
                (char) ('A' + suggestedMove.precol) + (8-suggestedMove.prerow) + " to " +
                        (char) ('A' + suggestedMove.targetCol) +
                        (8 - suggestedMove.targetRow);

                g2.setFont(new Font("Arial", Font.PLAIN, 30));
                g2.setColor(Color.YELLOW);
                g2.drawString("Suggestion: ", 800, 540);
                g2.drawString(moveText , 800, 580);
                g2.drawString("Score: "+suggestedMove.bestScore, 800, 620);
            }
        } else {
            g2.drawString("Black's Move", 820,500);
            if (suggestedMove != null) {
                String moveText = suggestedMove.piece.type + "@" + 
                (char) ('A' + suggestedMove.precol) + (8-suggestedMove.prerow) + " to " +
                        (char) ('A' + suggestedMove.targetCol) +
                        (8 - suggestedMove.targetRow);

                g2.setFont(new Font("Arial", Font.PLAIN, 30));
                g2.setColor(Color.YELLOW);
                g2.drawString("Suggestion: ", 800, 540);
                g2.drawString(moveText , 800, 580);
                g2.drawString("Score: "+suggestedMove.bestScore, 800, 620);
            }
        }
        
        }
          if(gameover){
            String s = "";
        if(currentcolor == WHITE){
            s= "WHITE WINS";
        } else {
            s = "BLACK WINS";
        }
        g2.setFont(new Font("Arial", Font.PLAIN, 90));
        g2.setColor(Color.green);
        g2.drawString(s, 200, 420);
        }   
        if(stalemate){
        g2.setFont(new Font("Arial", Font.PLAIN, 90));
        g2.setColor(Color.green);
        g2.drawString("Draw", 200, 420);   
        }   
    }
    

    @Override
    public void run() {
        //game loop,updates 60 times per second
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime)/drawInterval;
            lastTime = currentTime;
            if (delta >= 1) {
                update();//updates game
                repaint();//calls on paintcomponent
                delta--;
            }
        }

    }
}