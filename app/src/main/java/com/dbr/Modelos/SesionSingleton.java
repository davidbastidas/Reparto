package com.dbr.Modelos;

public class SesionSingleton {

    private static SesionSingleton mInstance;

    public static SesionSingleton getInstance() {
        if (mInstance == null) {
            mInstance = new SesionSingleton();
        }
        return mInstance;
    }

    private String ip, ruta, rutaMemoriaTelefono, passwordAdmin, passwordServicios, estadoDatos, estadoEnvio;
    private int fkId;
    private String nombreUsuario;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getRutaMemoriaTelefono() {
        return rutaMemoriaTelefono;
    }

    public void setRutaMemoriaTelefono(String rutaMemoriaTelefono) {
        this.rutaMemoriaTelefono = rutaMemoriaTelefono;
    }

    public String getPasswordAdmin() {
        return passwordAdmin;
    }

    public void setPasswordAdmin(String passwordAdmin) {
        this.passwordAdmin = passwordAdmin;
    }

    public String getPasswordServicios() {
        return passwordServicios;
    }

    public void setPasswordServicios(String passwordServicios) {
        this.passwordServicios = passwordServicios;
    }

    public String getEstadoDatos() {
        return estadoDatos;
    }

    public void setEstadoDatos(String estadoDatos) {
        this.estadoDatos = estadoDatos;
    }

    public String getEstadoEnvio() {
        return estadoEnvio;
    }

    public void setEstadoEnvio(String estadoEnvio) {
        this.estadoEnvio = estadoEnvio;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public int getFkId() {
        return fkId;
    }

    public void setFkId(int fkId) {
        this.fkId = fkId;
    }
}
