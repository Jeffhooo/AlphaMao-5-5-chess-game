package com.jeff.alphamao;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by jeff on 17-6-4.
 */

public class Recorder {
    private static final int RECORD_FIRST_WIN = -6;
    private static final int RECORD_FIRST_LOSS = -7;
    private static final int RECORD_NOT_SURE = -8;

    private LinkedList<Recorder> nextPiece;
    int pieceIndex;
    int result;

    public Recorder(int pieceIndex) {
        this.pieceIndex = pieceIndex;
        nextPiece = new LinkedList<Recorder>();
        result = RECORD_NOT_SURE;
    }

    public int getPieceIndex() { return pieceIndex; }

    public Recorder find(int pieceIndex) {
        int size = nextPiece.size();
        for(int i = 0; i < size; i++) {
            if(nextPiece.get(i).getPieceIndex() == pieceIndex) {
                return nextPiece.get(i);
            }
        }
        return null;
    }

    public void put(Recorder recorder) {
        nextPiece.add(recorder);
    }

    public int getResult() { return result; }

    public void setResult(int result) {
        this.result = result;
    }
}
