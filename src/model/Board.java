package model;

import util.IllegalMoveException;
import util.Move;
import util.Position;

import java.util.*;

public class Board {
    private final ChessPiece[][] board; // Contains null for empty pieces
    private final Map<Position, ChessPiece> whitePieces;
    private final List<ChessPiece> whiteCaptured; // Black pieces which white has captured
    private final Map<Position, ChessPiece> blackPieces;
    private final List<ChessPiece> blackCaptured; // White pieces which black has captured
    private Pawn doubleStep;  // Reference to a pawn that just moved twice
    private int score; // Total piece value ( Positive if white is winning, negative if black is )
    
    public Board() {
        this.board = new ChessPiece[8][8];
        this.whitePieces = new HashMap<>();
        this.blackPieces = new HashMap<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                addPiece(new Position(row, col));
            }
        }
        this.whiteCaptured = new ArrayList<>();
        this.blackCaptured = new ArrayList<>();
        this.doubleStep = null;
        this.score = 0;
    }
    
    private void addPiece(Position position) {
        boolean isWhite = position.row() <= 1;
        ChessPiece piece = createPiece(position, isWhite);
        board[position.row()][position.col()] = piece;  // Add piece to the board
        if (piece == null) {
            return;
        }
        if (isWhite) {
            whitePieces.put(position, piece);
        } else {
            blackPieces.put(position, piece);
        }
    }
    
    private ChessPiece createPiece(Position position, boolean isWhite) {
        if (position.row() < 6 && position.row() > 1) {
            return null;
        }
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
    
    public ChessPiece getPiece(Position position) {
        return board[position.row()][position.col()];
    }
    
    private void removePiece(Position position) {
        board[position.row()][position.col()] = null;
    }
    private void setPiece(ChessPiece piece, Position position) {
        board[position.row()][position.col()] = piece;
        piece.setPosition(position);
    }
    
    
    // Moves piece and returns the move
    public Move movePiece(Position position, Position destination, boolean isWhiteTurn) throws IllegalMoveException {
        ChessPiece thisPiece = getMovingPiece(position, isWhiteTurn);  // Handles empty position
        if (!isValidMove(thisPiece, position, destination)) {
            throw new IllegalMoveException("Invalid move");
        }
        ChessPiece otherPiece = getPiece(destination);  // Piece being captured or null
        Move.MoveType moveType = determineMoveType(thisPiece, position, otherPiece, destination);  // Handles capture of same color
        Move.CheckType checkType = determineCheckType(thisPiece, otherPiece);  // NONE, CHECK, CHECKMATE
        
        try {
            performMove(thisPiece, position, otherPiece, destination, moveType);  // Handles own king in check
        } catch (IllegalMoveException e) {
            undoMove(thisPiece, position, otherPiece, destination, moveType);
            throw new IllegalMoveException("Move puts own king in check");
        }
        
        if (thisPiece instanceof Pawn) {
            this.doubleStep = (Pawn)thisPiece;
        } else {
            this.doubleStep = null;
            if (thisPiece instanceof King) {
                ((King) thisPiece).setHasMoved();
            } else if (thisPiece instanceof Rook) {
                ((Rook) thisPiece).setHasMoved();
            }
        }
        
        return new Move(position, destination, thisPiece, otherPiece, moveType, checkType);
    }
    
    private ChessPiece getMovingPiece(Position position, boolean isWhiteTurn) throws IllegalMoveException {
        ChessPiece thisPiece = getPiece(position);
        if (thisPiece == null) {
            throw new IllegalMoveException("No piece to be moved");
        }
        if (thisPiece.isWhite() != isWhiteTurn) {
            throw new IllegalMoveException("Can not move other player's piece");
        }
        return thisPiece;
    }
    
    private boolean isValidMove(ChessPiece thisPiece, Position position, Position destination) {
        if (!isInBounds(destination)) {
            return false;
        }
        if (position.equals(destination)) {
            return false;
        }
        if (!thisPiece.canMove(destination)) {
            return false;
        }
        if (thisPiece instanceof Pawn && destination.col() == position.col() && getPiece(destination) != null) {
            return false;
        }
        return canSee(thisPiece, position, destination);
    }
    
    private boolean isInBounds(Position position) {
        return (position.row() <= 7 && position.row() >= 0 && position.col() <= 7 && position.col() >= 0);
    }
    
    private Move.MoveType determineMoveType(ChessPiece thisPiece, Position position, ChessPiece otherPiece, Position destination) throws IllegalMoveException {
        boolean isCastling = false;
        if (thisPiece instanceof Pawn) {
            if (isEnPassant(thisPiece, position, otherPiece, destination)) {
                return Move.MoveType.EN_PASSANT;
            }
            if ((thisPiece.isWhite() && destination.row() == 7)  ||
                    (!thisPiece.isWhite() && destination.row() == 0)) {
                return Move.MoveType.PROMOTION;
            }
            
        } else if (thisPiece instanceof King && otherPiece instanceof Rook) {
            if (((King) thisPiece).hasMoved() && ((Rook) otherPiece).hasMoved()) {
                isCastling = true;
            }
        } else if (thisPiece instanceof Rook && otherPiece instanceof King) {
            if (((Rook) thisPiece).hasMoved() && ((King) otherPiece).hasMoved()) {
                isCastling = true;
            }
        }
        
        if (otherPiece != null && !isCastling && (thisPiece.isWhite() == otherPiece.isWhite())) {
            throw new IllegalMoveException("Capturing piece of same color");
        }
        
        if (isCastling) {
            int dx = destination.col() - position.col();
            for (int i = 1; i < Math.abs(dx); i+=Integer.compare(0, dx)) {
                Position pos = new Position(position.row(), position.col() + i);
                if (getPiece(pos) != null) {
                    throw new IllegalMoveException("Can't castle, pieces in the way");
                }
                if (isAttacked(!thisPiece.isWhite(), pos)) {
                    throw new IllegalMoveException("Can't castle through check");
                }
            }
            return Move.MoveType.CASTLE;
        }
        
        return Move.MoveType.NORMAL;
    }
    
    private boolean isEnPassant(ChessPiece thisPiece, Position position, ChessPiece otherPiece, Position destination) {
        if (doubleStep == null) {
            return false;
        }
        if (getPiece(new Position(position.row(), position.col() + 1)) == doubleStep) {
            return destination.col() == position.col() + 1;
        }
        else if (getPiece(new Position(position.row(), position.col() - 1)) == doubleStep) {
            return destination.col() == position.col() - 1;
        }
        return false;
    }
    
    private Move.CheckType determineCheckType(ChessPiece thisPiece, ChessPiece otherPiece) {
        Position otherKingPos = findKingPos(!thisPiece.isWhite());
        if (isAttacked(thisPiece.isWhite(), otherKingPos)) {
            if (hasNoMoves(otherKingPos)) {
                return Move.CheckType.CHECKMATE;
            }
            return Move.CheckType.CHECK;
        }
        return Move.CheckType.NONE;
    }
    

    
    private void performMove(ChessPiece thisPiece, Position position, ChessPiece otherPiece,
                             Position destination, Move.MoveType moveType) throws IllegalMoveException {
        if (moveType == Move.MoveType.CASTLE) {
            performCastle(thisPiece, position, otherPiece, destination);
        }
        else if (moveType == Move.MoveType.EN_PASSANT) {
            performEnPassant(thisPiece, position, otherPiece, destination);
        }
        else if (otherPiece != null) {
            capturePiece(otherPiece);
            movePiece(thisPiece, position, destination);
        }
        else {
            movePiece(thisPiece, position, destination);
        }
        
        Position kingPos = findKingPos(thisPiece.isWhite());
        
        if (isAttacked(!thisPiece.isWhite(), kingPos)) {
            throw new IllegalMoveException("Move puts own king in check");
        }
    }
    
    private void capturePiece(ChessPiece piece) {
        Position position = piece.getPosition();
        removePiece(position);
        if (piece.isWhite()) {
            whitePieces.remove(position);
            blackCaptured.add(piece);
            score -= piece.getValue();
        } else {
            blackPieces.remove(position);
            whiteCaptured.add(piece);
            score += piece.getValue();
        }
    }
    
    private void movePiece(ChessPiece piece, Position position, Position destination) {
        if (getPiece(destination) != null) {
            throw new IllegalArgumentException("There is a piece there");
        }
        removePiece(position);
        setPiece(piece, destination);
        if (piece.isWhite()) {
            whitePieces.remove(position);
            whitePieces.put(destination, piece);
        } else {
            blackPieces.remove(position);
            blackPieces.put(destination, piece);
        }
    }
    
    private void undoMove(ChessPiece piece, Position position, Position destination) {
        movePiece(piece, destination, position);
    }
    
    private void undoCapture(ChessPiece piece, Position position) {
        setPiece(piece, position);
        if (piece.isWhite()) {
            whitePieces.put(position, piece);
            blackCaptured.remove(piece);
            score += piece.getValue();
        } else {
            blackPieces.put(position, piece);
            whiteCaptured.remove(piece);
            score -= piece.getValue();
        }
    }
    

    
    private void performCastle(ChessPiece thisPiece, Position position, ChessPiece otherPiece, Position destination) {
        if (thisPiece instanceof King) {
            if (destination.col() == 0) {
                performQueenSideCastle(thisPiece, position, otherPiece, destination);
            } else {
                performKingSideCastle(thisPiece, position, otherPiece, destination);
            }
        } else {
            if (destination.col() == 0) {
                performQueenSideCastle(otherPiece, destination, thisPiece, position);
            } else {
                performKingSideCastle(otherPiece, destination, thisPiece, position);
            }
        }

    }
    
    private void undoCastle(ChessPiece thisPiece, Position position, ChessPiece otherPiece, Position destination) {
        if (thisPiece instanceof King) {
            if (destination.col() == 0) {
                undoQueenSideCastle(thisPiece, position, otherPiece, destination);
            } else {
                undoKingSideCastle(thisPiece, position, otherPiece, destination);
            }
        } else {
            if (destination.col() == 0) {
                undoQueenSideCastle(otherPiece, destination, thisPiece, position);
            } else {
                undoKingSideCastle(otherPiece, destination, thisPiece, position);
            }
        }
    }
    
    private void performQueenSideCastle(ChessPiece king, Position kingPosition, ChessPiece rook, Position rookPosition) {
        if (king.isWhite()) {
            movePiece(king, kingPosition, new Position(0, 2));
            movePiece(rook, rookPosition, new Position(0, 3));
        } else {
            movePiece(king, kingPosition, new Position(7, 2));
            movePiece(rook, rookPosition, new Position(7, 3));
        }
    }
    
    private void undoQueenSideCastle(ChessPiece king, Position kingPosition, ChessPiece rook, Position rookPosition) {
        if (king.isWhite()) {
            undoMove(king, kingPosition, new Position(0, 2));
            undoMove(rook, rookPosition, new Position(0, 3));
        } else {
            undoMove(king, kingPosition, new Position(7, 2));
            undoMove(rook, rookPosition, new Position(7, 3));
        }
    }
    
    
    private void performKingSideCastle(ChessPiece king, Position kingPosition, ChessPiece rook, Position rookPosition) {
        if (king.isWhite()) {
            movePiece(king, kingPosition, new Position(0, 6));
            movePiece(rook, rookPosition, new Position(0, 5));
        } else {
            movePiece(king, kingPosition, new Position(7, 6));
            movePiece(rook, rookPosition, new Position(7, 5));
        }
    }
    
    private void undoKingSideCastle(ChessPiece king, Position kingPosition, ChessPiece rook, Position rookPosition) {
        if (king.isWhite()) {
            undoMove(king, kingPosition, new Position(0, 6));
            undoMove(rook, rookPosition, new Position(0, 5));
        } else {
            undoMove(king, kingPosition, new Position(7, 6));
            undoMove(rook, rookPosition, new Position(7, 5));
        }
    }
    
    private void performEnPassant(ChessPiece thisPiece, Position position, ChessPiece otherPiece, Position destination) {
        movePiece(thisPiece, position, destination);
        capturePiece(otherPiece);
    }
    
    private void undoEnPassant(ChessPiece thisPiece, Position position, ChessPiece otherPiece, Position destination) {
        undoMove(thisPiece, position, destination);
        undoCapture(otherPiece, otherPiece.getPosition());
    }
    
    private void undoMove(ChessPiece thisPiece, Position position, ChessPiece otherPiece, Position destination, Move.MoveType moveType) {
        if (moveType == Move.MoveType.CASTLE) {
            undoCastle(thisPiece, position, otherPiece, destination);
        }
        if (moveType == Move.MoveType.EN_PASSANT) {
            undoEnPassant(thisPiece, position, otherPiece, destination);
        }
        else {
            undoMove(thisPiece, position, destination);
            if (otherPiece != null) {
                undoCapture(otherPiece, destination);
            }
        }
    }
    
    private Position findKingPos(boolean isWhite) {
        for (Position pos : isWhite ? whitePieces.keySet() : blackPieces.keySet()) {
            if (getPiece(pos) instanceof King) {
                return pos;
            }
        }
        return null;
    }

    private boolean isAttacked(boolean byWhiteTeam, Position position) {
        for (Position pos : byWhiteTeam ? whitePieces.keySet() : blackPieces.keySet()) {
            if (getPiece(pos).canMove(position) && canSee(getPiece(pos), pos, position)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean canSee(ChessPiece piece, Position position, Position destination) {
        if (piece instanceof Knight || piece instanceof Pawn || piece instanceof King) return true;
        int dy = destination.row() - position.row();
        int dx = destination.col() - position.col();
        int dirY = Integer.compare(dy, 0);
        int dirX = Integer.compare(dx, 0);
        for (int i = position.row() + dirY, j = position.col() + dirX; i != destination.row() || j != destination.col();
             i += dirY, j += dirX) {  // Go towards destination one tile at a time
            if (getPiece(new Position(i, j)) != null)
                return false;
        }
        return true;
    }
    
    private boolean hasNoMoves(Position position) {
        King king = (King)getPiece(position);
        for (Position pos : king.possibleMoves()) {
            if (!isAttacked(!king.isWhite, pos)) {
                return false;
            }
        }
        return true;
    }
    
    
    public List<ChessPiece> getWhiteCaptured() {
        return whiteCaptured;
    }
    
    public List<ChessPiece> getBlackCaptured() {
        return blackCaptured;
    }
    
    public int getScore() {
        return score;
    }
    
    public ChessPiece[][] getBoardCopy() {
        ChessPiece[][] copy = new ChessPiece[8][8];
        for (int i = 0; i < 8; i++) {
            copy[i] = new ChessPiece[8];
            System.arraycopy(board[i], 0, copy[i], 0, 8);
        }
        return copy;
    }
    
    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(board[i][j] == null ? "    " : " " + board[i][j].toString() + " ");
            }
            System.out.println();
        }
        return boardString.toString();
    }
    
    public static void main(String[] args) throws IllegalMoveException {
        Board b = new Board();
        b.movePiece(new Position(1, 5), new Position(3, 5), true);
        b.movePiece(new Position(6, 4), new Position(5, 4), false);
        b.movePiece(new Position(1, 6), new Position(3, 6), true);
        b.movePiece(new Position(7, 3), new Position(3, 7), false);
        System.out.println(b.getPiece(new Position(7, 3)));
        System.out.println(b);
    
    }
}
