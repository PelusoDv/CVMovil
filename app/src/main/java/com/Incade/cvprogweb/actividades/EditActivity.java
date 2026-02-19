package com.Incade.cvprogweb.actividades;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.Incade.cvprogweb.R;
import com.Incade.cvprogweb.database.DBCV;
import com.Incade.cvprogweb.modelos.Proyecto;
import com.Incade.cvprogweb.modelos.Usuario;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    DBCV dbHelper;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootScroll), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DBCV(EditActivity.this);

        long idUsuario = getIntent().getLongExtra("idUsuario", -1);

        Usuario usuario = dbHelper.getUsuario(idUsuario);
        List<String> habilidades = dbHelper.habilidades(usuario.getId());
        List<Proyecto> proyectos = dbHelper.proyectos(usuario.getId());

        // Mostramos los datos originales en los textbox
        EditText editName = findViewById(R.id.name);
        editName.setText(usuario.getNombre());
        EditText editTitle = findViewById(R.id.title);
        editTitle.setText(usuario.getTitulo());

        // Recorremos cada habilidad en la lista con un for
        for (int i=0; i<habilidades.size(); i++) {
            //  Extraemos cada habilidad de la lista
            String habilidad = habilidades.get(i);
            // Determinamos el nombre del ID de cada TextView en el layout
            String textViewName = "habilidad" + (i+1);
            // Obtenemos el ID real del recurso
            int resId = getResources().getIdentifier(textViewName, "id", getPackageName());
            // Buscamos el TextView correspondiente
            EditText editText = findViewById(resId);
            // Si el TextView existe, establecemos el texto
            if (editText != null) {
                editText.setText(habilidad);
            }
        }

        // Recorremos cada proyecto en la lista con un for
        int idAnterior = R.id.proyectosHead; // variable auxiliar con el id del texto dentro del contenedor de proyectos
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

            //  1 Ahora creamos el EditText donde se muestra el titulo del proyecto
            EditText textoProyecto = new EditText(this);
            textoProyecto.setId(View.generateViewId());
            //  2 Tambien el EditText donde se muestra la URL de la imagen
            EditText imagenProyecto = new EditText(this);
            imagenProyecto.setId(View.generateViewId());
            //  3 Y establecemos su contenido
            imagenProyecto.setText(img);
            textoProyecto.setText(pro);
            //  4 Tambien creamos un par de TextView para indicar a cada uno
            TextView tituloProyecto = new TextView(this);
            tituloProyecto.setId(View.generateViewId());
            TextView urlProyecto = new TextView(this);
            urlProyecto.setId(View.generateViewId());
            //  5 Y creamos un boton para borrar proyectos
            Button deleteBtn = new Button(this);
            deleteBtn.setId(View.generateViewId());
            //  6 Almacenamos los IDs de los botones generados en una lista para mas adelante
            List<Integer> deleteBtnIds = new ArrayList<>();
            deleteBtnIds.add(i, deleteBtn.getId());

            //  Ahora seteamos los parametros de los EditText
            //  1 Convertimos dp a píxeles
            int marginVertical = (int) (4 * density + 0.5f);
            int paddingVertical = (int) (2 * density);
            int paddingHoriz = (int) (8 * density);
            // 2 Seteamos atributos de los EditText
            textoProyecto.setPadding(paddingHoriz, paddingVertical, paddingHoriz, paddingVertical);
            imagenProyecto.setPadding(paddingHoriz, paddingVertical, paddingHoriz, paddingVertical);
            textoProyecto.setBackgroundResource(R.drawable.input);
            imagenProyecto.setBackgroundResource(R.drawable.input);
            textoProyecto.setInputType(InputType.TYPE_CLASS_TEXT);
            imagenProyecto.setInputType(InputType.TYPE_CLASS_TEXT);
            //  3 Los constraints del texto
            ConstraintLayout.LayoutParams proyectParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            proyectParams.setMargins(0, marginVertical, 0, 0);
            proyectParams.topToBottom = tituloProyecto.getId();
            proyectParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            proyectParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            proyectParams.bottomToTop = urlProyecto.getId();
            //  4 los constraints de la imagen
            ConstraintLayout.LayoutParams imageParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            imageParams.setMargins(0, marginVertical, 0, 0);
            imageParams.topToBottom = urlProyecto.getId();
            imageParams.bottomToTop = deleteBtn.getId();
            imageParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            imageParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            //  5 Seteamos atributos de los TextView
            tituloProyecto.setTextColor(Color.parseColor("#5C5C5C"));
            urlProyecto.setTextColor(Color.parseColor("#5C5C5C"));
            tituloProyecto.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            urlProyecto.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tituloProyecto.setText("Titulo del proyecto:");
            urlProyecto.setText("URL de imagen del proyecto:");
            // 6 Los constraints del titulo
            ConstraintLayout.LayoutParams tituloParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            tituloParams.setMargins(0, marginVertical, 0, 0);
            tituloParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            tituloParams.bottomToTop = textoProyecto.getId();
            tituloParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            tituloParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            // 6 Los constraints de la URL
            ConstraintLayout.LayoutParams urlParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            urlParams.setMargins(0, marginVertical, 0, 0);
            urlParams.topToBottom = textoProyecto.getId();
            urlParams.bottomToTop = imagenProyecto.getId();
            urlParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            urlParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            //  7 Ahora seteamos todos los layouts
            textoProyecto.setLayoutParams(proyectParams);
            imagenProyecto.setLayoutParams(imageParams);
            tituloProyecto.setLayoutParams(tituloParams);
            urlProyecto.setLayoutParams(urlParams);
            // 8 Los constraints del boton
            ConstraintLayout.LayoutParams btnParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    (int) (38 * density));
            btnParams.topToBottom = imagenProyecto.getId();
            btnParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            // btnParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            btnParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            btnParams.setMargins(0, marginVertical, 0, 0);
            deleteBtn.setLayoutParams(btnParams);
            // 9 los demas parametros del btn
            deleteBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.button_selector));
            deleteBtn.setBackgroundTintList(null);
            deleteBtn.setText("Borrar Proyecto");
            deleteBtn.setTextColor(Color.parseColor("#FFFFFF"));
            deleteBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            deleteBtn.setAllCaps(false);
            deleteBtn.setPadding(paddingHoriz, paddingVertical, paddingHoriz, paddingVertical);

            //  Finalmente añadimos todos al contenedor del proyecto
            nuevoProyecto.addView(tituloProyecto);
            nuevoProyecto.addView(textoProyecto);
            nuevoProyecto.addView(urlProyecto);
            nuevoProyecto.addView(imagenProyecto);
            nuevoProyecto.addView(deleteBtn);
        }
    }
}