package model;

import util.Position;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends ChessPiece {
    public Pawn(Position position, boolean isWhite) {
        super(position, isWhite, 100, PieceType.PAWN);
    }
    
    @Override
    public ChessPiece copyTo(Position newPosition) {
        return new Pawn(newPosition, this.isWhite);
    }
    
    @Override
    public boolean canMove(Position destination) {
        int dy = destination.row() - position.row();
        int dx = destination.col() - position.col();
        if (Math.abs(dy) > 2 || Math.abs(dx) > 1) {
            return false;
        }
        if (isWhite) {
            return canMoveWhite(destination, dy, dx);
        }
        return canMoveBlack(destination, dy, dx);
    }
    
    private boolean canMoveWhite(Position destination, int dy, int dx) {
        if (dy < 1) {
            return false;
        }
        if (dy == 2) {
            if (position.row() != 1) {
                return false;
            }
            return dx == 0;
        }
        return true;
    }
    
    private boolean canMoveBlack(Position destination, int dy, int dx) {
        if (dy > -1) {
            return false;
        }
        if (dy == -2) {
            if (position.row() != 6) {
                return false;
            }
            return dx == 0;
        }
        return true;
    }
    
    @Override
    public List<Position> possibleMoves() {
        List<Position> possibleMoves = new ArrayList<>();
        
        int direction = isWhite ? 1 : -1;
        
        Position forward = new Position(position.row() + direction, position.col());
        possibleMoves.add(forward);
        
        if ((isWhite && position.row() == 1) || (!isWhite && position.row() == 6)) {
            Position initialTwoSquareMove = new Position(position.row() + 2 * direction, position.col());
            possibleMoves.add(initialTwoSquareMove);
        }
        
        Position captureLeft = new Position(position.row() + direction, position.col() - 1);
        Position captureRight = new Position(position.row() + direction, position.col() + 1);
        possibleMoves.add(captureLeft);
        possibleMoves.add(captureRight);
    
        for (int i = possibleMoves.size() - 1; i >= 0; i--) {
            if (Board.isOutOfBounds(possibleMoves.get(i))) {
                possibleMoves.remove(i);
            }
        }
        
        return possibleMoves;
    }
    
}