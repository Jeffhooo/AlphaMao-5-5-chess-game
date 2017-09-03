package com.jeff.alphamao;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Jeff on 2017/9/3.
 */

public class GreedyRobot extends Robot{

    private String name;
    private int symbol;
    private Recorder currentRecorder;

    public GreedyRobot(String name, int symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    @Override
    public int placeNextPiece(int symbol, int[] chessboard) {

        int theOtherSymbol = symbol == WHITE? BLACK : WHITE;
        int nextPiece = NOT_FOUND;

        if((nextPiece = searchPieceCanWin(chessboard, symbol))
                != NOT_FOUND) return nextPiece;

        else if((nextPiece = searchPieceCanWin(chessboard, theOtherSymbol))
                != NOT_FOUND) return nextPiece;

        else if((nextPiece = tryToPickTheBestPiece(chessboard, symbol))
                != NOT_FOUND) return nextPiece;

        return nextPiece;
    }

    private int searchPieceCanWin(int[] chessboard, int symbol){
        int piece = NOT_FOUND;

        for(int i = 0; i < 25; i++) {
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

    protected int tryToPickTheBestPiece(int[] chessboard, int symbol) {
        int nextPiece = NOT_FOUND;
        int bestScore = -1;
        ArrayList<Integer> bestPieces = new ArrayList<>();

        for(int i = 0; i < 25; i++) {
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
}
