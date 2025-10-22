package piece;

import chessboard.GamePanel;
import chessboard.Type;

public class queen extends piece{
    public queen (int color, int col, int row){
        super (color, col, row);

        type = Type.QUEEN;

        if (color == GamePanel.WHITE){
            image = getImage("white-queen");
        } else {
            image = getImage("black-queen");
        }
    }
    public boolean canMove(int targetCol, int targetRow){
        if(onBoard(targetCol, targetRow)&& isSameSquare(targetCol, targetRow)== false) {
            //target square - original square is equal to 1 (any direction)
            if (targetCol == preCol || targetRow == preRow) {
                if (isvalidsquare(targetCol, targetRow)&& pieceIsOnStraightLine(targetCol, targetRow)==false){
                    return true;
                }
            } else if (((double)Math.abs(targetCol - preCol)/(double)Math.abs(targetRow - preRow))==1){
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
