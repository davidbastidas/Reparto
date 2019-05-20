package com.dbr.Vistas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.dbr.Controlador.AnomaliasController;
import com.dbr.Controlador.AuditoriaController;
import com.dbr.Controlador.ObservacionRapidaController;
import com.dbr.Modelos.Anomalias;
import com.dbr.Modelos.Auditorias;
import com.dbr.Modelos.Constants;
import com.dbr.Modelos.ObservacionRapida;
import com.dbr.Modelos.Servicio;
import com.dbr.Modelos.ServicioSesion;
import com.dbr.R;
import com.dbr.Vistas.Adaptader.AdapterDetalleServicio;

import java.util.ArrayList;

public class DetalleActivity extends AppCompatActivity {

    Button b_ir_anomalia;
    ListView l_detalle;
    long servicioId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        setTitle("Detalle del Servicio");

        b_ir_anomalia = findViewById(R.id.b_ir_anomalia);
        l_detalle = findViewById(R.id.l_detalle);

        Auditorias auditoria = null;
        AuditoriaController audCont = new AuditoriaController();

        ArrayList<Servicio> data = null;
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            servicioId = 0;
        } else {
            servicioId = extras.getLong(Constants.EXTRA_SERVICIO_ID);
            auditoria = audCont.consultar(0, 0, "id = " + servicioId, this).get(0);
            data = llenarPorAuditoria(auditoria);
            if(auditoria.getEstado() != 0){
                b_ir_anomalia.setEnabled(false);
            }
            ServicioSesion.getInstance().setPideGps(auditoria.getPideGps());
        }

        AdapterDetalleServicio adapter = new AdapterDetalleServicio(this, data);
        l_detalle.setAdapter(adapter);

        b_ir_anomalia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServicioSesion.getInstance().setId(servicioId);
                if(ServicioSesion.getInstance().getPideGps() == 1){
                    if (Constants.isGpsActivo(DetalleActivity.this)) {
                        Intent intentar = new Intent(DetalleActivity.this, AnomaliaActivity.class);
                        startActivityForResult(intentar, Constants.SERVICIO_REQUEST_CODE);
                    } else {
                        Constants.ActivarGPS(DetalleActivity.this);
                    }
                } else {
                    Intent intentar = new Intent(DetalleActivity.this, AnomaliaActivity.class);
                    startActivityForResult(intentar, Constants.SERVICIO_REQUEST_CODE);
                }

            }
        });
    }

    private ArrayList<Servicio> llenarPorAuditoria(Auditorias auditoria) {
        ArrayList<Servicio> data = new ArrayList<>();
        Servicio datum = null;

        datum = new Servicio();
        datum.setTitulo("Medidor");
        datum.setSubtitulo("" + auditoria.getMedidor());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("NIC");
        datum.setSubtitulo("" + auditoria.getNic());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("Direccion");
        datum.setSubtitulo(auditoria.getDireccion());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("Cliente");
        datum.setSubtitulo("" + auditoria.getCliente());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("Barrio");
        datum.setSubtitulo("" + auditoria.getBarrio());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("NIS");
        datum.setSubtitulo("" + auditoria.getNis());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("NIF");
        datum.setSubtitulo("" + auditoria.getNif());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("Paquete");
        datum.setSubtitulo(auditoria.getPaquete());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("Nis");
        datum.setSubtitulo("" + auditoria.getNis());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("Localidad");
        datum.setSubtitulo(auditoria.getLocalidad());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("Ruta");
        datum.setSubtitulo("" + auditoria.getRuta());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("Itin");
        datum.setSubtitulo("" + auditoria.getItin());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("Lectura");
        datum.setSubtitulo(auditoria.getLectura());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("Anomalia");
        String anomalia = "";
        if(auditoria.getAnomalia() != 0){
            AnomaliasController an = new AnomaliasController();
            ArrayList<Anomalias> consultar = an.consultar(0, 0, "id = " + auditoria.getAnomalia(), this);
            anomalia = consultar.get(0).getNombre();
        }
        datum.setSubtitulo("" + anomalia);
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("Tipo Servicio");
        String obsRapida = "";
        if(auditoria.getObservacionRapida() != 0){
            ObservacionRapidaController oc = new ObservacionRapidaController();
            ArrayList<ObservacionRapida> consultar = oc.consultar(0, 0, "id = " + auditoria.getObservacionRapida(), this);
            obsRapida = consultar.get(0).getNombre();
        }
        datum.setSubtitulo("" + obsRapida);
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("Otras Observaciones");
        datum.setSubtitulo(auditoria.getObservacionAnalisis());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("IDBD");
        datum.setSubtitulo("" + auditoria.getId());
        data.add(datum);

        ServicioSesion.getInstance().setPideGps(auditoria.getPideGps());

        return data;
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
}
