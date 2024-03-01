package util;

import model.*;

public class MoveFactory {
    
    public Move create(Position position, Position destination, boolean isWhiteTurn, Move.PromotionType promotionType, Board board) throws IllegalMoveException {
        ChessPiece thisPiece = confirmPiece(position, isWhiteTurn, board);
        validateMove(thisPiece, position, destination, board);
        ChessPiece otherPiece = board.getPiece(destination);
        return new Move(
                position,
                thisPiece,
                destination,
                otherPiece,
                determineMoveType(thisPiece, otherPiece, destination, board),
                promotionType);
        }
    private Move.MoveType determineMoveType(ChessPiece thisPiece, ChessPiece otherPiece, Position destination, Board board) throws IllegalMoveException {
        if (otherPiece != null && (thisPiece.isWhite() == otherPiece.isWhite())) {
            if (isCastling(thisPiece, otherPiece)) {
                return Move.MoveType.CASTLE;
            }
            throw new IllegalMoveException("Capturing piece of same color");
        } else if (thisPiece instanceof Pawn) {
            if (isEnPassant(thisPiece, destination, board)) {
                return Move.MoveType.EN_PASSANT;
            } else if (isPromotion(thisPiece, destination)) {
                return Move.MoveType.PROMOTION;
            }
        }
        return Move.MoveType.NORMAL;
    }

    private boolean isCastling(ChessPiece thisPiece, ChessPiece otherPiece) {
        if (thisPiece instanceof King && otherPiece instanceof Rook) {
            return !((King)thisPiece).hasMoved() && !((Rook)otherPiece).hasMoved();
        }
        return false;
    }
    
    private boolean isEnPassant(ChessPiece thisPiece, Position destination, Board board) {
        if (board.getDoubleStep() == null) {
            return false;
        }
        return new Position(destination.row() + (thisPiece.isWhite() ? 1 : -1), destination.col())
                .equals(board.getDoubleStep().getPosition());
    }
    
    private boolean isPromotion(ChessPiece thisPiece, Position destination) {
        return (thisPiece.isWhite() && destination.row() == 7)  || (!thisPiece.isWhite() && destination.row() == 0);
    }
    
    private ChessPiece confirmPiece(Position position, boolean isWhiteTurn, Board board) throws IllegalMoveException {
        ChessPiece thisPiece = board.getPiece(position);
        if (thisPiece == null) {
            throw new IllegalMoveException("No piece to be moved");
        }
        if (thisPiece.isWhite() != isWhiteTurn) {
            throw new IllegalMoveException("Can not move other player's piece");
        }
        return thisPiece;
    }
    
    private void validateMove(ChessPiece thisPiece, Position position, Position destination, Board board) throws IllegalMoveException {
        if (isOutOfBounds(destination) || isOutOfBounds(position)) {
            throw new IllegalMoveException("Out of bounds");
        }
        if (destination.equals(position)) {
            throw new IllegalMoveException("Can't move 0 tiles");
        }
        if (position.equals(destination) || !thisPiece.canMove(destination)) {
            throw new IllegalMoveException("Illegal move for type " + thisPiece.getClass().toString());
        }
        if (thisPiece instanceof Pawn) {
        if ((destination.col() == position.col() && board.getPiece(destination) != null)
                || (destination.col() != position.col() && board.getPiece(destination) == null)){
                throw new IllegalMoveException("Illegal move for type Pawn");
            }
        }
        if (!board.canSee(thisPiece, position, destination)) {
            throw new IllegalMoveException(thisPiece.getClass().toString() + "can't jump over tiles");
        }
    }
    
    private boolean isOutOfBounds(Position position) {
        return (position.row() > 7 || position.row() < 0 || position.col() > 7 || position.col() < 0);
    }
}
