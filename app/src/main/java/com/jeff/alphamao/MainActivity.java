package com.jeff.alphamao;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

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

    private static final int CELL_0 = R.id.cell_0;
    private static final int CELL_1 = R.id.cell_1;
    private static final int CELL_2 = R.id.cell_2;
    private static final int CELL_3 = R.id.cell_3;
    private static final int CELL_4 = R.id.cell_4;
    private static final int CELL_5 = R.id.cell_5;
    private static final int CELL_6 = R.id.cell_6;
    private static final int CELL_7 = R.id.cell_7;
    private static final int CELL_8 = R.id.cell_8;
    private static final int CELL_9 = R.id.cell_9;
    private static final int CELL_10 = R.id.cell_10;
    private static final int CELL_11 = R.id.cell_11;
    private static final int CELL_12 = R.id.cell_12;
    private static final int CELL_13 = R.id.cell_13;
    private static final int CELL_14 = R.id.cell_14;
    private static final int CELL_15 = R.id.cell_15;
    private static final int CELL_16 = R.id.cell_16;
    private static final int CELL_17 = R.id.cell_17;
    private static final int CELL_18 = R.id.cell_18;
    private static final int CELL_19 = R.id.cell_19;
    private static final int CELL_20 = R.id.cell_20;
    private static final int CELL_21 = R.id.cell_21;
    private static final int CELL_22 = R.id.cell_22;
    private static final int CELL_23 = R.id.cell_23;
    private static final int CELL_24 = R.id.cell_24;

    private static final int RESET = R.id.reset;

    private static final int UPDATE_CHESSBOARD = 100;
    private static final int SHOW_RESULT = 101;

    private final int DRAW_BLANK  = R.drawable.blank;
    private final int DRAW_BLACK  = R.drawable.black;
    private final int DRAW_WHITE  = R.drawable.white;


    private static final int[] CELL_LIST
            = {CELL_0, CELL_1, CELL_2, CELL_3, CELL_4,
               CELL_5, CELL_6, CELL_7, CELL_8, CELL_9,
               CELL_10, CELL_11, CELL_12, CELL_13, CELL_14,
               CELL_15, CELL_16, CELL_17, CELL_18, CELL_19,
               CELL_20, CELL_21, CELL_22, CELL_23, CELL_24};

    private static final int HUMAN_VS_ROBOT = -10;
    private static final int ROBOT_VS_ROBOT = -11;

    private MyImageView[] imageViewList;
    private Button startButton;
    private Button resetButton;
    private Switch blackFirstSwitch;
    private Switch modeSwitch;
    private int[] chessboard;
    private Robot robot1;
    private Robot robot2;
    private Thread robot1Thread;
    private Thread robot2Thread;

    private boolean finish;

    private int currentSymbol;
    private int userSymbol;
    private int mode;
    private int chessboardSize;
    private StringBuilder recorder;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_CHESSBOARD:
                    int pieceIndex = msg.arg1;
                    int symbol = msg.arg2;
                    MyImageView clickedView = imageViewList[pieceIndex];
                    clickedView.setImageResource
                            ((symbol == BLACK)? DRAW_BLACK : DRAW_WHITE);
                    break;

                case SHOW_RESULT:
                    int result = msg.arg1;
                    switch (result) {
                        case BLACK_WIN:
                            Toast.makeText(MainActivity.this, "Black Win!",
                                    Toast.LENGTH_SHORT).show();
                            break;

                        case WHITE_WIN:
                            Toast.makeText(MainActivity.this, "White Win!",
                                    Toast.LENGTH_SHORT).show();
                            break;

                        case DRAW:
                            Toast.makeText(MainActivity.this, "Draw!",
                                    Toast.LENGTH_SHORT).show();
                            break;

                        case NOT_SURE:
                            break;

                        default:
                            break;
                    }
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageViewList = new MyImageView[25];
        chessboard = new int[25];
        finish = true;
        currentSymbol = BLACK;
        userSymbol = BLACK;
        mode = HUMAN_VS_ROBOT;
        chessboardSize = 25;
        recorder = new StringBuilder();

        initChessboard();
        initImageViews(imageViewList, CELL_LIST);
        initButton();
        initSwitch();

        robot1 = new Robot("Robot1", WHITE, chessboardSize, mode);
//        robot1Thread = getRobotThread(robot1);

        robot2 = new Robot("Robot2", BLACK, chessboardSize, mode);
//        robot2Thread = getRobotThread(robot2);

    }

    private void initChessboard() {
        for(int i = 0; i < chessboardSize; i++)
            chessboard[i] = BLANK;
    }

    private void initImageViews(MyImageView[] imageViews, int[] CELLS) {
        for(int i = 0; i < 25; i++) {
            imageViews[i] = (MyImageView) findViewById(CELLS[i]);
            imageViews[i].setIndex(i);
            imageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyImageView clickedView = (MyImageView)v;
                    final int index = clickedView.getIndex();
                    if(mode == HUMAN_VS_ROBOT
                            && !finish
                            && currentSymbol == userSymbol
                            && chessboard[index] == BLANK) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                sendMessage(UPDATE_CHESSBOARD, index, userSymbol);
                                changeChessBoard(index, userSymbol);
                                changeCurrentSymbol();
                            }
                        }).start();
                    }
                }
            });
        }
    }

    private void initButton() {
        resetButton = (Button) findViewById(RESET);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < chessboardSize; i++) {
                    chessboard[i] = BLANK;
                    imageViewList[i].setImageResource(DRAW_BLANK);
                }
                finish = true;
            }
        });

        startButton = (Button) findViewById(R.id.start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finish) {
                    currentSymbol = blackFirstSwitch.isChecked()? BLACK : WHITE;
                    mode = modeSwitch.isChecked()? HUMAN_VS_ROBOT : ROBOT_VS_ROBOT;
                    finish = false;
                    switch (mode) {
                        case HUMAN_VS_ROBOT:
                            robot1.setMode(HUMAN_VS_ROBOT);
                            robot1Thread = getRobotThread(robot1);
                            robot1Thread.start();
                            break;

                        case ROBOT_VS_ROBOT:
                            robot1.setMode(ROBOT_VS_ROBOT);
                            robot2.setMode(ROBOT_VS_ROBOT);
                            robot1Thread = getRobotThread(robot1);
                            robot2Thread = getRobotThread(robot2);
                            robot1Thread.start();
                            robot2Thread.start();
                            break;

                        default:
                            break;
                    }
                }
            }
        });
    }

    private void initSwitch() {
        blackFirstSwitch = (Switch) findViewById(R.id.black_first);
        blackFirstSwitch.setOnCheckedChangeListener
                (new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged
                    (CompoundButton buttonView, boolean isChecked) {
                currentSymbol = blackFirstSwitch.isChecked()? BLACK : WHITE;
            }
        });
        blackFirstSwitch.setChecked(true);

        modeSwitch = (Switch) findViewById(R.id.mode);
        modeSwitch.setOnCheckedChangeListener
                (new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged
                    (CompoundButton buttonView, boolean isChecked) {
                mode = modeSwitch.isChecked()? HUMAN_VS_ROBOT : ROBOT_VS_ROBOT;
            }
        });


    }

    private void recordPieceIndex(int pieceIndex){
        recorder.append(" " + pieceIndex);
    }

    private void recordResult(boolean isFirstWin){
        String res = isFirstWin? "Win" : "Loss";
        recorder.append(" " + res);
    }

    private synchronized void changeCurrentSymbol() {
        currentSymbol = currentSymbol == WHITE? BLACK : WHITE;
    }

    private Thread getRobotThread(final Robot robot) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while(!finish) {
                    int robotSymbol = robot.getSymbol();
                    if(currentSymbol == robotSymbol) {
                        int result = robot.judge
                                (chessboard, robotSymbol == BLACK? WHITE : BLACK);
                        if(result != NOT_SURE) {
                            finish = true;
                            sendMessage(SHOW_RESULT, result, 0);
                        }

                        if(!finish) {
                            int nextStep = robot.placeNextPiece(robotSymbol, chessboard);
                            if (nextStep != NOT_FOUND) {
                                sendMessage(UPDATE_CHESSBOARD, nextStep, robotSymbol);
                                changeChessBoard(nextStep, robotSymbol);
                            }

                            result = robot.judge(chessboard, robotSymbol);
                            if(result != NOT_SURE) {
                                finish = true;
                                sendMessage(SHOW_RESULT, result, 0);
                            }
                            changeCurrentSymbol();
                        }
                    }
                }
            }
        });
    }

    private void sendMessage(int what, int arg1, int arg2) {
        Message msg = new Message();
        switch (what) {
            case UPDATE_CHESSBOARD:
                msg.what = UPDATE_CHESSBOARD;
                msg.arg1 = arg1;
                msg.arg2 = arg2;
                mHandler.sendMessage(msg);
                break;

            case SHOW_RESULT:
                msg.what = SHOW_RESULT;
                msg.arg1 = arg1;
                mHandler.sendMessage(msg);
                break;

            default:
                break;
        }
    }

    private synchronized void changeChessBoard(int index, int symbol) {
        chessboard[index] = symbol;
    }
}
