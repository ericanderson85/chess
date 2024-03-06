package util;

import model.Board;

import java.util.List;

public class Search {
    public static int search(Board board, int depth, int alpha, int beta, boolean isWhiteMove) {
        if (depth == 0) {
            return Evaluate.evaluate(board, isWhiteMove);
        }
        
        List<Pair<Board, Move>> moves = board.generateMoves(isWhiteMove);
        if (moves.isEmpty()) {
            if (board.kingInCheck(isWhiteMove)) {
                return isWhiteMove ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            }
            return 0;
        }
        
        
        int maxEval = isWhiteMove ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (Pair<Board, Move> move : moves) {
            int currentEval = search(move.getKey(), depth - 1, alpha, beta, !isWhiteMove);
            if ((isWhiteMove && currentEval > maxEval) || (!isWhiteMove && currentEval < maxEval)) {
                maxEval = currentEval;
            }
            if (isWhiteMove && currentEval > alpha) {
                alpha = currentEval;
            } else if (!isWhiteMove && currentEval < beta) {
                beta = currentEval;
            }
            if (beta <= alpha) {
                break;
            }
        }
        return maxEval;
    }
}
