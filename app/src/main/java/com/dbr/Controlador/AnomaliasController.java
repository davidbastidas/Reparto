package com.dbr.Controlador;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dbr.Database.SQLite;
import com.dbr.Modelos.Anomalias;
import com.dbr.Modelos.Constants;

import java.util.ArrayList;

public class AnomaliasController {

	int tamanoConsulta = 0;
	long lastInsert;
	public long getLastInsert() {
		return lastInsert;
	}

	public int getTamanoConsulta() {
		return tamanoConsulta;
	}

	public synchronized void insertar(Anomalias anomalias, Activity activity) {
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		if (db != null) {
			ContentValues registro = new ContentValues();
			registro.put("id", anomalias.getId());
			registro.put("nombre", anomalias.getNombre());
			registro.put("codigo", anomalias.getCodigo());
			registro.put("lectura", anomalias.getLectura());
			registro.put("foto", anomalias.getFoto());
			registro.put("orden", anomalias.getOrden());
			lastInsert = db.insert(Constants.TABLA_ANOMALIAS, null, registro);
		}
	}
	public synchronized int actualizar(ContentValues registro, String where, Activity activity){
		int actualizados = 0;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		if (db != null) {
	        actualizados = db.update(Constants.TABLA_ANOMALIAS, registro, where, null);
		}
		return actualizados;
	}
	public synchronized int eliminar(String where, Activity activity){
		int registros = 0;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();

		if (db != null) {
			registros = db.delete(Constants.TABLA_ANOMALIAS, where, null);
		}
		return registros;
	}
	public synchronized ArrayList<Anomalias> consultar(int pagina, int limite, String condicion, Activity activity){
		Anomalias dataSet;
		ArrayList<Anomalias> anomalias = new ArrayList<Anomalias>();
		Cursor c = null, countCursor = null;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		String limit = "";
		if(limite != 0){
			limit = " LIMIT " + pagina + "," + limite;
		}
		String where = "";
		if(!condicion.equals("")){
			where = " WHERE " + condicion;
		}
		c = db.rawQuery("SELECT * FROM " + Constants.TABLA_ANOMALIAS + " " + where+" ORDER BY orden "+limit, null);
		countCursor = db.rawQuery("SELECT count(id) FROM " + Constants.TABLA_ANOMALIAS + " " + where, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta = countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		if (c.moveToFirst()) {
			do {
				dataSet = new Anomalias();
				dataSet.setId(c.getLong(0));
				dataSet.setNombre(c.getString(1));
				dataSet.setCodigo(c.getString(2));
				dataSet.setLectura(c.getInt(3));
				dataSet.setFoto(c.getInt(4));
				dataSet.setOrden(c.getInt(5));
				anomalias.add(dataSet);
			} while (c.moveToNext());
		}
		c.close();
		countCursor.close();
		return anomalias;
	}
	public synchronized int count(String condicion, Activity activity){
		Cursor countCursor = null;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		String where = "";
		if(!condicion.equals("")){
			where = " WHERE "+condicion;
		}
		countCursor=db.rawQuery("SELECT count(id) FROM " + Constants.TABLA_ANOMALIAS + " " + where, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta = countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		countCursor.close();
		return tamanoConsulta;
	}
}
