package com.dbl.Vistas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.se.omapi.Session;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.dbl.Controlador.AnomaliasController;
import com.dbl.Controlador.AuditoriaController;
import com.dbl.Controlador.ObservacionRapidaController;
import com.dbl.Controlador.PciController;
import com.dbl.Modelos.Anomalias;
import com.dbl.Modelos.Auditorias;
import com.dbl.Modelos.Constants;
import com.dbl.Modelos.ObservacionRapida;
import com.dbl.Modelos.Pci;
import com.dbl.Modelos.Servicio;
import com.dbl.Modelos.ServicioSesion;
import com.dbl.R;
import com.dbl.Vistas.Adaptader.AdapterDetalleServicio;

import java.util.ArrayList;

public class DetalleActivity extends AppCompatActivity {

    Button b_ir_anomalia;
    ListView l_detalle;
    long servicioId, servicioTipoId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        setTitle("Detalle del Servicio");

        b_ir_anomalia = findViewById(R.id.b_ir_anomalia);
        l_detalle = findViewById(R.id.l_detalle);

        Auditorias auditoria = null;
        AuditoriaController audCont = new AuditoriaController();

        Pci pci = null;
        PciController pciCont = new PciController();

        ArrayList<Servicio> data = null;
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            servicioId = 0;
        } else {
            servicioId = extras.getLong(Constants.EXTRA_SERVICIO_ID);
            servicioTipoId = extras.getLong(Constants.EXTRA_SERVICIO_TIPO_ID);
            if(servicioTipoId == Constants.EXTRA_SERVICIO_TIPO_AUDITORIA){
                auditoria = audCont.consultar(0, 0, "id = " + servicioId, this).get(0);
                data = llenarPorAuditoria(auditoria);
                if(auditoria.getEstado() != 0){
                    b_ir_anomalia.setEnabled(false);
                }
                ServicioSesion.getInstance().setPideGps(auditoria.getPideGps());
            } else if(servicioTipoId == Constants.EXTRA_SERVICIO_TIPO_PCI){
                pci = pciCont.consultar(0, 0, "id = " + servicioId, this).get(0);
                data = llenarPorPci(pci);
                if(pci.getEstado() != 0){
                    b_ir_anomalia.setEnabled(false);
                }
                ServicioSesion.getInstance().setPideGps(pci.getPideGps());
            }
        }

        AdapterDetalleServicio adapter = new AdapterDetalleServicio(this, data);
        l_detalle.setAdapter(adapter);

        b_ir_anomalia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServicioSesion.getInstance().setId(servicioId);
                ServicioSesion.getInstance().setTipoServicio(servicioTipoId);
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
        datum.setTitulo("Motivo");
        datum.setSubtitulo(auditoria.getMotivo());
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
        datum.setTitulo("Habitado?");
        if(auditoria.getHabitado() == 1){
            datum.setSubtitulo("SI");
        } else {
            datum.setSubtitulo("NO");
        }
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("Visible?");
        if(auditoria.getVisible() == 1){
            datum.setSubtitulo("SI");
        } else {
            datum.setSubtitulo("NO");
        }
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

    private ArrayList<Servicio> llenarPorPci(Pci pci) {
        ArrayList<Servicio> data = new ArrayList<>();
        Servicio datum = null;

        datum = new Servicio();
        datum.setTitulo("CT");
        datum.setSubtitulo("" + pci.getCt());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("MT");
        datum.setSubtitulo("" + pci.getMt());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("Medidor");
        datum.setSubtitulo("" + pci.getMedidor());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("Direccion");
        datum.setSubtitulo(pci.getDireccion());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("Medidor Anterior");
        datum.setSubtitulo("" + pci.getMedidorAnterior());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("Medidor Posterior");
        datum.setSubtitulo("" + pci.getMedidorPosterior());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("Anomalia Anterior");
        datum.setSubtitulo("" + pci.getAnAnterior());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("Barrio");
        datum.setSubtitulo(pci.getBarrio());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("Municipio");
        datum.setSubtitulo("" + pci.getMunicipio());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("Codigo");
        datum.setSubtitulo(pci.getCodigo());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("Unicom");
        datum.setSubtitulo("" + pci.getUnicom());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("Ruta");
        datum.setSubtitulo("" + pci.getRuta());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("Itin");
        datum.setSubtitulo("" + pci.getItin());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("Lectura");
        datum.setSubtitulo(pci.getLectura());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("Anomalia");
        String anomalia = "";
        if(pci.getAnomalia() != 0){
            AnomaliasController an = new AnomaliasController();
            ArrayList<Anomalias> consultar = an.consultar(0, 0, "id = " + pci.getAnomalia(), this);
            anomalia = consultar.get(0).getNombre();
        }
        datum.setSubtitulo("" + anomalia);
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("Otras Observaciones");
        datum.setSubtitulo(pci.getObservacionAnalisis());
        data.add(datum);

        datum = new Servicio();
        datum.setTitulo("IDBD");
        datum.setSubtitulo("" + pci.getId());
        data.add(datum);

        ServicioSesion.getInstance().setPideGps(pci.getPideGps());
        ServicioSesion.getInstance().setUltimaAnomalia(pci.getUltimaAnomalia());
        ServicioSesion.getInstance().setLectura1(pci.getLectura1());
        ServicioSesion.getInstance().setLectura2(pci.getLectura2());
        ServicioSesion.getInstance().setDesviacionAceptda(pci.getDesviacionAceptada());

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
