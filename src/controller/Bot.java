package controller;

import util.*;
import model.*;

public class Bot implements ChessPlayer {
    private final boolean isWhite;
    private final int DEPTH = 4;
    
    public Bot(boolean isWhite) {
        this.isWhite = isWhite;
    }
    
    @Override
    public Pair<Board, Move> movePiece(Board board) {
        Pair<Board, Move> movePair = null;
        int bestValue = isWhite ? Integer.MIN_VALUE : Integer.MAX_VALUE;
    
        for (Pair<Board, Move> move : board.generateMoves(isWhite)) {
            Board newBoard = move.getKey();
            int moveValue = Search.search(newBoard, DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, !isWhite);
            if ((isWhite && moveValue > bestValue) || (!isWhite && moveValue < bestValue)) {
                bestValue = moveValue;
                movePair = move;
            }
        }
        return movePair;
    }
    
    
    @Override
    public Move.PromotionType getPromotionType() {
        return Move.PromotionType.QUEEN;
    }
}
