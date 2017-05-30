package com.jeff.alphamao;

import android.widget.Toast;

import java.util.Random;

/**
 * Created by jeff on 17-5-29.
 */

public class Robot {

    private static final int BLANK = 0;
    private static final int BLACK = 3;
    private static final int WHITE = 4;

    private static final int WIN = -1;
    private static final int LOSS = -2;
    private static final int NOT_SURE = -3;
    private static final int DRAW = -4;
    private static final int NOT_FOUND = -5;

    private String name;
    private int symbol;
    private int chessboardSize;

    public Robot(String name, int symbol, int chessboardSize) {
        this.name = name;
        this.symbol = symbol;
        this.chessboardSize = chessboardSize;
    }

    public int placeNextPiece(int symbol, int[] chessboard) {
        int theOtherSymbol = symbol == WHITE? BLACK : WHITE;
        int nextPiece = NOT_FOUND;

        if((nextPiece = searchPieceCanWin(chessboard, symbol))
                != NOT_FOUND) return nextPiece;

        if((nextPiece = searchPieceCanWin(chessboard, theOtherSymbol))
                != NOT_FOUND) return nextPiece;

        if((nextPiece = tryToPickTheBestPiece(chessboard, symbol))
                != NOT_FOUND) return nextPiece;

        return nextPiece;
    }

    public int judge(int[] chessboard, int symbol) {
        if(isWin(symbol, chessboard)) return WIN;
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
        int bestPiece = NOT_FOUND;
        int bestScore = -1;
        for(int i = 0; i < chessboardSize; i++) {
            if(chessboard[i] == BLANK) {
                int score = calculatePieceScore(chessboard, i, symbol);
                if(score > bestScore) {
                    bestPiece = i;
                    bestScore = score;
                }
            }
        }
        return bestPiece;
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

}
