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
        return false;
    }
    
    @Override
    public List<Move> possibleMoves(Position pos) {
        return null;
    }
}
