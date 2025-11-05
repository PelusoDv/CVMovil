package com.Incade.cvprogweb.actividades;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.Incade.cvprogweb.R;
import com.Incade.cvprogweb.database.DBCV;
import com.Incade.cvprogweb.modelos.Usuario;
import com.Incade.cvprogweb.recursos.CusToast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RegistroActivity extends AppCompatActivity {

    // Helper para DB (clase con los métodos CRUD)
    DBCV dbHelper;
    byte[] imgPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DBCV(RegistroActivity.this);

        CusToast msgBox = new CusToast();
        Button back = findViewById(R.id.volver);
        Button registro = findViewById(R.id.registrar);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        Button upload = findViewById(R.id.imgbtn);

        upload.setOnClickListener(v -> {
            //seguir desde aca, buscar como cargar una imagen de perfil a la base de datos
            Intent data = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            data.addCategory(Intent.CATEGORY_OPENABLE);
            data.setType("image/*");
            startActivityForResult(data, 100);
        });

        EditText email = findViewById(R.id.correo);
        EditText password = findViewById(R.id.password);
        EditText nombre = findViewById(R.id.name);
        EditText titulo = findViewById(R.id.title);

        Usuario user = new Usuario();

        registro.setOnClickListener(v -> {
            if (email.getText().toString().isBlank()) {
                msgBox.showCustomToast(this,"El correo no debe estar vacio", 1500);
            } else {
                user.setEmail(email.getText().toString().trim());
            }
            if (password.getText().toString().isBlank()) {
                msgBox.showCustomToast(this,"La contraseña no debe estar vacia", 1500);
            } else {
                user.setPassword(password.getText().toString().trim());
            }
            if (nombre.getText().toString().isBlank()) {
                msgBox.showCustomToast(this,"El nombre no debe estar vacio", 1500);
            } else {
                user.setNombre(nombre.getText().toString().trim());
            }
            if (titulo.getText().toString().isBlank()) {
                msgBox.showCustomToast(this,"El titulo no debe estar vacio", 1500);
            } else {
                user.setTitulo(titulo.getText().toString().trim());
            }
            if (imgPerfil != null) {
                user.setProfile_img(imgPerfil);
            } else {
                msgBox.showCustomToast(this, "Seleccione una imagen de perfil", 1500);
            }

            if (user.getEmail() != null || user.getPassword() != null || user.getNombre() != null || user.getTitulo() != null || user.getProfile_img() != null) {
                dbHelper.addUser(user);
                Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                intent.putExtra("mensaje", "Usuario creado!");
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                // Este bloque es para redimencionar la imagen que se va a cargar --------------------------------------
                int maxSize = 500; // ancho o alto máximo en px
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

                float scale = (float) maxSize / Math.max(width, height);
                int scaledWidth = Math.round(scale * width);
                int scaledHeight = Math.round(scale * height);

                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
                // De esta manera se almacena una imagen mas liviana que no afecte el cache de android al cargar un usuario

                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                imgPerfil = stream.toByteArray();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}