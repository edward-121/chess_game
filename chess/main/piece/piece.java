package piece;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import chessboard.GamePanel;
import chessboard.chessboard;
import chessboard.Type;
public abstract class piece {
     protected static Map<String, BufferedImage> imageCache = new HashMap<>();
    public BufferedImage image;

    protected BufferedImage getImage(String imgpath) {
        if (!imageCache.containsKey(imgpath)) {
            try {
                BufferedImage img = ImageIO.read(getClass().getResourceAsStream(imgpath + ".png"));
                imageCache.put(imgpath, img);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return imageCache.get(imgpath);
    }
    public Type type;
    public int x, y;
    public int col, row, preCol, preRow;
    public int color;
    public piece occupiedsquare;
    public boolean moved, twoStepped;

    public piece (int color, int col, int row ){
        this.color = color;
        this.col = col;
        this.row = row;
        x = getX(col);
        y= getY(row);
        preCol = col;
        preRow = row;
    }
    
    public int getY(int col) {
        return col * chessboard.sq_size;
    }
    public int getX(int row) {
        return row * chessboard.sq_size;
    }
    public int getCol (int x) {
        return (x + chessboard.hf_sq_size)/chessboard.sq_size;
    }
    public int getRow (int y) {
        return (y + chessboard.hf_sq_size)/chessboard.sq_size;
    }
    public int getIndex(){//find the piece that gets deleted
        for (int index = 0; index < GamePanel.simpieces.size(); index ++){
            if (GamePanel.simpieces.get(index)==this){
                return index;
            }
        }
        return 0;
    }

    public void updatePosition(){
//EN PASSANT
        if(type == Type.PAWN){
            if(Math.abs(row-preRow)==2){
                twoStepped = true;
            }
        }

        x = getX(col);
        y = getY(row);
        preCol = getCol(x);
        preRow = getRow(y);
        moved = true;
    }
    public void resetPosition(){
        col = preCol;
        row = preRow;
        x = getX(col);
        y = getY(row);
    }
    public boolean canMove(int targetCol, int targetRow){
        return false;
    }
        public boolean onBoard(int targetCol, int targetRow){
        if(targetCol >= 0 && targetCol <= 7 && targetRow >=0 && targetRow <=7){
                return true;
        }
        return false;
    }

        public boolean isSameSquare (int targetCol, int targetRow){
            if (targetCol==preCol&&targetRow ==preRow){
                return true;
            } 
            return false;
        }
    public piece occupiedsquare(int targetCol, int targetRow) {
        for(piece piece : GamePanel.simpieces){
            if (piece.col == targetCol && piece.row == targetRow && piece != this){
                return piece;
            }
        }
        return null;
    }
    public boolean isvalidsquare (int targetCol, int targetRow){
        occupiedsquare = occupiedsquare(targetCol, targetRow);
        if (occupiedsquare ==null){
            return true;//if empty return true
        } else {//othewise check if piece is capturable
            if (occupiedsquare.color != this.color){
                return true;
            } else {
                occupiedsquare = null;
            }
        }
        return false;
    }
    public boolean pieceIsOnStraightLine(int targetCol, int targetRow){
        //moving left
        for (int c = preCol-1; c > targetCol; c--){
            for (piece piece : GamePanel.simpieces){
            if (piece.col == c&&piece.row == targetRow){
                occupiedsquare = piece;
                return true;
                }
            }
        }
        //moving right
        for (int c = preCol+1; c < targetCol; c++){
            for (piece piece : GamePanel.simpieces){
            if (piece.col == c&&piece.row == targetRow){
                occupiedsquare = piece;
                return true;
                }
            }
        }//moving up
        for (int r = preRow-1; r > targetRow; r--){
            for (piece piece : GamePanel.simpieces){
            if (piece.col == targetCol &&piece.row == r){
                occupiedsquare = piece;
                return true;
                }
            }
        }//moving down
        for (int r = preRow+1; r < targetRow; r++){
            for (piece piece : GamePanel.simpieces){
            if (piece.col == targetCol &&piece.row == r){
                occupiedsquare = piece;
                return true;
                }
            }
        }
        return false;
    }
    public boolean pieceIsOnDiagonal(int targetCol, int targetRow){
        if (targetRow < preRow){ 
        //up left
        for (int c = preCol-1; c > targetCol; c--){
            int diff = Math.abs(c-preCol);
            for (piece piece : GamePanel.simpieces){
                if (piece.col == c && piece.row == preRow - diff){
                    occupiedsquare = piece;
                    return true;
                }
            }
        }
        //up right
        for (int c = preCol+1; c < targetCol; c++){
            int diff = Math.abs(c-preCol);
            for (piece piece : GamePanel.simpieces){
                if (piece.col == c && piece.row == preRow - diff){
                    occupiedsquare = piece;
                    return true;
                }
            }
        }
        }
        if (targetRow > preRow){
        //down left
        for (int c = preCol-1; c > targetCol; c--){
            int diff = Math.abs(c-preCol);
            for (piece piece : GamePanel.simpieces){
                if (piece.col == c && piece.row == preRow + diff){
                    occupiedsquare = piece;
                    return true;
                }
            }
        }
        //down right
        for (int c = preCol+1; c < targetCol; c++){
            int diff = Math.abs(c-preCol);
            for (piece piece : GamePanel.simpieces){
                if (piece.col == c && piece.row == preRow + diff){
                    occupiedsquare = piece;
                    return true;
                }
            }
        }
        }
        
        return false;
    }
public abstract piece copy();   
    public void draw(Graphics2D g2) {
        g2.drawImage(image, x, y, chessboard.sq_size, chessboard.sq_size, null);
    }
}
