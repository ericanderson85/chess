package util;
import model.ChessPiece;

public record Move(Position startPosition, Position endPosition, ChessPiece movedPiece, ChessPiece capturedPiece,
                   Move.MoveType moveType,  Move.CheckType checkType) {
    public enum MoveType {
        NORMAL, CASTLE, EN_PASSANT, PROMOTION
    }
    
    public enum CheckType {
        NONE, CHECK, STALEMATE, CHECKMATE
    }
    
    // To do: logic to annotate
    @Override
    public String toString() {
        StringBuilder notation = new StringBuilder();
        return notation.toString();
    }
}
