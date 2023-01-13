package com.ejemplo.spaceinvader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class SpaceInvader extends View {
    Context context;
    Bitmap imagenFondo, imagenVidas;
    Handler handler;
    long MILLIS_ACTUALIZACION = 30;
    static int anchoPantalla, altoPantalla;
    int puntos = 0;
    int vidas = 3;
    Paint dibujoPuntuacion;
    int TAMANO_TEXTO = 80;
    boolean en_pausa = false;
    NuestraNave nuestraNave;
    OvniEnemigo ovniEnemigo;
    Random aleatorio;
    ArrayList<Disparo> disparosOvni, nuestrosDisparos;
    Explosion explosion;
    ArrayList<Explosion> explosiones;
    boolean enemigoDisparando = false;

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
           invalidate();
        }
    };

    public SpaceInvader(Context context) {
        super(context);
        this.context = context;
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        anchoPantalla = size.x;
        altoPantalla = size.y;
        aleatorio = new Random();
        disparosOvni = new ArrayList<>();
        nuestrosDisparos = new ArrayList<>();
        explosiones = new ArrayList<>();
        nuestraNave = new NuestraNave(context);
        ovniEnemigo = new OvniEnemigo(context);
        handler = new Handler();
        imagenFondo = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        imagenVidas = BitmapFactory.decodeResource(context.getResources(), R.drawable.life);
        dibujoPuntuacion = new Paint();
        dibujoPuntuacion.setColor(Color.RED);
        dibujoPuntuacion.setTextSize(TAMANO_TEXTO);
        dibujoPuntuacion.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw background, Points and life on Canvas
        canvas.drawBitmap(imagenFondo, 0, 0, null);
        canvas.drawText("Pt: " + puntos, 0, TAMANO_TEXTO, dibujoPuntuacion);
        for(int i = vidas; i>=1; i--){
            canvas.drawBitmap(imagenVidas, anchoPantalla - imagenVidas.getWidth() * i, 0, null);
        }
        // When life becomes 0, stop game and launch GameOver Activity with points
        if(vidas == 0){
            en_pausa = true;
            handler = null;
            Intent intent = new Intent(context, GameOver.class);
            intent.putExtra("puntos", puntos);
            context.startActivity(intent);
            ((Activity) context).finish();
        }
        // Move enemySpaceship
        ovniEnemigo.ex += ovniEnemigo.enemyVelocity;
        // If enemySpaceship collides with right wall, reverse enemyVelocity
        if(ovniEnemigo.ex + ovniEnemigo.getEnemySpaceshipWidth() >= anchoPantalla){
            ovniEnemigo.enemyVelocity *= -1;
        }
        // If enemySpaceship collides with left wall, again reverse enemyVelocity
        if(ovniEnemigo.ex <=0){
            ovniEnemigo.enemyVelocity *= -1;
        }
        // Till enemyShotAction is false, enemy should fire shots from random travelled distance
        if(enemigoDisparando == false){
            if(ovniEnemigo.ex >= 200 + aleatorio.nextInt(400)){
                Disparo enemyShot = new Disparo(context, ovniEnemigo.ex + ovniEnemigo.getEnemySpaceshipWidth() / 2, ovniEnemigo.ey );
                disparosOvni.add(enemyShot);
                // We're making enemyShotAction to true so that enemy can take a short at a time
                enemigoDisparando = true;
            }
            if(ovniEnemigo.ex >= 400 + aleatorio.nextInt(800)){
                Disparo enemyShot = new Disparo(context, ovniEnemigo.ex + ovniEnemigo.getEnemySpaceshipWidth() / 2, ovniEnemigo.ey );
                disparosOvni.add(enemyShot);
                // We're making enemyShotAction to true so that enemy can take a short at a time
                enemigoDisparando = true;
            }
            else{
                Disparo enemyShot = new Disparo(context, ovniEnemigo.ex + ovniEnemigo.getEnemySpaceshipWidth() / 2, ovniEnemigo.ey );
                disparosOvni.add(enemyShot);
                // We're making enemyShotAction to true so that enemy can take a short at a time
                enemigoDisparando = true;
            }
        }
        // Draw the enemy Spaceship
        canvas.drawBitmap(ovniEnemigo.getEnemySpaceship(), ovniEnemigo.ex, ovniEnemigo.ey, null);
        // Draw our spaceship between the left and right edge of the screen
        if(nuestraNave.ox > anchoPantalla - nuestraNave.getOurSpaceshipWidth()){
            nuestraNave.ox = anchoPantalla - nuestraNave.getOurSpaceshipWidth();
        }else if(nuestraNave.ox < 0){
            nuestraNave.ox = 0;
        }
        // Draw our Spaceship
        canvas.drawBitmap(nuestraNave.getOurSpaceship(), nuestraNave.ox, nuestraNave.oy, null);
        // Draw the enemy shot downwards our spaceship and if it's being hit, decrement life, remove
        // the shot object from enemyShots ArrayList and show an explosion.
        // Else if, it goes away through the bottom edge of the screen also remove
        // the shot object from enemyShots.
        // When there is no enemyShots no the screen, change enemyShotAction to false, so that enemy
        // can shot.
        for(int i = 0; i < disparosOvni.size(); i++){
            disparosOvni.get(i).shy += 15;
            canvas.drawBitmap(disparosOvni.get(i).getShot(), disparosOvni.get(i).shx, disparosOvni.get(i).shy, null);
            if((disparosOvni.get(i).shx >= nuestraNave.ox)
                && disparosOvni.get(i).shx <= nuestraNave.ox + nuestraNave.getOurSpaceshipWidth()
                && disparosOvni.get(i).shy >= nuestraNave.oy
                && disparosOvni.get(i).shy <= altoPantalla){
                vidas--;
                disparosOvni.remove(i);
                explosion = new Explosion(context, nuestraNave.ox, nuestraNave.oy);
                explosiones.add(explosion);
            }else if(disparosOvni.get(i).shy >= altoPantalla){
                disparosOvni.remove(i);
            }
            if(disparosOvni.size() < 1){
                enemigoDisparando = false;
            }
        }
        // Draw our spaceship shots towards the enemy. If there is a collision between our shot and enemy
        // spaceship, increment points, remove the shot from ourShots and create a new Explosion object.
        // Else if, our shot goes away through the top edge of the screen also remove
        // the shot object from enemyShots ArrayList.
        for(int i = 0; i < nuestrosDisparos.size(); i++){
            nuestrosDisparos.get(i).shy -= 15;
            canvas.drawBitmap(nuestrosDisparos.get(i).getShot(), nuestrosDisparos.get(i).shx, nuestrosDisparos.get(i).shy, null);
            if((nuestrosDisparos.get(i).shx >= ovniEnemigo.ex)
               && nuestrosDisparos.get(i).shx <= ovniEnemigo.ex + ovniEnemigo.getEnemySpaceshipWidth()
               && nuestrosDisparos.get(i).shy <= ovniEnemigo.getEnemySpaceshipWidth()
               && nuestrosDisparos.get(i).shy >= ovniEnemigo.ey){
                puntos++;
                nuestrosDisparos.remove(i);
                explosion = new Explosion(context, ovniEnemigo.ex, ovniEnemigo.ey);
                explosiones.add(explosion);
            }else if(nuestrosDisparos.get(i).shy <=0){
                nuestrosDisparos.remove(i);
            }
        }
        // Do the explosion
        for(int i = 0; i < explosiones.size(); i++){
            canvas.drawBitmap(explosiones.get(i).getExplosion(explosiones.get(i).explosionFrame), explosiones.get(i).eX, explosiones.get(i).eY, null);
            explosiones.get(i).explosionFrame++;
            if(explosiones.get(i).explosionFrame > 8){
                explosiones.remove(i);
            }
        }
        // If not paused, we’ll call the postDelayed() method on handler object which will cause the
        // run method inside Runnable to be executed after 30 milliseconds, that is the value inside
        // UPDATE_MILLIS.
        if(!en_pausa)
            handler.postDelayed(runnable, MILLIS_ACTUALIZACION);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchX = (int)event.getX();
        // When event.getAction() is MotionEvent.ACTION_UP, if ourShots arraylist size < 1,
        // create a new Shot.
        // This way we restrict ourselves of making just one shot at a time, on the screen.
        if(event.getAction() == MotionEvent.ACTION_UP){
            if(nuestrosDisparos.size() < 1){
                Disparo ourShot = new Disparo(context, nuestraNave.ox + nuestraNave.getOurSpaceshipWidth() / 2, nuestraNave.oy);
                nuestrosDisparos.add(ourShot);
            }
        }
        // When event.getAction() is MotionEvent.ACTION_DOWN, control ourSpaceship
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            nuestraNave.ox = touchX;
        }
        // When event.getAction() is MotionEvent.ACTION_MOVE, control ourSpaceship
        // along with the touch.
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            nuestraNave.ox = touchX;
        }
        // Returning true in an onTouchEvent() tells Android system that you already handled
        // the touch event and no further handling is required.
        return true;
    }
}
