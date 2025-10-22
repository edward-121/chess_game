package piece;

import chessboard.GamePanel;
import chessboard.Type;

public class bishop extends piece{
    public bishop (int color, int col, int row){
        super (color, col, row);

        type = Type.BISHOP;
        
        if (color == GamePanel.WHITE){
            image = getImage("white-bishop");
        } else {
            image = getImage("black-bishop");
        }
    }
    public boolean canMove(int targetCol, int targetRow){
        if(onBoard(targetCol, targetRow)&& isSameSquare(targetCol, targetRow)== false) {
            //target square - original square is equal to 1 (any direction)
            if ((targetCol != preCol)&&(targetRow != preRow)&&((double) Math.abs(targetCol - preCol)/ (double)Math.abs(targetRow - preRow))==1) {
                if (isvalidsquare(targetCol, targetRow)&& pieceIsOnDiagonal(targetCol, targetRow)==false){
                    return true;
                }
            }
        }
    return false;
    }
    @Override
        public piece copy() {
        rook copyRook = new rook(this.color, this.col, this.row);
        copyRook.moved = this.moved;
        copyRook.preCol = this.preCol;
        copyRook.preRow = this.preRow;

        // Copy any other fields that affect rook state here

        return copyRook;
    }
}
