package com.example.indianludobattle.Activity.Ludo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.example.indianludobattle.Activity.Ludo.Characters.Ludo.Piece.Piece;
import com.example.indianludobattle.Activity.Ludo.Characters.Ludo.Player.Player2;
import com.example.indianludobattle.Activity.Ludo.Characters.Ludo.Player.Player3;
import com.example.indianludobattle.Activity.Ludo.Characters.PathPostion;
import com.example.indianludobattle.Activity.Ludo.Characters.Position;
import com.example.indianludobattle.Activity.MainActivity;
import com.example.indianludobattle.Activity.Win.WinActivity;
import com.example.indianludobattle.MyUtil.MyPreferences;
import com.example.indianludobattle.MyUtil.Reminder;
import com.example.indianludobattle.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class NewLudoActivity extends AppCompatActivity {

    public static PathPostion[] romPath;
    MyPreferences myPreferences;
    String UserId;
    private LudoGameView ludoGameView;
    DatabaseReference reference;
    String playerOne="1234567890",playerTwo="9876543210";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        myPreferences = new MyPreferences(this);
        UserId = myPreferences.getUserId();
        GetMultiPlayerScore();

        SetMultiPlayerScore(playerOne,0,0,0,"RED",playerOne);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        PieceChoice choice = new PieceChoice(2); // Green
        PieceChoice choice1 = new PieceChoice(4); // Yellow
        PieceChoice choice2 = new PieceChoice(3); // Blue
        PieceChoice choice3 = new PieceChoice(1);// Red
        PieceChoice[] choices = new PieceChoice[2];
        choices[0] = choice3;
        choices[1] = choice2;

        ludoGameView = new LudoGameView(this, size.x, size.y, choices);
        setContentView(ludoGameView);
    }


    @Override
    protected void onResume() {
        super.onResume();
        ludoGameView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ludoGameView.onPause();
    }

    enum Turn {
        RED,
        GREEN,
        BLUE,
        YELLOW;
    }

    class PieceChoice {
        public int PlayerId;

        public PieceChoice(int id) {
            this.PlayerId = id;
        }
    }

    private class LudoGameView extends SurfaceView implements Runnable {

        Turn t;
        private Context context;
        private Thread gameThread;
        private int ScreenWidth, ScreenHeight;
        private SurfaceHolder holder;
        private Canvas canvas;
        private Paint paint;
        private int totalPlayers = 0,diceMoveCountRed=0,diceMoveCountBlue=0,diceMoveCountGreen=0,diceMoveCountYellow=0,PlayerScoreRed=0,PlayerScoreBlue=0,PlayerScoreYellow=0,PlayerScoreGreen=0;
        private boolean initRed, initBlue, initGreen, initYellow, gameFinished = false,isMoving = false,toHome = false;
        private boolean isPlaying;
        private Bitmap bmpMap, bmpBg, bmpStar, bmpStarWhite, bmpRedPiece,bmpDice, bmpGreenPiece, bmpYellowPiece, bmpBluePiece;
        private Position[] redPath, bluePath, yellowPath, greenPath;
        private Player3 playerRed, playerBlue, playerGreen, playerYellow,playerIndicator;
        private Position[] red, blue, yellow, green;
        private String redPieceColor = "#FD7077", bluePieceColor = "#A4D9F8", greenPieceColor = "#A1FDCB", yellowPieceColor = "#FBEE9C";
        int Player1DiceArray[]= {6,6,6,6,6,6,6,6,1,4,1,5,3,5,1,2,3,6,3,4,6,4,2,3,1,5,6,2};
        int Player2DiceArray[]= {6,6,6,6,6,6,6,6,3,6,3,4,6,1,4,1,5,3,5,1,2,4,2,3,1,5,6,2};
        private int CircleSize;
        String PlayerIdServer="";

        int interseptAddtionalRed=0,interseptAddtionalBlue=0;

        public LudoGameView(Context context, int screenX, int screenY, PieceChoice[] choices) {
            super(context);
            this.context = context;
            this.ScreenWidth = screenX;
            this.ScreenHeight = screenY;
            holder = getHolder();
            paint = new Paint();
            bmpStar = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_star_icon);
            bmpStarWhite = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_star_icon_white);
            bmpDice = Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_8888);
            romPath = new PathPostion[52];
            totalPlayers = choices.length;


            for (int i = 0; i < choices.length; i++) {
                PieceChoice p = choices[i];
                Log.d("LudoActivity", "choice : " + p.PlayerId);

                if (p.PlayerId == 1)
                    initRed = true;
                else if (p.PlayerId == 2)
                    initGreen = true;
                else if (p.PlayerId == 3)
                    initBlue = true;
                else if (p.PlayerId == 4)
                    initYellow = true;

                if (i == 0) {
                    if (p.PlayerId == 1)
                        t = Turn.RED;
                    else if (p.PlayerId == 2)
                        t = Turn.GREEN;
                    else if (p.PlayerId == 3)
                        t = Turn.BLUE;
                    else if (p.PlayerId == 4)
                        t = Turn.YELLOW;
                }
            }
        }

        @Override
        public void run() {
            while (isPlaying) {
                update();
                Draw();
                control();
            }
        }

        void control() {
            try {
                gameThread.sleep(17);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        void Draw() {
            if (holder.getSurface().isValid()) {
                canvas = holder.lockCanvas();
                canvas.drawBitmap(bmpMap, 0, 0, null);
                if (t == Turn.RED) {
                    canvas.drawBitmap(bmpDice, 300, 50, null);
                }
                else if (t == Turn.GREEN) {
                    canvas.drawBitmap(bmpDice, ScreenWidth - 400, 60, null);
                }
                else if (t == Turn.BLUE)
                    canvas.drawBitmap(bmpDice, ScreenWidth - 400, ScreenHeight - 180, null);
                else
                    canvas.drawBitmap(bmpDice, 100, ScreenHeight - 180, null);

                Log.v("kjhksdh",""+UserId+","+PlayerIdServer);
                if(UserId.equalsIgnoreCase(PlayerIdServer)) {
                    Log.v("kjhksdh","Entry");
                    setBlinkingAnimation(""+t);
                }

                DrawPlayerPiece();
                DrawPlayerNextDice();
                DrawPlayerScore(PlayerScoreRed+interseptAddtionalRed,PlayerScoreGreen,PlayerScoreBlue+interseptAddtionalBlue,PlayerScoreYellow);
//                DrawArrows();
                holder.unlockCanvasAndPost(canvas);
            }
        }
        private static final int FADE_MILLISECONDS = 1300;
        private static final int FADE_STEP =120;
        private static final int ALPHA_STEP = 255 / (FADE_MILLISECONDS / FADE_STEP);
        private Paint alphaPaint = new Paint();
        private int currentAlpha = 255,currentAlpha1 = 255;

        private void setBlinkingAnimation(String blink){
            float r = 40.0f;
            int blockWidthHight = (int) (ScreenHeight * (r / 100f));
            int left = (ScreenWidth / 2) - (ScreenHeight / 2);
            int redBorderWidth = (int) ((float) blockWidthHight * (20f / 100f));
            int topY = ScreenHeight - blockWidthHight;
            int topRightX = ScreenHeight - blockWidthHight + left;
            if(blink.equalsIgnoreCase("RED")) {
                canvas.drawRect(left, 2, blockWidthHight - 2 + left, blockWidthHight - 2, alphaPaint);
                if (currentAlpha > 0) {
                    canvas.drawRect(left, 2, blockWidthHight - 2 + left, blockWidthHight - 2, alphaPaint);
                    alphaPaint.setAlpha(currentAlpha);
                    currentAlpha -= ALPHA_STEP;
                    postInvalidateDelayed(FADE_STEP, blockWidthHight, blockWidthHight, blockWidthHight, blockWidthHight);
                } else {
                    currentAlpha = 255;
                    alphaPaint.setAlpha(currentAlpha);
                }

                paint.setColor(Color.WHITE);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(40);
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                canvas.drawText("Player 1", left + redBorderWidth + 130, 50, paint);
            }else if(blink.equalsIgnoreCase("BLUE")) {
                canvas.drawRect(topRightX + 2, topY - 2, ScreenHeight + left - 2, ScreenHeight - 2, alphaPaint);
                if (currentAlpha1 > 0) {
                    canvas.drawRect(topRightX + 2, topY - 2, ScreenHeight + left - 2, ScreenHeight - 2, alphaPaint);
                    alphaPaint.setAlpha(currentAlpha1);
                    currentAlpha1 -= ALPHA_STEP;
                    postInvalidateDelayed(FADE_STEP, blockWidthHight, blockWidthHight, blockWidthHight, blockWidthHight);
                } else {
                    currentAlpha1 = 255;
                    alphaPaint.setAlpha(currentAlpha1);
                }
            }
            int whiteAreaWidth = (blockWidthHight - redBorderWidth) - (redBorderWidth + 4);
            int scoreCircle = (int) ((float) whiteAreaWidth * (200f / 200f));
            int leftX = (left + redBorderWidth + (scoreCircle / 2));
            int leftY = (redBorderWidth + (scoreCircle / 2) + 10);
            paint.setColor(Color.WHITE);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(40);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("Player 4", topRightX + 210, topY + 50, paint);
            scoreCircle = (int) ((float) whiteAreaWidth * (200f / 200f));
            leftX = (topRightX + redBorderWidth + (scoreCircle / 2));
            leftY = (topY + redBorderWidth + (scoreCircle / 2) + 20);
            paint.setColor(getResources().getColor(R.color.skyBlueColor));
            canvas.drawCircle((float) leftX, (float) leftY, scoreCircle / 2, paint);
            paint.setColor(getResources().getColor(R.color.skyColor));
            canvas.drawCircle((float) leftX, (float) leftY, (scoreCircle - 8) / 2, paint);
            paint.setColor(getResources().getColor(R.color.skyBlueTextColor));
            paint.setTextSize(60);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("Score", (float) leftX, (float) leftY, paint);
            paint.setColor(Color.BLACK);
            paint.setColor(getResources().getColor(R.color.skyBlueTextColor));
            paint.setTextSize(40);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("0", (float) leftX, (float) leftY + 45, paint);
            paint.setColor(Color.BLACK);
        }
        private void DrawArrows()
        {
            if(t == Turn.RED)
            {
                Piece[] pieces = playerRed.getRedPiecePositions();
                for (int i = 0; i < pieces.length; i++) {
                    if (pieces[i].getIsKilled()) {
                        playerIndicator.getIndicator(i).setY();
                        paint.setColor(Color.parseColor("#ffffff"));
                        canvas.drawPath( playerIndicator.getIndicator(i).getPath(), paint);
                    }
                }
            }

            if(t == Turn.BLUE)
            {
                Piece[] pieces = playerBlue.getRedPiecePositions();
                for (int i = 0; i < pieces.length; i++) {
                    if (pieces[i].getIsKilled()) {
                        playerIndicator.getIndicator(i).setY();
                        paint.setColor(Color.parseColor("#ffffff"));
                        canvas.drawPath( playerIndicator.getIndicator(i).getPath(), paint);
                    }
                }
            }

        }
        private void DrawPlayerScore(int PlayerScoreRed,int PlayerScoreGreen,int PlayerScoreBlue,int PlayerScoreYellow) {
            int additionRed = 0,additionBlue=0;
            Piece[] pieces = new Piece[0];
                pieces = playerRed.getRedPiecePositions();
            for (int j = 0; j < 4; j++) {
                if(pieces[j].getComplete()) {
                    additionRed+=500;
                }
            }

            pieces = playerBlue.getRedPiecePositions();
            for (int j = 0; j < 4; j++) {
                if(pieces[j].getComplete()) {
                    additionBlue+=500;
                }
            }


            float r = 40.0f;
            int blockWidthHight = (int) (ScreenHeight * (r / 100f));
            int left = (ScreenWidth / 2) - (ScreenHeight / 2);
            int redBorderWidth = (int) ((float) blockWidthHight * (20f / 100f));
            int whiteAreaWidth = (blockWidthHight - redBorderWidth) - (redBorderWidth + 4);
            int scoreCircle = (int) ((float) whiteAreaWidth * (200f / 200f));
            int leftX = (left + redBorderWidth + (scoreCircle / 2));
            int leftY = (redBorderWidth + (scoreCircle / 2) + 10);
            paint.setColor(getResources().getColor(R.color.red200Color));
            canvas.drawCircle((float) leftX, (float) leftY, scoreCircle / 2, paint);
            paint.setColor(getResources().getColor(R.color.red200Color));
            canvas.drawCircle((float) leftX, (float) leftY, (scoreCircle - 8) / 2, paint);
            paint.setColor(getResources().getColor(R.color.red700Color));
            paint.setTextSize(60);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("Score", (float) leftX, (float) leftY, paint);
            paint.setColor(Color.BLACK);
            paint.setColor(getResources().getColor(R.color.red700Color));
            paint.setTextSize(40);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText(""+(PlayerScoreRed+additionRed), (float) leftX, (float) leftY + 45, paint);


            int topY = ScreenHeight - blockWidthHight;
            int topRightX = ScreenHeight - blockWidthHight + left;
            scoreCircle = (int) ((float) whiteAreaWidth * (200f / 200f));
            leftX = (topRightX + redBorderWidth + (scoreCircle / 2));
            leftY = (topY + redBorderWidth + (scoreCircle / 2) + 20);
            paint.setColor(getResources().getColor(R.color.skyBlueTextColor));
            paint.setTextSize(40);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText(""+(PlayerScoreBlue+additionBlue), (float) leftX, (float) leftY + 45, paint);
            paint.setColor(Color.BLACK);


            scoreCircle = (int) ((float) whiteAreaWidth * (200f / 200f));
            leftX = (topRightX + redBorderWidth + (scoreCircle / 2));
            leftY = (redBorderWidth + (scoreCircle / 2) + 10);
            paint.setColor(getResources().getColor(R.color.green700Color));
            paint.setTextSize(40);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText(""+PlayerScoreGreen, (float) leftX, (float) leftY + 45, paint);
            paint.setColor(Color.BLACK);

            scoreCircle = (int) ((float) whiteAreaWidth * (200f / 200f));
            leftX = (left + redBorderWidth + (scoreCircle / 2));
            leftY = (topY + redBorderWidth + (scoreCircle / 2) + 10);
            paint.setColor(getResources().getColor(R.color.yellow700Color));
            paint.setTextSize(40);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText(""+PlayerScoreYellow, (float) leftX, (float) leftY + 45, paint);
            paint.setColor(Color.BLACK);

            Log.v("jksdfh",""+Player1DiceArray.length);
            if(diceMoveCountRed==Player1DiceArray.length-1 && diceMoveCountBlue==Player2DiceArray.length-1){
                Log.v("shfjsgfh","Game Over");
                startActivity(new Intent(getApplicationContext(), WinActivity.class));
                finish();
            }
        }

        private void DrawPlayerNextDice() {
            float r = 40.0f;
            int blockWidthHight = (int) (ScreenHeight * (r / 100f));
            int redBorderWidth = (int) ((float) blockWidthHight * (20f / 100f));
            for (int i = 0; i < 4; i++) {
                int whiteAreaWidth1 = (blockWidthHight - redBorderWidth) - (redBorderWidth + 4);
                Log.d("LudoActivity", "white area " + whiteAreaWidth1);

                int circleWidth1 = (int) ((float) whiteAreaWidth1 * (45f / 100f));
                Log.d("LudoActivity", "circle width " + circleWidth1);
                int leftX1 = ScreenWidth-100;
                int leftY1 = ScreenHeight-100-(130*i);
                paint.setColor(Color.BLACK);
                canvas.drawCircle((float) leftX1, (float) leftY1, circleWidth1 / 2, paint);
                paint.setColor(getResources().getColor(R.color.colorPrimaryDark));
                canvas.drawCircle((float) leftX1, (float) leftY1, (circleWidth1 - 4) / 2, paint);
                paint.setColor(Color.WHITE);
                paint.setTextSize(80);
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                Log.v("kjhksdh","Entry");
                if (UserId.equalsIgnoreCase(playerOne)) {
                    if (diceMoveCountRed + 4 < Player1DiceArray.length)
                        canvas.drawText("" + Player1DiceArray[diceMoveCountRed + i], (float) leftX1, (float) leftY1 + 30, paint);
                    else {
                        canvas.drawText("*", (float) leftX1, (float) leftY1 + 30, paint);
                    }
                }else if (UserId.equalsIgnoreCase(playerTwo)) {
                    if (diceMoveCountBlue + 4 < Player2DiceArray.length)
                        canvas.drawText("" + Player2DiceArray[diceMoveCountBlue + i], (float) leftX1, (float) leftY1 + 30, paint);
                    else {
                        canvas.drawText("*", (float) leftX1, (float) leftY1 + 30, paint);
                    }
                }
            }
        }
        private int setDiceNo(int next) {
            bmpDice = Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bmpDice);
            if(t == Turn.RED)
                paint.setColor(getResources().getColor(R.color.redColour));
            else if(t == Turn.BLUE)
                paint.setColor(getResources().getColor(R.color.blueColor));
            else if(t == Turn.GREEN)
                paint.setColor(getResources().getColor(R.color.greenColor));
            else if (t == Turn.YELLOW)
                paint.setColor(getResources().getColor(R.color.yellowColor));
            c.drawRoundRect(new RectF(0,0,100,100),6,6,paint);


            paint.setColor(Color.WHITE);

            switch (next)
            {
                case 1:
                    c.drawCircle(50,50,10,paint);
                    break;
                case 2:
                    c.drawCircle(20,20,10,paint);
                    c.drawCircle(80,80,10,paint);
                    break;
                case 3:
                    c.drawCircle(20,20,10,paint);
                    c.drawCircle(50,50,10,paint);
                    c.drawCircle(80,80,10,paint);
                    break;
                case 4:
                    c.drawCircle(30,30,10,paint);
                    c.drawCircle(70,70,10,paint);
                    c.drawCircle(30,70,10,paint);
                    c.drawCircle(70,30,10,paint);
                    break;
                case 5:
                    c.drawCircle(20,20,10,paint);
                    c.drawCircle(80,80,10,paint);
                    c.drawCircle(50,50,10,paint);
                    c.drawCircle(20,80,10,paint);
                    c.drawCircle(80,20,10,paint);
                    break;
                case 6:
                    c.drawCircle(20,20,10,paint);
                    c.drawCircle(20,50,10,paint);
                    c.drawCircle(20,80,10,paint);
                    c.drawCircle(80,20,10,paint);
                    c.drawCircle(80,80,10,paint);
                    c.drawCircle(80,50,10,paint);
                    break;
            }

            return next;
        }

        void update() {
            if (bmpMap == null) {
                generateMap(0, 0);
            }
            if(isMoving) {
                MovePiece();
            }
            if(toHome)
            {
                MoveToHome();
            }
        }

       private void MoveToHome() {
            if(initRed) {
                Piece[] pieces = playerRed.getRedPiecePositions();
                for (int i = 0; i < 4; i++) {
                    Piece p = pieces[i];
                    if (p.getHome()) {
                        if (p.UpdateToHome()) {
                            toHome = false;
                        }
                        return;
                    }
                }
            }

            if(initBlue) {
                Piece[] pieces = playerBlue.getRedPiecePositions();
                for (int i = 0; i < 4; i++) {
                    Piece p = pieces[i];
                    if (p.getHome()) {
                        if (p.UpdateToHome()) {
                            toHome = false;
                        }
                        return;
                    }
                }
            }

        }

        private void MovePiece() {
            if(t == Turn.BLUE)
            {
                Piece[] pieces = playerRed.getRedPiecePositions();
                for (int i = 0; i < pieces.length; i++)
                {
                    Piece p = pieces[i];
                    if(p.getMove())
                    {
                        if(p.UpdatePosition())
                        {
                            boolean clear = true;
                            p.setMove(false);
                            isMoving = false;
//                            toMove = false;
//                            t=Turn.BLUE;
                            if(initBlue)
                            {
                                for (int j = 0; j < 4; j++)
                                {
                                    Piece p1 = playerBlue.getRedPiecePositions()[j];
                                    if(p.getCollision().intersect(p1.getCollision()))
                                    {
                                        if(!p.getIsStar(p.getTarget()))
                                        {
                                            Log.v("kjdshf","Blue");
                                            p1.setHome(true);
                                            toHome = true;
                                            interseptAddtionalRed+=50;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
            else if(t == Turn.RED)
            {
                Piece[] pieces = playerBlue.getRedPiecePositions();
                for (int i = 0; i < pieces.length; i++)
                {
                    Piece p = pieces[i];
                    if(p.getMove())
                    {
                        if(p.UpdatePosition())
                        {
                            boolean clear = true;
                            p.setMove(false);
                            isMoving = false;
//                            t=Turn.RED;
                            if(initRed) {
                                for (int j = 0; j < 4; j++)
                                {
                                    Piece p1 = playerRed.getRedPiecePositions()[j];
                                    if(p.getCollision().intersect(p1.getCollision()))
                                    {
                                        if(!p.getIsStar(p.getTarget()))
                                        {
                                            Log.v("kjdshf","Red");
                                            p1.setHome(true);
                                            toHome = true;
                                            clear = false;
                                            interseptAddtionalBlue+=50;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }

        private void generateMap(int count, int redScore) {
            Log.v("LogLudoActivity", "GenareteMap-1, ScreenWidth-" + ScreenWidth + " ,ScreenHeight-" + ScreenHeight);
            bmpMap = Bitmap.createBitmap(ScreenWidth, ScreenHeight, Bitmap.Config.ARGB_8888);
            Canvas map = new Canvas(bmpMap);

            bmpBg = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg);
            Bitmap drawBackgroundBitmapImage = Bitmap.createScaledBitmap(bmpBg, ScreenWidth, ScreenHeight, true);
            map.drawBitmap(drawBackgroundBitmapImage, 0f, 0f, null);

            float r = 40.0f;
            int blockWidthHight = (int) (ScreenHeight * (r / 100f));
            int left = (ScreenWidth / 2) - (ScreenHeight / 2);

            paint.setColor(Color.BLACK);
            map.drawRect(left - 2, 0, blockWidthHight + left - 2, blockWidthHight, paint);

            // Create Red Player Box
            paint.setColor(getResources().getColor(R.color.redColour));  // Red // Shailendra frame rectange
            map.drawRect(left, 2, blockWidthHight - 2 + left, blockWidthHight - 2, paint);
            int redBorderWidth = (int) ((float) blockWidthHight * (20f / 100f));
            paint.setColor(Color.WHITE);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(40);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            map.drawText("Player 1", left + redBorderWidth + 130, 50, paint);


            int whiteAreaWidth = (blockWidthHight - redBorderWidth) - (redBorderWidth + 4);
            int scoreCircle = (int) ((float) whiteAreaWidth * (200f / 200f));
            int leftX = (left + redBorderWidth + (scoreCircle / 2));
            int leftY = (redBorderWidth + (scoreCircle / 2) + 10);
            paint.setColor(getResources().getColor(R.color.red200Color));
            map.drawCircle((float) leftX, (float) leftY, scoreCircle / 2, paint);
            paint.setColor(getResources().getColor(R.color.red200Color));
            map.drawCircle((float) leftX, (float) leftY, (scoreCircle - 8) / 2, paint);
            paint.setColor(getResources().getColor(R.color.red700Color));
            paint.setTextSize(60);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            map.drawText("Score", (float) leftX, (float) leftY, paint);
            paint.setColor(Color.BLACK);
            paint.setColor(getResources().getColor(R.color.red700Color));
            paint.setTextSize(40);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            map.drawText("", (float) leftX, (float) leftY + 45, paint);
            paint.setColor(Color.BLACK);


            // Create Yellow Player Box
            int topY = ScreenHeight - blockWidthHight;
            paint.setColor(Color.BLACK);
            map.drawRect(left - 2, topY, blockWidthHight + left - 2, ScreenHeight, paint);
            paint.setColor(getResources().getColor(R.color.yellowColor));
            map.drawRect(left, topY, blockWidthHight - 2 + left, ScreenHeight - 2, paint);
            Log.d("LudoActivity", "redBorder height " + redBorderWidth);
            paint.setColor(Color.WHITE);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(40);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            map.drawText("Player 3", left + redBorderWidth + 130, topY + 50, paint);
            scoreCircle = (int) ((float) whiteAreaWidth * (200f / 200f));
            leftX = (left + redBorderWidth + (scoreCircle / 2));
            leftY = (topY + redBorderWidth + (scoreCircle / 2) + 10);
            paint.setColor(getResources().getColor(R.color.yellow500Color));
            map.drawCircle((float) leftX, (float) leftY, scoreCircle / 2, paint);
            paint.setColor(getResources().getColor(R.color.yellow200Color));
            map.drawCircle((float) leftX, (float) leftY, (scoreCircle - 8) / 2, paint);
            paint.setColor(getResources().getColor(R.color.yellow700Color));
            paint.setTextSize(60);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            map.drawText("Score", (float) leftX, (float) leftY, paint);
            paint.setColor(Color.BLACK);
            paint.setColor(getResources().getColor(R.color.yellow700Color));
            paint.setTextSize(40);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            map.drawText("", (float) leftX, (float) leftY + 45, paint);
            paint.setColor(Color.BLACK);

            // Create Green Player Box
            int topRightX = ScreenHeight - blockWidthHight + left;
            paint.setColor(Color.BLACK);
            map.drawRect(topRightX - 2, 0, ScreenHeight + left, blockWidthHight, paint);
            paint.setColor(getResources().getColor(R.color.greenColor));
            map.drawRect(topRightX + 2, 2, ScreenHeight + left - 2, blockWidthHight - 2, paint);
            paint.setColor(Color.WHITE);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(40);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            map.drawText("Player 2", topRightX + 210, 50, paint);
            scoreCircle = (int) ((float) whiteAreaWidth * (200f / 200f));
            leftX = (topRightX + redBorderWidth + (scoreCircle / 2));
            leftY = (redBorderWidth + (scoreCircle / 2) + 10);
            paint.setColor(getResources().getColor(R.color.green500Color));
            map.drawCircle((float) leftX, (float) leftY, scoreCircle / 2, paint);
            paint.setColor(getResources().getColor(R.color.green200Color));
            map.drawCircle((float) leftX, (float) leftY, (scoreCircle - 8) / 2, paint);
            paint.setColor(getResources().getColor(R.color.green700Color));
            paint.setTextSize(60);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            map.drawText("Score", (float) leftX, (float) leftY, paint);
            paint.setColor(Color.BLACK);
            paint.setColor(getResources().getColor(R.color.green700Color));
            paint.setTextSize(40);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            map.drawText("", (float) leftX, (float) leftY + 45, paint);
            paint.setColor(Color.BLACK);


            // Create Blue Player Box
            map.drawRect(topRightX - 2, topY, ScreenHeight + left, ScreenHeight, paint);
            paint.setColor(getResources().getColor(R.color.blueColor));
            map.drawRect(topRightX + 2, topY - 2, ScreenHeight + left - 2, ScreenHeight - 2, paint);
            paint.setColor(Color.WHITE);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(40);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            map.drawText("Player 4", topRightX + 210, topY + 50, paint);
            scoreCircle = (int) ((float) whiteAreaWidth * (200f / 200f));
            leftX = (topRightX + redBorderWidth + (scoreCircle / 2));
            leftY = (topY + redBorderWidth + (scoreCircle / 2) + 20);
            paint.setColor(getResources().getColor(R.color.skyBlueColor));
            map.drawCircle((float) leftX, (float) leftY, scoreCircle / 2, paint);
            paint.setColor(getResources().getColor(R.color.skyColor));
            map.drawCircle((float) leftX, (float) leftY, (scoreCircle - 8) / 2, paint);
            paint.setColor(getResources().getColor(R.color.skyBlueTextColor));
            paint.setTextSize(60);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            map.drawText("Score", (float) leftX, (float) leftY, paint);
            paint.setColor(Color.BLACK);
            paint.setColor(getResources().getColor(R.color.skyBlueTextColor));
            paint.setTextSize(40);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            map.drawText("", (float) leftX, (float) leftY + 45, paint);
            paint.setColor(Color.BLACK);


            // Create Step Path For Piece Red and Yellow Middle
            int pathWidth = (int) ((float) ScreenHeight * (20f / 100f));
            int siglePathWidth = pathWidth / 3;
            Bitmap scaledBmp = Bitmap.createScaledBitmap(bmpStar, siglePathWidth, siglePathWidth, true);
            Bitmap scaledBmpWhite = Bitmap.createScaledBitmap(bmpStarWhite, siglePathWidth, siglePathWidth, true);

            Position p;
            red = new Position[4];

            redPath = new Position[6];
            for (int i = 0; i < 3; i++) {
                int pathY = blockWidthHight + (siglePathWidth * i);
                int pathX = left;
                for (int j = 0; j < 6; j++) {
                    paint.setColor(Color.BLACK);
                    map.drawRect(pathX - 2, pathY, pathX + siglePathWidth,
                            pathY + siglePathWidth, paint);

                    if ((i == 0 && j == 1)) {
                        paint.setColor(getResources().getColor(R.color.redColour));
                        map.drawRect(pathX, pathY, pathX + siglePathWidth,
                                pathY + siglePathWidth - 2, paint);
                        map.drawBitmap(scaledBmpWhite, (float) pathX - 2, (float) pathY, null);

                    } else if ((i == 1 && j > 0)) {
                        paint.setColor(getResources().getColor(R.color.redColour));
                        map.drawRect(pathX, pathY, pathX + siglePathWidth,
                                pathY + siglePathWidth - 2, paint);

                        if (j == 5)
                            redPath[5] = new Position(pathX + siglePathWidth, pathY);
                        p = new Position(pathX, pathY);
                        redPath[j - 1] = p;

                    } else {
                        paint.setColor(Color.WHITE);
                        map.drawRect(pathX, pathY, pathX + siglePathWidth,
                                pathY + siglePathWidth - 2, paint);
                    }

                    if (i == 2 && j == 2)
                        map.drawBitmap(scaledBmp, pathX, pathY, null);

                    switch (i) {
                        case 0:
                            if (j == 1) {
                                p = new Position(pathX + siglePathWidth / 2 - 10, pathY + siglePathWidth / 2);
                                red[0] = p;
                                p = new Position(pathX + siglePathWidth / 2, pathY + siglePathWidth / 2);
                                red[1] = p;
                                p = new Position(pathX + siglePathWidth / 2 + 10, pathY + siglePathWidth / 2);
                                red[2] = p;
                                p = new Position(pathX + siglePathWidth / 2 + 20, pathY + siglePathWidth / 2);
                                red[3] = p;
                                romPath[1] = new PathPostion(pathX, pathY, true);
                            } else {
                                romPath[j] = new PathPostion(pathX, pathY, false);
                            }
                            break;
                        case 1:
                            if (j == 0)
                                romPath[51] = new PathPostion(pathX, pathY, false);
                            break;
                        case 2:
                            if (j == 1)
                                romPath[50 - j] = new PathPostion(pathX, pathY, true);
                            else
                                romPath[50 - j] = new PathPostion(pathX, pathY, false);
                            break;
                    }

                    pathX += siglePathWidth;
                }
            }

            // Create Step Path For Piece Red and Green Middle
            greenPath = new Position[6];
            for (int i = 0; i < 3; i++) {
                int pathY = 0; //blockWidthHight + (siglePathWidth * i);
                int pathX = left + blockWidthHight + (siglePathWidth * i);
                for (int j = 0; j < 6; j++) {
                    paint.setColor(Color.BLACK);
                    map.drawRect(pathX - 2, pathY, pathX + siglePathWidth, pathY + siglePathWidth, paint);

                    if ((i == 1 && j > 0)) {
                        paint.setColor(getResources().getColor(R.color.greenColor));
                        map.drawRect(pathX, pathY + 2, pathX + siglePathWidth,
                                pathY + siglePathWidth, paint);
                        if (j == 5)
                            greenPath[5] = new Position(pathX, pathY + siglePathWidth);

                        greenPath[j - 1] = new Position(pathX, pathY);

                    } else if (i == 2 && j == 1) {
                        paint.setColor(getResources().getColor(R.color.greenColor));
                        map.drawRect(pathX, pathY + 2, pathX + siglePathWidth,
                                pathY + siglePathWidth, paint);

                        map.drawBitmap(scaledBmpWhite, (float) pathX - 2, (float) pathY, null);
                    } else {
                        paint.setColor(Color.WHITE);
                        map.drawRect(pathX, pathY + 2, pathX + siglePathWidth,
                                pathY + siglePathWidth, paint);
                    }

                    if (i == 0 && j == 1) {
                        map.drawBitmap(scaledBmp, pathX, pathY, null);
                    }

                    switch (i) {
                        case 0:
                            if (j == 1)
                                romPath[11 - j] = new PathPostion(pathX, pathY, true);
                            else
                                romPath[11 - j] = new PathPostion(pathX, pathY, false);
                            break;
                        case 1:
                            if (j == 0)
                                romPath[12] = new PathPostion(pathX, pathY, false);
                            break;
                        case 2:
                            if (j == 1)
                                romPath[13 + j] = new PathPostion(pathX, pathY, true);
                            else
                                romPath[13 + j] = new PathPostion(pathX, pathY, false);
                            break;
                    }

                    pathY += siglePathWidth;
                }
            }

            // Create Step Path For Piece Green and Blue Middle
            blue = new Position[4];
            bluePath = new Position[6];
            for (int i = 0; i < 3; i++) {
                int pathY = blockWidthHight + (siglePathWidth * i);
                int pathX = left + ScreenHeight - siglePathWidth;
                for (int j = 0; j < 6; j++) {
                    paint.setColor(Color.BLACK);
                    map.drawRect(pathX, pathY, pathX + siglePathWidth, pathY + siglePathWidth, paint);

                    if ((i == 1 && j > 0)) {
                        paint.setColor(getResources().getColor(R.color.blueColor));
                        map.drawRect(pathX, pathY, pathX + siglePathWidth - 2,
                                pathY + siglePathWidth - 2, paint);
                        p = new Position(pathX, pathY);
                        bluePath[j - 1] = p;

                        if (j == 5)
                            bluePath[5] = new Position(pathX - siglePathWidth, pathY);
                    } else if (i == 2 && j == 1) {
                        paint.setColor(getResources().getColor(R.color.blueColor));
                        map.drawRect(pathX, pathY, pathX + siglePathWidth - 2,
                                pathY + siglePathWidth - 2, paint);
                        map.drawBitmap(scaledBmpWhite, (float) pathX - 2, (float) pathY, null);
                    } else {
                        paint.setColor(Color.WHITE);
                        map.drawRect(pathX, pathY, pathX + siglePathWidth - 2,
                                pathY + siglePathWidth - 2, paint);
                    }

                    if (i == 0 && j == 1)
                        map.drawBitmap(scaledBmp, pathX, pathY, null);

                    switch (i) {
                        case 0:
                            if (j == 1) {

                                romPath[24 - j] = new PathPostion(pathX, pathY, true);
                            }
                            else
                                romPath[24 - j] = new PathPostion(pathX, pathY, false);
                            break;
                        case 1:
                            if (j == 0)
                                romPath[25] = new PathPostion(pathX, pathY, false);
                            break;
                        case 2:
                            if (j == 1) {
                                p = new Position(pathX+(siglePathWidth/2)-20, pathY + (siglePathWidth/2));
                                blue[0] = p;
                                p = new Position(pathX+(siglePathWidth/2)-10, pathY + (siglePathWidth/2));
                                blue[1] = p;
                                p = new Position(pathX+(siglePathWidth/2), pathY + (siglePathWidth/2));
                                blue[2] = p;
                                p = new Position(pathX+(siglePathWidth/2)+10, pathY + (siglePathWidth/2));
                                blue[3] = p;
                                romPath[26 + j] = new PathPostion(pathX, pathY, true);
                            }
                            else
                                romPath[26 + j] = new PathPostion(pathX, pathY, false);
                            break;
                    }
                    pathX -= siglePathWidth;
                }
            }

            // Create Step Path For Piece Blue and Yellow Middle
            yellowPath = new Position[6];
            for (int i = 0; i < 3; i++) {
                int pathY = ScreenHeight - blockWidthHight; //blockWidthHight + (siglePathWidth * i);
                int pathX = left + blockWidthHight + (siglePathWidth * i);
                for (int j = 0; j < 6; j++) {
                    paint.setColor(Color.BLACK);
                    map.drawRect(pathX - 2, pathY, pathX + siglePathWidth, pathY + siglePathWidth, paint);

                    if ((i == 0 && j == 4)) {
                        paint.setColor(getResources().getColor(R.color.yellowColor));
                        map.drawRect(pathX, pathY, pathX + siglePathWidth,
                                pathY + siglePathWidth - 2, paint);
                        map.drawBitmap(scaledBmpWhite, (float) pathX - 2, (float) pathY, null);

                    } else if (i == 1 && j < 5) {
                        Log.d("LudoActivity", "In Yellow");
                        Log.d("LudoActivity", "i is " + i + " & j is " + j);
                        paint.setColor(getResources().getColor(R.color.yellowColor));
                        map.drawRect(pathX, pathY, pathX + siglePathWidth,
                                pathY + siglePathWidth - 2, paint);
                        if (j == 0) {
                            yellowPath[5] = new Position(pathX, pathY - siglePathWidth);
                        }

                        yellowPath[4 - j] = new Position(pathX, pathY);
                    } else {
                        paint.setColor(Color.WHITE);
                        map.drawRect(pathX, pathY, pathX + siglePathWidth,
                                pathY + siglePathWidth - 2, paint);
                    }

                    if (i == 2 && j == 4)
                        map.drawBitmap(scaledBmp, pathX, pathY, null);

                    switch (i) {
                        case 0:
                            if (j == 4)
                                romPath[44 - j] = new PathPostion(pathX, pathY, true);
                            else
                                romPath[44 - j] = new PathPostion(pathX, pathY, false);
                            break;
                        case 1:
                            if (j == 5)
                                romPath[38] = new PathPostion(pathX, pathY, false);
                            break;
                        case 2:
                            if (j == 4)
                                romPath[32 + j] = new PathPostion(pathX, pathY, true);
                            else
                                romPath[32 + j] = new PathPostion(pathX, pathY, false);
                    }
                    pathY += siglePathWidth;
                }
            }


            map.drawRect(left + blockWidthHight, blockWidthHight,
                    left + blockWidthHight + pathWidth, blockWidthHight + pathWidth, paint);
            paint.setColor(getResources().getColor(R.color.redColour));
            Path path = new Path();
            path.moveTo(left + blockWidthHight + 2, blockWidthHight + 2);
            path.lineTo(left + blockWidthHight + 2, blockWidthHight + pathWidth - 2);
            path.lineTo(left + blockWidthHight + (pathWidth / 2), blockWidthHight + (pathWidth / 2));
            path.lineTo(left + blockWidthHight + 2, blockWidthHight + 2);
            path.close();
            map.drawPath(path, paint);

            paint.setColor(getResources().getColor(R.color.blueColor));
            path = new Path();
            path.moveTo(left + blockWidthHight + pathWidth - 2, blockWidthHight + 2);
            path.lineTo(left + blockWidthHight + pathWidth - 2, blockWidthHight + pathWidth - 2);
            path.lineTo(left + blockWidthHight + (pathWidth / 2), blockWidthHight + (pathWidth / 2));
            path.lineTo(left + blockWidthHight + pathWidth - 2, blockWidthHight + 2);
            path.close();
            map.drawPath(path, paint);

            paint.setColor(getResources().getColor(R.color.greenColor));
            path = new Path();
            path.moveTo(left + blockWidthHight + 2, blockWidthHight + 2);
            path.lineTo(left + blockWidthHight + pathWidth - 2, blockWidthHight + 2);
            path.lineTo(left + blockWidthHight + (pathWidth / 2), blockWidthHight + (pathWidth / 2));
            path.lineTo(left + blockWidthHight + 2, blockWidthHight + 2);
            path.close();
            map.drawPath(path, paint);

            paint.setColor(getResources().getColor(R.color.yellowColor));
            path = new Path();
            path.moveTo(left + blockWidthHight + 2, blockWidthHight + pathWidth - 2);
            path.lineTo(left + blockWidthHight + pathWidth - 2, blockWidthHight + pathWidth - 2);
            path.lineTo(left + blockWidthHight + (pathWidth / 2), blockWidthHight + (pathWidth / 2));
            path.lineTo(left + blockWidthHight + 2, blockWidthHight + pathWidth - 2);
            path.close();
            map.drawPath(path, paint);


            // Place for Player position for dice
            Bitmap playerBit1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.player1);
            Bitmap playerBit2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.player2);
            Bitmap playerBit3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.player3);
            Bitmap playerBit4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.player4);

            paint.setColor(Color.WHITE);
            Rect playerRectRed = new Rect(250, 160, 450, 370);
            map.drawRoundRect(new RectF(250, 160, 450, 370), 10, 10, paint);
            map.drawBitmap(playerBit1, new Rect(-50, -50, 600, 600), playerRectRed, null);
            map.drawRoundRect(new RectF(280, 30, 420, 170), 10, 10, paint);

//            indicator = new Position[4];
//            p = new Position(150, 260);
//            indicator[0] = p;
//            indicator[1] = p;
//            indicator[2] = p;
//            indicator[3] = p;
            // Green Player dice Box
            paint.setColor(Color.WHITE);
            Rect playerRectBlue = new Rect(ScreenWidth - 450, ScreenHeight - 400, ScreenWidth - 250, ScreenHeight - 190);
            map.drawRoundRect(new RectF(ScreenWidth - 450, ScreenHeight - 400, ScreenWidth - 250, ScreenHeight - 190), 10, 10, paint);
            map.drawBitmap(playerBit2, new Rect(-50, -50, 600, 600), playerRectBlue, null);
            map.drawRoundRect(new RectF(ScreenWidth - 420, ScreenHeight - 200, ScreenWidth - 280, ScreenHeight - 60), 10, 10, paint);

            // Green Player dice Box
            paint.setColor(Color.WHITE);
            Rect playerRectGreen = new Rect(ScreenWidth - 450, 170, ScreenWidth - 250, 370);
            map.drawRoundRect(new RectF(ScreenWidth - 450, 170, ScreenWidth - 250, 370), 10, 10, paint);
            map.drawBitmap(playerBit3, new Rect(-50, -50, 600, 600), playerRectGreen, null);
            map.drawRoundRect(new RectF(ScreenWidth - 420, 40, ScreenWidth - 280, 180), 10, 10, paint);

            // Yellow Player dice Box
            paint.setColor(Color.WHITE);
            Rect playerRectYellow = new Rect(250, ScreenHeight - 400, 450, ScreenHeight - 190);
            map.drawRoundRect(new RectF(250, ScreenHeight - 400, 450, ScreenHeight - 190), 10, 10, paint);
            map.drawBitmap(playerBit4, new Rect(-50, -50, 600, 600), playerRectYellow, null);
            map.drawRoundRect(new RectF(280, ScreenHeight - 200, 420, ScreenHeight - 60), 10, 10, paint);

            map.save();

            // Draw Red Player Piece
            bmpRedPiece = Bitmap.createBitmap(siglePathWidth, siglePathWidth + 20, Bitmap.Config.ARGB_8888);
            Canvas redPiece = new Canvas(bmpRedPiece);
            int pieceWidth = (int) (siglePathWidth * (80f / 100f));
            int pieceHeight = siglePathWidth;
            paint.setColor(getResources().getColor(R.color.maroonColor));
            redPiece.drawCircle(siglePathWidth / 2, pieceWidth + 10, 25, paint);
            paint.setColor(getResources().getColor(R.color.white));
            redPiece.drawCircle(siglePathWidth / 2, pieceWidth + 10, 20, paint);
            int x = siglePathWidth / 2 - (pieceWidth / 2);
            int y = 0;
            paint.setColor(Color.parseColor(redPieceColor));
            redPiece.drawCircle(siglePathWidth / 2, pieceWidth / 2, pieceWidth / 2, paint);
            path = new Path();
            path.moveTo(x, pieceWidth / 2);
            path.lineTo(siglePathWidth / 2, pieceHeight);
            path.lineTo(x + pieceWidth, pieceWidth / 2);
            path.close();
            redPiece.drawPath(path, paint);
            paint.setColor(getResources().getColor(R.color.redColour));
            redPiece.drawCircle(siglePathWidth / 2, pieceWidth / 2, 20, paint);
            redPiece.save();

            // Draw Blue Player Piece
            bmpBluePiece = Bitmap.createBitmap(siglePathWidth, siglePathWidth + 20, Bitmap.Config.ARGB_8888);
            Canvas bluePiece = new Canvas(bmpBluePiece);
            paint.setColor(getResources().getColor(R.color.maroonColor));
            bluePiece.drawCircle(siglePathWidth / 2, pieceWidth + 10, 25, paint);
            paint.setColor(getResources().getColor(R.color.white));
            bluePiece.drawCircle(siglePathWidth / 2, pieceWidth + 10, 20, paint);
            paint.setColor(Color.parseColor(bluePieceColor));
            bluePiece.drawCircle(siglePathWidth / 2, pieceWidth / 2, pieceWidth / 2, paint);
            bluePiece.drawPath(path, paint);
            paint.setColor(getResources().getColor(R.color.blueColor));
            bluePiece.drawCircle(siglePathWidth / 2, pieceWidth / 2, 20, paint);
            bluePiece.save();

            // Draw Yellow Player Piece
            bmpYellowPiece = Bitmap.createBitmap(siglePathWidth, siglePathWidth + 20, Bitmap.Config.ARGB_8888);
            Canvas yellowPiece = new Canvas(bmpYellowPiece);
            paint.setColor(getResources().getColor(R.color.maroonColor));
            yellowPiece.drawCircle(siglePathWidth / 2, pieceWidth + 10, 25, paint);
            paint.setColor(getResources().getColor(R.color.white));
            yellowPiece.drawCircle(siglePathWidth / 2, pieceWidth + 10, 20, paint);
            paint.setColor(Color.parseColor(yellowPieceColor));
            yellowPiece.drawCircle(siglePathWidth / 2, pieceWidth / 2, pieceWidth / 2, paint);
            yellowPiece.drawPath(path, paint);
            paint.setColor(getResources().getColor(R.color.yellowColor));
            yellowPiece.drawCircle(siglePathWidth / 2, pieceWidth / 2, 20, paint);
            yellowPiece.save();

            // Draw Green Player Piece
            bmpGreenPiece = Bitmap.createBitmap(siglePathWidth, siglePathWidth + 20, Bitmap.Config.ARGB_8888);
            Canvas greenPiece = new Canvas(bmpGreenPiece);
            paint.setColor(getResources().getColor(R.color.maroonColor));
            greenPiece.drawCircle(siglePathWidth / 2, pieceWidth + 10, 25, paint);
            paint.setColor(getResources().getColor(R.color.white));
            greenPiece.drawCircle(siglePathWidth / 2, pieceWidth + 10, 20, paint);
            paint.setColor(Color.parseColor(greenPieceColor));
            greenPiece.drawCircle(siglePathWidth / 2, pieceWidth / 2, pieceWidth / 2, paint);
            greenPiece.drawPath(path, paint);
            paint.setColor(getResources().getColor(R.color.greenColor));
            greenPiece.drawCircle(siglePathWidth / 2, pieceWidth / 2, 20, paint);
            greenPiece.save();

            int circleWidth = (int) ((float) whiteAreaWidth * (25f / 100f));
            CircleSize = circleWidth - 4;
            if (initRed) {
                if (playerRed == null) {
                    playerRed = new Player3(1, 50, red, siglePathWidth, redPath, redPieceColor, context);
//                    playerRed.setArrows(new int[]{red[0].getX(),red[1].getX(),red[2].getX(),red[3].getX()},
//                            new int[]{red[0].getY(),red[1].getY(),red[2].getY(),red[3].getY()},CircleSize);
                }
            }
            if(initBlue)
            {
                if(playerBlue == null) {
                    playerBlue = new Player3(27, 25, blue, siglePathWidth,bluePath,bluePieceColor,context);
//                    playerBlue.setArrows(new int[]{blue[0].getX(),blue[1].getX(),blue[2].getX(),blue[3].getX()},
//                            new int[]{blue[0].getY(),blue[1].getY(),blue[2].getY(),blue[3].getY()},CircleSize);
                }
            }
//            playerIndicator = new Player3();
//            circleWidth = (int) ((float) whiteAreaWidth * (45f / 100f));
//            CircleSize = circleWidth - 4;
//            playerIndicator.setIndicator(new int[]{indicator[0].getX(),indicator[1].getX(),indicator[2].getX(),indicator[3].getX()},
//                    new int[]{indicator[0].getY(),indicator[1].getY(),indicator[2].getY(),indicator[3].getY()},CircleSize);
        }

        private void DrawPlayerPiece() {
            if (playerRed != null) {
                Piece[] pieces = playerRed.getRedPiecePositions();
                for (int i = 0; i < pieces.length; i++) {
                    Piece p = pieces[i];
                    paint.setColor(Color.parseColor(redPieceColor));
                    canvas.drawBitmap(bmpRedPiece, p.getX() - bmpRedPiece.getWidth() / 2, p.getY() - bmpRedPiece.getHeight() + 20, null);
                }
            }
            if(playerBlue != null)
            {
                Piece[] pieces = playerBlue.getRedPiecePositions();
                for (int i = 0; i < pieces.length; i++)
                {
                    Piece p = pieces[i];
                    paint.setColor(Color.parseColor(bluePieceColor));
                    canvas.drawBitmap(bmpBluePiece,p.getX()-bmpBluePiece.getWidth()/2,p.getY()-bmpBluePiece.getHeight()+20,null);
                }
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (gameFinished == false) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        Log.v("jsdgjhdg",""+t);
                        if (t == Turn.RED) {
                            if(UserId.equalsIgnoreCase(playerOne)) {
                                Piece[] pieces = playerRed.getRedPiecePositions();
                                for (int i = 0; i < pieces.length; i++) {
                                    if (pieces[i].getCollision().contains((int) event.getX(), (int) event.getY())) {
                                        int placeToMove = Player1DiceArray[diceMoveCountRed];
                                        if (playerRed.checkCanMoveMe(i, placeToMove)) {
                                            SetMultiPlayerScore(playerOne, i, diceMoveCountRed, placeToMove, "BLUE", playerTwo);
//                                        MovePlayerPiece(i, diceMoveCountRed, placeToMove,"RED");
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        else if (t == Turn.BLUE) {
                            if(UserId.equalsIgnoreCase(playerTwo)) {
                                Piece[] pieces = playerBlue.getRedPiecePositions();
                                for (int i = 0; i < pieces.length; i++) {
                                    if (pieces[i].getCollision().contains((int) event.getX(), (int) event.getY())) {
                                        int placeToMove = Player2DiceArray[diceMoveCountBlue];
                                        if (playerBlue.checkCanMoveMe(i, placeToMove)) {
                                            SetMultiPlayerScore(playerTwo, i, diceMoveCountBlue, placeToMove, "RED", playerOne);
//                                        MovePlayerPiece(i, diceMoveCountBlue, placeToMove,"BLUE");
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                }
            } else {
                startActivity(new Intent(context, MainActivity.class));
                return true;
            }
            return true;
        }
        public void MovePlayerPiece(int pieceId,int diceArrayCount,int diceArrayNumber,String turn) {
            Log.v("jhsgfjjhd", "pieceId-"+pieceId+", diceArrayCount-" + diceArrayCount + " ,diceArrayNumber-" + diceArrayNumber+ " ,turn-" + turn);
            Piece[] pieces = new Piece[0];
            if(turn.equalsIgnoreCase("RED")) {
                pieces = playerRed.getRedPiecePositions();
            }
            else if(turn.equalsIgnoreCase("BLUE")) {
                pieces = playerBlue.getRedPiecePositions();
            }
//            if (playerRed.checkCanMoveMe(pieceId, diceArrayNumber)) {
                isMoving = true;
                pieces[pieceId].setTarget(diceArrayNumber);
                pieces[pieceId].setMove(true);



                if (turn.equalsIgnoreCase("RED")) {
                    PlayerScoreRed += diceArrayNumber;
                    setDiceNo(diceArrayNumber);
                    diceMoveCountRed++;
                } else if (turn.equalsIgnoreCase("BLUE")) {
                    PlayerScoreBlue += diceArrayNumber;
                    setDiceNo(diceArrayNumber);
                    diceMoveCountBlue++;
                } else if (turn.equalsIgnoreCase("GREEN")) {
                    PlayerScoreGreen += diceArrayNumber;
                    setDiceNo(diceArrayNumber);
                    diceMoveCountGreen++;
                } else if (turn.equalsIgnoreCase("YELLOW")) {
                    PlayerScoreYellow += diceArrayNumber;
                    setDiceNo(diceArrayNumber);
                    diceMoveCountYellow++;
                }
//            }
        }

        public void onResume() {
            isPlaying = true;
            gameThread = new Thread(this);
            gameThread.start();
        }

        public void onPause() {
            isPlaying = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void SetMultiPlayerScore(String playerId,int pieceId,int diceArrayCount,int diceArrayNumber,String nestTurn,String nestTurnPlayerId)
    {
        reference = FirebaseDatabase.getInstance().getReference("MultiPlayer").child("Rooms").child("Room001");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("playerId", playerId);
        hashMap.put("pieceId", pieceId);
        hashMap.put("diceArrayCount", diceArrayCount);
        hashMap.put("diceArrayNumber", diceArrayNumber);
        hashMap.put("nestTurn", nestTurn);
        hashMap.put("nestTurnPlayerId", nestTurnPlayerId);
        reference.updateChildren(hashMap);
        isPlay=false;
        if(countDownTimer!=null)
        {
            countDownTimer.cancel();
        }
    }
    boolean isPlay=false;
    private void GetMultiPlayerScore() {
        DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference("MultiPlayer").child("Rooms").child("Room001");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.v("LogLudoActivity","playerId-"+dataSnapshot.child("playerId").getValue().toString()
                        +", pieceId-"+dataSnapshot.child("pieceId").getValue().toString()
                        +", diceArrayCount-"+dataSnapshot.child("diceArrayCount").getValue().toString()
                        +", diceArrayNumber-"+dataSnapshot.child("diceArrayNumber").getValue().toString()
                        +", nestTurn-"+dataSnapshot.child("nestTurn").getValue().toString()
                );

                String playerId=dataSnapshot.child("playerId").getValue().toString();
                String nestTurnPlayerId=dataSnapshot.child("nestTurnPlayerId").getValue().toString();
                int pieceId=Integer.parseInt(""+dataSnapshot.child("pieceId").getValue().toString());
                int diceArrayCount=Integer.parseInt(""+dataSnapshot.child("diceArrayCount").getValue().toString());
                int diceArrayNumber=Integer.parseInt(""+dataSnapshot.child("diceArrayNumber").getValue().toString());
                String nestTurn=dataSnapshot.child("nestTurn").getValue().toString();
                ludoGameView.PlayerIdServer=nestTurnPlayerId;
                String turn="";
                if(nestTurn.equalsIgnoreCase("RED")) {
                    ludoGameView.t = Turn.RED;
                    turn="BLUE";
                }else if(nestTurn.equalsIgnoreCase("BLUE")) {
                    ludoGameView.t = Turn.BLUE;
                    turn="RED";
                }
                if(pieceId==0 && diceArrayCount==0 && diceArrayNumber==0)
                {}else{
                    ludoGameView.MovePlayerPiece(pieceId, diceArrayCount, diceArrayNumber, turn);
                }
                if(UserId.equalsIgnoreCase(playerId)) {
                    isPlay = true;
                    AutoPlay(playerId, nestTurnPlayerId, diceArrayCount, turn);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    int timeCount1=0;
    CountDownTimer countDownTimer=null;
    private void AutoPlay(String playerTwo,String playerOne,int diceArrayCount,String turn) {
//        countDownTimer=new CountDownTimer(10000, 1000) {
//            public void onTick(long millisUntilFinished) {
//                Log.v("jdfdfg","" + millisUntilFinished / 1000);
//            }
//            public void onFinish() {
//                Log.v("jdfdfg","finish");
//
//                if(isPlay)
//                    SetMultiPlayerScore(playerOne,0,diceArrayCount,0,turn,playerTwo);
//
//                countDownTimer.cancel();
//            }
//        };
//        countDownTimer.start();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                timeCount1++;
//                Log.v("bjhsdf","trigger"+timeCount1);
////                if(isPlay)
////                    SetMultiPlayerScore(playerOne,0,diceArrayCount,0,turn,playerTwo);
//
//            }
//        }, 1000);
//        Reminder reminder=new Reminder(5);
//        Log.v("sdkjfgh",""+reminder.getClock());
    }

}