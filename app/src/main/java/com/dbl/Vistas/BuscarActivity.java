package com.dbl.Vistas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.dbl.Controlador.AuditoriaController;
import com.dbl.Modelos.Constants;
import com.dbl.R;

public class BuscarActivity extends AppCompatActivity {

    Button b_ir_buscar;
    EditText e_nic, e_medidor, e_direccion, e_ct, e_mt;
    RadioButton rb_pendientes, rb_realizados;
    AuditoriaController vis = null;
    boolean realizados = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);
        setTitle("Busqueda de Servicio");
        e_nic = findViewById(R.id.e_nic);
        e_medidor = findViewById(R.id.e_medidor);
        e_direccion = findViewById(R.id.e_direccion);
        e_ct = findViewById(R.id.e_ct);
        e_mt = findViewById(R.id.e_mt);
        rb_pendientes = findViewById(R.id.rb_pendientes);
        rb_realizados = findViewById(R.id.rb_realizados);
        b_ir_buscar = findViewById(R.id.b_ir_buscar);
        rb_realizados.setChecked(true);
        rb_pendientes.setChecked(false);

        b_ir_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!e_nic.getText().toString().equals("") ||
                        !e_medidor.getText().toString().equals("") ||
                        !e_direccion.getText().toString().equals("") ||
                        !e_ct.getText().toString().equals("") ||
                        !e_mt.getText().toString().equals("")) {
                    Intent intentar = new Intent(BuscarActivity.this, ServiciosActivity.class);
                    intentar.putExtra(Constants.EXTRA_NIC, e_nic.getText().toString());
                    intentar.putExtra(Constants.EXTRA_MEDIDOR, e_medidor.getText().toString());
                    intentar.putExtra(Constants.EXTRA_DIRECCION, e_direccion.getText().toString());
                    intentar.putExtra(Constants.EXTRA_CT, e_ct.getText().toString());
                    intentar.putExtra(Constants.EXTRA_MT, e_mt.getText().toString());
                    intentar.putExtra(Constants.EXTRA_REALIZADO, realizados);
                    startActivityForResult(intentar, Constants.SERVICIO_REQUEST_CODE);
                } else {
                    Toast.makeText(BuscarActivity.this, "Debe ingresar el servicio a buscar", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rb_pendientes:
                if (checked)
                    realizados = false;
                break;
            case R.id.rb_realizados:
                if (checked)
                    realizados = true;
                break;
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
}
