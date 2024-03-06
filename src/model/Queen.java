package model;

import util.Position;

import java.util.ArrayList;
import java.util.List;

public class Queen extends ChessPiece{
    public Queen(Position position, boolean isWhite) {
        super(position, isWhite, 900, PieceType.QUEEN);
    }
    
    @Override
    public ChessPiece copyTo(Position newPosition) {
        return new Queen(newPosition, this.isWhite);
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
        int[][] directions = {
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}, {-1, 0}, {1, 0}, {0, -1}, {0, 1}
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
