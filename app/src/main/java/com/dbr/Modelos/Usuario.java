package com.dbr.Modelos;

public class Usuario {

	private long id;
	private String nombre;
	private String nickname;
	private int tipo;
	private int fkDelegacion;
	private int fkId;

	public Usuario() {
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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public int getFkDelegacion() {
		return fkDelegacion;
	}

	public void setFkDelegacion(int fkDelegacion) {
		this.fkDelegacion = fkDelegacion;
	}

	public int getFkId() {
		return fkId;
	}

	public void setFkId(int fkId) {
		this.fkId = fkId;
	}
}
