package com.dbl.Controlador;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dbl.Database.SQLite;
import com.dbl.Modelos.Pci;
import com.dbl.Modelos.Constants;
import com.dbl.Modelos.Pci;

import java.util.ArrayList;

public class PciController {

	int tamanoConsulta = 0;
	long lastInsert;
	public long getLastInsert() {
		return lastInsert;
	}

	public int getTamanoConsulta() {
		return tamanoConsulta;
	}

	public synchronized void insertar(Pci pci, Activity activity) {
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		if (db != null) {
			ContentValues registro = new ContentValues();
			registro.put("id", pci.getId());
			registro.put("ct", pci.getCt());
			registro.put("mt", pci.getMt());
			registro.put("direccion", pci.getDireccion());
			registro.put("medidor", pci.getMedidor());
			registro.put("medidor_anterior", pci.getMedidorAnterior());
			registro.put("medidor_posterior", pci.getMedidorPosterior());
			registro.put("barrio", pci.getBarrio());
			registro.put("lectura", pci.getLectura());
			registro.put("anomalia", pci.getAnomalia());
			registro.put("observacion_analisis", pci.getObservacionAnalisis());
			registro.put("municipio", pci.getMunicipio());
			registro.put("codigo", pci.getCodigo());
			registro.put("an_anterior", pci.getAnAnterior());
			registro.put("lectura_anterior", pci.getLecturaAnterior());
			registro.put("unicom", pci.getUnicom());
			registro.put("ruta", pci.getRuta());
			registro.put("itin", pci.getItin());
			registro.put("latitud", pci.getLatitud());
			registro.put("longitud", pci.getLongitud());
			registro.put("orden", 0);
			registro.put("foto", pci.getFoto());
			registro.put("fecha_realizado", pci.getFechaRealizado());
			registro.put("lector_asignado_id", pci.getLectorAsignadoId());
			registro.put("lector_realiza_id", 0);
			registro.put("estado", 0);
			registro.put("last_insert", 0);
			registro.put("pide_gps", pci.getPideGps());
			registro.put("ultima_anomalia", pci.getUltimaAnomalia());
			registro.put("lectura1", pci.getLectura1());
			registro.put("lectura2", pci.getLectura2());
			registro.put("desviacion_aceptada", pci.getDesviacionAceptada());
			lastInsert = db.insert(Constants.TABLA_PCI, null, registro);
		}
	}
	public synchronized int actualizar(ContentValues registro, String where, Activity activity){
		int actualizados = 0;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		if (db != null) {
	        actualizados = db.update(Constants.TABLA_PCI, registro, where, null);
		}
		return actualizados;
	}
	public synchronized int eliminar(String where, Activity activity){
		int registros=0;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();

		if (db != null) {
			registros=db.delete(Constants.TABLA_PCI, where, null);
		}
		return registros;
	}
	public synchronized void eliminarTodo(Activity activity){
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		if (db != null) {
			db.execSQL("DELETE FROM " + Constants.TABLA_PCI);
		}
	}
	public synchronized ArrayList<Pci> consultar(int pagina, int limite, String condicion, Activity activity){
		Pci dataSet;
		ArrayList<Pci> visitas = new ArrayList<Pci>();
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
		c = db.rawQuery("SELECT * FROM " + Constants.TABLA_PCI + " " + where+" ORDER BY id "+limit, null);
		countCursor = db.rawQuery("SELECT count(id) FROM " + Constants.TABLA_PCI + " " + where, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta = countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		if (c.moveToFirst()) {
			do {
				dataSet = new Pci();
				dataSet.setId(c.getLong(0));
				dataSet.setCt(c.getString(1));
				dataSet.setMt(c.getString(2));
				dataSet.setDireccion(c.getString(3));
				dataSet.setMedidor(c.getString(4));
				dataSet.setMedidorAnterior(c.getString(5));
				dataSet.setMedidorPosterior(c.getString(6));
				dataSet.setBarrio(c.getString(7));
				dataSet.setLectura(c.getString(8));
				dataSet.setAnomalia(c.getLong(9));
				dataSet.setObservacionAnalisis(c.getString(10));
				dataSet.setMunicipio(c.getString(11));
				dataSet.setCodigo(c.getString(12));
				dataSet.setAnAnterior(c.getString(13));
				dataSet.setLecturaAnterior(c.getString(14));
				dataSet.setUnicom(c.getInt(15));
				dataSet.setRuta(c.getInt(16));
				dataSet.setItin(c.getInt(17));
				dataSet.setLatitud(c.getString(18));
				dataSet.setLongitud(c.getString(19));
				dataSet.setOrden(c.getLong(20));
				dataSet.setFoto(c.getString(21));
				dataSet.setFechaRealizado(c.getString(22));
				dataSet.setLectorAsignadoId(c.getLong(23));
				dataSet.setLectorRealizaId(c.getLong(24));
				dataSet.setEstado(c.getLong(25));
				dataSet.setLastInsert(c.getLong(26));
				dataSet.setPideGps(c.getInt(27));
				dataSet.setUltimaAnomalia(c.getString(28));
				dataSet.setLectura1(c.getInt(29));
				dataSet.setLectura2(c.getInt(30));
				dataSet.setDesviacionAceptada(c.getInt(31));
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
		countCursor=db.rawQuery("SELECT count(id) FROM " + Constants.TABLA_PCI + " " + where, null);
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
		countCursor = db.rawQuery("SELECT max(orden) FROM " + Constants.TABLA_PCI, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta = countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		countCursor.close();
		return tamanoConsulta;
	}

	public synchronized ArrayList<Pci> consultaBarrios(Activity activity){
		Pci dataSet;
		ArrayList<Pci> visitas = new ArrayList<Pci>();
		Cursor c = null;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		String limit = "";
		c = db.rawQuery("SELECT barrio FROM " + Constants.TABLA_PCI + " WHERE estado = 0 GROUP BY barrio ORDER BY barrio", null);
		if (c.moveToFirst()) {
			do {
				dataSet = new Pci();
				dataSet.setBarrio(c.getString(0));
				visitas.add(dataSet);
			} while (c.moveToNext());
		}
		c.close();
		return visitas;
	}
}
