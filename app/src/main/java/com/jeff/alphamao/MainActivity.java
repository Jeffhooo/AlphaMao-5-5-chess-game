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
    private static final int CROSS = 1;
    private static final int CIRCLE = 2;

    private static final int USER_WIN = -1;
    private static final int USER_LOSS = -2;
    private static final int CAN_NOT_JUDGE = -3;
    private static final int DRAW = -4;

    private static final int CELL_0 = R.id.cell_0;
    private static final int CELL_1 = R.id.cell_1;
    private static final int CELL_2 = R.id.cell_2;
    private static final int CELL_3 = R.id.cell_3;
    private static final int CELL_4 = R.id.cell_4;
    private static final int CELL_5 = R.id.cell_5;
    private static final int CELL_6 = R.id.cell_6;
    private static final int CELL_7 = R.id.cell_7;
    private static final int CELL_8 = R.id.cell_8;

    private static final int RESET = R.id.reset;

    private final int DRAW_CROSS = R.drawable.cross;
    private final int DRAW_CIRCLE = R.drawable.circle;
    private final int DRAW_BLANK = R.drawable.blank;

    private static final int[] CELLS = {CELL_0, CELL_1, CELL_2, CELL_3, CELL_4,
                                        CELL_5, CELL_6, CELL_7, CELL_8,};

    private static final int HUMAN_VS_ROBOT = -10;
    private static final int ROBOT_VS_ROBOT = -11;

    private MyImageView[] imageViews;
    private Button resetButton;
    private Switch crossFirstSwitch;
    private int[] chessboard;
    private Robot robot1;
    private Robot robot2;

    private boolean finish;

    private int currentSymbol;
    private int userSymbol;
    private int mode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageViews = new MyImageView[9];
        chessboard = new int[9];
        finish = false;
        currentSymbol = CROSS;
        userSymbol = CROSS;
        mode = HUMAN_VS_ROBOT;

        initChessboard();
        initImageViews(imageViews, CELLS);
        initResetButton();
        initSwitch();

        robot1 = new Robot("Robot1", CIRCLE);
        robot2 = new Robot("Robot2", CROSS);
    }

    private void initChessboard() {
        for(int i = 0; i < 9; i++)
            chessboard[i] = BLANK;
    }

    private void initImageViews(MyImageView[] imageViews, int[] CELLS) {
        for(int i = 0; i < 9; i++) {
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
                                ((userSymbol == CROSS)? DRAW_CROSS : DRAW_CIRCLE);
                        chessboard[clickedView.getIndex()] = userSymbol;
                        if(robot1.judge(chessboard) == USER_WIN) {
                            Toast.makeText(MainActivity.this, "User Win!",
                                    Toast.LENGTH_SHORT).show();
                            finish = true;
                        }
                        changeCurrentSymbol();
                        if(!finish) {
                            robotClick(robot1, robot1.getSymbol());
                            changeCurrentSymbol();
                        }
                    }
                }
            });
        }
    }

    private void clearChessboard(int[] chessboard, MyImageView[] imageViews) {
        for(int i = 0; i < 9; i++) {
            chessboard[i] = BLANK;
            imageViews[i].setImageResource(DRAW_BLANK);
        }
        finish = false;
        currentSymbol = crossFirstSwitch.isChecked()? CROSS : CIRCLE;
        if(currentSymbol == CIRCLE) {
            robotClick(robot1, robot1.getSymbol());
            changeCurrentSymbol();
        }
    }

    private void initResetButton() {
        resetButton = (Button) findViewById(RESET);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearChessboard(chessboard, imageViews);
            }
        });
    }

    private void initSwitch() {
        crossFirstSwitch = (Switch) findViewById(R.id.cross_first);
        crossFirstSwitch.setOnCheckedChangeListener
                (new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged
                    (CompoundButton buttonView, boolean isChecked) {
                currentSymbol = crossFirstSwitch.isChecked()? CROSS : CIRCLE;
            }
        });
        crossFirstSwitch.setChecked(true);
    }

    public void robotClick(Robot robot, int symbol) {
        int DRAW_TYPE = (symbol == CIRCLE)? DRAW_CIRCLE : DRAW_CROSS;
        int nextStep = robot.process(symbol, chessboard);
        if(nextStep >= 0) {
            MyImageView updateImageView =
                    (MyImageView) findViewById
                            (MainActivity.CELLS[nextStep]);
            updateImageView.setImageResource(DRAW_TYPE);
            chessboard[nextStep] = symbol;
        }

        int result = robot.judge(chessboard);
        switch(result) {
            case USER_LOSS:
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

    public int getCurrentSymbol() {
        return currentSymbol;
    }

    public void changeCurrentSymbol() {
        currentSymbol = currentSymbol == CIRCLE? CROSS : CIRCLE;
    }

    public boolean getFinish() {
        return finish;
    }
}
