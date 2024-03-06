package util;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class BoardValidator {
    public static Pair<Board, Move> validate(Board board, Position position, Position destination, boolean isWhiteTurn) throws IllegalMoveException {
        Move newMove = MoveFactory.create(board, position, destination, isWhiteTurn, Move.PromotionType.QUEEN);
        Board newBoard = BoardFactory.create(board, newMove);
        return new Pair<>(newBoard, newMove);
    }
    
    public static Board createNewBoard() {
        return BoardFactory.create();
    }
    
    private static class MoveFactory {
        public static Move create(Board board, Position position, Position destination, boolean isWhiteTurn, Move.PromotionType promotionType) throws IllegalMoveException {
            ChessPiece thisPiece = confirmPiece(board, position, isWhiteTurn);
            validateMove(board, thisPiece, position, destination);
            ChessPiece otherPiece = board.getPiece(destination);
            return new Move(
                    position,
                    thisPiece,
                    destination,
                    otherPiece,
                    determineMoveType(board, thisPiece, otherPiece, destination),
                    promotionType);
        }
        
        private static Move.MoveType determineMoveType(Board board, ChessPiece thisPiece, ChessPiece otherPiece, Position destination) throws IllegalMoveException {
            if (otherPiece != null && (thisPiece.isWhite() == otherPiece.isWhite())) {
                if (isCastling(thisPiece, otherPiece)) {
                    return Move.MoveType.CASTLE;
                }
                throw new IllegalMoveException("Capturing piece of same color");
            } else if (thisPiece instanceof Pawn) {
                if (isEnPassant(board, thisPiece, destination)) {
                    return Move.MoveType.EN_PASSANT;
                } else if (isPromotion(thisPiece, destination)) {
                    return Move.MoveType.PROMOTION;
                }
            }
            return Move.MoveType.NORMAL;
        }
        
        private static boolean isCastling(ChessPiece thisPiece, ChessPiece otherPiece) {
            if (thisPiece instanceof King && otherPiece instanceof Rook) {
                return !((King) thisPiece).hasMoved() && !((Rook) otherPiece).hasMoved();
            }
            return false;
        }
        
        private static boolean isEnPassant(Board board, ChessPiece thisPiece, Position destination) {
            if (board.getDoubleStep() == null) {
                return false;
            }
            return new Position(destination.row() + (thisPiece.isWhite() ? 1 : -1), destination.col())
                    .equals(board.getDoubleStep().getPosition());
        }
        
        private static boolean isPromotion(ChessPiece thisPiece, Position destination) {
            return (thisPiece.isWhite() && destination.row() == 7) || (!thisPiece.isWhite() && destination.row() == 0);
        }
        
        private static ChessPiece confirmPiece(Board board, Position position, boolean isWhiteTurn) throws IllegalMoveException {
            ChessPiece thisPiece = board.getPiece(position);
            if (thisPiece == null) {
                throw new IllegalMoveException("No piece to be moved");
            }
            if (thisPiece.isWhite() != isWhiteTurn) {
                throw new IllegalMoveException("Can not move other player's piece");
            }
            return thisPiece;
        }
        
        private static void validateMove(Board board, ChessPiece thisPiece, Position position, Position destination) throws IllegalMoveException {
            if (Board.isOutOfBounds(destination)) {
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
                        || (destination.col() != position.col() && board.getPiece(destination) == null)) {
                    throw new IllegalMoveException("Illegal move for type Pawn");
                }
            }
            if (!board.canSee(thisPiece, destination)) {
                throw new IllegalMoveException(thisPiece.getClass().toString() + "can't jump over tiles");
            }
        }
    }
    
    private static class BoardFactory {
        public static Board create() {
            List<ChessPiece> whitePieces = new ArrayList<>();
            List<ChessPiece> blackPieces = new ArrayList<>();
            return new Board(
                    newBoard(whitePieces, blackPieces),
                    new Position(7, 4),
                    new Position(0, 4),
                    whitePieces,
                    blackPieces,
                    null
            );
        }
        
        public static Board create(Board previous, Move move) throws IllegalMoveException {
            Board newBoard = switch (move.moveType()) {
                case NORMAL -> performNormalMove(previous, move);
                case CASTLE -> performCastle(previous, move);
                case EN_PASSANT -> performEnPassant(previous, move);
                case PROMOTION -> performPromotion(previous, move);
            };
            if (ownKingInCheck(newBoard, move.movedPiece().isWhite())) {
                throw new IllegalMoveException("Move puts own king in check");
            }
            
            return newBoard;
        }
        
        private static boolean ownKingInCheck(Board board, boolean isWhite) {
            Position kingPos = board.getKingPos(isWhite);
            for (ChessPiece piece : isWhite ? board.getPieces(false) : board.getPieces(true)) {
                if (piece.canMove(kingPos) && board.canSee(piece, kingPos)) {
                    return true;
                }
            }
            return false;
        }
        
        private static Board performNormalMove(Board previous, Move move) {
            if (move.moveType() != Move.MoveType.NORMAL) {
                throw new IllegalArgumentException("Incorrect board created");
            }
            ChessPiece[][] newBoard = previous.getBoardCopy();
            
            List<ChessPiece> whitePieces = new ArrayList<>(previous.getPieces(true));
            List<ChessPiece> blackPieces = new ArrayList<>(previous.getPieces(false));
            updatePieces(newBoard, whitePieces, blackPieces, move);
            
            Pawn doubleStep = getDoubleStep(move);
            
            return new Board(
                    newBoard,
                    getKingPos(whitePieces),
                    getKingPos(blackPieces),
                    whitePieces,
                    blackPieces,
                    doubleStep
            );
        }
        
        private static void updatePieces(ChessPiece[][] board, List<ChessPiece> whitePieces, List<ChessPiece> blackPieces, Move move) {
            remove(board, move.capturedPiece(), whitePieces, blackPieces);
            remove(board, move.movedPiece(), whitePieces, blackPieces);
            add(board, move.movedPiece(), move.destination(), whitePieces, blackPieces);
        }
        
        private static Board performCastle(Board previous, Move move) {
            if (move.moveType() != Move.MoveType.CASTLE) {
                throw new IllegalArgumentException("Expected castle move type");
            }
            ChessPiece[][] newBoard = previous.getBoardCopy();
            
            List<ChessPiece> whitePieces = new ArrayList<>(previous.getPieces(true));
            List<ChessPiece> blackPieces = new ArrayList<>(previous.getPieces(false));
            updatePiecesCastle(newBoard, whitePieces, blackPieces, move);
            
            
            return new Board(
                    newBoard,
                    getKingPos(whitePieces),
                    getKingPos(blackPieces),
                    whitePieces,
                    blackPieces,
                    null
            );
        }
        
        private static Position getKingPos(List<ChessPiece> pieces) {
            for (ChessPiece piece : pieces) {
                if (piece instanceof King) {
                    return piece.getPosition();
                }
            }
            return null;
        }
        
        private static void updatePiecesCastle(ChessPiece[][] board, List<ChessPiece> whitePieces, List<ChessPiece> blackPieces, Move move) {
            int kingCol;
            int rookCol;
            if (move.destination().col() == 7) {
                kingCol = 6;
                rookCol = 5;
            } else {
                kingCol = 2;
                rookCol = 3;
            }
            
            remove(board, move.movedPiece(), whitePieces, blackPieces);
            remove(board, move.capturedPiece(), whitePieces, blackPieces);
            add(board, move.movedPiece(), new Position(move.position().row(), kingCol), whitePieces, blackPieces);
            add(board, move.capturedPiece(), new Position(move.position().row(), rookCol), whitePieces, blackPieces);
        }
        
        
        private static Board performEnPassant(Board previous, Move move) {
            if (move.moveType() != Move.MoveType.EN_PASSANT) {
                throw new IllegalArgumentException("Expected en passant move type");
            }
            ChessPiece[][] newBoard = previous.getBoardCopy();
            
            List<ChessPiece> whitePieces = new ArrayList<>(previous.getPieces(true));
            List<ChessPiece> blackPieces = new ArrayList<>(previous.getPieces(false));
            updatePiecesEnPassant(previous, newBoard, whitePieces, blackPieces, move);
            
            
            return new Board(
                    newBoard,
                    getKingPos(whitePieces),
                    getKingPos(blackPieces),
                    whitePieces,
                    blackPieces,
                    null
            );
        }
        
        private static void updatePiecesEnPassant(Board previousBoard, ChessPiece[][] board, List<ChessPiece> whitePieces, List<ChessPiece> blackPieces, Move move) {
            Position capturedPosition = new Position(move.position().row(), move.destination().col());
            remove(board, previousBoard.getPiece(capturedPosition), whitePieces, blackPieces);
            remove(board, move.movedPiece(), whitePieces, blackPieces);
            add(board, move.movedPiece(), move.destination(), whitePieces, blackPieces);
        }
        
        private static Board performPromotion(Board previous, Move move) {
            if (move.moveType() != Move.MoveType.PROMOTION) {
                throw new IllegalArgumentException("Expected promotion move type");
            }
            ChessPiece[][] newBoard = previous.getBoardCopy();
            
            List<ChessPiece> whitePieces = new ArrayList<>(previous.getPieces(true));
            List<ChessPiece> blackPieces = new ArrayList<>(previous.getPieces(false));
            updatePieces(newBoard, whitePieces, blackPieces, move);
            updatePromotionPiece(newBoard, whitePieces, blackPieces, move);
            
            return new Board(
                    newBoard,
                    getKingPos(whitePieces),
                    getKingPos(blackPieces),
                    whitePieces,
                    blackPieces,
                    null
            );
        }
        
        private static void updatePromotionPiece(ChessPiece[][] board, List<ChessPiece> whitePieces, List<ChessPiece> blackPieces, Move move) {
            remove(board, move.movedPiece(), whitePieces, blackPieces);
            add(board, getPromotionPiece(move), move.destination(), whitePieces, blackPieces);
        }
        
        private static ChessPiece getPromotionPiece(Move move) {
            return switch (move.promotionType()) {
                case QUEEN -> new Queen(move.destination(), move.movedPiece().isWhite());
                case ROOK -> new Rook(move.destination(), move.movedPiece().isWhite());
                case KNIGHT -> new Knight(move.destination(), move.movedPiece().isWhite());
                case BISHOP -> new Bishop(move.destination(), move.movedPiece().isWhite());
                case NONE ->
                        throw new IllegalArgumentException("Can't get promotion piece for no promotion");
            };
        }
        
        
        private static Pawn getDoubleStep(Move move) {
            if (!(move.movedPiece() instanceof Pawn)) {
                return null;
            }
            if ((move.position().row() != 1 || move.destination().row() != 3)
                    && (move.position().row() != 6 || move.destination().row() != 4)) {
                return null;
            }
            return (Pawn) move.movedPiece();
        }
        
        private static void remove(ChessPiece[][] board, ChessPiece piece, List<ChessPiece> whitePieces, List<ChessPiece> blackPieces) {
            if (piece == null) {
                return;
            }
            board[piece.getPosition().row()][piece.getPosition().col()] = null;
            if (piece.isWhite()) {
                whitePieces.remove(piece);
            } else {
                blackPieces.remove(piece);
            }
        }
        private static void add(ChessPiece[][] board, ChessPiece piece, Position position, List<ChessPiece> whitePieces, List<ChessPiece> blackPieces) {
            if (piece == null) {
                board[position.row()][position.col()] = null;
                return;
            }
            ChessPiece newPiece = piece.copyTo(position);
            board[position.row()][position.col()] = newPiece;
            if (piece.isWhite()) {
                whitePieces.add(newPiece);
            } else {
                blackPieces.add(newPiece);
            }
        }
        
        private static void addNoCopy(ChessPiece[][] board, ChessPiece piece, Position position, List<ChessPiece> whitePieces, List<ChessPiece> blackPieces) {
            if (piece == null) {
                board[position.row()][position.col()] = null;
                return;
            }
            board[position.row()][position.col()] = piece;
            if (piece.isWhite()) {
                whitePieces.add(piece);
            } else {
                blackPieces.add(piece);
            }
        }
        
        private static ChessPiece[][] newBoard(List<ChessPiece> whitePieces, List<ChessPiece> blackPieces) {
            ChessPiece[][] board = new ChessPiece[8][8];
            ChessPieceFactory chessPieceFactory = new ChessPieceFactory();
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    Position position = new Position(row, col);
                    ChessPiece piece = chessPieceFactory.create(position);
                    addNoCopy(board, piece, position, whitePieces, blackPieces);
                }
            }
            return board;
        }
    }
    
    
}
