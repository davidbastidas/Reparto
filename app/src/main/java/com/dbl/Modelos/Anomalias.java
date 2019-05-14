package com.dbl.Modelos;

public class Anomalias {

	private long id;
	private String nombre;
	private String codigo;
	private int lectura;
	private int foto;
	private int orden;

	public Anomalias() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public int getLectura() {
		return lectura;
	}

	public void setLectura(int lectura) {
		this.lectura = lectura;
	}

	public int getFoto() {
		return foto;
	}

	public void setFoto(int foto) {
		this.foto = foto;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}
}
