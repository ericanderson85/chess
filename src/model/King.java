package model;

import util.Position;

import java.util.ArrayList;
import java.util.List;

public class King extends ChessPiece {
    private final boolean hasMoved;
    public King(Position position, boolean isWhite) {
        super(position, isWhite, 20000, PieceType.KING);
        this.hasMoved = false;
    }
    
    public King(Position position, boolean isWhite, boolean hasMoved) {
        super(position, isWhite, 9, PieceType.KING);
        this.hasMoved = hasMoved;
    }
    
    @Override
    public ChessPiece copyTo(Position newPosition) {
        return new King(newPosition, this.isWhite, true);
    }
    
    @Override
    public boolean canMove(Position destination) {
        int dy = destination.row() - position.row();
        int dx = destination.col() - position.col();
        return (Math.abs(dy) <= 1 && Math.abs(dx) <= 1) || isCastleMove(destination);
    }
    
    private boolean isCastleMove(Position destination) {
        if (hasMoved) {
            return false;
        }
        
        if (destination.col() != 0 && destination.col() != 7) {
            return false;
        }
        
        if (isWhite()) {
            if (destination.row() != 0) {
                return false;
            }
        } else {
            if (destination.row() != 7) {
                return false;
            }
        }
        
        return true;
    }
    
    public boolean hasMoved() {
        return hasMoved;
    }
    
    @Override
    public List<Position> possibleMoves() {
        List<Position> list = new ArrayList<>();
        int[][] directions = {
                {1, 1}, {1, 0}, {1, -1},
                {0, 1}, {0, -1},
                {-1, 1}, {-1, 0}, {-1, -1}
        };
        for (int[] direction : directions) {
            Position destination = new Position(position.row() + direction[0], position.col() + direction[1]);
            if (!Board.isOutOfBounds(destination)) {
                list.add(destination);
            }
        }
        return list;
    }
}
