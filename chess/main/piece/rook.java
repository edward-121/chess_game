package piece;

import chessboard.GamePanel;
import chessboard.Type;

public class rook extends piece{
    public rook (int color, int col, int row){
        super (color, col, row);

        type = Type.ROOK;

        if (color == GamePanel.WHITE){
            image = getImage("white-rook");
        } else {
            image = getImage("black-rook");
        }
    }
    public boolean canMove(int targetCol, int targetRow){
        if(onBoard(targetCol, targetRow)&& isSameSquare(targetCol, targetRow)== false) {
            //target square - original square is equal to 1 (any direction)
            if (targetCol == preCol || targetRow == preRow) {
                if (isvalidsquare(targetCol, targetRow)&& pieceIsOnStraightLine(targetCol, targetRow)==false){
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
