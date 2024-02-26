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
    
    public ChessPiece createPiece(Position position, boolean isWhite) {
        if (position.row() < 6 && position.row() > 1) {
            return null;
        }
        if (position.row() == 0 || position.row() == 7) {
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
    
    // Moves piece and returns the move
    public Move movePiece(Position position, Position destination, boolean isWhiteTurn) throws IllegalMoveException {
        ChessPiece thisPiece = movingPiece(position, isWhiteTurn);  // Handles empty position
        validateMove(thisPiece, position, destination);  // Checks if the piece is able to move in that way and there are no pieces blocking it
        ChessPiece otherPiece = getPiece(destination);  // Piece being captured or null
        Move.MoveType moveType = moveType(thisPiece, otherPiece);  // Handles capture of same color
        boolean castling = moveType == Move.MoveType.CASTLE;
        Move.CheckType checkType = checkType(thisPiece, otherPiece);  // NONE, CHECK, CHECKMATE
        
        try {
            performMove(thisPiece, position, otherPiece, destination, castling);  // Handles own king in check
        } catch (IllegalMoveException e) {
            if (castling) undoCastle();
            else if (otherPiece == null) undoSwap(thisPiece, position, destination);
            else undoCapture(thisPiece, position, otherPiece, destination);
            throw new IllegalMoveException("Move puts own king in check");
        }
        
        return new Move(position, destination, thisPiece, otherPiece, moveType, checkType);
    }
    
    private ChessPiece movingPiece(Position position, boolean isWhiteTurn) throws IllegalMoveException {
        ChessPiece thisPiece = getPiece(position);
        if (thisPiece == null) {
            throw new IllegalMoveException("No piece to be moved");
        }
        if (thisPiece.isWhite() != isWhiteTurn) {
            throw new IllegalMoveException("Can not move other player's piece");
        }
        return thisPiece;
    }
    
    private void validateMove(ChessPiece thisPiece, Position position, Position destination) throws IllegalMoveException {
        if (!thisPiece.canMove(position))
            throw new IllegalMoveException("Piece can not move in this way");
        if (thisPiece instanceof Knight) return;  // Knights don't have to worry about collisions
        int dy = position.row() - destination.row();
        int dx = position.col() - destination.col();
        for (int i = position.row(), j = position.col(); i != destination.row() || j != destination.col();
             i += Integer.compare(dy, 0), j += Integer.compare(dx, 0)) {  // Go towards destination one tile at a time
            if (getPiece(new Position(i, j)) != null)
                throw new IllegalMoveException("A piece is in the way");
        }
    }
    
    private ChessPiece capturedPiece(ChessPiece thisPiece, Position destination, boolean isWhiteTurn) throws IllegalMoveException {
        ChessPiece otherPiece = getPiece(destination);
        if (otherPiece != null && otherPiece.isWhite() == isWhiteTurn) {
            throw new IllegalMoveException("Capturing same color piece");
        }
        return otherPiece;
    }
    
    private Move.MoveType moveType(ChessPiece thisPiece, ChessPiece otherPiece) {
        
        if (thisPiece instanceof Pawn) {
            // Logic to check if the pawn is promoting, doing en passant, or neither
        } else if (thisPiece instanceof King || thisPiece instanceof Rook) {
            // Logic to check if the piece is castling
        }
        
        return Move.MoveType.NORMAL;  // Placeholder
    }
    
    private Move.CheckType checkType(ChessPiece thisPiece, ChessPiece otherPiece) {
        return Move.CheckType.NONE;  // Placeholder
    }
    
    private void performMove(ChessPiece thisPiece, Position position, ChessPiece otherPiece,
                             Position destination, boolean castling) throws IllegalMoveException {
        if (castling == true) performCastle(thisPiece, position, otherPiece, destination);
        else if (otherPiece != null) performCapture(thisPiece, position, otherPiece, destination);
        else performSwap(thisPiece, position, destination);
        
        // Check if move puts own king in check
        if (false) {
            throw new IllegalMoveException("Move puts own king in check");
        }
    }
    
    private void performCastle(ChessPiece thisPiece, Position position, ChessPiece otherPiece, Position destination) {
        // Logic to perform castle
    }
    
    private void performCapture(ChessPiece thisPiece, Position position, ChessPiece otherPiece, Position destination) {
        performSwap(thisPiece, position, destination);
        if (thisPiece.isWhite()) {
            whiteCaptured.add(otherPiece);  // Add otherPiece to whiteCaptured
            blackPieces.remove(destination);  // Remove otherPiece from blackPieces
            score += otherPiece.getValue();  // Add value
        } else {
            blackCaptured.add(otherPiece);
            whitePieces.remove(destination);
            score -= otherPiece.getValue();  // Subtract value
        }
    }
    
    private void performSwap(ChessPiece thisPiece, Position position, Position destination) {
        if (thisPiece.isWhite()) {
            whitePieces.remove(position);  // Remove thisPiece from old destination
            whitePieces.put(destination, thisPiece);  // Put thisPiece in new destination
        } else {
            blackPieces.remove(position);
            blackPieces.put(destination, thisPiece);
        }
        thisPiece.setPosition(destination);  // Set thisPiece's position
        board[position.row()][position.col()] = null;  // Old position is empty
        board[destination.row()][destination.col()] = thisPiece;  // Set position on board
    }
    
    private void undoCastle() {
        // Logic to undo castle
    }
    
    private void undoCapture(ChessPiece thisPiece, Position position, ChessPiece otherPiece, Position destination) {
        undoSwap(thisPiece, position, destination);
        if (thisPiece.isWhite()) {
            blackPieces.put(destination, otherPiece);  // Put otherPiece back
            whiteCaptured.remove(otherPiece);  // Remove otherPiece from captured
            score -= otherPiece.getValue();  // Revert score
        } else {
            whitePieces.put(destination, otherPiece);
            blackCaptured.remove(otherPiece);
            score += otherPiece.getValue();
        }
        board[destination.row()][destination.col()] = otherPiece;  // Put otherPiece back on board
    }
    
    private void undoSwap(ChessPiece thisPiece, Position position, Position destination) {
        if (thisPiece.isWhite()) {
            whitePieces.remove(destination);  // Remove thisPiece from destination
            whitePieces.put(position, thisPiece);  // Put otherPiece in original position
        } else {
            blackPieces.remove(destination);
            blackPieces.put(position, thisPiece);
        }
        thisPiece.setPosition(position);  // Revert thisPiece's position
        board[position.row()][position.col()] = thisPiece;  // Revert position on board
        board[destination.row()][destination.col()] = null;  // Destination now empty
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
    
    // To do : make this look good
    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        for (int i = 0; i < 8; i++) boardString.append(Arrays.toString(board[i])).append("\n");
        return boardString.toString();
    }
    
    public static void main(String[] args) throws IllegalMoveException {
        Board b = new Board();
        b.movePiece(new Position(1, 4), new Position(3, 4), true);
        System.out.println(b);
    }
}
