package com.ngocbich.gamecaro;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Ngoc Bich on 4/7/2018.
 */

public class BoardGame {
    ABNegamaxBot bot;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint;

    private int bitmapWidth;
    private int bitmapHeight;
    private int colQuantity;
    private int rowQuantity;

    private int[][] board;
    private int player;
    private Context context;

    private List<Line> lineList;//chua cac diem de ve ban co
    private Bitmap bitmap1;
    private Bitmap bitmap2;

    public BoardGame(int bitmapWidth, int bitmapHeight, int colQuantity, int rowQuantity, Context context) {
        this.bitmapWidth = bitmapWidth;
        this.bitmapHeight = bitmapHeight;
        this.colQuantity = colQuantity;
        this.rowQuantity = rowQuantity;
        this.context = context;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getBitmapWidth() {
        return bitmapWidth;
    }

    public void setBitmapWidth(int bitmapWidth) {
        this.bitmapWidth = bitmapWidth;
    }

    public int getBitmapHeight() {
        return bitmapHeight;
    }

    public void setBitmapHeight(int bitmapHeight) {
        this.bitmapHeight = bitmapHeight;
    }

    public int getColQuantity() {
        return colQuantity;
    }

    public void setColQuantity(int colQuantity) {
        this.colQuantity = colQuantity;
    }

    public int getRowQuantity() {
        return rowQuantity;
    }

    public void setRowQuantity(int rowQuantity) {
        this.rowQuantity = rowQuantity;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public void init() {
        bot = new ABNegamaxBot();
        bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();

        board = new int[this.rowQuantity][this.colQuantity];

        //gan gia tri cho bang co voi -1 la chua di
        for (int i = 0; i < rowQuantity; i++) {
            for (int j = 0; j < colQuantity; j++) {
                board[i][j] = -1;
            }
        }

        //nguoi choi dau tien co gia tri la 0; nguoi thu hai co gia tri la 1;
        player = 0;

        int celwidth = bitmapWidth / colQuantity;
        int celheight = bitmapHeight / rowQuantity;
        lineList = new ArrayList<>();//luu cac diem ve cac o cua bang co
        for (int i = 0; i <= rowQuantity; i++) {
            lineList.add(new Line(0, i * celheight, bitmapWidth, i * celheight));
        }
        for (int i = 0; i <= colQuantity; i++) {
            lineList.add(new Line(i * celwidth, 0, i * celwidth, bitmapHeight));
        }

        bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.cross);
        bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.circle);
    }

    public Bitmap drawBoard(float strokeWidth) {
        paint.setStrokeWidth(strokeWidth);
        for (Line line : lineList) {
            canvas.drawLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY(), paint);
        }
        return bitmap;
    }

    public boolean onTouch(final View v, MotionEvent event) throws ExecutionException, InterruptedException {
        int celWidth = v.getWidth() / colQuantity;
        int celHeight = v.getHeight() / rowQuantity;

        int colIndex = (int) event.getX() / celWidth;
        int rowIndex = (int) event.getY() / celHeight;

        if (board[rowIndex][colIndex] != -1) {
            return true;
        }

        board[rowIndex][colIndex] = player;
        onDraw(rowIndex, colIndex, v);
        v.invalidate();
     //   player=(player+1)%2;

        if (isGameOver()) {
            Toast.makeText(context, "End game", Toast.LENGTH_SHORT).show();
        } else {
            ABNegamaxRecord record=new Ai().execute("").get();
            onDraw(record.getMove().getRowIndex(), record.getMove().getColIndex(), v);
            v.invalidate();
        }

        return true;
    }

    public void onDraw(int rowIndex, int colIndex, View v) {
        int celBitMapWidth = bitmapWidth / colQuantity;
        int celBitMapHeight = bitmapHeight / rowQuantity;
        int padding = 3;
        if (player == 0) {
            canvas.drawBitmap(bitmap1, new Rect(0, 0, bitmap1.getWidth(), bitmap1.getHeight()),
                    new Rect(colIndex * celBitMapWidth + padding, rowIndex * celBitMapHeight + padding,
                            (colIndex + 1) * celBitMapWidth - padding, (rowIndex + 1) * celBitMapHeight - padding), paint);
            player = 1;
        } else if (player == 1) {
            canvas.drawBitmap(bitmap2, new Rect(0, 0, bitmap2.getWidth(), bitmap2.getHeight()),
                    new Rect(colIndex * celBitMapWidth + padding, rowIndex * celBitMapHeight + padding,
                            (colIndex + 1) * celBitMapWidth - padding, (rowIndex + 1) * celBitMapHeight - padding), paint);
            player = 0;
        }
    }

    public boolean checkWin(int player) {

        for(int i=0;i<rowQuantity;i++){
            int countRow=0;
            for(int t=0;t<colQuantity;t++){
                if(board[i][t]==player) countRow++;
                else countRow=0;
                if(countRow>=5)return true;
            }
        }
        for(int i=0;i<rowQuantity;i++){
            int countCol=0;
            for(int j=0;j<colQuantity;j++){
                if(board[j][i]==player)countCol++;
                else countCol=0;
                if (countCol>=5)return true;
            }
        }


        int count = 0;
        int j = 0;
        for (int i = 3; i < rowQuantity; i++) {
            if (board[i][j] == player) {
                count++;
            } else count = 0;
            if (count >= 5) return true;
            j++;
        }

        count = 0;
        j = 0;
        for (int i = 2; i < rowQuantity; i++) {
            if (board[i][j] == player) {
                count++;
            } else count = 0;
            if (count >= 5) return true;
            j++;
        }

        count = 0;
        j = 0;
        for (int i = 1; i < rowQuantity; i++) {
            if (board[i][j] == player) {
                count++;
            } else count = 0;
            if (count >= 5) return true;
            j++;
        }

        count = 0;
        j = 0;
        for (int i = 0; i < rowQuantity; i++) {
            if (board[i][j] == player) {
                count++;
            } else count = 0;
            if (count >= 5) return true;
            j++;
        }

        count = 0;
        j = 0;
        for (int i = 3; i < colQuantity; i++) {
            if (board[j][i] == player) {
                count++;
            } else count = 0;
            if (count >= 5) return true;
            j++;
        }

        count = 0;
        j = 0;
        for (int i = 2; i < colQuantity; i++) {
            if (board[j][i] == player) {
                count++;
            } else count = 0;
            if (count >= 5) return true;
            j++;
        }

        count = 0;
        j = 0;
        for (int i = 1; i < colQuantity; i++) {
            if (board[j][i] == player) {
                count++;
            } else count = 0;
            if (count >= 5) return true;
            j++;
        }

        count = 0;
        j = 0;
        for (int i = 4; i >= 0; i--) {
            if (board[i][j] == player) {
                count++;
            } else count = 0;
            if (count >= 5) return true;
            j++;
        }

        count = 0;
        j = 0;
        for (int i = 5; i >= 0; i--) {
            if (board[i][j] == player) {
                count++;
            } else count = 0;
            if (count >= 5) return true;
            j++;
        }
        if (count >= 5) return true;

        count = 0;
        j = 0;
        for (int i = 6; i >= 0; i--) {
            if (board[i][j] == player) {
                count++;
            } else count = 0;
            if (count >= 5) return true;
            j++;
        }
        if (count >= 5) return true;

        count = 0;
        j = 0;
        for (int i = 7; i >= 0; i--) {
            if (board[i][j] == player) {
                count++;
            } else count = 0;
            if (count >= 5) return true;
            j++;
        }

        count = 0;
        j = 7;
        for (int i = 1; i < rowQuantity; i++) {
            if (board[i][j] == player) {
                count++;
            } else
                count = 0;
            if (count >= 5) return true;
            j--;
        }


        count = 0;
        j = 7;
        for (int i = 2; i < rowQuantity; i++) {
            if (board[i][j] == player) {
                count++;
            } else
                count = 0;
            if (count >= 5) return true;
            j--;
        }

        count = 0;
        j = 7;
        for (int i = 3; i < rowQuantity; i++) {
            if (board[i][j] == player) count++;
            else
                count = 0;
            if (count >= 5) return true;
            j--;
        }

        return false;
    }

    public boolean isGameOver() {
        if (checkWin(1) || checkWin(0)) return true;

        //kiem tra hoa
        int count = 0;
        for (int i = 0; i < rowQuantity; i++) {
            for (int j = 0; j < colQuantity; j++) {
                if (board[i][j] == -1)
                    count++;
            }
        }
        if (count == 0) return true;
        return false;
    }

    //lay cac o con trong tren ban co
    public List<Move> getMoves() {
        List<Move> moves = new ArrayList<>();
        for (int i = 0; i < rowQuantity; i++) {
            for (int j = 0; j < colQuantity; j++) {
                if (board[i][j] == -1) {
                    moves.add(new Move(i, j));
                }
            }
        }
        return moves;
    }

    //ghi nhan nuoc di
    public void makeMove(Move move) {
        board[move.getRowIndex()][move.getColIndex()] = player;
        player = (player + 1) % 2;
    }

    public int evaluate(int player) {
        if (checkWin(player)) {
            return 1;
        }
        if (checkWin((player + 1) % 2)) {
            return -1;
        }
        return 0;
    }

    public int[][] getNewBoard() {
        int[][] newBoard = new int[rowQuantity][colQuantity];
        for (int i = 0; i < rowQuantity; i++) {
            for (int j = 0; j < colQuantity; j++) {
                newBoard[i][j] = board[i][j];
            }
        }
        return newBoard;
    }

    public int getCurrentDepth() {
        int count = 0;
        for (int i = 0; i < rowQuantity; i++) {
            for (int j = 0; j < colQuantity; j++) {
                if (board[i][j] == -1) count++;
            }
        }
        return count;
    }

    class Ai extends AsyncTask<String,Void,ABNegamaxRecord>{

        @Override
        protected ABNegamaxRecord doInBackground(String... strings) {
            int count = getCurrentDepth();
            final int currentDepth = rowQuantity * colQuantity - count;
            ABNegamaxRecord record = bot.abNegamax(BoardGame.this, 1, colQuantity*rowQuantity,currentDepth,Integer.MIN_VALUE,Integer.MAX_VALUE);
            makeMove(record.getMove());
            player=(player+1)%2;
            return record;
        }
    }
}
