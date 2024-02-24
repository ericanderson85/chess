package model;

import util.Move;
import util.Position;

import java.util.List;

public class Knight extends ChessPiece {
    
    public Knight(Position position, boolean isWhite) {
        super(position, isWhite, 3);
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
