package model;

import util.Move;
import util.Position;

import java.util.ArrayList;
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
