package util;
import model.ChessPiece;

public record Move(Position position, ChessPiece movedPiece, Position destination, ChessPiece capturedPiece, Move.MoveType moveType, Move.PromotionType promotionType) {
    public enum MoveType {
        NORMAL, CASTLE, EN_PASSANT, PROMOTION
    }
    
    public enum PromotionType {
        NONE, QUEEN, ROOK, KNIGHT, BISHOP
    }
    
    // To do : Chess annotation of move
    @Override
    public String toString() {
        return movedPiece.toString() + " --> " + destination;
    }
}
