package com.jeff.alphamao;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import static android.os.SystemClock.sleep;


/**
 * Created by jeff on 17-5-29.
 */

public class Robot {

    private static final int BLANK = 0;
    private static final int BLACK = 3;
    private static final int WHITE = 4;

    private static final int BLACK_WIN = -1;
    private static final int WHITE_WIN = -2;
    private static final int NOT_SURE = -3;
    private static final int DRAW = -4;
    private static final int NOT_FOUND = -5;

    private static final int RECORD_FIRST_WIN = -6;
    private static final int RECORD_FIRST_LOSS = -7;
    private static final int RECORD_NOT_SURE = -8;

    private static final int HUMAN_VS_ROBOT = -10;
    private static final int ROBOT_VS_ROBOT = -11;

    private String name;
    private int symbol;
    private int chessboardSize;
    private LinkedList<Recorder> recorders;
    private Recorder currentRecorder;
    private boolean isBlackFirst;
    private int step;
    private int mode;


    public Robot(String name, int symbol, int chessboardSize, int mode) {
        this.name = name;
        this.symbol = symbol;
        this.chessboardSize = chessboardSize;
        this.recorders = new LinkedList<>();
        this.step = 0;
        this.mode = mode;
    }

    public int placeNextPiece(int symbol, int[] chessboard) {
        if(mode == ROBOT_VS_ROBOT) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
            }
        }

        int theOtherSymbol = symbol == WHITE? BLACK : WHITE;
        int nextPiece = NOT_FOUND;
        ++step;

        if((nextPiece = searchPieceCanWin(chessboard, symbol))
                != NOT_FOUND) {
            return nextPiece;
        }

        else if((nextPiece = searchPieceCanWin(chessboard, theOtherSymbol))
                != NOT_FOUND) return nextPiece;

        else if((nextPiece = tryToPickTheBestPiece(chessboard, symbol))
                != NOT_FOUND) return nextPiece;


        return nextPiece;
    }

    public int judge(int[] chessboard, int symbol) {
        if(isWin(symbol, chessboard)) {
            return symbol == BLACK? BLACK_WIN : WHITE_WIN;
        }
        for(int i = 0; i < chessboardSize; i++) {
            if(chessboard[i] == BLANK) break;
            if(i == chessboardSize-1) return DRAW;
        }
        return NOT_SURE;
    }

    private boolean isWin(int symbol, int[] chessboard) {

        for(int i = 0; i < 5; i++) {
            int row = i*5;
            if(chessboard[row] == symbol
                    && chessboard[row+1] == symbol
                    && chessboard[row+2] == symbol
                    && chessboard[row+3] == symbol
                    && chessboard[row+4] == symbol)
                return true;
        }

        for(int i = 0; i < 5; i++) {
            if(chessboard[i] == symbol
                    && chessboard[i+5] == symbol
                    && chessboard[i+10] == symbol
                    && chessboard[i+15] == symbol
                    && chessboard[i+20] == symbol)
                return true;
        }

        if(chessboard[0] == symbol
                && chessboard[6] == symbol
                && chessboard[12] == symbol
                && chessboard[18] == symbol
                && chessboard[24] == symbol)
            return true;

        if(chessboard[4] == symbol
                && chessboard[8] == symbol
                && chessboard[12] == symbol
                && chessboard[16] == symbol
                && chessboard[20] == symbol)
            return true;

        return false;
    }

    private int searchPieceCanWin(int[] chessboard, int symbol){
        int piece = NOT_FOUND;
        for(int i = 0; i < chessboardSize; i++) {
            if(chessboard[i] == BLANK) {
                chessboard[i] = symbol;
                int[] copyChessboard = chessboard.clone();
                if(isWin(symbol, copyChessboard)){
                    piece = i;
                    break;
                }
                chessboard[i] = BLANK;
            }
        }
        return piece;
    }

    private int tryToPickTheBestPiece(int[] chessboard, int symbol) {
        int nextPiece = NOT_FOUND;
        int bestScore = -1;
        ArrayList<Integer> bestPieces = new ArrayList<>();
        for(int i = 0; i < chessboardSize; i++) {
            if(chessboard[i] == BLANK) {
                int score = calculatePieceScore(chessboard, i, symbol);
                if(score == bestScore) {
                    bestPieces.add(i);
                } else if(score > bestScore) {
                    bestPieces.clear();
                    bestPieces.add(i);
                    bestScore = score;
                }
            }
        }

        int size = bestPieces.size();
        if(size > 0) {
            Random random = new Random();
            nextPiece = bestPieces.get(random.nextInt(size));
        }

        return nextPiece;
    }

    private int calculatePieceScore(int[] chessboard, int pieceIndex, int symbol) {
        int theOtherSymbol = (symbol == WHITE)? BLACK : WHITE;
        int score = 0;
        int rowFirstIndex = (pieceIndex/5)*5;
        int col = pieceIndex-rowFirstIndex;

        int symbolCounter = 0;
        for(int i = 0; i < 5; i++) {
            int temp = chessboard[rowFirstIndex+i];
            if(temp == theOtherSymbol) break;
            if(temp == symbol) ++symbolCounter;
            if(i == 4) {
                score += symbolCounter+1;
            }
        }

        symbolCounter = 0;
        for(int i = 0; i < 5; i++) {
            int temp = chessboard[col+i*5];
            if(temp == theOtherSymbol) break;
            if(temp == symbol) ++symbolCounter;
            if(i == 4) {
                score += symbolCounter+1;
            }
        }

        if(pieceIndex == 12) {
            symbolCounter = 0;
            for(int i = 0; i < 5; i++) {
                int temp = chessboard[i*6];
                if(temp == theOtherSymbol) break;
                if(temp == symbol) ++symbolCounter;
                if(i == 4) {
                    score += symbolCounter+1;
                }
            }

            symbolCounter = 0;
            for(int i = 0; i < 5; i++) {
                int temp = chessboard[4+i*4];
                if(temp == theOtherSymbol) break;
                if(temp == symbol) ++symbolCounter;
                if(i == 4) {
                    score += symbolCounter+1;
                }
            }
        }
        return score;
    }

    public String getName() {
        return name;
    }

    public int getSymbol() {
        return symbol;
    }

    public void record(int pieceIndex) {
        Recorder temp = currentRecorder.find(pieceIndex);
        if(temp == null) {
            temp = new Recorder(pieceIndex);
            currentRecorder.put(temp);
        }
        currentRecorder = temp;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
