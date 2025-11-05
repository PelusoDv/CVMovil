package com.Incade.cvprogweb.actividades;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.Incade.cvprogweb.R;
import com.Incade.cvprogweb.database.DBCV;
import com.Incade.cvprogweb.modelos.Habilidad;
import com.Incade.cvprogweb.modelos.Proyecto;
import com.Incade.cvprogweb.modelos.Usuario;
import com.bumptech.glide.Glide;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Helper para DB (clase con los métodos CRUD)
    DBCV dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DBCV(MainActivity.this);

        long idUsuario = getIntent().getLongExtra("idUsuario", -1);

        Usuario usuario = dbHelper.getUsuario(idUsuario);

        // Con los datos del usuario establecemos los valores de header
        TextView nombre = findViewById(R.id.nombre);
        nombre.setText(usuario.getNombre());
        TextView titulo = findViewById(R.id.titulo);
        titulo.setText(usuario.getTitulo());

        // Establecemos tambien la foto de perfil
        Bitmap profile_img = BitmapFactory.decodeByteArray(usuario.getProfile_img(), 0, usuario.getProfile_img().length);
        ImageView fotoPerfil = findViewById(R.id.profile_img);
        if (profile_img != null) {
            fotoPerfil.setImageBitmap(profile_img);
        } else {
            fotoPerfil.setImageResource(R.drawable.ic_launcher_foreground); // una imagen por defecto
        }

        // Buscamos las habilidades del usuario segun id
        List<String> habilidades = dbHelper.habilidades(usuario.getId());
        // Recorremos cada habilidad en la lista con un for
        for (int i=0; i <habilidades.size(); i++) {
            //  Extraemos cada habilidad de la lista
            String habilidad = habilidades.get(i);
            // Determinamos el nombre del ID de cada TextView en el layout
            String textViewName = "habilidad" + (i+1);
            // Obtenemos el ID real del recurso
            int resId = getResources().getIdentifier(textViewName, "id", getPackageName());
            // Buscamos el TextView correspondiente
            TextView textView = findViewById(resId);
            // Si el TextView existe, establecemos el texto
            if (textView != null) {
                textView.setText(habilidad);
            }
        }

        // Buscamos los proyectos del usuario segun id
        List<Proyecto> proyectos = dbHelper.proyectos(usuario.getId());
        // Recorremos cada proyecto en la lista con un for
        for (int i=0; i<proyectos.size(); i++) {
            //  Extraemos los datos de cada proyecto de la lista
            Proyecto proyecto = proyectos.get(i);
            String pro = proyecto.getProyecto();
            String img = proyecto.getImagen();
            // Determinamos el nombre del ID del TextView y el ImgeView en el layout
            String textViewName = "proyecto" + (i+1) + "text";
            String imageViewName = "imgProyecto" + (i+1);
            // Obtenemos el ID real de los recursos
            int textId = getResources().getIdentifier(textViewName,"id", getPackageName());
            int imgId = getResources().getIdentifier(imageViewName, "id", getPackageName());
            // Buscamos los elementos correspondientes
            TextView text = findViewById(textId);
            ImageView imagen = findViewById(imgId);
            // Si el TextView existe, establecemos el texto
            if (text != null) {
                text.setText(pro);
            }
            // Si el ImageView existe, establecemos la imagen con la url usando GLIDE
            if (imagen != null) {
                Glide.with(this)
                        .load(img)
                        .placeholder(R.drawable.ic_launcher_background) // opcional: mientras carga
                        .error(R.drawable.ic_launcher_foreground)       // opcional: si falla la carga
                        .into(imagen);
            }
        }
    }
}