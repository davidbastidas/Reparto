package com.dbr.Controlador;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.dbr.Database.SQLite;
import com.dbr.Modelos.Auditorias;
import com.dbr.Modelos.Constants;

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
			registro.put("nis", visita.getNis());
			registro.put("nif", visita.getNif());
			registro.put("ruta", visita.getRuta());
			registro.put("itin", visita.getItin());
			registro.put("medidor", visita.getMedidor());
			registro.put("paquete", visita.getPaquete());

			registro.put("lectura", visita.getLectura());
			registro.put("anomalia", visita.getAnomalia());
			registro.put("observacion_rapida", visita.getObservacionRapida());
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
				dataSet.setNis(c.getLong(6));
				dataSet.setNif(c.getLong(7));
				dataSet.setRuta(c.getLong(8));
				dataSet.setItin(c.getLong(9));
				dataSet.setMedidor(c.getString(10));
				dataSet.setPaquete(c.getString(11));

				dataSet.setLectura(c.getString(12));
				dataSet.setAnomalia(c.getLong(13));
				dataSet.setObservacionRapida(c.getLong(14));
				dataSet.setObservacionAnalisis(c.getString(15));
				dataSet.setLatitud(c.getString(16));
				dataSet.setLongitud(c.getString(17));
				dataSet.setOrden(c.getLong(18));
				dataSet.setFoto(c.getString(19));
				dataSet.setFechaRealizado(c.getString(20));
				dataSet.setLectorAsignadoId(c.getLong(21));
				dataSet.setLectorRealizaId(c.getLong(22));
				dataSet.setEstado(c.getLong(23));
				dataSet.setLastInsert(c.getLong(24));
				dataSet.setPideGps(c.getInt(25));
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
