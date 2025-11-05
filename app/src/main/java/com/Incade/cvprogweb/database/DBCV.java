package com.Incade.cvprogweb.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.Incade.cvprogweb.modelos.Proyecto;
import com.Incade.cvprogweb.modelos.Usuario;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBCV extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "usuarios.db"; // Este nombre depende de la database guardada en app/src/main/assets/databases.
    private static final int DATABASE_VERSION = 1; // esto casi nunca se cambia

    public DBCV(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = getWritableDatabase();
        if (!checkDatabaseExistence(db)) {
            try (db) {
                copyDatabase(context);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private boolean checkDatabaseExistence(SQLiteDatabase db) {
        String path = db.getPath();
        File file = new File(path);
        return file.exists();
    }
    private void copyDatabase(Context context) throws IOException {
        close();
        OutputStream output = new FileOutputStream(getDatabasePath(context));
        InputStream input = context.getAssets().open("databases/" + DATABASE_NAME);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
        input.close();
        output.flush();
        output.close();
    }
    private String getDatabasePath(Context context) {
        return context.getDatabasePath(DATABASE_NAME).getPath();
    }

    //Aca se declaran nombres de tablas para ahorrar codigo ----------------------------------------//
    private static final String TABLE_USUARIO = "usuarios";
    private static final String TABLE_HABILIDAD = "habilidades";
    private static final String TABLE_PROYECTO = "proyectos";
    private static final String COL_ID = "id";
    private static final String FK_ID = "usuario_id";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";
    private static final String COL_NOMBRE = "nombre";
    private static final String COL_TITULO = "titulo";
    private  static final String COL_IMG_PERF = "profile_img";
    // ---------------------------------------------------------------------------------------------//

    /**
    * comprobarUsuarioLocal:
    * - Busca en la tabla de usuarios un registro que coincida con el email y password.
    * - Si lo encuentra rellena y devuelve un objeto User con los datos.
    * - Si no lo encuentra devuelve un User con id = -1 (indicador "no encontrado").
    */
    public long comprobarUsuarioLocal(String email, String password) {
        long userId = -1; // default "not found" indicator

        // Obtenemos DB en modo lectura
        SQLiteDatabase db = getReadableDatabase();

        // Query parametrizada para evitar inyección SQL
        String query = " SELECT " + COL_ID + " FROM " + TABLE_USUARIO + " WHERE " + COL_EMAIL + " = ? AND " + COL_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        // Si el cursor tiene resultados, movemos al primero y leemos columnas
        if (cursor.moveToFirst()) {
            userId = cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID));
        }

        // Cerramos cursor y DB para liberar recursos
        cursor.close();
        db.close();

        return userId;
    }

    public  Usuario getUsuario(long id){
        Usuario user = new Usuario();

        SQLiteDatabase db = getReadableDatabase();

        String query = " SELECT * FROM " + TABLE_USUARIO + " WHERE " + COL_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            user.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD)));
            user.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(COL_NOMBRE)));
            user.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow(COL_TITULO)));
            user.setProfile_img(cursor.getBlob(cursor.getColumnIndexOrThrow(COL_IMG_PERF)));
        }
        return user;
    };

    /**
     * addUser:
     * - Inserta un nuevo usuario en la tabla.
     * - Devuelve el id generado por sqlite (long) o -1 si hubo error/entrada nula.
     */
    public long addUser(Usuario user) {
        if (user == null) return -1;

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_EMAIL, user.getEmail());
        values.put(COL_PASSWORD, user.getPassword());
        values.put(COL_NOMBRE, user.getNombre());
        values.put(COL_TITULO, user.getTitulo());
        values.put(COL_IMG_PERF, user.getProfile_img());

        long id = db.insert(TABLE_USUARIO, null, values);
        db.close();
        return id;
    }

    /**
     * updatePassword:
     * - Actualiza la contraseña del usuario que coincida con nombreUsuario.
     * - Devuelve true si se actualizó al menos una fila, false si no.
     *
     *   (cambiar pass)
     *    [nombre] --> [nueva pass]
     */
    public boolean updatePassword(String email, String nuevaPassword) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PASSWORD, nuevaPassword);

        int rowsUpdated = db.update(TABLE_USUARIO, values, COL_EMAIL + " = ?", new String[]{email});
        db.close();
        return rowsUpdated > 0;
    }

    /**
     * deleteUser:
     * - Elimina usuarios cuyo nombre coincida con el pasado por parametro.
     * - Devuelve el numero de filas eliminadas (0 si no hubo coincidencias).
     *
     */
    public int deleteUser(String email) {
        SQLiteDatabase db = getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_USUARIO, COL_EMAIL + " = ?", new String[]{email});
        db.close();
        return rowsDeleted;
    }

    public List<String> habilidades(long usuarioID) {
        List<String> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_HABILIDAD + " WHERE " + FK_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(usuarioID)});

        if (cursor.moveToFirst()) {
            do {
                String habilidad = cursor.getString(cursor.getColumnIndexOrThrow("habilidad"));
                lista.add(habilidad);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    public List<Proyecto> proyectos(long usuarioID) {
        List<Proyecto> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PROYECTO + " WHERE " + FK_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(usuarioID)});

        if (cursor.moveToFirst()) {
            do {
                Proyecto proyecto = new Proyecto();
                proyecto.setProyecto(cursor.getString(cursor.getColumnIndexOrThrow("proyecto")));
                proyecto.setImagen(cursor.getString(cursor.getColumnIndexOrThrow("imagen")));
                lista.add(proyecto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }
}
