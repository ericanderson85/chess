package model;

import util.Move;
import util.Position;

import java.util.List;

public class Rook extends ChessPiece{
    
    public Rook(Position position, boolean isWhite) {
        super(position, isWhite, 5);
    }
    
    @Override
    public boolean canMove(Position destination) {
        int dy = destination.row() - position.row();
        int dx = destination.col() - position.col();
        // The case for the destination being the same as the position is already handled
        return dx == 0 || dy == 0;
    }
    
    @Override
    public List<Move> possibleMoves(Position pos) {
        return null;
    }
}
