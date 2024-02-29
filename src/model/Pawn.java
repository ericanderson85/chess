package model;

import util.Move;
import util.Position;

import java.util.List;

public class Pawn extends ChessPiece {
    public Pawn(Position position, boolean isWhite) {
        super(position, isWhite, 1);
    }
    
    @Override
    public boolean canMove(Position destination) {
        int dy = destination.row() - position.row();
        int dx = destination.col() - position.col();
        if (Math.abs(dy) > 2 || Math.abs(dx) > 1) {
            return false;
        }
        if (isWhite) {
            return canMoveWhite(destination, dy, dx);
        }
        return canMoveBlack(destination, dy, dx);
    }
    
    private boolean canMoveWhite(Position destination, int dy, int dx) {
        if (dy < 1) {
            return false;
        }
        if (dy == 2) {
            if (position.row() != 1) {
                return false;
            }
            return dx == 0;
        }
        return true;
    }
    
    private boolean canMoveBlack(Position destination, int dy, int dx) {
        if (dy > -1) {
            return false;
        }
        if (dy == -2) {
            if (position.row() != 6) {
                return false;
            }
            return dx == 0;
        }
        return true;
    }
    
    @Override
    public List<Position> possibleMoves() {
        return null;
    }
    
    @Override
    public String toString() {
        return "P" + (isWhite ? "w" : "b");
    }
}