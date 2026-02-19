package com.Incade.cvprogweb.actividades;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootScroll), (v, insets) -> {
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
        for (int i=0; i<habilidades.size(); i++) {
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
        int idAnterior = R.id.finPro; // variable auxiliar con el id del texto dentro del contenedor de proyectos
        for (int i=0; i<proyectos.size(); i++) {
            //  Variable para convertir dp a px
            float density = getResources().getDisplayMetrics().density;

            //  Extraemos los datos de cada proyecto de la lista
            Proyecto proyecto = proyectos.get(i);
            String pro = proyecto.getProyecto();
            String img = proyecto.getImagen();

            //  Buscamos el contenedor de los proyectos
            ConstraintLayout contenedor = findViewById(R.id.proyectos);

            //  Creamos un nuevo contenedor para los datos del proyecto a mostrar
            ConstraintLayout nuevoProyecto = new ConstraintLayout(this);
            nuevoProyecto.setId(View.generateViewId());

            //  1 Convertimos dp a píxeles
            int marginBottomPx = (int) (16 * density);
            int minHeightPx = (int) (100 * density);
            //  2 Creamos los parametros para establecer al contenedor
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_PARENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT);
            //  3 Establecemos los dp previamente convertidos
            params.bottomMargin = marginBottomPx;
            nuevoProyecto.setMinHeight(minHeightPx);
            //  4 Ahora los constraints
            params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            if (i==proyectos.size()-1){ // si es la ultima iteracion
                params.topToBottom = idAnterior; // top pegado al proyecto anterior
                params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID; // bottom al contenedor
            } else {
                params.bottomToTop = nuevoProyecto.getId(); // hacemos que el bottom se pege al top del nuevo proyecto
                findViewById(idAnterior).setLayoutParams(params); // establecemos estos parametros para el objeto anterior
                params.topToBottom = idAnterior; // pegamos el top al objeto anterior
            }
            idAnterior = nuevoProyecto.getId(); // guardamos el id para la proxima iteracion
            //  5 Seteamos los atributos del contenedor
            nuevoProyecto.setLayoutParams(params);

            //  Añadimos el contenedor del nuevo proyecto al contenedor de proyectos
            contenedor.addView(nuevoProyecto);

            //  Ahora creamos el TextView para mostrar
            TextView textoProyecto = new TextView(this);
            textoProyecto.setId(View.generateViewId());
            //  Y establecemos su contenido
            textoProyecto.setText(pro);

            //  Y tambien el ImageView
            ImageView imagenProyecto = new ImageView(this);
            imagenProyecto.setId(View.generateViewId());
            //  Y tambien su contenido con GLIDE
            Glide.with(this)
                    .load(img)
                    .placeholder(R.drawable.ic_launcher_background) // opcional: mientras carga
                    .error(R.drawable.ic_launcher_foreground)       // opcional: si falla la carga
                    .into(imagenProyecto);

            //  Ahora seteamos los parametros de diseño del TextView
            //  1 Convertimos dp a píxeles
            int paddingBottomPx = (int) (8 * density);
            //  2 Seteamos atributos para el TextView
            textoProyecto.setPadding(0,0,0,paddingBottomPx); // Padding
            textoProyecto.setTextColor(Color.parseColor("#D65250B5")); // Color del texto
            textoProyecto.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); // Tamaño del texto
            textoProyecto.setTextAlignment(View.TEXT_ALIGNMENT_CENTER); // Alinecion del texto
            textoProyecto.setShadowLayer( // Sombras
                    5.0f,     // radius
                    0f,       // dx (separacion en el eje x)
                    2.5f,     // dy (separacion en el eje y)
                    Color.parseColor("#A6000000") // color
            );
            //  3 Los constrains del texto
            ConstraintLayout.LayoutParams textParams = new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_PARENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT);
            textParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            textParams.bottomToTop = imagenProyecto.getId();
            //  4 Seteamos los constrains
            textoProyecto.setLayoutParams(textParams);

            //  Ahora seteamos los parametros de diseño del ImageView
            //  1 Convertimos los dp a pixeles
            int widthpx = (int) (250 * density);
            int heightpx = (int) (85 * density);
            //  2 Establecemos el alto y ancho
            ConstraintLayout.LayoutParams imgParams = new ConstraintLayout.LayoutParams(widthpx, heightpx);
            //  3 Establecemos los constraints
            imgParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            imgParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            imgParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            imgParams.topToBottom = textoProyecto.getId();
            //  4 Seteamos los constrains
            imagenProyecto.setLayoutParams(imgParams);
            // 5 atributos adicionales
            imagenProyecto.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imagenProyecto.setContentDescription("Imagen del " + i + "º Proyecto");

            //  Finalmente añadimos el TextView e ImageView al contenedor del nuevo proyecto
            nuevoProyecto.addView(textoProyecto);
            nuevoProyecto.addView(imagenProyecto);
        }

        // Boton para la nueva pantalal de edicion
        Button editBtn = findViewById(R.id.editbtn);

        editBtn.setOnClickListener( v -> {
            Intent intent = new Intent(MainActivity.this, EditActivity.class);

            intent.putExtra("idUsuario", usuario.getId());

            startActivity(intent);
        });
    }
}