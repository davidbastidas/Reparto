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

import com.dbr.Controlador.AuditoriaController;
import com.dbr.Modelos.Auditorias;
import com.dbr.Modelos.Constants;
import com.dbr.R;
import com.dbr.Vistas.Adaptader.AdapterBarrios;

import java.util.ArrayList;

public class BarriosActivity extends AppCompatActivity {

    EditText e_buscar_barrio;
    ListView l_barrios;
    AdapterBarrios adapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barrios);
        setTitle("Elija un Barrio");

        e_buscar_barrio = findViewById(R.id.e_buscar_barrio);
        l_barrios = findViewById(R.id.l_barrios);

        AuditoriaController res = new AuditoriaController();
        ArrayList<Auditorias> barrios = res.consultaBarrios(this);
        adapter = new AdapterBarrios(this, barrios);
        l_barrios.setAdapter(adapter);
        l_barrios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                try {
                    Auditorias barrio = (Auditorias) parent.getItemAtPosition(position);
                    Intent intentar = new Intent(BarriosActivity.this, ServiciosActivity.class);
                    intentar.putExtra(Constants.EXTRA_BARRIO, barrio.getBarrio());
                    startActivityForResult(intentar, Constants.SERVICIO_REQUEST_CODE);
                } catch (Exception ex) {
                    Toast.makeText(BarriosActivity.this, "Error en barrio: " + ex, Toast.LENGTH_LONG).show();
                }
            }
        });
        e_buscar_barrio.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                adapter.getFilter().filter(cs.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) {}
        });
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
