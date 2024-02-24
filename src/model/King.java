package model;

import util.Move;
import util.Position;

import java.util.List;

public class King extends ChessPiece{
    public King(Position position, boolean isWhite) {
        super(position, isWhite, 1);
    }
    
    @Override
    public boolean canMove(Position destination) {
        return false;
    }
    
    @Override
    public List<Move> possibleMoves(Position pos) {
        return null;
    }
}
