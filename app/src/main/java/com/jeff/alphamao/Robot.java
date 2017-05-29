package com.jeff.alphamao;

/**
 * Created by jeff on 17-5-29.
 */

public class Robot {

    private static final int BLANK = 0;
    private static final int CROSS = 1;
    private static final int CIRCLE = 2;

    private static final int USER_WIN = -1;
    private static final int USER_LOSS = -2;
    private static final int CAN_NOT_JUDGE = -3;
    private static final int DRAW = -4;

    private String name;
    private int symbol;

    public Robot(String name, int symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public int process(int symbol, int[] chessboard) {
        int theOtherSymbol = symbol == CIRCLE? CROSS : CIRCLE;

        if(chessboard[4] == BLANK) return 4;

        for(int i = 0; i < 9; i++) {
            if(chessboard[i] == BLANK) {
                chessboard[i] = symbol;
                int[] copyChessboard = chessboard.clone();
                if(isWin(symbol, copyChessboard))
                    return i;
                chessboard[i] = BLANK;
            }
        }

        for(int i = 0; i < 9; i++) {
            if(chessboard[i] == BLANK) {
                chessboard[i] = theOtherSymbol;
                int[] copyChessboard = chessboard.clone();
                if(isWin(theOtherSymbol, copyChessboard))
                    return i;
                chessboard[i] = BLANK;
            }
        }

        for(int i = 0; i < 9; i++) {
            if(chessboard[i] == BLANK) {
                chessboard[i] = symbol;
                int[] copyChessboard = chessboard.clone();
                if(canWin(symbol, copyChessboard))
                    return i;
                chessboard[i] = BLANK;
            }
        }

        for(int i = 0; i < 9; i++) {
            if(chessboard[i] == BLANK)
                return i;
        }
        return DRAW;
    }

    private boolean canWin(int symbol, int[] chessboard) {
        int theOtherSymbol = symbol == CIRCLE? CROSS : CIRCLE;

        if(isWin(symbol, chessboard)) return true;

        while(true) {
            int nextStep = process(theOtherSymbol, chessboard);
            if(nextStep == DRAW) return false;
            chessboard[nextStep] = theOtherSymbol;
            if(isWin(theOtherSymbol, chessboard)) return false;

            nextStep = process(symbol, chessboard);
            if(nextStep == DRAW) return false;
            chessboard[nextStep] = symbol;
            if(isWin(symbol, chessboard)) return true;
        }
    }

    public int judge(int[] chessboard) {
        if(isWin(CROSS, chessboard)) return USER_WIN;
        if(isWin(CIRCLE, chessboard)) return USER_LOSS;

        for(int i = 0; i < 9; i++) {
            if(chessboard[i] == BLANK) break;
            if(i == 8) return DRAW;
        }

        return CAN_NOT_JUDGE;
    }

    private boolean isWin(int symbol, int[] chessboard) {

        for(int i = 0; i < 3; i++) {
            int row = i*3;
            if(chessboard[row] == symbol
                    && chessboard[row+1] == symbol
                    && chessboard[row+2] == symbol)
                return true;
        }

        for(int i = 0; i < 3; i++) {
            if(chessboard[i] == symbol
                    && chessboard[i+3] == symbol
                    && chessboard[i+6] == symbol)
                return true;
        }

        if(chessboard[0] == symbol
                && chessboard[4] == symbol
                && chessboard[8] == symbol)
            return true;

        if(chessboard[2] == symbol
                && chessboard[4] == symbol
                && chessboard[6] == symbol)
            return true;

        return false;
    }

    public String getName() {
        return name;
    }

    public int getSymbol() {
        return symbol;
    }
}
