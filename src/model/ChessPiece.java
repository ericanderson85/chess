package model;

import util.Move;
import util.Position;

import java.util.List;

public abstract class ChessPiece {
    protected Position position;
    protected final boolean isWhite;
    protected final int value;
    
    protected ChessPiece(Position position, boolean isWhite, int value) {
        this.position = position;
        this.isWhite = isWhite;
        this.value = value;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public void setPosition(Position position) {
        this.position = position;
    }
    public boolean isWhite() {
        return isWhite;
    }
    public int getValue() {
        return value;
    }
    
    public abstract boolean canMove(Position destination);
    
    public abstract List<Move> possibleMoves(Position pos);
    
}
