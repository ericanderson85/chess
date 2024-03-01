package model;

import util.Position;

import java.util.List;

public class Rook extends ChessPiece {
    private boolean hasMoved;
    public Rook(Position position, boolean isWhite) {
        super(position, isWhite, 5);
        this.hasMoved = false;
    }
    
    @Override
    public boolean canMove(Position destination) {
        int dy = destination.row() - position.row();
        int dx = destination.col() - position.col();
        // The case for the destination being the same as the position is already handled
        return dx == 0 || dy == 0;
    }
    
    public boolean hasMoved() {
        return hasMoved;
    }
    
    public void setMoved() {
        this.hasMoved = false;
    }
    
    @Override
    public List<Position> possibleMoves() {
        return null;
    }
    
    @Override
    public String toString() {
        return "R" + (isWhite ? "w" : "b");
    }
}
