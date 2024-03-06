package model;

import util.Position;

import java.util.List;

public abstract class ChessPiece {
    protected final Position position;
    protected final boolean isWhite;
    protected final int value;
    protected final PieceType pieceType;
    
    protected ChessPiece(Position position, boolean isWhite, int value, PieceType pieceType) {
        this.position = position;
        this.isWhite = isWhite;
        this.value = value;
        this.pieceType = pieceType;
    }
    
    public abstract ChessPiece copyTo (Position newPosition);
    
    public Position getPosition() {
        return position;
    }
    public boolean isWhite() {
        return isWhite;
    }
    public int getValue() {
        return value;
    }
    public PieceType getType() {
        return pieceType;
    }
    
    public abstract boolean canMove(Position destination);
    
    public abstract List<Position> possibleMoves();
    
    @Override
    public String toString() {
        return pad((isWhite ? "W" : "B") + this.getClass().getSimpleName(), 7);
    }
    
    public static String pad(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append(' ');
        }
        sb.append(inputString);
        
        return sb.toString();
    }
    
    public enum PieceType {
        PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING
    }
}
