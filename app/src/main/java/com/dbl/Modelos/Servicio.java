package com.dbl.Modelos;

public class Servicio {
    private long id;
    private long tipoServicio;
    private String titulo;
    private String subtitulo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(long tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }
}
