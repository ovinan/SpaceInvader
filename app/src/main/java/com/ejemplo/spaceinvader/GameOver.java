package com.ejemplo.spaceinvader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameOver extends AppCompatActivity {

    TextView tvPuntos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        int puntos = getIntent().getExtras().getInt("puntos");
        tvPuntos = findViewById(R.id.tvPuntos);
        tvPuntos.setText("" + puntos);
    }

    public void restart(View view) {
        Intent intent = new Intent(GameOver.this, Inicio.class);
        startActivity(intent);
        finish();
    }

    public void exit(View view) {
        finish();
    }
}
