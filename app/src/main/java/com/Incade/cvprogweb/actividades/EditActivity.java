package com.Incade.cvprogweb.actividades;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.Incade.cvprogweb.R;
import com.Incade.cvprogweb.database.DBCV;
import com.Incade.cvprogweb.modelos.Habilidad;
import com.Incade.cvprogweb.modelos.Proyecto;
import com.Incade.cvprogweb.modelos.Usuario;
import com.Incade.cvprogweb.recursos.CusToast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    DBCV dbHelper;

    byte[] imgPerfil;
    private List<Proyecto> listaProyectos = new ArrayList<>();

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

        CusToast msgBox = new CusToast();
        dbHelper = new DBCV(EditActivity.this);
        long idUsuario = getIntent().getLongExtra("idUsuario", -1);

        Usuario usuario = dbHelper.getUsuario(idUsuario);
        List<Habilidad> habilidades = dbHelper.habilidades(usuario.getId());
        if (habilidades == null) {
            habilidades = new ArrayList<>();
        }
        List<Proyecto> proyectos = dbHelper.proyectos(usuario.getId());

        // Mostramos los datos originales en los textbox
        EditText editName = findViewById(R.id.name);
        editName.setText(usuario.getNombre());
        EditText editTitle = findViewById(R.id.title);
        editTitle.setText(usuario.getTitulo());

        // Recorremos cada habilidad en la lista con un for
        for (int i = 0; i < habilidades.size(); i++) {
            //  Extraemos cada habilidad de la lista
            String habilidad = habilidades.get(i).getHabilidad();
            // Determinamos el nombre del ID de cada TextView en el layout
            String textViewName = "habilidad" + (i + 1);
            // Obtenemos el ID real del recurso
            int resId = getResources().getIdentifier(textViewName, "id", getPackageName());
            // Buscamos el EditText correspondiente
            TextView textView = findViewById(resId);
            // Establecemos el texto
            textView.setText(habilidad);
        }

        //  Variable para convertir dp a px
        float density = getResources().getDisplayMetrics().density;
        // Recorremos cada proyecto en la lista con un for
        for (int i = 0; i < proyectos.size(); i++) {
            //  Extraemos los datos de cada proyecto de la lista
            Proyecto proyecto = proyectos.get(i);
            String pro = proyecto.getProyecto();
            String img = proyecto.getImagen();

            //  Buscamos el contenedor de los proyectos
            LinearLayout contenedor = findViewById(R.id.proyectos);

            //  Creamos un nuevo contenedor para los datos del proyecto a mostrar
            LinearLayout nuevoProyecto = new LinearLayout(this);
            nuevoProyecto.setOrientation(LinearLayout.VERTICAL);

            //  1 Convertimos dp a píxeles
            int marginBottomPx = (int) (8 * density);
            //  2 Creamos los parametros para establecer al contenedor
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            //  3 Ahora los constraints
            params.bottomMargin = marginBottomPx;

            //  4 Seteamos los atributos del contenedor
            nuevoProyecto.setLayoutParams(params);

            //  Añadimos el contenedor del nuevo proyecto al contenedor de proyectos
            contenedor.addView(nuevoProyecto);

             //  1 Ahora creamos el EditText donde se muestra el titulo del proyecto
            EditText textoProyecto = new EditText(this);
            //  2 Tambien el EditText donde se muestra la URL de la imagen
            EditText imagenProyecto = new EditText(this);
            //  3 Y establecemos su contenido
            imagenProyecto.setText(img);
            textoProyecto.setText(pro);
            //  4 Tambien creamos un par de TextView para indicar a cada uno
            TextView tituloProyecto = new TextView(this);
            TextView urlProyecto = new TextView(this);
            //  5 Y creamos un boton para borrar proyectos
            Button deleteBtn = new Button(this);
            //  6 Almacenamos datos de los campos para mas adelante
            Proyecto nuevo = new Proyecto();
            nuevo.setId(proyecto.getId()); // importante
            nuevo.setProyecto(textoProyecto.getText().toString().trim());
            nuevo.setImagen(imagenProyecto.getText().toString().trim());

            listaProyectos.add(nuevo);

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
            textoProyecto.setTextColor(Color.parseColor("#858585"));
            imagenProyecto.setTextColor(Color.parseColor("#858585"));
            textoProyecto.setInputType(InputType.TYPE_CLASS_TEXT);
            imagenProyecto.setInputType(InputType.TYPE_CLASS_TEXT);
            //  3 Los constraints del texto
            LinearLayout.LayoutParams proyectParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            proyectParams.topMargin = marginVertical;
            //  4 los constraints de la imagen
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            imageParams.topMargin = marginVertical;
            //  5 Seteamos atributos de los TextView
            tituloProyecto.setTextColor(Color.parseColor("#5C5C5C"));
            urlProyecto.setTextColor(Color.parseColor("#5C5C5C"));
            tituloProyecto.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            urlProyecto.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tituloProyecto.setText("Titulo del proyecto:");
            urlProyecto.setText("URL de imagen del proyecto:");
            // 6 Los constraints del titulo
            LinearLayout.LayoutParams tituloParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            tituloParams.topMargin = marginVertical;
            // 6 Los constraints de la URL
            LinearLayout.LayoutParams urlParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            urlParams.topMargin = marginVertical;
            //  7 Ahora seteamos todos los layouts
            textoProyecto.setLayoutParams(proyectParams);
            imagenProyecto.setLayoutParams(imageParams);
            tituloProyecto.setLayoutParams(tituloParams);
            urlProyecto.setLayoutParams(urlParams);
            // 8 Los constraints del boton
            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    (int) (38 * density));
            btnParams.topMargin = marginVertical;
            deleteBtn.setLayoutParams(btnParams);
            // 9 los demas parametros del btn
            deleteBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.button_selector));
            deleteBtn.setBackgroundTintList(null);
            deleteBtn.setText("Borrar Proyecto");
            deleteBtn.setTextColor(Color.parseColor("#FFFFFF"));
            deleteBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            deleteBtn.setAllCaps(false);
            deleteBtn.setPadding(paddingHoriz, paddingVertical, paddingHoriz, paddingVertical);
            deleteBtn.setOnClickListener(v -> {
                //ConstraintLayout proyect = (ConstraintLayout) v.getParent();
                dbHelper.deleteProyecto(proyecto.getId());
                proyectos.remove(proyecto);
                listaProyectos.remove(nuevo);
                contenedor.removeView(nuevoProyecto);
            });

            //  Finalmente añadimos todos al contenedor del proyecto
            nuevoProyecto.addView(tituloProyecto);
            nuevoProyecto.addView(textoProyecto);
            nuevoProyecto.addView(urlProyecto);
            nuevoProyecto.addView(imagenProyecto);
            nuevoProyecto.addView(deleteBtn);
        }

        Button upload = findViewById(R.id.editProfile);
        Button addProyect = findViewById(R.id.addProyects);
        Button save = findViewById(R.id.save);

        upload.setOnClickListener(v -> {
            //seguir desde aca, buscar como cargar una imagen de perfil a la base de datos
            Intent data = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            data.addCategory(Intent.CATEGORY_OPENABLE);
            data.setType("image/*");
            startActivityForResult(data, 100);
        });

        addProyect.setOnClickListener(v -> {
            //  Buscamos el contenedor de los proyectos
            LinearLayout contenedor = findViewById(R.id.proyectosNuevos);

            //  Creamos un nuevo contenedor para los datos del proyecto a mostrar
            LinearLayout nuevoProyecto = new LinearLayout(this);
            nuevoProyecto.setOrientation(LinearLayout.VERTICAL);

            //  1 Convertimos dp a píxeles
            int marginBottomPx = (int) (8 * density);
            int minHeightPx = (int) (100 * density);

            //  2 Creamos los parametros para establecer al contenedor
            LinearLayout.LayoutParams paramsNuevo = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            //  3 Establecemos los dp previamente convertidos
            paramsNuevo.bottomMargin = marginBottomPx;
            //  5 Seteamos los atributos del contenedor
            nuevoProyecto.setLayoutParams(paramsNuevo);

            //  Añadimos el contenedor del nuevo proyecto al contenedor de proyectos
            contenedor.addView(nuevoProyecto);

            //  1 Ahora creamos el EditText donde se muestra el titulo del proyecto
            EditText textoProyecto = new EditText(this);
            //  2 Tambien el EditText donde se muestra la URL de la imagen
            EditText imagenProyecto = new EditText(this);
            //  3 Tambien creamos un par de TextView para indicar a cada uno
            TextView tituloProyecto = new TextView(this);
            TextView urlProyecto = new TextView(this);
            //  4 Y creamos un boton para borrar proyectos
            Button deleteBtn = new Button(this);
            //  5 Almacenamos datos de los campos para mas adelante
            Proyecto nuevo = new Proyecto();
            nuevo.setId(-1); // nuevo proyecto
            nuevo.setProyecto(textoProyecto.getText().toString().trim());
            nuevo.setImagen(imagenProyecto.getText().toString().trim());

            listaProyectos.add(nuevo);

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
            textoProyecto.setTextColor(Color.parseColor("#858585"));
            imagenProyecto.setTextColor(Color.parseColor("#858585"));
            textoProyecto.setInputType(InputType.TYPE_CLASS_TEXT);
            imagenProyecto.setInputType(InputType.TYPE_CLASS_TEXT);
            //  3 Los constraints del texto
            LinearLayout.LayoutParams proyectParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            proyectParams.topMargin = marginVertical;
            //  4 los constraints de la imagen
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            imageParams.topMargin = marginVertical;
            //  5 Seteamos atributos de los TextView
            tituloProyecto.setTextColor(Color.parseColor("#5C5C5C"));
            urlProyecto.setTextColor(Color.parseColor("#5C5C5C"));
            tituloProyecto.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            urlProyecto.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tituloProyecto.setText("Titulo del proyecto:");
            urlProyecto.setText("URL de imagen del proyecto:");
            // 6 Los constraints del titulo
            LinearLayout.LayoutParams tituloParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            tituloParams.topMargin = marginVertical;
            // 6 Los constraints de la URL
            LinearLayout.LayoutParams urlParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            urlParams.topMargin = marginVertical;
            //  7 Ahora seteamos todos los layouts
            textoProyecto.setLayoutParams(proyectParams);
            imagenProyecto.setLayoutParams(imageParams);
            tituloProyecto.setLayoutParams(tituloParams);
            urlProyecto.setLayoutParams(urlParams);
            // 8 Los constraints del boton
            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    (int) (38 * density));
            btnParams.topMargin = marginVertical;
            deleteBtn.setLayoutParams(btnParams);
            // 9 los demas parametros del btn
            deleteBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.button_selector));
            deleteBtn.setBackgroundTintList(null);
            deleteBtn.setText("Borrar Proyecto");
            deleteBtn.setTextColor(Color.parseColor("#FFFFFF"));
            deleteBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            deleteBtn.setAllCaps(false);
            deleteBtn.setPadding(paddingHoriz, paddingVertical, paddingHoriz, paddingVertical);
            deleteBtn.setOnClickListener(e -> {
                listaProyectos.remove(nuevo);
                contenedor.removeView(nuevoProyecto);
            });

            //  Finalmente añadimos todos al contenedor del proyecto
            nuevoProyecto.addView(tituloProyecto);
            nuevoProyecto.addView(textoProyecto);
            nuevoProyecto.addView(urlProyecto);
            nuevoProyecto.addView(imagenProyecto);
            nuevoProyecto.addView(deleteBtn);
        });

        save.setOnClickListener(v -> {
            if (editName.getText().toString().isBlank()) {
                msgBox.showCustomToast(this, "El nombre no debe estar vacio", 1500);
                return;
            } else {
                usuario.setNombre(editName.getText().toString().trim());
            }
            if (editTitle.getText().toString().isBlank()) {
                msgBox.showCustomToast(this, "El titulo no debe estar vacio", 1500);
                return;
            } else {
                usuario.setTitulo(editTitle.getText().toString().trim());
            }
            if (imgPerfil != null) {
                usuario.setProfile_img(imgPerfil);
            }

            List<String> nuevasHabilidades = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                // Determinamos el nombre del ID de cada TextView en el layout
                String textViewName = "habilidad" + (i + 1);
                // Obtenemos el ID real del recurso
                int resId = getResources().getIdentifier(textViewName, "id", getPackageName());
                // Buscamos el TextView correspondiente
                TextView habilidad = findViewById(resId);
                if (!habilidad.getText().toString().isBlank()) {
                    nuevasHabilidades.add(habilidad.getText().toString().trim());
                }
            }

            boolean proyectosOK = true;
            for (Proyecto proyecto : listaProyectos) {

                if (proyecto.getProyecto().isBlank()) {
                    continue; // no guardamos proyectos vacíos
                }

                if (proyecto.getId() == -1) {
                    long generado = dbHelper.addProyecto(idUsuario, proyecto);
                    Log.d("SAVE", "Insert resultado: " + generado);
                    if (generado == -1) proyectosOK = false;
                } else {
                    boolean actualizado = dbHelper.updateProyecto(proyecto.getId(), proyecto);
                    Log.d("SAVE", "Update id " + proyecto.getId() + ": " + actualizado);
                    if (!actualizado) proyectosOK = false;
                }
            }

            boolean datosActualizados = dbHelper.updateUserData(idUsuario, usuario);
            boolean habilidadesActualizadas = dbHelper.replaceHabilidades(idUsuario, nuevasHabilidades);

            if (datosActualizados && habilidadesActualizadas && proyectosOK) {
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                intent.putExtra("mensaje", "Datos actualizados!");
                intent.putExtra("idUsuario", usuario.getId());
                startActivity(intent);
                finish();
            } else {
                msgBox.showCustomToast(this,"Error al guardar datos", 1500);
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