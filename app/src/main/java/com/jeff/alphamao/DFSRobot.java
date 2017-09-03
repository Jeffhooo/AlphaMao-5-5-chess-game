package com.jeff.alphamao;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Jeff on 2017/9/3.
 */

public class DFSRobot extends GreedyRobot{

    private String name;
    private int symbol;
    private Recorder currentRecorder;
    private int depth;

    public DFSRobot(String name, int symbol, int depth) {
        super(name, symbol);
        this.name = name;
        this.symbol = symbol;
        this.depth = depth;
    }

    @Override
    public int placeNextPiece(int symbol, int[] chessboard) {
        return placeNextPiece(symbol, chessboard, getDepth());
    }

    private int placeNextPiece(int symbol, int[] chessboard, int depth) {

        int theOtherSymbol = symbol == WHITE? BLACK : WHITE;
        int nextPiece = NOT_FOUND;

        if((nextPiece = searchPieceCanWin(chessboard, symbol))
                != NOT_FOUND) return nextPiece;

        else if((nextPiece = searchPieceCanWin(chessboard, theOtherSymbol))
                != NOT_FOUND) return nextPiece;

        for (int i = 0; i < 25; i++) {
            if (chessboard[i] == BLANK) {
                chessboard[i] = symbol;
                int[] copyChessboard = chessboard.clone();
                if (canWin(symbol, copyChessboard, depth))
                    return i;
                chessboard[i] = BLANK;
            }
        }

        if((nextPiece = tryToPickTheBestPiece(chessboard, symbol))
                != NOT_FOUND) return nextPiece;

        return NOT_FOUND;
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

    private boolean canWin(int symbol, int[] chessboard, int depth) {
        --depth;
        if(depth < 0) return false;

        int theOtherSymbol = symbol == WHITE? BLACK : WHITE;
        if(isWin(symbol, chessboard)) return true;

        while(true) {
            int nextStep = placeNextPiece(theOtherSymbol, chessboard, depth);
            if(nextStep == NOT_FOUND) return false;
            chessboard[nextStep] = theOtherSymbol;
            if(isWin(theOtherSymbol, chessboard)) return false;

            nextStep = placeNextPiece(symbol, chessboard, depth);
            if(nextStep == NOT_FOUND) return false;
            chessboard[nextStep] = symbol;
            if(isWin(symbol, chessboard)) return true;
        }
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

    public int getDepth() { return this.depth; }
}
