package model;

import util.Move;
import util.Position;

import java.util.List;

public class King extends ChessPiece{
    private boolean hasMoved;
    public King(Position position, boolean isWhite) {
        super(position, isWhite, 1);
        this.hasMoved = false;
    }
    
    @Override
    public boolean canMove(Position destination) {
        int dy = destination.row() - position.row();
        int dx = destination.col() - position.col();
        return (Math.abs(dy) <= 1 && Math.abs(dx) <= 1) || isCastleMove(destination);
    }
    
    private boolean isCastleMove(Position destination) {
        if (hasMoved) {
            return false;
        }
        
        if (destination.col() != 0 && destination.col() != 7) {
            return false;
        }
        
        if (isWhite()) {
            if (destination.row() != 0) {
                return false;
            }
        } else {
            if (destination.row() != 7) {
                return false;
            }
        }
        
        return true;
    }
    
    public boolean hasMoved() {
        return hasMoved;
    }
    
    @Override
    public List<Move> possibleMoves(Position pos) {
        return null;
    }
}
