package com.example.indianludobattle.Activity.Ludo.Characters.Ludo.Player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import com.example.indianludobattle.Activity.Ludo.Characters.Arrows;
import com.example.indianludobattle.Activity.Ludo.Characters.Ludo.Piece.Piece;
import com.example.indianludobattle.Activity.Ludo.Characters.PathPostion;
import com.example.indianludobattle.Activity.Ludo.Characters.Position;
import com.example.indianludobattle.Activity.Ludo.LudoActivity;
import com.example.indianludobattle.Activity.Ludo.NewLudoActivity;


/**
 * Created by Win on 24-04-2018.
 */

public class Player3 {

    private int StartIndex,EndIndex;

    private Piece[] pieces;

    private boolean isPass;

    private Arrows[] arrows;

    private boolean isWin = false;

    private String PlayerColor = "";

    private Bitmap bmpPiece;

    private int pieceSize;
    private Context mcontext;

    public Player3(int si, int ei, Position[] piecePos, int pieceSize, Position[] path, String col, Context context) {
        Log.d("LudoActivity","pieceSize : " + pieceSize);
        this.StartIndex = si;
        this.EndIndex = ei;
        this.mcontext = context;

        this.pieceSize = pieceSize;
        PlayerColor = col;
        PathPostion[] romPath = new PathPostion[57];
        if(path == null)
            Log.d("LudoActivity","path is empty");
        else {
            Log.d("LudoActivity", "path is not empty");
            Log.d("LudoActivity","Path count " + path.length);
        }

        for (int i = 0; i < 51; i++)
        {
            Log.d("LudoActivity", " si : " + si + ", i : "+ i + " Total : " + (si + i));
            if(si + i > 51) {
                Log.d("LudoActivity", " si : " + si + ", i : "+ i + " Total - 51 : " + (si + i - 52));
                romPath[i] = NewLudoActivity.romPath[si + i - 52];
            }
            else {
                romPath[i] = NewLudoActivity.romPath[si + i];
            }
        }

        int j = 0;
        for (int i = 51; i < 57; i++) {
            romPath[i] = new PathPostion(path[j].getX(),path[j].getY(),false);
            j++;
        }

        pieces = new Piece[4];

        for (int i = 0; i < piecePos.length; i++)
        {
            pieces[i] = new Piece(piecePos[i].getX(),piecePos[i].getY(), pieceSize, romPath,context);
        }

        arrows = new Arrows[4];

        isPass = false;

        bmpPiece = Bitmap.createBitmap(pieceSize,pieceSize, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpPiece);
        Paint p = new Paint();

        p.setColor(Color.WHITE);
        c.drawCircle(0,0,pieceSize - 4,p);
        p.setColor(Color.WHITE);
        Path traingle = new Path();
        traingle.moveTo(4,pieceSize / 2);
        traingle.lineTo(pieceSize / 2,pieceSize - 8);
        traingle.lineTo(pieceSize - 4,pieceSize / 2);
        traingle.lineTo(4,pieceSize / 2);

        c.drawPath(traingle,p);
        p.setColor(Color.parseColor(col));

        c.drawCircle(0,0,pieceSize - 8,p);
    }
    public Player3(){
        arrows = new Arrows[4];
    }
    public void setArrows(int[] x, int[] y, int size)
    {
        for (int i = 0; i< 4; i++)
        {
            arrows[i] = new Arrows(x[i],y[i],size);
        }
    }

    public void setIndicator(int[] x, int[] y, int size)
    {
        for (int i = 0; i< 4; i++)
        {
            arrows[i] = new Arrows(x[i],y[i],size);
        }
    }

    public Bitmap getBmpPiece()
    {
        return bmpPiece;
    }

    public int getPieceSize()
    {
        return pieceSize;
    }
    public Arrows getArrows(int index)
    {
        return arrows[index];
    }
    public Arrows getIndicator(int index)
    {
        return arrows[index];
    }

    public int getSI()
    {
        return StartIndex;
    }
    public int getEI()
    {
        return EndIndex;
    }

    public boolean checkCanMove(int id,int next) {
        int r = pieces[id].canMove(next);
        if(r == 1)
            return true;
        else if(r == 2)
        {
            if(isPass)
                return  true;
            else
                return false;
        }
        else
            return false;
    }
    public boolean checkCanMoveMe(int id,int next) {
        int r = pieces[id].canMove(next);
        Log.v("jsdfhg","r-"+r);
        Log.v("jsdfhg","isPass-"+isPass);
        if(r == 1) {
            Log.v("jsdfhg","return true 1");
            return true;
        }
        else if(r == 2)
        {

            if(!isPass) {
                Log.v("jsdfhg","return true 2");
                return true;
            }
            else {
                Log.v("jsdfhg","return false 1");
                return false;
            }
        }
        else {
            Log.v("jsdfhg","return false 2");
            return false;
        }
    }
    public void setIsPass()
    {
        isPass = true;
    }
    public boolean getIsPass()
    {
        return isPass;
    }

    public boolean getWin(){return isWin;}
    public void setWin(boolean value){isWin = value;}

    public Piece[] getRedPiecePositions()
    {
        return pieces;
    }
}
