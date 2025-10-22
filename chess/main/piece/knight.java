package piece;

import chessboard.GamePanel;
import chessboard.Type;

public class knight extends piece{
    public knight (int color, int col, int row){
        super (color, col, row);

        type = Type.KNIGHT;
        
        if (color == GamePanel.WHITE){
            image = getImage("white-knight");
        } else {
            image = getImage("black-knight");
        }
    }
    public boolean canMove(int targetCol, int targetRow){
        if(onBoard(targetCol, targetRow)&& isSameSquare(targetCol, targetRow)== false) {
            //target square - original square is equal to 1 (any direction)
            if (Math.abs((targetCol - preCol) * (targetRow - preRow))==2) {
                if (isvalidsquare(targetCol, targetRow)){
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
