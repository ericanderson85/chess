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
    
    // To do : Chess annotation of move
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        return str.toString();
    }
}
