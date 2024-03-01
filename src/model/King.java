package model;

import util.Position;

import java.util.List;

public class King extends ChessPiece {
    private boolean hasMoved;
    private boolean inCheck;
    public King(Position position, boolean isWhite) {
        super(position, isWhite, 1);
        this.hasMoved = false;
        this.inCheck = false;
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
    
    public void setMoved() {
        this.hasMoved = true;
    }
    
    @Override
    public List<Position> possibleMoves() {
        return List.of(
                new Position(position.row() + 1, position.col() + 1),
                new Position(position.row() + 1, position.col()),
                new Position(position.row() + 1, position.col() - 1),
                new Position(position.row(), position.col() + 1),
                new Position(position.row(), position.col() - 1),
                new Position(position.row() - 1, position.col() + 1),
                new Position(position.row() - 1, position.col()),
                new Position(position.row() - 1, position.col() - 1)
        );
    }
}
