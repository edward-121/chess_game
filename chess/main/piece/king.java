package piece;

import chessboard.GamePanel;
import chessboard.Type;

public class king extends piece{
    public king (int color, int col, int row){
        super (color, col, row);

        type = Type.KING;

        if (color == GamePanel.WHITE){
            image = getImage("white-king");
        } else {
            image = getImage("black-king");
        }
    }
    public boolean canMove(int targetCol, int targetRow){
        if(onBoard(targetCol, targetRow)&& isSameSquare(targetCol, targetRow)== false) {
            //target square - original square is equal to 1 (any direction)
            if (Math.abs(targetCol - preCol) + Math.abs(targetRow - preRow)==1 ||
                Math.abs(targetCol - preCol) * Math.abs(targetRow - preRow)==1){
                if (isvalidsquare(targetCol, targetRow)){
                    return true;
                }//castling
            }
            if (moved==false){
                //kingside
                if (targetCol == preCol + 2 && targetRow == preRow && pieceIsOnStraightLine(targetCol, targetRow)==false){
                    for (piece piece : GamePanel.simpieces){
                        if(piece.col == preCol +3 && piece.row == preRow &&piece.moved == false){
                            GamePanel.castlingP = piece;
                            return true;
                        }
                    }
                }//queenside
                if (targetCol == preCol -2  && targetRow == preRow && pieceIsOnStraightLine(1, targetRow)==false){
                    piece p[] = new piece[2];
                    for (piece piece : GamePanel.simpieces){
                        if (piece.col == preCol-3 &&piece.row == targetRow){
                            p[0]= piece;
                        }
                        if (piece.col == preCol-4 &&piece.row == targetRow){
                            p[1]= piece;
                        }
                        if (p[0] == null && p[1] != null && p[1].moved == false){
                            GamePanel.castlingP=p[1];
                            //implemkent not castling thru check
                            return true;
                        }
                    }

                }
            }
        }
    return false;
    }
        @Override
    public piece copy() {
        king copyKing = new king(this.color, this.col, this.row);
        copyKing.moved = this.moved;
        copyKing.preCol = this.preCol;
        copyKing.preRow = this.preRow;

        // Copy other relevant fields if needed (like castling flags)

        return copyKing;
    }
}
