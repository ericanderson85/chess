package controller;

import util.IllegalMoveException;
import util.Move;
import util.Position;
import model.*;

import java.util.Stack;

public class Game {
    private final Board board;
    private final Stack<ChessPiece[][]> boardHistory;  // All board positions in the game
    private final Stack<Move> moveHistory;  // All moves in the game
    private boolean isWhiteTurn;
    private int fiftyMoveRule;  // Set to 0 upon pawn move or capture
    private int whiteClock;  // Time remaining on white's clock (seconds)
    private int blackClock;  // Time remaining on black's clock (seconds)
    
    public Game(int clock) {
        this.board = new Board();
        this.boardHistory = new Stack<>();
        this.moveHistory = new Stack<>();
        this.isWhiteTurn = true;
        this.fiftyMoveRule = 0;
        this.whiteClock = clock;
        this.blackClock = clock;
        startGame();
    }
    
    private void startGame() {
        // Logic to start game
    }
    
    public void movePiece(Position position, Position destination) {
        Move move;
        
        try {
            move = board.movePiece(position, destination, isWhiteTurn);
        } catch (IllegalMoveException e) {
            System.out.println("-------------\nIllegal move:\n-------------");
            System.out.println(e.getMessage());
            System.out.println("-------------");
            return;
        }
    
        isWhiteTurn = !isWhiteTurn;  // Set turn to other team
        boardHistory.push(board.getBoardCopy());  // Add board to board history
        moveHistory.push(move);  // Add move to move history
        
        switch (move.checkType()) {
            case CHECK -> {
                if (isWhiteTurn) {} // Alert black they are in check
                else {} // Alert white they are in check
            }
            case CHECKMATE -> {
                if (isWhiteTurn) endGame(0);  // White wins
                else endGame(1);  // Black wins
                return;
            }
            case STALEMATE -> {
                endGame(2);  // End game in a draw
                return;
            }
        }
        
        
        if (move.movedPiece() instanceof Pawn) {
            if (move.moveType() == Move.MoveType.PROMOTION) {
                if (isWhiteTurn) {
                    // Prompt white for promotion type
                } else {
                    // Prompt black for promotion type
                }
            }
            fiftyMoveRule = 0;  // Fifty move rule reset on pawn move
        } else if (move.capturedPiece() != null) fiftyMoveRule = 0;  // Fifty move rule reset on capture
        else fiftyMoveRule++;  // Fifty move rule increments if move is not a pawn move or capture
        

        
        if (fiftyMoveRule == 100 || isThreefoldRepetition()) {  // Both players have to make 50 moves for rule to take effect
            endGame(2); // Game ends in a draw
        }
    }
    
    private boolean isThreefoldRepetition() {
        if (moveHistory.size() < 6) return false;  // Can't have two identical boards until 6 moves
        ChessPiece[][] lastBoard = boardHistory.pop();
        int boardIndex = boardHistory.indexOf(lastBoard);
        if (boardIndex >= 0) {  // Board has occurred at least twice if true
            boardHistory.remove(boardIndex);  // Remove board to check for third occurrence
            if (boardHistory.contains(lastBoard)) {
                boardHistory.insertElementAt(lastBoard, boardIndex);
                boardHistory.push(lastBoard);
                return true;
            }
        }
        return false;
    }
    
    private void endGame(int winner) {
        switch (winner) {
            case 0 -> System.out.println("White wins");
            case 1 -> System.out.println("Black wins");
            default -> System.out.println("The game ends in a draw");
        }
    }
    
}
