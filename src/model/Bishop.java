package model;

import util.Position;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends ChessPiece {
    public Bishop(Position position, boolean isWhite) {
        super(position, isWhite, 330, PieceType.KING);
    }
    
    @Override
    public ChessPiece copyTo(Position newPosition) {
        return new Bishop(newPosition, this.isWhite);
    }
    
    @Override
    public boolean canMove(Position destination) {
        int dy = destination.row() - position.row();
        int dx = destination.col() - position.col();
        return Math.abs(dy) == Math.abs(dx);
    }
    
    @Override
    public List<Position> possibleMoves() {
        List<Position> moves = new ArrayList<>();
        int[][] directions = {
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };
        int row = position.row();
        int col = position.col();
        for (int[] direction : directions) {
            for (int i = row + direction[0], j = col + direction[1]; i < 8 && i >= 0 && j < 8 && j >= 0;
                 i += direction[0], j += direction[1]) {
                moves.add(new Position(i, j));
            }
        }
        return moves;
    }
}
