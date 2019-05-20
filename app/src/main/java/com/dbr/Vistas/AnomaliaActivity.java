package com.dbr.Vistas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dbr.Controlador.AnomaliasController;
import com.dbr.Modelos.Anomalias;
import com.dbr.Modelos.Constants;
import com.dbr.Modelos.ServicioSesion;
import com.dbr.R;
import com.dbr.Vistas.Adaptader.AdapterAnomalias;

import java.util.ArrayList;

public class AnomaliaActivity extends AppCompatActivity {

    EditText e_buscar_anomalia;
    ListView l_anomalias;
    AdapterAnomalias adapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anomalia);
        setTitle("Elegir una Anomalia");
        e_buscar_anomalia = findViewById(R.id.e_buscar_anomalia);
        l_anomalias = findViewById(R.id.l_anomalias);

        AnomaliasController anom = new AnomaliasController();

        ArrayList<Anomalias> anomalias = anom.consultar(0, 0, "", this);
        adapter = new AdapterAnomalias(this, anomalias);
        l_anomalias.setAdapter(adapter);
        l_anomalias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                try {
                    Anomalias anomalia = (Anomalias) parent.getItemAtPosition(position);
                    ServicioSesion.getInstance().setPideFoto(anomalia.getFoto());
                    ServicioSesion.getInstance().setPideLectura(anomalia.getLectura());
                    validarAnomalia((int) anomalia.getId());
                } catch (Exception ex) {
                    Toast.makeText(AnomaliaActivity.this, "Error en anomalia: " + ex, Toast.LENGTH_LONG).show();
                }
            }
        });
        e_buscar_anomalia.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                AnomaliaActivity.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) {}
        });
    }

    private void validarAnomalia(int id) {
        ServicioSesion.getInstance().setAnomalia(id);

        switch (id){
            case Constants.AN001_001:
                ServicioSesion.getInstance().setObservacionObligatoria(true);
                break;
            case Constants.AN088:
                ServicioSesion.getInstance().setObservacionObligatoria(true);
                break;
            default:
                ServicioSesion.getInstance().setObservacionObligatoria(false);
                break;
        }

        if(ServicioSesion.getInstance().getPideGps() == 1){
            if (Constants.isGpsActivo(AnomaliaActivity.this)) {
                Intent intentar = new Intent(AnomaliaActivity.this, ObservacionActivity.class);
                startActivityForResult(intentar, Constants.SERVICIO_REQUEST_CODE);
            } else {
                Constants.ActivarGPS(AnomaliaActivity.this);
            }
        } else {
            Intent intentar = new Intent(AnomaliaActivity.this, ObservacionActivity.class);
            startActivityForResult(intentar, Constants.SERVICIO_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.SERVICIO_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}
