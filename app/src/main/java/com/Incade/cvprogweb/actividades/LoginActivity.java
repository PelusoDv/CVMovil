package com.Incade.cvprogweb.actividades;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.Incade.cvprogweb.database.DBCV;
import com.Incade.cvprogweb.modelos.Usuario;
import com.Incade.cvprogweb.recursos.CusToast;
import com.Incade.cvprogweb.R;

public class LoginActivity extends AppCompatActivity {

    // Helper para DB (clase con los métodos CRUD)
    DBCV dbHelper;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DBCV(LoginActivity.this);

        CusToast msgBox = new CusToast();

        // Vistas del layout
        EditText correoEdit = findViewById(R.id.correo);
        EditText passEdit = findViewById(R.id.pass_toggle);
        Button ingresoBtn = findViewById(R.id.ingresar);
        TextView btnRegistro = findViewById(R.id.registrar);

        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.btn3);

        correoEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && correoEdit.getText().toString().equals(getString(R.string.correo))) {
                correoEdit.setText("");
                correoEdit.setTextColor(Color.BLACK);
            } else if (!hasFocus && correoEdit.getText().toString().isBlank()) {
                correoEdit.setText(R.string.correo);
                correoEdit.setTextColor(Color.parseColor("#858585"));
            }
        });

        passEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && passEdit.getText().toString().equals(getString(R.string.pass_toggle))) {
                passEdit.setText("");
                passEdit.setTextColor(Color.BLACK);
            } else if (!hasFocus && passEdit.getText().toString().isBlank()) {
                passEdit.setText(R.string.pass_toggle);
                passEdit.setTextColor(Color.parseColor("#858585"));
            }
        });

        ingresoBtn.setOnClickListener(v -> {
            // Variables para mantener texto ingresado
            String email = correoEdit.getText().toString().trim();
            String contraseña = passEdit.getText().toString().trim();
            // Consultamos localmente si existe un usuario con ese nombre + contraseña
            long idUsuario = dbHelper.comprobarUsuarioLocal(email, contraseña);

            if ((idUsuario != -1)) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                // Agregamos id para la Actividad principal
                intent.putExtra("idUsuario", idUsuario);

                startActivity(intent);
            } else {
                msgBox.showCustomToast(this, "Email o Contraseña Incorrectos!", 1500);
            }
        });

        View.OnClickListener listener = v -> {
            int id = v.getId();
                if (id == R.id.btn1) {
                    msgBox.showCustomToast(this,"Google", 1250);
                }
                if (id == R.id.btn2) {
                    msgBox.showCustomToast(this,"Facebook", 1250);
                }
                if (id == R.id.btn3) {
                    msgBox.showCustomToast(this,"Apple", 1250);
                }
        };

        btn1.setOnClickListener(listener);
        btn2.setOnClickListener(listener);
        btn3.setOnClickListener(listener);

        btnRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
            startActivity(intent);
        });

        if (getIntent().getStringExtra("mensaje") != null){
            msgBox.showCustomToast(this,getIntent().getStringExtra("mensaje"), 1500);
        }
    }
}