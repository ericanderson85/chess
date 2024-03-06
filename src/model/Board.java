package model;

import util.*;

import java.util.*;

public class Board {
    private final ChessPiece[][] board; // Contains null for empty pieces
    private final Position blackKingPos;
    private final Position whiteKingPos;
    private final List<ChessPiece> whitePieces;
    private final List<ChessPiece> blackPieces;
    private final Pawn doubleStep;  // Reference to a pawn that just moved twice
    
    public Board(ChessPiece[][] board, Position whiteKingPos, Position blackKingPos, List<ChessPiece> whitePieces,
                 List<ChessPiece> blackPieces, Pawn doubleStep) {
        this.board = board;
        this.whiteKingPos = whiteKingPos;
        this.blackKingPos = blackKingPos;
        this.whitePieces = whitePieces;
        this.blackPieces = blackPieces;
        this.doubleStep = doubleStep;
    }
    
    public ChessPiece getPiece(Position position) {
        return board[position.row()][position.col()];
    }
    
    public Position getKingPos(boolean isWhite) {
        return isWhite ? whiteKingPos : blackKingPos;
    }
    
    public boolean canSee(ChessPiece piece, Position destination) {
        if (piece instanceof Knight || piece instanceof King) return true;
        Position position = piece.getPosition();
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
    
    public boolean kingInCheck(boolean isWhite) {
        Position kingPos = getKingPos(isWhite);
        for (ChessPiece piece : isWhite ? blackPieces : whitePieces) {
            if (piece.canMove(kingPos) && canSee(piece, kingPos)) {
                return true;
            }
        }
        return false;
    }
    
    public List<ChessPiece> getPieces(boolean isWhite) {
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
    
    public static boolean isOutOfBounds(Position position) {
        return (position.row() > 7 || position.row() < 0 || position.col() > 7 || position.col() < 0);
    }
    
    public List<Pair<Board, Move>> generateMoves(boolean isWhite) {
        List<Pair<Board, Move>> boards = new ArrayList<>();
        Collection<ChessPiece> pieces = getPieces(isWhite);
        for (ChessPiece piece : pieces) {
            for (Position destination : piece.possibleMoves()) {
                try {
                    if (getPiece(destination) instanceof King) {
                        continue;
                    }
                    boards.add(BoardValidator.validate(this, piece.getPosition(), destination, isWhite));
                } catch (IllegalMoveException ignored) {
                }
            }
        }
        return boards;
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board1 = (Board) o;
        return Arrays.deepEquals(board, board1.board);
    }
    
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
    
    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            boardString.append(i + 1).append("   ");
            for (int j = 0; j < 8; j++) {
                boardString.append(board[i][j] == null ? j % 2 == 0 ? " _______ " : " ------- " : " " + board[i][j].toString() + " ");
            }
            boardString.append('\n');
        }
        boardString.append('\n');
        boardString.append("   ");
        for (int i = 0; i < 8; i++) {
            boardString.append("     ").append((char) (i + 'a')).append("   ");
        }
        return boardString.toString();
    }
    
}