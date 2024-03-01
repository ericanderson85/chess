package model;

import util.Position;

import java.util.*;

public class Board {
    private final ChessPiece[][] board; // Contains null for empty pieces
    private final Map<Position, ChessPiece> whitePieces;
    private final Map<Position, ChessPiece> blackPieces;
    private final Pawn doubleStep;  // Reference to a pawn that just moved twice
    private final int score; // Total piece value ( Positive if white is winning, negative if black is )
    
    
    public Board(ChessPiece[][] board, Map<Position, ChessPiece> whitePieces,
                 Map<Position, ChessPiece> blackPieces, Pawn doubleStep, int score) {
        this.board = board;
        this.whitePieces = whitePieces;
        this.blackPieces = blackPieces;
        this.doubleStep = doubleStep;
        this.score = score;
    }
    
    public ChessPiece getPiece(Position position) {
        return board[position.row()][position.col()];
    }
    
    public Position getKingPos(boolean isWhite) {
        for (Position pos : isWhite ? whitePieces.keySet() : blackPieces.keySet()) {
            if (getPiece(pos) instanceof King) {
                return pos;
            }
        }
        return null;
    }
    
    public boolean canSee(ChessPiece piece, Position position, Position destination) {
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
    
    public int getScore() {
        return score;
    }
    
    public Map<Position, ChessPiece> getPieces(boolean isWhite) {
        return isWhite ? whitePieces : blackPieces;
    }
    
    public Pawn getDoubleStep() {
        return doubleStep;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board1 = (Board) o;
        return score == board1.score && Arrays.deepEquals(board, board1.board) && whitePieces.equals(board1.whitePieces) && blackPieces.equals(board1.blackPieces);
    }
    
    @Override
    public int hashCode() {
        int result = Objects.hash(whitePieces, blackPieces, score);
        result = 31 * result + Arrays.deepHashCode(board);
        return result;
    }
    
    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            boardString.append(i + 1).append("   ");
            for (int j = 0; j < 8; j++) {
                boardString.append(board[i][j] == null ? "      " : " " + board[i][j].toString() + " ");
            }
            boardString.append('\n');
        }
        boardString.append('\n');
        boardString.append("   ");
        for (int i = 0; i < 8; i++) {
            boardString.append("   ").append((char) (i + 'a'));
        }
        return boardString.toString();
    }

}
