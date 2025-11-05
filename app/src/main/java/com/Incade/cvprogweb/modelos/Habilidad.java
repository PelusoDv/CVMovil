package com.Incade.cvprogweb.modelos;

public class Habilidad {
    private long id;
    private String habilidad;
    private long usuarioId; // Relación con usuarios.id

    public Habilidad() {
    }

    public Habilidad(String habilidad, long usuarioId) {
        this.habilidad = habilidad;
        this.usuarioId = usuarioId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHabilidad() {
        return habilidad;
    }

    public void setHabilidad(String habilidad) {
        this.habilidad = habilidad;
    }

    public long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(long usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public String toString() {
        return "Habilidad{" +
                "id=" + id +
                ", habilidad='" + habilidad + '\'' +
                ", usuarioId=" + usuarioId +
                '}';
    }
}
