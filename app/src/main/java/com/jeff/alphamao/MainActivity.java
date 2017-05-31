package com.jeff.alphamao;

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

    private static final int WIN = -1;
    private static final int LOSS = -2;
    private static final int NOT_SURE = -3;
    private static final int DRAW = -4;
    private static final int NOT_FOUND = -5;

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

    private final int DRAW_CROSS  = R.drawable.cross;
    private final int DRAW_CIRCLE = R.drawable.circle;
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
    private Button resetButton;
    private Switch blackFirstSwitch;
    private int[] chessboard;
    private Robot robot1;
    private Robot robot2;

    private boolean finish;

    private int currentSymbol;
    private int userSymbol;
    private int mode;
    private int chessboardSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageViewList = new MyImageView[25];
        chessboard = new int[25];
        finish = false;
        currentSymbol = BLACK;
        userSymbol = BLACK;
        mode = HUMAN_VS_ROBOT;
        chessboardSize = 25;

        initChessboard();
        initImageViews(imageViewList, CELL_LIST);
        initResetButton();
        initSwitch();

        robot1 = new Robot("Robot1", WHITE, chessboardSize);
        robot2 = new Robot("Robot2", BLACK, chessboardSize);
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
                    int index = clickedView.getIndex();
                    if(mode == HUMAN_VS_ROBOT
                            && !finish
                            && currentSymbol == userSymbol
                            && chessboard[index] == BLANK) {
                        clickedView.setImageResource
                                ((userSymbol == BLACK)? DRAW_BLACK : DRAW_WHITE);
                        chessboard[clickedView.getIndex()] = userSymbol;
                        if(robot1.judge(chessboard, userSymbol) == WIN) {
                            Toast.makeText(MainActivity.this, "User Win!",
                                    Toast.LENGTH_SHORT).show();
                            finish = true;
                        }
                        changeCurrentSymbol();
                        if(!finish && currentSymbol == WHITE) {
                            robotClick(robot1, robot1.getSymbol());
                            changeCurrentSymbol();
                        }
                    }
                }
            });
        }
    }

    private void clearChessboard(int[] chessboard, MyImageView[] imageViews) {
        for(int i = 0; i < chessboardSize; i++) {
            chessboard[i] = BLANK;
            imageViews[i].setImageResource(DRAW_BLANK);
        }
        finish = false;
        currentSymbol = blackFirstSwitch.isChecked()? BLACK : WHITE;
        if(currentSymbol == WHITE) {
            robotClick(robot1, robot1.getSymbol());
            changeCurrentSymbol();
        }
    }

    private void initResetButton() {
        resetButton = (Button) findViewById(RESET);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearChessboard(chessboard, imageViewList);
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
    }

    private void robotClick(Robot robot, int symbol) {
        int DRAW_TYPE = (symbol == WHITE)? DRAW_WHITE : DRAW_BLACK;
        int nextStep = robot.placeNextPiece(symbol, chessboard);
        if(nextStep != NOT_FOUND) {
            MyImageView updateImageView =
                    (MyImageView) findViewById
                            (MainActivity.CELL_LIST[nextStep]);
            updateImageView.setImageResource(DRAW_TYPE);
            chessboard[nextStep] = symbol;
        }

        int result = robot.judge(chessboard, robot.getSymbol());
        switch(result) {
            case WIN:
                Toast.makeText(MainActivity.this, robot.getName() + " Win!",
                        Toast.LENGTH_SHORT).show();
                finish = true;
                break;

            case DRAW:
                Toast.makeText(MainActivity.this, "Draw!",
                        Toast.LENGTH_SHORT).show();
                finish = true;
                break;

            default:break;
        }

    }

    private void changeCurrentSymbol() {
        currentSymbol = currentSymbol == WHITE? BLACK : WHITE;
    }

}
