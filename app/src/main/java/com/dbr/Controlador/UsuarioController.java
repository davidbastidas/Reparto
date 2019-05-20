package com.dbr.Controlador;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.dbr.Database.SQLite;
import com.dbr.Modelos.Constants;
import com.dbr.Modelos.Usuario;

public class UsuarioController {

	int tamanoConsulta=0;
	public int getTamanoConsulta() {
		return tamanoConsulta;
	}

	public void insertar(Usuario usuario, Activity activity) {
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		if (db != null) {
			db.execSQL("INSERT OR IGNORE INTO " + Constants.TABLA_USUARIOS + " (" +
					"nombre," +
					"nickname," +
					"tipo," +
					"fk_delegacion," +
					"fk_id" +
					") "
					+ "VALUES ('"+ usuario.getNombre() +
					"',"+usuario.getNickname() +
					",'"+usuario.getTipo() + "'" +
					","+usuario.getFkDelegacion() +
					","+usuario.getFkId() +
					")");
		}
	}
	public synchronized int actualizar(ContentValues registro, String where, Activity activity){
		int actualizados = 0;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		if (db != null) {
	        actualizados = db.update(Constants.TABLA_USUARIOS, registro, where, null);
		}
		return actualizados;
	}
	public synchronized int eliminar(String where, Activity activity){
		int registros=0;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		if (db != null) {
			registros=db.delete(Constants.TABLA_USUARIOS, where, null);
		}
		return registros;
	}
	public synchronized Usuario consultar(int pagina, int limite, String condicion, Activity activity){
		Usuario dataSet = null;
		Cursor c= null,countCursor = null;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		String limit="";
		if(limite!=0){
			limit=" LIMIT "+pagina+","+limite;
		}
		String where="";
		if(!condicion.equals("")){
			where=" WHERE "+condicion;
		}
		c = db.rawQuery("SELECT * FROM " + Constants.TABLA_USUARIOS + " " + where + " " + limit, null);
		countCursor=db.rawQuery("SELECT count(id) FROM " + Constants.TABLA_USUARIOS + " " + where, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta=countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		if (c.moveToFirst()) {
			do {
				dataSet = new Usuario();
				dataSet.setId(c.getLong(0));
				dataSet.setNombre(c.getString(1));
				dataSet.setNickname(c.getString(2));
				dataSet.setTipo(c.getInt(3));
				dataSet.setFkDelegacion(c.getInt(4));
				dataSet.setFkId(c.getInt(5));
			} while (c.moveToNext());
		}
		c.close();
		countCursor.close();
		return dataSet;
	}
	public synchronized int count(String condicion, Activity activity){
		Cursor countCursor = null;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		String where="";
		if(!condicion.equals("")){
			where=" WHERE "+condicion;
		}
		countCursor=db.rawQuery("SELECT count(id) FROM " + Constants.TABLA_USUARIOS + " " + where, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta=countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		countCursor.close();
		return tamanoConsulta;
	}
}
