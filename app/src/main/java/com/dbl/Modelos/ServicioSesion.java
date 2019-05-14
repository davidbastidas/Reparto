package com.dbl.Modelos;

public class ServicioSesion {

    private static ServicioSesion mInstance;

    public static ServicioSesion getInstance() {
        if (mInstance == null) {
            mInstance = new ServicioSesion();
        }
        return mInstance;
    }

    public static void resetSesion() {
        mInstance = null;
    }

    private long id;
    private long tipoServicio;
    private long nic;
    private String medidor;
    private String lectura;
    private long anomalia;
    private long observacionRapida;
    private int habitado;
    private int visible;
    private String observacionAnalisis;
    private String latitud;
    private String longitud;
    private long orden;
    private String foto;
    private String fechaRealizado;
    private long lectorRealizaId;
    private int pideGps;
    private int pideLectura;
    private int pideFoto;
    private String ultimaAnomalia;
    private int lectura1;
    private int lectura2;
    private int desviacionAceptda = 30;

    private boolean observacionObligatoria;

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

    public long getNic() {
        return nic;
    }

    public void setNic(long nic) {
        this.nic = nic;
    }

    public String getMedidor() {
        return medidor;
    }

    public void setMedidor(String medidor) {
        this.medidor = medidor;
    }

    public String getLectura() {
        return lectura;
    }

    public void setLectura(String lectura) {
        this.lectura = lectura;
    }

    public long getAnomalia() {
        return anomalia;
    }

    public void setAnomalia(long anomalia) {
        this.anomalia = anomalia;
    }

    public long getObservacionRapida() {
        return observacionRapida;
    }

    public void setObservacionRapida(long observacionRapida) {
        this.observacionRapida = observacionRapida;
    }

    public int getHabitado() {
        return habitado;
    }

    public void setHabitado(int habitado) {
        this.habitado = habitado;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public String getObservacionAnalisis() {
        return observacionAnalisis;
    }

    public void setObservacionAnalisis(String observacionAnalisis) {
        this.observacionAnalisis = observacionAnalisis;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public long getOrden() {
        return orden;
    }

    public void setOrden(long orden) {
        this.orden = orden;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFechaRealizado() {
        return fechaRealizado;
    }

    public void setFechaRealizado(String fechaRealizado) {
        this.fechaRealizado = fechaRealizado;
    }

    public long getLectorRealizaId() {
        return lectorRealizaId;
    }

    public void setLectorRealizaId(long lectorRealizaId) {
        this.lectorRealizaId = lectorRealizaId;
    }

    public int getPideGps() {
        return pideGps;
    }

    public void setPideGps(int pideGps) {
        this.pideGps = pideGps;
    }

    public int getPideLectura() {
        return pideLectura;
    }

    public void setPideLectura(int pideLectura) {
        this.pideLectura = pideLectura;
    }

    public int getPideFoto() {
        return pideFoto;
    }

    public void setPideFoto(int pideFoto) {
        this.pideFoto = pideFoto;
    }

    public boolean isObservacionObligatoria() {
        return observacionObligatoria;
    }

    public void setObservacionObligatoria(boolean observacionObligatoria) {
        this.observacionObligatoria = observacionObligatoria;
    }

    public String getUltimaAnomalia() {
        return ultimaAnomalia;
    }

    public void setUltimaAnomalia(String ultimaAnomalia) {
        this.ultimaAnomalia = ultimaAnomalia;
    }

    public int getLectura1() {
        return lectura1;
    }

    public void setLectura1(int lectura1) {
        this.lectura1 = lectura1;
    }

    public int getLectura2() {
        return lectura2;
    }

    public void setLectura2(int lectura2) {
        this.lectura2 = lectura2;
    }

    public int getDesviacionAceptda() {
        return desviacionAceptda;
    }

    public void setDesviacionAceptda(int desviacionAceptda) {
        this.desviacionAceptda = desviacionAceptda;
    }
}
