package com.ejemplo.spaceinvader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class NuestraNave {
    Context context;
    Bitmap ourSpaceship;
    int ox, oy;
    Random random;

    public NuestraNave(Context context) {
        this.context = context;
        ourSpaceship = BitmapFactory.decodeResource(context.getResources(), R.drawable.rocket1);
        random = new Random();
        ox = random.nextInt(SpaceInvader.anchoPantalla);
        oy = SpaceInvader.altoPantalla - ourSpaceship.getHeight();
    }

    public Bitmap getOurSpaceship(){
        return ourSpaceship;
    }

    int getOurSpaceshipWidth(){
        return ourSpaceship.getWidth();
    }
}
