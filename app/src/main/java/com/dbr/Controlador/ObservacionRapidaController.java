package com.dbr.Controlador;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dbr.Database.SQLite;
import com.dbr.Modelos.Constants;
import com.dbr.Modelos.ObservacionRapida;

import java.util.ArrayList;

public class ObservacionRapidaController {

	int tamanoConsulta = 0;
	long lastInsert;
	public long getLastInsert() {
		return lastInsert;
	}

	public int getTamanoConsulta() {
		return tamanoConsulta;
	}

	public synchronized void insertar(ObservacionRapida observacionRapida, Activity activity) {
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		if (db != null) {
			ContentValues registro = new ContentValues();
			registro.put("id", observacionRapida.getId());
			registro.put("nombre", observacionRapida.getNombre());
			lastInsert = db.insert(Constants.TABLA_OBSERVACION_RAPIDA, null, registro);
		}
	}
	public synchronized int actualizar(ContentValues registro, String where, Activity activity){
		int actualizados = 0;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		if (db != null) {
	        actualizados = db.update(Constants.TABLA_OBSERVACION_RAPIDA, registro, where, null);
		}
		return actualizados;
	}
	public synchronized int eliminar(String where, Activity activity){
		int registros = 0;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();

		if (db != null) {
			registros = db.delete(Constants.TABLA_OBSERVACION_RAPIDA, where, null);
		}
		return registros;
	}
	public synchronized ArrayList<ObservacionRapida> consultar(int pagina, int limite, String condicion, Activity activity){
		ObservacionRapida dataSet;
		ArrayList<ObservacionRapida> observacionRapidas = new ArrayList<ObservacionRapida>();
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
		c = db.rawQuery("SELECT * FROM " + Constants.TABLA_OBSERVACION_RAPIDA + " " + where+" ORDER BY id "+limit, null);
		countCursor = db.rawQuery("SELECT count(id) FROM " + Constants.TABLA_OBSERVACION_RAPIDA + " " + where, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta = countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		if (c.moveToFirst()) {
			do {
				dataSet = new ObservacionRapida();
				dataSet.setId(c.getLong(0));
				dataSet.setNombre(c.getString(1));
				observacionRapidas.add(dataSet);
			} while (c.moveToNext());
		}
		c.close();
		countCursor.close();
		return observacionRapidas;
	}
	public synchronized int count(String condicion, Activity activity){
		Cursor countCursor = null;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		String where = "";
		if(!condicion.equals("")){
			where = " WHERE "+condicion;
		}
		countCursor = db.rawQuery("SELECT count(id) FROM " + Constants.TABLA_OBSERVACION_RAPIDA + " " + where, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta = countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		countCursor.close();
		return tamanoConsulta;
	}
}
