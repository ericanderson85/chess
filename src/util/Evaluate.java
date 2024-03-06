package util;

import model.*;

import java.util.List;

public class Evaluate {
    
    public static int evaluate(Board board, boolean isWhiteTurn) {
        int score = 0;
        
        List<ChessPiece> whitePieces = board.getPieces(true);
        List<ChessPiece> blackPieces = board.getPieces(false);
        
        for (ChessPiece piece : whitePieces) {
            score += switch (piece.getType()) {
                case PAWN -> 100 + PieceValueTables.PAWN_TABLE[piece.getPosition().row()][piece.getPosition().col()];
                case KNIGHT -> 320 + PieceValueTables.KNIGHT_TABLE[piece.getPosition().row()][piece.getPosition().col()];
                case BISHOP -> 330 + PieceValueTables.BISHOP_TABLE[piece.getPosition().row()][piece.getPosition().col()];
                case ROOK -> 500 + PieceValueTables.ROOK_TABLE[piece.getPosition().row()][piece.getPosition().col()];
                case QUEEN -> 900 + PieceValueTables.QUEEN_TABLE[piece.getPosition().row()][piece.getPosition().col()];
                case KING -> 20000 + PieceValueTables.KING_TABLE[piece.getPosition().row()][piece.getPosition().col()];
            };
        }
        
        for (ChessPiece piece : blackPieces) {
            score -= switch (piece.getType()) {
                case PAWN -> 100 + PieceValueTables.PAWN_TABLE[7 - piece.getPosition().row()][piece.getPosition().col()];
                case KNIGHT -> 320 + PieceValueTables.KNIGHT_TABLE[7 - piece.getPosition().row()][piece.getPosition().col()];
                case BISHOP -> 330 + PieceValueTables.BISHOP_TABLE[7 - piece.getPosition().row()][piece.getPosition().col()];
                case ROOK -> 500 + PieceValueTables.ROOK_TABLE[7 - piece.getPosition().row()][piece.getPosition().col()];
                case QUEEN -> 900 + PieceValueTables.QUEEN_TABLE[7 - piece.getPosition().row()][piece.getPosition().col()];
                case KING -> 20000 + PieceValueTables.KING_TABLE[7 - piece.getPosition().row()][piece.getPosition().col()];
            };
        }
        return score;
    }
}
