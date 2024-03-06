package util;

import model.Board;

public class Result {
    private final Board board;
    private final Move move;
    private final int evaluation;
    
    public Result(Board board, Move move, int evaluation) {
        this.board = board;
        this.move = move;
        this.evaluation = evaluation;
    }
    
    public Result(Board board, Move move) {
        this.board = board;
        this.move = move;
        this.evaluation = 0;
    }
    
    public Board getBoard() {
        return board;
    }
    
    public Move getMove() {
        return move;
    }
    
    public int getEvaluation() {
        return evaluation;
    }
}
