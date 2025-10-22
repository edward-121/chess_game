package piece;
import chessboard.GamePanel;
import chessboard.Type;

public class pawn extends piece{
    public pawn(int color, int col, int row){
        super (color, col, row);

        type = Type.PAWN;

        if (color == GamePanel.WHITE) {
            image = getImage("white-pawn");
        } else {
            image = getImage("black-pawn");
        }
        
    }
    public boolean canMove(int targetCol, int targetRow){
        if(onBoard(targetCol, targetRow)&& isSameSquare(targetCol, targetRow)== false) {
           int moveValue;
            if(color == GamePanel.WHITE){
                moveValue = -1;
            }
            else {
                moveValue = 1;
            }

            occupiedsquare = occupiedsquare(targetCol, targetRow);

            if (targetCol == preCol && targetRow == preRow + moveValue && occupiedsquare ==null){
                return true;
            }
            //if piece hasnt moved yet
            if (targetCol == preCol && targetRow == preRow + moveValue*2 && occupiedsquare == null && moved==false
            && pieceIsOnStraightLine(targetCol, targetRow) == false) {
                return true;
            }
            //capturing
            if (Math.abs(targetCol - preCol)==1 && targetRow == preRow + moveValue && occupiedsquare !=null &&occupiedsquare.color != color){
                return true;
            }
            //en passant
            if (Math.abs(targetCol - preCol)==1 && targetRow == preRow + moveValue){
                for(piece piece: GamePanel.simpieces){
                    if (piece.col == targetCol && piece.row == preRow && piece.twoStepped == true){
                        occupiedsquare = piece;
                        return true;
                    }
                }
            }
        }
        return false;
    }
    @Override
     public piece copy() {
        pawn copyPawn = new pawn(this.color, this.col, this.row);

        // Copy any other relevant fields
        copyPawn.moved = this.moved;
        copyPawn.twoStepped = this.twoStepped;
        copyPawn.preCol = this.preCol;
        copyPawn.preRow = this.preRow;

        // If there are more fields that affect the pawn state, copy them here

        return copyPawn;
    }
}
