package model;

import util.Move;
import util.Position;

import java.util.List;

public class Queen extends ChessPiece{
    public Queen(Position position, boolean isWhite) {
        super(position, isWhite, 9);
    }
    
    @Override
    public boolean canMove(Position destination) {
        int dy = destination.row() - position.row();
        int dx = destination.col() - position.col();
        return dx == 0 || dy == 0 || Math.abs(dx) == Math.abs(dy);
    }
    
    @Override
    public List<Position> possibleMoves() {
        return null;
    }
    
    @Override
    public String toString() {
        return "Q" + (isWhite ? "w" : "b");
    }
}
