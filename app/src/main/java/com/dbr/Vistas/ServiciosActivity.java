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
import android.widget.TextView;
import android.widget.Toast;

import com.dbr.Controlador.AuditoriaController;
import com.dbr.Modelos.Auditorias;
import com.dbr.Modelos.Constants;
import com.dbr.Modelos.Servicio;
import com.dbr.R;
import com.dbr.Vistas.Adaptader.AdapterServicios;

import java.util.ArrayList;

public class ServiciosActivity extends AppCompatActivity {

    TextView t_pagina;
    EditText e_buscar_servicio;
    ListView l_visitas;
    AuditoriaController audCont = null;
    AdapterServicios adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitas);
        setTitle("Lista de Servicios");

        t_pagina = findViewById(R.id.t_pagina);
        e_buscar_servicio = findViewById(R.id.e_buscar_servicio);
        l_visitas = findViewById(R.id.l_visitas);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            cargarLista();
        } else {
            if (extras.containsKey(Constants.EXTRA_BARRIO)) {
                String barrio = extras.getString(Constants.EXTRA_BARRIO);
                barrio = " and barrio like '%" + barrio + "%'";
                Servicio servicio = null;
                ArrayList<Servicio> servicios = new ArrayList<>();
                audCont = new AuditoriaController();
                ArrayList<Auditorias> auditorias = audCont.consultar(0, 0, "estado=0" + barrio, this);
                for (Auditorias aud : auditorias) {
                    servicio = new Servicio();
                    servicio.setId(aud.getId());
                    servicio.setTitulo(aud.getDireccion());
                    servicio.setSubtitulo(aud.getBarrio() + " - NIC: " + aud.getNic() + " - MED: " + aud.getMedidor());
                    servicios.add(servicio);
                }

                adapter = new AdapterServicios(this, servicios);
                l_visitas.setAdapter(adapter);
                t_pagina.setText(servicios.size() + " Servicios por Barrio");
            }else if (extras.containsKey(Constants.EXTRA_NIC)) {
                String nic = extras.getString(Constants.EXTRA_NIC);
                String medidor = extras.getString(Constants.EXTRA_MEDIDOR);
                String direccion = extras.getString(Constants.EXTRA_DIRECCION);
                boolean realizados = extras.getBoolean(Constants.EXTRA_REALIZADO);

                String sqlAud = "", sqlPci = "";
                if(!direccion.equals("")){
                    sqlAud = " and direccion like '%" + direccion + "%'";
                    sqlPci = " and direccion like '%" + direccion + "%'";
                }
                if(!nic.equals("")){
                    sqlAud = " and nic like '%" + nic + "%'";
                }
                if(!medidor.equals("")){
                    sqlAud = " and medidor like '%" + medidor + "%'";
                    sqlPci = " and medidor like '%" + medidor + "%'";
                }

                int rel = 0;
                if(realizados){
                    rel = 1;
                }
                Servicio servicio = null;
                ArrayList<Servicio> servicios = new ArrayList<>();
                audCont = new AuditoriaController();
                ArrayList<Auditorias> auditorias = audCont.consultar(0, 0, "estado = " + rel + sqlAud, this);
                for (Auditorias aud : auditorias) {
                    servicio = new Servicio();
                    servicio.setId(aud.getId());
                    servicio.setTitulo(aud.getDireccion());
                    servicio.setSubtitulo(aud.getBarrio() + " - NIC: " + aud.getNic() + " - MED: " + aud.getMedidor());
                    servicios.add(servicio);
                }

                adapter = new AdapterServicios(this, servicios);
                l_visitas.setAdapter(adapter);
                t_pagina.setText(servicios.size() + " Servicio(s) Encontrado(s)");
            }
        }

        e_buscar_servicio.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                ServiciosActivity.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) {}
        });

        l_visitas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                try {
                    Servicio servicio = (Servicio) parent.getItemAtPosition(position);
                    Intent intentar = new Intent(ServiciosActivity.this, DetalleActivity.class);
                    intentar.putExtra(Constants.EXTRA_SERVICIO_ID, servicio.getId());
                    startActivityForResult(intentar, Constants.SERVICIO_REQUEST_CODE);
                } catch (Exception ex) {
                    Toast.makeText(ServiciosActivity.this, "Error en el servicio: " + ex, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void cargarLista(){
        Servicio servicio = null;
        ArrayList<Servicio> servicios = new ArrayList<>();
        audCont = new AuditoriaController();
        ArrayList<Auditorias> auditorias = audCont.consultar(0, 0, "estado=0", this);
        for (Auditorias aud : auditorias) {
            servicio = new Servicio();
            servicio.setId(aud.getId());
            servicio.setTitulo(aud.getDireccion());
            servicio.setSubtitulo(aud.getBarrio() + " - NIC: " + aud.getNic() + " - MED: " + aud.getMedidor());
            servicios.add(servicio);
        }
        
        //combinando las visitas
        adapter = new AdapterServicios(this, servicios);
        l_visitas.setAdapter(adapter);
        t_pagina.setText(servicios.size() + " Servicios");
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
