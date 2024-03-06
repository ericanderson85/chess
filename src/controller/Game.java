package controller;

import util.*;
import model.*;

import java.sql.SQLOutput;
import java.util.*;

public class Game {
    private Board currentBoard;
    private final Stack<Board> boardHistory;
    private boolean isWhiteTurn;
    private int fiftyMoveRuleCounter;
    private int whiteSecondsLeft;
    private int blackSecondsLeft;
    private ChessPlayer blackPlayer;
    private ChessPlayer whitePlayer;
    private boolean gameOver;
    
    
    public Game(int initialClock, ChessPlayer whitePlayer, ChessPlayer blackPlayer) {
        this.whiteSecondsLeft = initialClock;
        this.blackSecondsLeft = initialClock;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.currentBoard = BoardValidator.createNewBoard();
        this.boardHistory = new Stack<>();
        this.boardHistory.push(currentBoard);
        this.isWhiteTurn = true;
        this.fiftyMoveRuleCounter = 0;
        this.gameOver = false;
        startGame();
    }
    
    private void startGame() {
        while (!gameOver) {
            getMove();
        }
    }
    
    private void getMove() {
        Pair<Board, Move> movePair;
        if (isWhiteTurn) {
            movePair = whitePlayer.movePiece(currentBoard);
        } else {
            movePair = blackPlayer.movePiece(currentBoard);
        }
        playerMove(movePair);
    }
    
    private void playerMove(Pair<Board, Move> movePair) {
        handleRepetition(movePair.getValue());
        isWhiteTurn = !isWhiteTurn;
        boardHistory.push(currentBoard);
        this.currentBoard = movePair.getKey();
        System.out.println(movePair);
        System.out.println("Evaluation: " + Evaluate.evaluate(movePair.getKey(), isWhiteTurn));
    }
    
    private void handleRepetition(Move move) {
        if (move.capturedPiece() == null || !(move.movedPiece() instanceof Pawn)) {
            fiftyMoveRuleCounter++;
        }
        
        if (move.movedPiece() instanceof Pawn) {
            fiftyMoveRuleCounter = 0;
        }
        
        if (fiftyMoveRuleCounter == 100 || isThreefoldRepetition()) {
            endGameInDraw();
        }
    }
    
    private boolean isThreefoldRepetition() {
        if (boardHistory.size() < 6) {
            return false;
        }
        int currentPieceCount = totalPieceCount(currentBoard);
        int count = 0;
        for (Board board : boardHistory) {
            if (totalPieceCount(board) != currentPieceCount) {
                return false;
            }
            if (currentBoard.equals(board)) {
                count++;
            }
            if (count >= 3) {
                return true;
            }
        }
        return false;
    }
    
    private int totalPieceCount(Board board) {
        return board.getPieces(true).size() + board.getPieces(false).size();
    }
    
    private void whiteVictory() {
        System.out.println("White wins");
        this.gameOver = true;
    }
    
    private void blackVictory() {
        System.out.println("Black wins");
        this.gameOver = true;
    }
    
    private void endGameInDraw() {
        System.out.println("The game ends in a draw");
        this.gameOver = true;
    }
    

    
    
    public static void main(String[] args) {
        new Game(10, new Bot(true), new Bot(false));
    }
    
}


