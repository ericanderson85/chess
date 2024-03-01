package model;

import util.Move;
import util.Position;

import java.util.ArrayList;
import java.util.List;

public class Knight extends ChessPiece {
    
    public Knight(Position position, boolean isWhite) {
        super(position, isWhite, 3);
    }
    
    @Override
    public boolean canMove(Position destination) {
        int dy = destination.row() - position.row();
        int dx = destination.col() - position.col();
        return ((Math.abs(dy) == 2 && Math.abs(dx) == 1) || (Math.abs(dy) == 1 && Math.abs(dx) == 2));
    }
    
    @Override
    public List<Position> possibleMoves() {
        List<Position> moves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Position destination = new Position(i, j);
                if (canMove(destination)) {
                    moves.add(destination);
                }
            }
        }
        return moves;
    }
    
}
