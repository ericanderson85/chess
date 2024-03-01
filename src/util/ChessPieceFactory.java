package util;

import model.*;

public class ChessPieceFactory {
    public ChessPiece create(Position position) {
        if (position.row() < 6 && position.row() > 1) {
            return null;
        }
        boolean isWhite = position.row() < 2;
        if (position.row() == 1 || position.row() == 6) {
            return new Pawn(position, isWhite);
        }
        return switch (position.col()) {
            case 0, 7 -> new Rook(position, isWhite);
            case 1, 6 -> new Knight(position, isWhite);
            case 2, 5 -> new Bishop(position, isWhite);
            case 3 -> new Queen(position, isWhite);
            case 4 -> new King(position, isWhite);
            default -> throw new IllegalArgumentException("Illegal column");
        };
    }
}
