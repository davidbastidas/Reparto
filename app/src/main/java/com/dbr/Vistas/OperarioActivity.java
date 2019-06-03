package com.dbr.Vistas;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dbr.Controlador.AnomaliasController;
import com.dbr.Controlador.AuditoriaController;
import com.dbr.Controlador.GestorConexion;
import com.dbr.Controlador.ObservacionRapidaController;
import com.dbr.Modelos.Anomalias;
import com.dbr.Modelos.Auditorias;
import com.dbr.Modelos.Constants;
import com.dbr.Modelos.ObservacionRapida;
import com.dbr.Modelos.ServicioSesion;
import com.dbr.Modelos.SesionSingleton;
import com.dbr.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OperarioActivity extends AppCompatActivity {

    Button b_buscar, b_barrios, b_visitas, b_scanner;
    TextView t_reporte, t_acerca, t_perfil;
    ProgressDialog progressDialog = null;
    Auditorias auditoriaEnviar = null;
    private int sizeAuditorias = 0;
    GestorConexion conexionGestor = new GestorConexion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operario);
        b_buscar = findViewById(R.id.b_buscar);
        b_barrios = findViewById(R.id.b_barrios);
        b_visitas = findViewById(R.id.b_visitas);
        b_scanner = findViewById(R.id.b_scanner);
        t_reporte = findViewById(R.id.t_reporte);
        t_acerca = findViewById(R.id.t_acerca);
        t_perfil = findViewById(R.id.t_perfil);
        t_perfil.setText("Bienvenido. " + SesionSingleton.getInstance().getNombreUsuario());
        if(SesionSingleton.getInstance().getNombreUsuario() == null){
            finish();
        }

        b_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentar = new Intent(OperarioActivity.this, BuscarActivity.class);
                startActivityForResult(intentar, Constants.SERVICIO_REQUEST_CODE);
            }
        });

        b_barrios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentar = new Intent(OperarioActivity.this, BarriosActivity.class);
                startActivityForResult(intentar, Constants.SERVICIO_REQUEST_CODE);
            }
        });

        b_visitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentar = new Intent(OperarioActivity.this, ServiciosActivity.class);
                startActivityForResult(intentar, Constants.SERVICIO_REQUEST_CODE);
            }
        });

        b_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

                    startActivityForResult(intent, Constants.SERVICIO_REQUEST_QR_SCANN);
                } catch (Exception e) {
                    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    startActivity(marketIntent);
                }
            }
        });

        mostrarReporte();
        acercaDe();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //enviarServicios();
        mostrarReporte();
        ServicioSesion.resetSesion();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_operario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.servicios:
                progressDialog = Constants.dialogIndeterminate(this, "Descargando Servicios...");
                new AsyncTask<String, Void, String>(){
                    @Override
                    protected String doInBackground(String... params) {
                        GestorConexion con = new GestorConexion();
                        return con.descargarServicios(SesionSingleton.getInstance().getFkId());
                    }
                    @Override
                    protected void onPostExecute(String result) {
                        alFinalizarDescargaServicios(result);
                    }
                }.execute();
                return true;
            case R.id.sincronizar:
                enviarServicios();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void mostrarReporte() {
        AuditoriaController audcont = new AuditoriaController();
        int audRealizadas = audcont.count("estado = 1", this);
        int audPendientes = audcont.count("estado = 0", this);
        int audEnviadas = audcont.count("last_insert > 0", this);

        t_reporte.setText(
                "Repartos Realizadas = " + audRealizadas+"\n"+
                "Repartos Pendientes = " + audPendientes + "\n" +
                "Repartos Enviadas = " + audEnviadas
        );
    }

    private void acercaDe(){
        t_acerca.setText(
                "Lectura de Medidores Version 1.1 2019-04-07 08:00:00"
        );
    }

    private void alFinalizarDescargaServicios(String result) {
        System.out.println("alFinalizarDescargaServicios= " + result);
        result = "{\"array\":" + result + "}";
        SesionSingleton se = SesionSingleton.getInstance();
        AuditoriaController audcont = new AuditoriaController();
        JSONObject json_data = null;
        boolean flag = true;
        try {
            json_data = new JSONObject(result);
            JSONArray jsonArray = json_data.getJSONArray("array");
            json_data = new JSONObject(jsonArray.get(0).toString());
            if(json_data.getBoolean("estado")){
                flag = true;
            }else{
                Toast.makeText(this, Constants.MSG_PETICION_RECHAZADA, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e){
            Toast.makeText(this, Constants.MSG_FORMATO_NO_VALIDO + e, Toast.LENGTH_LONG).show();
        } finally {

        }

        try {
            if(flag){
                //insertando la tabla anomalias
                JSONArray jArrayAnomalias = json_data.getJSONArray("anomalias");
                int size = jArrayAnomalias.length();
                AnomaliasController anoCon = new AnomaliasController();
                Anomalias anomalia = null;
                for (int i = 0; i < size; ++i) {
                    if(i == 0){
                        anoCon.eliminar("", this);
                    }
                    JSONObject tr = jArrayAnomalias.getJSONObject(i);
                    anomalia = new Anomalias();
                    anomalia.setId(tr.getLong("id"));
                    anomalia.setNombre(tr.getString("nombre"));
                    anomalia.setCodigo(tr.getString("codigo"));
                    anomalia.setLectura(tr.getInt("lectura"));
                    anomalia.setFoto(tr.getInt("foto"));
                    anomalia.setOrden(tr.getInt("orden"));
                    anoCon.insertar(anomalia, this);
                }

                //insertando la tabla observaciones rapidas
                JSONArray jArrayObsRapidas = json_data.getJSONArray("observaciones_rapidas");
                size = jArrayObsRapidas.length();
                ObservacionRapidaController obsCon = new ObservacionRapidaController();
                ObservacionRapida observacion = null;
                for (int i = 0; i < size; ++i) {
                    if(i == 0){
                        obsCon.eliminar("", this);
                    }
                    JSONObject tr = jArrayObsRapidas.getJSONObject(i);
                    observacion = new ObservacionRapida();
                    observacion.setId(tr.getLong("id"));
                    observacion.setNombre(tr.getString("nombre"));
                    obsCon.insertar(observacion, this);
                }

                //insertando la tabla auditorias
                JSONArray jArrayServicios = json_data.getJSONArray("auditorias");
                size = jArrayServicios.length();
                AuditoriaController audCon = new AuditoriaController();
                audCon.eliminar("estado = 0 and last_insert = 0", this);
                Auditorias auditoria = null;
                for (int i = 0; i < size; ++i) {
                    JSONObject tr = jArrayServicios.getJSONObject(i);
                    auditoria = new Auditorias();
                    auditoria.setId(tr.getLong("id"));
                    auditoria.setBarrio(tr.getString("barrio"));
                    auditoria.setLocalidad(tr.getString("localidad"));
                    auditoria.setCliente(tr.getString("cliente"));
                    auditoria.setDireccion(tr.getString("direccion"));
                    auditoria.setNic(tr.getLong("nic"));
                    auditoria.setNis(tr.getLong("nis"));
                    auditoria.setNif(tr.getLong("nif"));
                    auditoria.setRuta(tr.getLong("ruta"));
                    auditoria.setItin(tr.getLong("itin"));
                    auditoria.setMedidor(tr.getString("medidor"));
                    auditoria.setPaquete(tr.getString("paquete"));
                    auditoria.setLectura("");
                    auditoria.setAnomalia(0);
                    auditoria.setObservacionRapida(0);
                    auditoria.setObservacionAnalisis("");
                    auditoria.setLatitud("");
                    auditoria.setLongitud("");
                    auditoria.setOrden(0);
                    auditoria.setFoto("");
                    auditoria.setFechaRealizado("");
                    auditoria.setLectorAsignadoId(SesionSingleton.getInstance().getFkId());
                    auditoria.setLectorRealizaId(0);
                    auditoria.setEstado(0);
                    auditoria.setLastInsert(0);
                    auditoria.setPideGps(tr.getInt("pide_gps"));
                    audCon.insertar(auditoria, this);
                }

                Toast.makeText(this, "Datos Descargados con exito.", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        } catch (Exception e) {
            System.out.println("Exception: "+e);
            Toast.makeText(this, Constants.MSG_LEYENDO_DATOS + "de los servicios. " + e, Toast.LENGTH_LONG).show();
        } finally {
            progressDialog.dismiss();
            mostrarReporte();
        }
    }

    private void enviarServicios() {
        AuditoriaController audCont = new AuditoriaController();
        ArrayList<Auditorias> arrayAuditorias = audCont.consultar(0, 0, "estado = 1 and last_insert = 0", this);
        sizeAuditorias = arrayAuditorias.size();

        if(sizeAuditorias > 0){
            progressDialog = Constants.dialogIndeterminate(this, "Sincronizando...");
            new AsyncTask<String, Void, String>(){
                @Override
                protected String doInBackground(String... params) {
                    return iniciarSincronizacion();
                }
                @Override
                protected void onPostExecute(String result) {
                    alFinalizarSincronizacion(result);
                }
            }.execute();
        }
    }

    private String iniciarSincronizacion(){
        AuditoriaController audCont = new AuditoriaController();
        ArrayList<Auditorias> arrayAuditorias = audCont.consultar(0, 0, "estado = 1 and last_insert = 0", this);
        sizeAuditorias = arrayAuditorias.size();
        for (int i = 0; i < sizeAuditorias; i++){
            auditoriaEnviar = new Auditorias();
            auditoriaEnviar.setId(arrayAuditorias.get(i).getId());
            auditoriaEnviar.setLectura(arrayAuditorias.get(i).getLectura());
            auditoriaEnviar.setAnomalia(arrayAuditorias.get(i).getAnomalia());
            auditoriaEnviar.setObservacionRapida(arrayAuditorias.get(i).getObservacionRapida());
            auditoriaEnviar.setObservacionAnalisis(arrayAuditorias.get(i).getObservacionAnalisis());
            auditoriaEnviar.setLatitud(arrayAuditorias.get(i).getLatitud());
            auditoriaEnviar.setLongitud(arrayAuditorias.get(i).getLongitud());
            auditoriaEnviar.setOrden(arrayAuditorias.get(i).getOrden());
            auditoriaEnviar.setFechaRealizado(arrayAuditorias.get(i).getFechaRealizado());
            auditoriaEnviar.setFoto(arrayAuditorias.get(i).getFoto());
            String response = conexionGestor.enviarAuditoria(auditoriaEnviar, SesionSingleton.getInstance().getFkId());
            System.err.println("response: " + response);
            try {
                JSONObject json_data = new JSONObject(response);
                ContentValues registro = new ContentValues();
                registro.put("last_insert", 1);
                if (json_data.getBoolean("estado")) {
                    audCont.actualizar(registro, "id = " + auditoriaEnviar.getId(), OperarioActivity.this);
                }
            } catch (final Exception e){
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(
                                OperarioActivity.this,
                                Constants.MSG_SINCRONIZACION + ". Auditoria.\n" + e,
                                Toast.LENGTH_LONG).show();
                    }
                });
            } finally {

            }
        }

        return "Sincronizacion finalizada.";
    }

    private void alFinalizarSincronizacion(String result) {
        System.out.println("alFinalizarSincronizacion= " + result);
        mostrarReporte();
        progressDialog.dismiss();
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.SERVICIO_REQUEST_QR_SCANN) {

            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                System.out.println("scaner: " +contents);
                Intent intentar = new Intent(OperarioActivity.this, ServiciosActivity.class);
                intentar.putExtra(Constants.EXTRA_NIC, contents);
                intentar.putExtra(Constants.EXTRA_MEDIDOR, "");
                intentar.putExtra(Constants.EXTRA_DIRECCION, "");
                intentar.putExtra(Constants.EXTRA_REALIZADO, false);
                startActivityForResult(intentar, Constants.SERVICIO_REQUEST_CODE);
            }
            if(resultCode == RESULT_CANCELED){
                //handle cancel
            }
        }
    }
}
