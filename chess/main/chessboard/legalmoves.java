package chessboard;
import java.util.ArrayList;
import java.util.List;
import piece.piece;


public class legalmoves {

    public List<int[]> allLegalMoves(Type type, int currentColor, int preCol, int preRow) {
        List<int[]> legalMoves = new ArrayList<>();
        // Find the piece at the given location
        piece targetPiece = null;
        for (piece p : GamePanel.simpieces) {
            if (p.col == preCol && p.row == preRow && p.color  == currentColor && p.type == type) {
                targetPiece = p;
                               System.out.println(targetPiece);
                                 System.out.println(p.color + " + " + currentColor);
                break;
            }
        }

        if (targetPiece == null) {
            return legalMoves; // No piece found
        }

        // Try every square on the board
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                // Skip the same square
                if (col == preCol && row == preRow) continue;
                //System.out.println(targetPiece + " " + targetPiece.col + " " + + targetPiece.row + " " + targetPiece.color);
                // Call canMove() and also check for king safety if needed
                if (targetPiece.canMove(col, row)) {
                    // Simulate move to check legality (e.g. king safety)
                    int oldCol = targetPiece.col;
                    int oldRow = targetPiece.row;

                    targetPiece.col = col;
                    targetPiece.row = row;

                    boolean legal = !opponentCanCaptureKing(currentColor);

                    // Undo move
                    targetPiece.col = oldCol;
                    targetPiece.row = oldRow;

                    if (legal) {
                        legalMoves.add(new int[]{col, row});
                    }
                }
            }
        }

        return legalMoves;
    }

    // Check if current player's king is threatened after a move
    private boolean opponentCanCaptureKing(int currentColor) {
        piece king = getKing(currentColor);
        if (king == null) return true;

        for (piece p : GamePanel.simpieces) {
            if (p.color != currentColor && p.canMove(king.col, king.row)) {
                return true;
            }
        }
        return false;
    }

    // Get king of the specified color
    private piece getKing(int color) {
        for (piece p : GamePanel.simpieces) {
            if (p.type == Type.KING && p.color == color) {
                return p;
            }
        }
        return null;
    }
}

