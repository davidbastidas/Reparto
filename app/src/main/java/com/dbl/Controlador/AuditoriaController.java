package com.dbl.Controlador;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.dbl.Database.SQLite;
import com.dbl.Modelos.Auditorias;
import com.dbl.Modelos.Constants;

import java.util.ArrayList;

public class AuditoriaController {

	int tamanoConsulta = 0;
	long lastInsert;
	public long getLastInsert() {
		return lastInsert;
	}

	public int getTamanoConsulta() {
		return tamanoConsulta;
	}

	public synchronized void insertar(Auditorias visita, Activity activity) {
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		if (db != null) {
			ContentValues registro = new ContentValues();
			registro.put("id", visita.getId());
			registro.put("barrio", visita.getBarrio());
			registro.put("localidad", visita.getLocalidad());
			registro.put("cliente", visita.getCliente());
			registro.put("direccion", visita.getDireccion());
			registro.put("nic", visita.getNic());
			registro.put("ruta", visita.getRuta());
			registro.put("itin", visita.getItin());
			registro.put("medidor", visita.getMedidor());
			registro.put("motivo", visita.getMotivo());
			registro.put("nis", visita.getNis());
			registro.put("lectura", visita.getLectura());
			registro.put("anomalia", visita.getAnomalia());
			registro.put("observacion_rapida", visita.getObservacionRapida());
			registro.put("habitado", visita.getHabitado());
			registro.put("visible", visita.getVisible());
			registro.put("observacion_analisis", visita.getObservacionAnalisis());
			registro.put("latitud", visita.getLatitud());
			registro.put("longitud", visita.getLongitud());
			registro.put("orden", 0);
			registro.put("foto", visita.getFoto());
			registro.put("fecha_realizado", visita.getFechaRealizado());
			registro.put("lector_asignado_id", visita.getLectorAsignadoId());
			registro.put("lector_realiza_id", 0);
			registro.put("estado", 0);
			registro.put("last_insert", 0);
			registro.put("pide_gps", visita.getPideGps());
			lastInsert = db.insert(Constants.TABLA_AUDITORIAS, null, registro);
		}
	}
	public synchronized int actualizar(ContentValues registro, String where, Activity activity){
		int actualizados = 0;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		if (db != null) {
	        actualizados = db.update(Constants.TABLA_AUDITORIAS, registro, where, null);
		}
		return actualizados;
	}
	public synchronized int eliminar(String where, Activity activity){
		int registros=0;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();

		if (db != null) {
			registros=db.delete(Constants.TABLA_AUDITORIAS, where, null);
		}
		return registros;
	}
	public synchronized void eliminarTodo(Activity activity){
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		if (db != null) {
			db.execSQL("DELETE FROM " + Constants.TABLA_AUDITORIAS);
		}
	}
	public synchronized ArrayList<Auditorias> consultar(int pagina, int limite, String condicion, Activity activity){
		Auditorias dataSet;
		ArrayList<Auditorias> visitas = new ArrayList<Auditorias>();
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
		c = db.rawQuery("SELECT * FROM " + Constants.TABLA_AUDITORIAS + " " + where+" ORDER BY id "+limit, null);
		countCursor = db.rawQuery("SELECT count(id) FROM " + Constants.TABLA_AUDITORIAS + " " + where, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta = countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		if (c.moveToFirst()) {
			do {
				dataSet = new Auditorias();
				dataSet.setId(c.getLong(0));
				dataSet.setBarrio(c.getString(1));
				dataSet.setLocalidad(c.getString(2));
				dataSet.setCliente(c.getString(3));
				dataSet.setDireccion(c.getString(4));
				dataSet.setNic(c.getLong(5));
				dataSet.setRuta(c.getLong(6));
				dataSet.setItin(c.getLong(7));
				dataSet.setMedidor(c.getString(8));
				dataSet.setMotivo(c.getString(9));
				dataSet.setNis(c.getLong(10));
				dataSet.setLectura(c.getString(11));
				dataSet.setAnomalia(c.getLong(12));
				dataSet.setObservacionRapida(c.getLong(13));
				dataSet.setHabitado(c.getInt(14));
				dataSet.setVisible(c.getInt(15));
				dataSet.setObservacionAnalisis(c.getString(16));
				dataSet.setLatitud(c.getString(17));
				dataSet.setLongitud(c.getString(18));
				dataSet.setOrden(c.getLong(19));
				dataSet.setFoto(c.getString(20));
				dataSet.setFechaRealizado(c.getString(21));
				dataSet.setLectorAsignadoId(c.getLong(22));
				dataSet.setLectorRealizaId(c.getLong(23));
				dataSet.setEstado(c.getLong(24));
				dataSet.setLastInsert(c.getLong(25));
				dataSet.setPideGps(c.getInt(26));
				visitas.add(dataSet);
			} while (c.moveToNext());
		}
		c.close();
		countCursor.close();
		return visitas;
	}
	public synchronized int count(String condicion, Activity activity){
		Cursor countCursor = null;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		String where = "";
		if(!condicion.equals("")){
			where = " WHERE "+condicion;
		}
		countCursor=db.rawQuery("SELECT count(id) FROM " + Constants.TABLA_AUDITORIAS + " " + where, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta = countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		countCursor.close();
		return tamanoConsulta;
	}

	public int ultimoOrden(Activity activity){
		Cursor countCursor = null;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		countCursor = db.rawQuery("SELECT max(orden) FROM " + Constants.TABLA_AUDITORIAS, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta = countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		countCursor.close();
		return tamanoConsulta;
	}

	public synchronized ArrayList<Auditorias> consultaBarrios(Activity activity){
		Auditorias dataSet;
		ArrayList<Auditorias> visitas = new ArrayList<Auditorias>();
		Cursor c = null;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		String limit = "";
		c = db.rawQuery("SELECT barrio FROM " + Constants.TABLA_AUDITORIAS + " WHERE estado = 0 GROUP BY barrio ORDER BY barrio", null);
		if (c.moveToFirst()) {
			do {
				dataSet = new Auditorias();
				dataSet.setBarrio(c.getString(0));
				visitas.add(dataSet);
			} while (c.moveToNext());
		}
		c.close();
		return visitas;
	}
}
