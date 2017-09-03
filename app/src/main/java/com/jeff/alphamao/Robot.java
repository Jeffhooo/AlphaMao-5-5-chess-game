package com.jeff.alphamao;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by jeff on 17-5-29.
 */

public abstract class Robot {

    public static final int BLANK = 0;
    public static final int BLACK = 3;
    public static final int WHITE = 4;

    public static final int BLACK_WIN = -1;
    public static final int WHITE_WIN = -2;
    public static final int NOT_SURE = -3;
    public static final int DRAW = -4;
    public static final int NOT_FOUND = -5;

    public static final int RECORD_FIRST_WIN = -6;
    public static final int RECORD_FIRST_LOSS = -7;
    public static final int RECORD_NOT_SURE = -8;

    public static final int HUMAN_VS_ROBOT = -10;
    public static final int ROBOT_VS_ROBOT = -11;

    abstract public int placeNextPiece(int symbol, int[] chessboard);

    abstract int getSymbol();

    public static int judge(int[] chessboard, int symbol) {
        if(isWin(symbol, chessboard)) {
            return symbol == BLACK? BLACK_WIN : WHITE_WIN;
        }

        for(int i = 0; i < 25; i++) {
            if(chessboard[i] == BLANK) break;
            if(i == 24) return DRAW;
        }

        return NOT_SURE;
    }

    public static boolean isWin(int symbol, int[] chessboard) {

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
}
