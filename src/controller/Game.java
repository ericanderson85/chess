package controller;

import util.IllegalMoveException;
import util.Move;
import util.Position;
import model.*;

import java.util.Arrays;
import java.util.Stack;

public class Game {
    private final Board board;
    private final Stack<ChessPiece[][]> boardHistory;
    private final Stack<Move> moveHistory;
    private boolean isWhiteTurn;
    private int fiftyMoveRuleCounter;
    private int whiteSecondsLeft;
    private int blackSecondsLeft;
    
    public Game(int initialClock) {
        this.board = new Board();
        this.boardHistory = new Stack<>();
        this.moveHistory = new Stack<>();
        this.isWhiteTurn = true;
        this.fiftyMoveRuleCounter = 0;
        this.whiteSecondsLeft = initialClock;
        this.blackSecondsLeft = initialClock;
        startGame();
    }
    
    private void startGame() {
        // Logic to start game
    }
    
    public void movePiece(Position position, Position destination) {
        Move currentMove;
        
        try {
            currentMove = board.movePiece(position, destination, isWhiteTurn);
        } catch (IllegalMoveException e) {
            System.out.println("-------------\nIllegal move:\n-------------");
            System.out.println(e.getMessage());
            System.out.println("-------------");
            return;
        }
        
        isWhiteTurn = !isWhiteTurn;
        boardHistory.push(board.getBoardCopy());
        moveHistory.push(currentMove);
        
        switch (currentMove.checkType()) {
            case CHECK -> {
                if (isWhiteTurn) {
                } // Alert black they are in check
                else {
                } // Alert white they are in check
            }
            case CHECKMATE -> {
                if (isWhiteTurn) whiteVictory();
                else blackVictory();
                return;
            }
            case STALEMATE -> {
                endGameInDraw();
                return;
            }
        }
        
        
        if (currentMove.capturedPiece() == null || !(currentMove.movedPiece() instanceof Pawn)) {
            fiftyMoveRuleCounter++;
        }
        
        if (currentMove.movedPiece() instanceof Pawn) {
            if (currentMove.moveType() == Move.MoveType.PROMOTION) {
                if (isWhiteTurn) {
                    // Prompt white for promotion type
                } else {
                    // Prompt black for promotion type
                }
            }
            fiftyMoveRuleCounter = 0;
        }
        
        
        if (fiftyMoveRuleCounter == 100 || isThreefoldRepetition()) {
            endGameInDraw();
        }
    }
    
    private boolean isThreefoldRepetition() {
        if (moveHistory.size() < 6) {
            return false;
        }
        ChessPiece[][] currentBoard = boardHistory.peek();
        int count = 0;
        for (ChessPiece[][] boardState : boardHistory) {
            if (Arrays.deepEquals(boardState, currentBoard)) {
                count++;
            }
            if (count >= 3) {
                return true;
            }
        }
        return false;
    }
    
    private void whiteVictory() {
        System.out.println("White wins");
    }
    
    private void blackVictory() {
        System.out.println("Black wins");
    }
    
    private void endGameInDraw() {
        System.out.println("The game ends in a draw");
    }
    
}
