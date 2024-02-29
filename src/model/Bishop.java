package model;

import util.Move;
import util.Position;

import java.util.List;

public class Bishop extends ChessPiece {
    public Bishop(Position position, boolean isWhite) {
        super(position, isWhite, 3);
    }
    
    @Override
    public boolean canMove(Position destination) {
        int dy = destination.row() - position.row();
        int dx = destination.col() - position.col();
        return Math.abs(dy) == Math.abs(dx);
    }
    
    @Override
    public List<Position> possibleMoves() {
        return null;
    }
    
    @Override
    public String toString() {
        return "B" + (isWhite ? "w" : "b");
    }
}
