package util;

import model.Board;

public interface ChessPlayer {
    Pair<Board, Move> movePiece(Board currentBoard);
    Move.PromotionType getPromotionType();
}
