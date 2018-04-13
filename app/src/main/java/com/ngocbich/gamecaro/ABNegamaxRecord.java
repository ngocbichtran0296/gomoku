package com.ngocbich.gamecaro;

/**
 * Created by Ngoc Bich on 4/8/2018.
 */

public class ABNegamaxRecord {
    private Move move;
    private int score;

    public ABNegamaxRecord(Move move, int score) {
        this.move = move;
        this.score = score;
    }

    public ABNegamaxRecord() {
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
