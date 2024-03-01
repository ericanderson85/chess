package controller;

import util.*;
import model.*;

import java.lang.reflect.Array;
import java.sql.SQLOutput;
import java.util.*;

public class Game {
    private Board currentBoard;
    private final Stack<Board> boardHistory;
    private boolean isWhiteTurn;
    private int fiftyMoveRuleCounter;
    private int whiteSecondsLeft;
    private int blackSecondsLeft;
    private final MoveFactory moveFactory;
    private final BoardFactory boardFactory;
    private boolean gameOver;
    
    
    public Game(int initialClock) {
        this.moveFactory = new MoveFactory();
        this.boardFactory = new BoardFactory();
        this.currentBoard = boardFactory.create();
        this.boardHistory = new Stack<>();
        this.boardHistory.push(currentBoard);
        this.isWhiteTurn = true;
        this.fiftyMoveRuleCounter = 0;
        this.whiteSecondsLeft = initialClock;
        this.blackSecondsLeft = initialClock;
        this.gameOver = false;
        startGame();
    }
    
    private void startGame() {
        while (!gameOver) {
            getMove();
        }
    }
    
    private void getMove() {
        if (isWhiteTurn) {
            playerMove(getPossibleBoards().poll());
            return;
        }
        getPossibleBoards();
        System.out.println((isWhiteTurn ? "White's" : "Black's") + " move");
        System.out.println(currentBoard);
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter square to move:");
        Position position = decodePosition(reader.next());
        System.out.println("Enter destination:");
        Position destination = decodePosition(reader.next());
        Pair<Board, Move> movePair;
        try {
            movePair = movePiece(position, destination);
        } catch (IllegalMoveException e) {
            System.out.println(e.getMessage());
            getMove();
            return;
        }
        playerMove(movePair);
    }
    
    private Position decodePosition(String str) {
        int row = Character.getNumericValue(str.charAt(1)) - 1;
        int col = str.charAt(0) - 97;
        return new Position(row, col);
    }
    
    public Pair<Board, Move> movePiece(Position position, Position destination) throws IllegalMoveException {
        Move newMove;
        Board newBoard;
        try {
            newMove = moveFactory.create(position, destination, isWhiteTurn, Move.PromotionType.QUEEN, currentBoard);
            newBoard = boardFactory.create(currentBoard, newMove);
        } catch (IllegalMoveException e) {
            throw new IllegalMoveException("\nIllegal move:\n" + e.getMessage());
        }
        
        return new Pair<>(newBoard, newMove);
    }
    
    private Move.PromotionType getPromotionType(Position position, Position destination) {
        if (!(currentBoard.getPiece(position) instanceof Pawn) || (destination.row() != 7 && destination.row() != 0)) {
            return Move.PromotionType.NONE;
        }
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter a piece (Queen, Rook, Bishop, Knight)");
        return switch (reader.next()) {
            case "Rook" -> Move.PromotionType.ROOK;
            case "Bishop" -> Move.PromotionType.BISHOP;
            case "Knight" -> Move.PromotionType.KNIGHT;
            default -> Move.PromotionType.QUEEN;
        };
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
    
    private Queue<Pair<Board, Move>> getPossibleBoards() {
        Queue<Pair<Board, Move>> boards = new PriorityQueue<>(Comparator.comparingInt((Pair<Board, Move> b) -> b.getKey().getScore()).reversed());
        Collection<ChessPiece> pieces = currentBoard.getPieces(true).values();
        for (ChessPiece piece : pieces) {
            for (Position destination : piece.possibleMoves()) {
                try {
                    boards.offer(movePiece(piece.getPosition(), destination));
                } catch (IllegalMoveException ignored) {
                }
            }
        }
        System.out.println(boards);
        return boards;
    }
    
    private void playerMove(Pair<Board, Move> movePair) {
        movePair.getValue().movedPiece().setPosition(movePair.getValue().destination());
        handleRepetition(movePair.getValue());
        isWhiteTurn = !isWhiteTurn;
        boardHistory.push(currentBoard);
        this.currentBoard = movePair.getKey();
    }
    
    
    public static void main(String[] args) {
        Game game = new Game(10);
    }
    
}


