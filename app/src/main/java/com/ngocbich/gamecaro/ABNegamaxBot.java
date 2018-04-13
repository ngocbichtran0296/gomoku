package com.ngocbich.gamecaro;

import android.util.Log;

/**
 * Created by Ngoc Bich on 4/8/2018.
 */

public class ABNegamaxBot {
    public ABNegamaxRecord abNegamax(BoardGame boardGame, int player, int maxDepth, int currentDepth,int alpha, int beta) {
        if (boardGame.isGameOver() || currentDepth==maxDepth) {
            return new ABNegamaxRecord(null, boardGame.evaluate(player));
        }
        Move bestMove = null;
        int bestScore=Integer.MIN_VALUE;

        for (Move move : boardGame.getMoves()) {

            BoardGame newboard = new BoardGame(boardGame.getBitmapWidth(), boardGame.getBitmapHeight(), boardGame.getColQuantity(), boardGame.getRowQuantity(), boardGame.getContext());
            newboard.setPlayer(boardGame.getPlayer());
            newboard.setBoard(boardGame.getNewBoard());

            newboard.makeMove(move);

            ABNegamaxRecord abNegamaxRecord = abNegamax(newboard, player, maxDepth,currentDepth+1, -beta, -alpha);
            int currentScore=-abNegamaxRecord.getScore();

            if(currentScore>bestScore){
                bestScore=currentScore;
                bestMove=move;
                Log.d("bestscore",bestScore+"");
                if(bestScore>=beta){
                    return new ABNegamaxRecord(bestMove,bestScore);
                }
                if (bestScore>alpha){
                    alpha=bestScore;
                }
            }

        }
        return new ABNegamaxRecord(bestMove,bestScore);
    }

    public ABNegamaxRecord minimax(BoardGame boardGame,int player, int currentDepth, int maxDepth){
        Move bestMove=null;
        int bestScore;
        if(boardGame.isGameOver() || currentDepth==maxDepth){
            return new ABNegamaxRecord(null,boardGame.evaluate(player));
        }

        if(boardGame.getPlayer()==player){
            bestScore=Integer.MIN_VALUE;
        }else {
            bestScore=Integer.MAX_VALUE;
        }
        Log.d("player", player + "");
        for(Move move:boardGame.getMoves()){
            BoardGame newboard=new BoardGame(boardGame.getBitmapWidth(), boardGame.getBitmapHeight(), boardGame.getColQuantity(), boardGame.getRowQuantity(), boardGame.getContext());
            newboard.setBoard(boardGame.getNewBoard());
            newboard.setPlayer(boardGame.getPlayer());

            newboard.makeMove(move);
            ABNegamaxRecord record=minimax(newboard,player,currentDepth++,maxDepth);

            if(boardGame.getPlayer()==player){
                if(record.getScore()>bestScore){
                    bestScore=record.getScore();
                    bestMove=move;
                }
            }else {
                if(record.getScore()<bestScore){
                    bestScore=record.getScore();
                    bestMove=move;
                }
            }
            Log.d("bestscore", bestScore + "");
        }
        return new ABNegamaxRecord(bestMove,bestScore);
    }
}
