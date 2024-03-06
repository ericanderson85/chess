package model;

import util.Position;

import java.util.ArrayList;
import java.util.List;

public class Knight extends ChessPiece {
    
    public Knight(Position position, boolean isWhite) {
        super(position, isWhite, 320, PieceType.KNIGHT);
    }
    
    @Override
    public Knight copyTo(Position newPosition) {
        return new Knight(newPosition, this.isWhite);
    }
    
    @Override
    public boolean canMove(Position destination) {
        int dy = destination.row() - position.row();
        int dx = destination.col() - position.col();
        return ((Math.abs(dy) == 2 && Math.abs(dx) == 1) || (Math.abs(dy) == 1 && Math.abs(dx) == 2));
    }
    
    @Override
    public List<Position> possibleMoves() {
        int[][] moves = {
                {-2, -1}, {-1, -2}, {1, -2}, {2, -1},
                {2, 1}, {1, 2}, {-1, 2}, {-2, 1}
        };
    
        List<Position> possibleMoves = new ArrayList<>();
        for (int[] move : moves) {
            Position destination = new Position(position.row() + move[0], position.col() + move[1]);
            if (!Board.isOutOfBounds(destination)) {
                possibleMoves.add(destination);
            }
        }
    
        return possibleMoves;
    }
    
}
