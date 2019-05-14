package com.dbl.Vistas;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dbl.Controlador.AuditoriaController;
import com.dbl.Controlador.PciController;
import com.dbl.Modelos.Constants;
import com.dbl.Modelos.SesionSingleton;
import com.dbl.Modelos.Auditorias;
import com.dbl.R;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    EditText e_ip, e_ruta_web;
    Button b_borrar_datos, b_guardar_settings, b_desbloquear_visitas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        setTitle("Administracion de la App");
        final SharedPreferences preferencias = getSharedPreferences(Constants.CONFIGURACION,
                Context.MODE_PRIVATE);
        final SesionSingleton sesion = SesionSingleton.getInstance();
        final SharedPreferences.Editor editor = preferencias.edit();

        e_ip = findViewById(R.id.e_ip);
        e_ruta_web = findViewById(R.id.e_ruta_web);
        b_borrar_datos = findViewById(R.id.b_borrar_datos);
        b_guardar_settings = findViewById(R.id.b_guardar_settings);
        b_desbloquear_visitas = findViewById(R.id.b_desbloquear_visitas);

        e_ip.setText(preferencias.getString(Constants.IP, ""));
        e_ruta_web.setText(preferencias.getString(Constants.RUTAWEB, ""));

        b_borrar_datos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                new AsyncTask<String, Void, String>(){
                                    @Override
                                    protected String doInBackground(String... params) {
                                        AuditoriaController vis = new AuditoriaController();
                                        vis.eliminarTodo(AdminActivity.this);

                                        PciController pci = new PciController();
                                        pci.eliminarTodo(AdminActivity.this);
                                        return "";
                                    }
                                    @Override
                                    protected void onPostExecute(String result) {
                                        Toast.makeText(
                                                AdminActivity.this,
                                                "Se BORRARON los servicios",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }.execute();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                builder.setMessage("Esta seguro de eliminar todo?").setPositiveButton("Si", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        b_guardar_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!preferencias.getString(Constants.IP, "").equals(e_ip.getText().toString())){
                    editor.putString(Constants.IP, e_ip.getText().toString());
                }
                if(!preferencias.getString(Constants.RUTAWEB, "").equals(e_ruta_web.getText().toString())){
                    editor.putString(Constants.RUTAWEB, e_ruta_web.getText().toString());
                }
                editor.commit();
                sesion.setIp(e_ip.getText().toString());
                sesion.setRuta(e_ruta_web.getText().toString());
                Toast.makeText(
                        AdminActivity.this,
                        "Se guardo la configuracion.",
                        Toast.LENGTH_LONG).show();

                finish();
            }
        });

        b_desbloquear_visitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                new AsyncTask<String, Void, String>(){
                                    @Override
                                    protected String doInBackground(String... params) {
                                        AuditoriaController vis = new AuditoriaController();
                                        ArrayList<Auditorias> consultar = vis.consultar(0, 0, "", AdminActivity.this);
                                        ContentValues registro = new ContentValues();
                                        registro.put("last_insert", 0);
                                        for (int i = 0; i < consultar.size(); i++){
                                            vis.actualizar(registro, "id = " + consultar.get(i).getId(), AdminActivity.this);
                                        }
                                        return "";
                                    }
                                    @Override
                                    protected void onPostExecute(String result) {
                                        Toast.makeText(
                                                AdminActivity.this,
                                                "Se restablecieron los servicios",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }.execute();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                builder.setMessage("Esta seguro de restablecer los servicios?").setPositiveButton("Si", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }
}
