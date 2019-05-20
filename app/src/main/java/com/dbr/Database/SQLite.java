package com.dbr.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dbr.Modelos.Constants;

public class SQLite extends SQLiteOpenHelper {

	private static SQLite mInstance;
	private static SQLiteDatabase myWritableDb;

	private SQLite(Context context) {
		super(context, Constants.DB_NAME, null, 1);
	}

	/**
     * Get default instance of the class to keep it a singleton
     *
     * @param context
     * the application context
     */
	public static SQLite getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new SQLite(context);
		}
		return mInstance;
	}

	/**
     * Returns a writable database instance in order not to open and close many
     * SQLiteDatabase objects simultaneously
     *
     * @return a writable instance to SQLiteDatabase
     */
	public SQLiteDatabase getMyWritableDatabase() {
		if ((myWritableDb == null) || (!myWritableDb.isOpen())) {
			myWritableDb = this.getWritableDatabase();
		}

		return myWritableDb;
	}

	@Override
    public void close() {
        super.close();
        if (myWritableDb != null) {
            myWritableDb.close();
            myWritableDb = null;
        }
    }
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + Constants.TABLA_USUARIOS + "(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" nombre TEXT," +
				" nickname TEXT," +
				" tipo int," +
				" fk_delegacion int," +
				" fk_id int" +
				")");

		db.execSQL("create table " + Constants.TABLA_AUDITORIAS + "(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" barrio TEXT," +
				" localidad TEXT," +
				" cliente TEXT," +
				" direccion TEXT," +
				" nic INTEGER," +
				" nis INTEGER," +
				" nif INTEGER," +
				" ruta INTEGER," +
				" itin INTEGER," +
				" unicom INTEGER," +
				" medidor TEXT," +
				" paquete TEXT," +
				" lectura TEXT," +
				" anomalia INTEGER," +
				" observacion_rapida INTEGER," +
				" observacion_analisis TEXT," +
				" latitud TEXT," +
				" longitud TEXT," +
				" orden INTEGER," +
				" foto TEXT," +
				" fecha_realizado DATETIME," +
				" lector_asignado_id INTEGER," +
				" lector_realiza_id INTEGER," +
				" estado INTEGER," +
				" last_insert INTEGER," +
				" pide_gps INTEGER" +
				")");

		db.execSQL("create table " + Constants.TABLA_ANOMALIAS + "(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" nombre TEXT," +
				" codigo TEXT," +
				" lectura INTEGER," +
				" foto INTEGER," +
				" orden INTEGER" +
				")");

		db.execSQL("create table " + Constants.TABLA_OBSERVACION_RAPIDA + "(" +
				" id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" nombre TEXT" +
				")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int versionAnterior,
                          int versionNueva) {
		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLA_USUARIOS);
		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLA_AUDITORIAS);
		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLA_ANOMALIAS);
		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLA_OBSERVACION_RAPIDA);
		onCreate(db);
	}
}
