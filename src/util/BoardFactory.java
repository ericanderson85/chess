package util;

import model.*;

import java.util.HashMap;
import java.util.Map;

public class BoardFactory {
    public Board create() {
        Map<Position, ChessPiece> whitePieces = new HashMap<>();
        Map<Position, ChessPiece> blackPieces = new HashMap<>();
        return new Board(
                newBoard(whitePieces, blackPieces),
                whitePieces,
                blackPieces,
                null,
                0
                
        );
    }
    
    public Board create(Board previous, Move move) throws IllegalMoveException {
        Board newBoard = switch (move.moveType()) {
            case NORMAL -> performNormalMove(previous, move);
            case CASTLE -> performCastle(previous, move);
            case EN_PASSANT -> performEnPassant(previous, move);
            case PROMOTION -> performPromotion(previous,  move);
        };
        if (ownKingInCheck(newBoard, move.movedPiece().isWhite())) {
            throw new IllegalMoveException("Move puts own king in check");
        }
        
        return newBoard;
    }
    
    private boolean ownKingInCheck(Board board, boolean isWhite) {
        Position kingPos = board.getKingPos(isWhite);
        for (ChessPiece piece : isWhite ? board.getPieces(false).values()
                : board.getPieces(true).values()) {
            if (piece.canMove(kingPos) && board.canSee(piece, piece.getPosition(),  kingPos)) {
                return true;
            }
        }
        return false;
    }
    
    private Board performNormalMove(Board previous, Move move) {
        if (move.moveType() != Move.MoveType.NORMAL) {
            throw new IllegalArgumentException("Incorrect board created");
        }
        ChessPiece[][] newBoard = previous.getBoardCopy();
        
        Map<Position, ChessPiece> whitePieces = new HashMap<>(previous.getPieces(true));
        Map<Position, ChessPiece> blackPieces = new HashMap<>(previous.getPieces(false));
        updatePieces(newBoard, whitePieces, blackPieces, move);
        
        Pawn doubleStep = getDoubleStep(move);
        int score = newScore(previous, move);
        
        return new Board(
                newBoard,
                whitePieces,
                blackPieces,
                doubleStep,
                score
        );
    }
    
    private void updatePieces(ChessPiece[][] newBoard, Map<Position, ChessPiece> whitePieces, Map<Position, ChessPiece> blackPieces, Move move) {
        if (move.movedPiece().isWhite()) {
            remove(move.destination(), newBoard, blackPieces);
            remove(move.position(), newBoard, whitePieces);
            add(move.movedPiece(), move.destination(), newBoard, whitePieces);
            return;
        }
        remove(move.destination(), newBoard, whitePieces);
        remove(move.position(), newBoard, blackPieces);
        add(move.movedPiece(), move.destination(), newBoard, blackPieces);
    }
    
    private Board performCastle(Board previous, Move move) {
        if (move.moveType() != Move.MoveType.CASTLE) {
            throw new IllegalArgumentException("Expected castle move type");
        }
        ChessPiece[][] newBoard = previous.getBoardCopy();
    
        Map<Position, ChessPiece> whitePieces = new HashMap<>(previous.getPieces(true));
        Map<Position, ChessPiece> blackPieces = new HashMap<>(previous.getPieces(false));
        updatePiecesCastle(newBoard, whitePieces, blackPieces, move);
        
        Pawn doubleStep = null;
        
        int score = previous.getScore();
    
        return new Board(
                newBoard,
                whitePieces,
                blackPieces,
                doubleStep,
                score
        );
    }
    
    private void updatePiecesCastle(ChessPiece[][] newBoard, Map<Position, ChessPiece> whitePieces, Map<Position, ChessPiece> blackPieces, Move move) {
        int kingCol;
        int rookCol;
        if (move.destination().col() == 7) {
            kingCol = 6;
            rookCol = 5;
        } else {
            kingCol = 2;
            rookCol = 3;
        }
        
        if (move.movedPiece().isWhite()) {
            remove(move.position(), newBoard, whitePieces);
            remove(move.destination(), newBoard, whitePieces);
            add(move.movedPiece(), new Position(0, kingCol), newBoard, whitePieces);
            add(move.capturedPiece(), new Position(0, rookCol), newBoard, whitePieces);
            return;
        }
        remove(move.position(), newBoard, blackPieces);
        remove(move.destination(), newBoard, blackPieces);
        add(move.movedPiece(), new Position(7, kingCol), newBoard, blackPieces);
        add(move.capturedPiece(), new Position(7, rookCol), newBoard, blackPieces);
    }
        
        
    private Board performEnPassant(Board previous, Move move) {
        if (move.moveType() != Move.MoveType.EN_PASSANT) {
            throw new IllegalArgumentException("Expected en passant move type");
        }
        ChessPiece[][] newBoard = previous.getBoardCopy();
    
        Map<Position, ChessPiece> whitePieces = new HashMap<>(previous.getPieces(true));
        Map<Position, ChessPiece> blackPieces = new HashMap<>(previous.getPieces(false));
        updatePiecesEnPassant(newBoard, whitePieces, blackPieces, move);
    
        Pawn doubleStep = null;
    
        int score = newScore(previous, move);
    
        return new Board(
                newBoard,
                whitePieces,
                blackPieces,
                doubleStep,
                score
        );
    }
    
    private void updatePiecesEnPassant(ChessPiece[][] newBoard, Map<Position, ChessPiece> whitePieces, Map<Position, ChessPiece> blackPieces, Move move) {
        if (move.movedPiece().isWhite()) {
            remove(new Position(move.destination().row() - 1, move.destination().col()), newBoard, blackPieces);
            remove(move.position(), newBoard, whitePieces);
            add(move.movedPiece(), move.destination(), newBoard, whitePieces);
            return;
        }
        remove(new Position(move.destination().row() + 1, move.destination().col()), newBoard, whitePieces);
        remove(move.position(), newBoard, blackPieces);
        add(move.movedPiece(), move.destination(), newBoard, blackPieces);
    }
    
    private Board performPromotion(Board previous, Move move) throws IllegalMoveException {
        if (move.moveType() != Move.MoveType.PROMOTION) {
            throw new IllegalArgumentException("Expected promotion move type");
        }
        ChessPiece[][] newBoard = previous.getBoardCopy();
    
        Map<Position, ChessPiece> whitePieces = new HashMap<>(previous.getPieces(true));
        Map<Position, ChessPiece> blackPieces = new HashMap<>(previous.getPieces(false));
        updatePieces(newBoard, whitePieces, blackPieces, move);
        updatePromotionPiece(newBoard, whitePieces, blackPieces, move);
    
        Pawn doubleStep = null;
    
        int score = newScorePromotion(previous, move);
    
        return new Board(
                newBoard,
                whitePieces,
                blackPieces,
                doubleStep,
                score
        );
    }
    
    private void updatePromotionPiece(ChessPiece[][] newBoard, Map<Position, ChessPiece> whitePieces, Map<Position, ChessPiece> blackPieces, Move move) {
        remove(move.destination(), newBoard, move.movedPiece().isWhite() ? whitePieces : blackPieces);
        add(getPromotionPiece(move), move.destination(), newBoard, move.movedPiece().isWhite() ? whitePieces : blackPieces);
    }
    
    private ChessPiece getPromotionPiece(Move move) {
        return switch(move.promotionType()) {
            case QUEEN -> new Queen(move.destination(), move.movedPiece().isWhite());
            case ROOK -> new Rook(move.destination(), move.movedPiece().isWhite());
            case KNIGHT -> new Knight(move.destination(), move.movedPiece().isWhite());
            case BISHOP -> new Bishop(move.destination(), move.movedPiece().isWhite());
            case NONE -> throw new IllegalArgumentException("Can't get promotion piece for no promotion");
        };
    }
    
    private int newScorePromotion(Board previous, Move move) {
        int difference = 0;
        difference += (move.capturedPiece() == null ? 0 : move.capturedPiece().getValue());
        difference += switch(move.promotionType()) {
            case QUEEN -> 8;
            case ROOK -> 4;
            case KNIGHT, BISHOP -> 2;
            case NONE -> throw new IllegalArgumentException("Can't get promotion score for no promotion");
        };

        return previous.getScore() + (move.movedPiece().isWhite() ? difference : -difference);
    }
    
    
    private Pawn getDoubleStep(Move move) {
        if (!(move.movedPiece() instanceof Pawn)) {
            return null;
        }
        if ((move.position().row() != 1 || move.destination().row() != 3)
                && (move.position().row() != 6 || move.destination().row() != 4)) {
            return null;
        }
        return (Pawn)move.movedPiece();
    }
    
    private void remove(Position position, ChessPiece[][] board, Map<Position, ChessPiece> pieces) {
        board[position.row()][position.col()] = null;
        pieces.remove(position);
    }
    
    private void add(ChessPiece piece, Position position, ChessPiece[][] board, Map<Position, ChessPiece> pieces) {
        board[position.row()][position.col()] = piece;
        if (piece != null) {
            pieces.put(position, piece);
            piece.setPosition(position);
        }
    }
    
    private int newScore(Board previous, Move move) {
        if (move.capturedPiece() == null) {
            return previous.getScore();
        }
        return previous.getScore() +
                (move.movedPiece().isWhite() ? move.capturedPiece().getValue() : -move.capturedPiece().getValue());
    }
    
    private ChessPiece[][] newBoard(Map<Position, ChessPiece> whitePieces, Map<Position, ChessPiece> blackPieces) {
        ChessPiece[][] board = new ChessPiece[8][8];
        initializeBoard(board, whitePieces, blackPieces);
        return board;
    }
    
    private void initializeBoard(ChessPiece[][] board, Map<Position, ChessPiece> whitePieces,
                                 Map<Position, ChessPiece> blackPieces) {
        ChessPieceFactory chessPieceFactory = new ChessPieceFactory();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position position = new Position(row, col);
                ChessPiece piece = chessPieceFactory.create(position);
                add(piece, position, board, piece == null ? null : piece.isWhite() ? whitePieces : blackPieces);
            }
        }
    }
}
