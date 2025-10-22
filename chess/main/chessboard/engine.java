package chessboard;

import java.util.ArrayList;
import piece.piece;

public class engine {

    private static final int MAX_DEPTH = 3;

    public Move findBestMove(ArrayList<piece> board, int currentColor) {
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;
        for (piece p : board) {
            if (p.color != currentColor) continue;

            for (int col = 0; col < 8; col++) {
                for (int row = 0; row < 8; row++) {
                   if (!p.isSameSquare(col, row) && p.canMove(col, row)) {
                ArrayList<piece> testBoard = deepCopyBoard(board);
                piece movingPiece = findPiece(testBoard, p, board);
                simulateMove(testBoard, movingPiece, col, row);

                int score = minimax(testBoard, MAX_DEPTH - 1, false, currentColor, Integer.MIN_VALUE, Integer.MAX_VALUE);
        System.out.println("Testing " + p.type + " at (" + p.col + "," + p.row + ") to " + (char) ('A' + col) + "" + (8-row) + " with score: " + score);

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = new Move(p, p.col, p.row, col, row, bestScore);
                }
            }
                }
            }
            if (bestMove == null) {
                            System.out.println("No legal/good moves found.");
                        }
        }

        return bestMove;
    }

    private int minimax(ArrayList<piece> board, int depth, boolean currentPlayer, int originalColor, int alpha, int beta) {
        if (depth == 0) {
            return evaluate(board, originalColor);
        }
        int currentTurnColor = currentPlayer ? originalColor : 1 - originalColor;
        int bestScore = currentPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (piece p : board) {
            if (p.color != currentTurnColor) continue;
            if ((currentPlayer && p.color != originalColor) || (!currentPlayer && p.color == originalColor)) {
                continue;
            }

            for (int col = 0; col < 8; col++) {
                for (int row = 0; row < 8; row++) {
                    
                   ArrayList<piece> newBoard = deepCopyBoard(board);
                  piece movingPiece = findPiece(newBoard, p, board);

                  if (movingPiece != null && !movingPiece.isSameSquare(col, row) && movingPiece.canMove(col, row)) {

                        simulateMove(newBoard, movingPiece, col, row);

                        int score = minimax(newBoard, depth - 1, !currentPlayer, originalColor, Integer.MIN_VALUE, Integer.MAX_VALUE);

                        if (currentPlayer) {
                        bestScore = Math.max(bestScore, score);
                        alpha = Math.max(alpha, bestScore);
                         } else {
                        bestScore = Math.min(bestScore, score);
                        beta = Math.min(beta, bestScore);
                    }

                    }
                }
            }
        }

        return bestScore;
    }
private ArrayList<piece> deepCopyBoard(ArrayList<piece> board) {
    ArrayList<piece> copy = new ArrayList<>();
    for (piece p : board) {
        copy.add(p.copy());
    }
    return copy;
}
    private piece simulateMove(ArrayList<piece> board, piece p, int newCol, int newRow) {
        piece captured = null;
        for (piece other : board) {
            if (other.col == newCol && other.row == newRow && other != p) {
                captured = other;
                board.remove(other);
                break;
            }
        }

        p.col = newCol;
        p.row = newRow;
        return captured;
    }

private piece findPiece(ArrayList<piece> board, piece target, ArrayList<piece> originalBoard) {
    int index = originalBoard.indexOf(target);
    if (index != -1 && index < board.size()) {
        return board.get(index);
    }
    return null;
}


    private int evaluate(ArrayList<piece> board, int color) {//yummy evaluatoin
        int score = 0;
        for (piece p : board) {//material eval
            int value = getPieceValue(p.type);
            if (p.color == color) score += value;
            else score -= value;
            if(p.col >= 3 && p.col <= 4 && p.row >= 3 && p.row <=4 && p.color == color){//center control
        score += 2;  // rewward for controlling center  
    } else if (p.col >= 3 && p.col <= 4 && p.row >= 3 && p.row <=4 && p.color != color){//center control
        score -= 2;  // opp controlling center 
    }
        }
        return score;
    }

    private int getPieceValue(Type type) {
        if (type == null) return 0;
        switch (type) {
            case PAWN: return 100;
            case KNIGHT: return 320;
            case BISHOP: return 330;
            case ROOK: return 500;
            case QUEEN: return 900;
            case KING: return 20000;
            default: return 0;
        }
    }

    // Helper class to represent a move
    public static class Move {
        public piece piece;
        public int targetCol, targetRow, precol, prerow, bestScore;
        public Move(piece piece, int precol, int prerow, int targetCol, int targetRow, int bestScore) {
            this.piece = piece;
            this.targetCol = targetCol;
            this.targetRow = targetRow;
            this.precol = precol;
            this.prerow = prerow;
            this.bestScore = bestScore;
            
        }
    }
}
