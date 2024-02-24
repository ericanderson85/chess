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
        return false;
    }
    
    @Override
    public List<Move> possibleMoves(Position pos) {
        return null;
    }
}
