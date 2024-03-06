package model;

import util.Position;

import java.util.ArrayList;
import java.util.List;

public class Rook extends ChessPiece {
    private final boolean hasMoved;
    private final boolean hasCastled;

    public Rook(Position position, boolean isWhite) {
        super(position, isWhite, 500, PieceType.ROOK);
        this.hasMoved = false;
        this.hasCastled = false;
    }
    
    public Rook (Position position, boolean isWhite, boolean hasMoved, boolean hasCastled) {
        super(position, isWhite, 500, PieceType.ROOK);
        this.hasMoved = hasMoved;
        this.hasCastled = hasCastled;
    }
    
    @Override
    public ChessPiece copyTo(Position newPosition) {
        return new Rook(newPosition, this.isWhite, true, this.hasCastled);
    }
    
    @Override
    public boolean canMove(Position destination) {
        int dy = destination.row() - position.row();
        int dx = destination.col() - position.col();
        // The case for the destination being the same as the position is already handled
        return dx == 0 || dy == 0;
    }
    
    public boolean hasMoved() {
        return hasMoved;
    }
    
    @Override
    public List<Position> possibleMoves() {
        List<Position> moves = new ArrayList<>();
        int[][] directions = {
                {-1, 0}, {0, 1}, {1, 0}, {0, -1}
        };
        int row = position.row();
        int col = position.col();
        for (int[] direction : directions) {
            for (int i = row, j = col; i < 8 && i >= 0 && j < 8 && j >= 0;
                 i += direction[0], j += direction[1]) {
                moves.add(new Position(i, j));
            }
        }
        return moves;
    }
}
