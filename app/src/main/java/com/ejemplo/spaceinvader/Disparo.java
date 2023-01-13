package com.ejemplo.spaceinvader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Disparo {
    Bitmap shot;
    Context context;
    int shx, shy;

    public Disparo(Context context, int shx, int shy) {
        this.context = context;
        shot = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.shot);
        this.shx = shx;
        this.shy = shy;
    }
    public Bitmap getShot(){
        return shot;
    }
    public int getShotWidth() {
        return shot.getWidth();
    }
    public int getShotHeight() {
        return shot.getHeight();
    }
}
