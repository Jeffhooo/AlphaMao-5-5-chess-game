package com.jeff.alphamao;

import java.util.Random;

/**
 * Created by jeff on 17-5-29.
 */

public class Robot {

    private static final int BLANK = 0;
    private static final int CROSS = 1;
    private static final int CIRCLE = 2;
    private static final int BLACK = 3;
    private static final int WHITE = 4;

    private static final int USER_WIN = -1;
    private static final int USER_LOSS = -2;
    private static final int CAN_NOT_JUDGE = -3;
    private static final int DRAW = -4;

    private String name;
    private int symbol;
    private int depth;

    public Robot(String name, int symbol, int depth) {
        this.name = name;
        this.symbol = symbol;
        this.depth = depth;
    }

    public int DFS(int symbol, int[] chessboard, int depth) {
        int theOtherSymbol = symbol == WHITE? BLACK : WHITE;
        int blankCounter = 0;

        for(int i = 0; i < 25; i++) {
            if(chessboard[i] == BLANK)
                ++blankCounter;
        }

        for(int i = 0; i < 25; i++) {
            if(chessboard[i] == BLANK) {
                chessboard[i] = symbol;
                int[] copyChessboard = chessboard.clone();
                if(isWin(symbol, copyChessboard))
                    return i;
                chessboard[i] = BLANK;
            }
        }

        for(int i = 0; i < 25; i++) {
            if(chessboard[i] == BLANK) {
                chessboard[i] = theOtherSymbol;
                int[] copyChessboard = chessboard.clone();
                if(isWin(theOtherSymbol, copyChessboard))
                    return i;
                chessboard[i] = BLANK;
            }
        }

        if(blankCounter < 9) {
            for (int i = 0; i < 25; i++) {
                if (chessboard[i] == BLANK) {
                    chessboard[i] = symbol;
                    int[] copyChessboard = chessboard.clone();
                    if (canWin(symbol, copyChessboard, depth))
                        return i;
                    chessboard[i] = BLANK;
                }
            }
        }

        Random random = new Random();

        for(int i = 0; i < 50; i++) {
            int temp = random.nextInt(24);
            if(chessboard[temp] == BLANK)
                return temp;
        }

        for(int i = 0; i < 25; i++) {
            if(chessboard[i] == BLANK)
                return i;
        }

        return DRAW;
    }



    private boolean canWin(int symbol, int[] chessboard, int depth) {
        --depth;
        if(depth < 0) return false;

        int theOtherSymbol = symbol == WHITE? BLACK : WHITE;
        if(isWin(symbol, chessboard)) return true;

        while(true) {
            int nextStep = DFS(theOtherSymbol, chessboard, depth);
            if(nextStep == DRAW) return false;
            chessboard[nextStep] = theOtherSymbol;
            if(isWin(theOtherSymbol, chessboard)) return false;

            nextStep = DFS(symbol, chessboard, depth);
            if(nextStep == DRAW) return false;
            chessboard[nextStep] = symbol;
            if(isWin(symbol, chessboard)) return true;
        }
    }

    public int judge(int[] chessboard) {
        if(isWin(BLACK, chessboard)) return USER_WIN;
        if(isWin(WHITE, chessboard)) return USER_LOSS;

        for(int i = 0; i < 25; i++) {
            if(chessboard[i] == BLANK) break;
            if(i == 24) return DRAW;
        }

        return CAN_NOT_JUDGE;
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

    public String getName() {
        return name;
    }

    public int getSymbol() {
        return symbol;
    }

    public int getDepth() { return depth;}
}
