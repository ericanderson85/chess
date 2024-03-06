package controller;

import model.Board;
import util.*;

import java.util.Scanner;

public class Player implements ChessPlayer {
    private final boolean isWhite;
    
    public Player(boolean isWhite) {
        this.isWhite = isWhite;
    }
    
    @Override
    public Pair<Board, Move> movePiece(Board currentBoard) {
        Pair<Board, Move> movePair = null;
        Scanner in = new Scanner(System.in);
        boolean validMoveEntered = false;
        while(!validMoveEntered) {
            System.out.println("Enter square to move:");
            Position position = decodePosition(in.next());
            System.out.println("Enter destination:");
            Position destination = decodePosition(in.next());
            try {
                movePair = BoardValidator.validate(currentBoard, position, destination, isWhite);
                validMoveEntered = true;
            } catch (IllegalMoveException e) {
                System.out.println(e.getMessage());
            }
        }
        return movePair;
    }
    
    @Override
    public Move.PromotionType getPromotionType() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter a piece (Queen, Rook, Bishop, Knight)");
        return switch (reader.next()) {
            case "Rook" -> Move.PromotionType.ROOK;
            case "Bishop" -> Move.PromotionType.BISHOP;
            case "Knight" -> Move.PromotionType.KNIGHT;
            default -> Move.PromotionType.QUEEN;
        };
    }
    
    private Position decodePosition(String str) {
        int row = Character.getNumericValue(str.charAt(1)) - 1;
        int col = str.charAt(0) - 97;
        return new Position(row, col);
    }
}
