package com.Incade.cvprogweb.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.Incade.cvprogweb.R;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_carga);

        var logo = findViewById(R.id.logo);
        var animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        logo.startAnimation(animation);

        // Mostrar el splash 2 segundos
        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }, 2250);
    }
}
