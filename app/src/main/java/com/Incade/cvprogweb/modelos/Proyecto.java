package com.Incade.cvprogweb.modelos;

public class Proyecto {
    private long id;
    private String proyecto;
    private String imagen; // URL de la imagen
    private long usuarioId; // Relación con usuarios.id

    public Proyecto() {
    }

    public Proyecto(String proyecto, String imagen, long usuarioId) {
        this.proyecto = proyecto;
        this.imagen = imagen;
        this.usuarioId = usuarioId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(long usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public String toString() {
        return "Proyecto{" +
                "id=" + id +
                ", proyecto='" + proyecto + '\'' +
                ", imagen='" + imagen + '\'' +
                ", usuarioId=" + usuarioId +
                '}';
    }
}
