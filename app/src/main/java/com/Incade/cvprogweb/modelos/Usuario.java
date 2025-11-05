package com.Incade.cvprogweb.modelos;

public class Usuario {
    private long id;
    private String email;
    private String password;
    private String nombre;
    private String titulo;
    private byte[] profile_img;

    public Usuario() {
    }

    public Usuario(String email, String password, String nombre, String titulo, byte[] profile_img) {
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.titulo = titulo;
        this.profile_img = profile_img;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public byte[] getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(byte[] profile_img) {
        this.profile_img = profile_img;
    }
}
