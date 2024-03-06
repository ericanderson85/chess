package util;
import model.ChessPiece;

public record Move(Position position, ChessPiece movedPiece, Position destination, ChessPiece capturedPiece, Move.MoveType moveType, Move.PromotionType promotionType) {
    public enum MoveType {
        NORMAL, CASTLE, EN_PASSANT, PROMOTION
    }
    
    public enum PromotionType {
        NONE, QUEEN, ROOK, KNIGHT, BISHOP
    }
    
    @Override
    public String toString() {
        if (capturedPiece != null) {
            return movedPiece.toString() + "  --X-->  " + capturedPiece;
        }
        return movedPiece.toString() + "  ----->  " + destination.toString();
    }
}
