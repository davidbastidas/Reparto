package com.dbr;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dbr.Controlador.GestorConexion;
import com.dbr.Controlador.UsuarioController;
import com.dbr.Modelos.Constants;
import com.dbr.Modelos.SesionSingleton;
import com.dbr.Modelos.Usuario;
import com.dbr.Vistas.AdminActivity;
import com.dbr.Vistas.OperarioActivity;

import org.json.JSONObject;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    EditText e_usuario, e_contrasena;
    Button b_entrar;
    boolean PASA_LOGIN = false;
    ProgressDialog progressDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        e_usuario = findViewById(R.id.e_usuario);
        e_contrasena = findViewById(R.id.e_contrasena);
        b_entrar = findViewById(R.id.b_entrar);

        b_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        preferenciasDeInicio();
        checkPermission();
    }

    public void login(){
        final String usuario = e_usuario.getText().toString().trim();
        final String pass = e_contrasena.getText().toString().trim();

        Intent intentar = null;
        if(usuario.equals("0000") && !pass.equals("")){
            if (pass.length() == 8) {
                String sub = pass.substring(4, 8);
                if (sub.equals("0000")) {
                    resetearContrasenaAdmin(this);
                } else {
                    Toast.makeText(this, "Contraseña debe ser de 4 Numeros",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                SesionSingleton sesion = SesionSingleton.getInstance();
                if (!sesion.getPasswordAdmin().equals("")) {
                    if (pass.equals(sesion.getPasswordAdmin())) {
                        // mostrar la vista admin
                        intentar = new Intent(this, AdminActivity.class);
                        startActivity(intentar);
                    } else {
                        Toast.makeText(this, "Contraseña incorrecta",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (pass.equals("0000")) {
                        // mostrar la vista admin
                        intentar = new Intent(this, AdminActivity.class);
                        startActivity(intentar);
                    } else {
                        Toast.makeText(this, "Contraseña incorrecta",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

        } else if(!usuario.equals("") && !pass.equals("")){
            progressDialog = Constants.dialogIndeterminate(this, "Conectando...");
            //probar logueando por internet
            new AsyncTask<String, Void, String>(){
                @Override
                protected String doInBackground(String... params) {
                    GestorConexion con = new GestorConexion();
                    return con.login(usuario, pass);
                }
                @Override
                protected void onPostExecute(String result) {
                    alFinalizarLogin(result);
                }
            }.execute();
        } else {
            Toast.makeText(this, "Contraseña y Usuario incorrecto",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void alFinalizarLogin(String result) {
        System.out.println("alFinalizarLogin= " + result);
        SesionSingleton se = SesionSingleton.getInstance();
        UsuarioController usu = new UsuarioController();
        JSONObject json_data = null;
        boolean usuarioEncontrado = false;
        try{
            json_data = new JSONObject(result);
            if(json_data.getBoolean("estado")){
                usuarioEncontrado = true;
            }else{
                Toast.makeText(this, Constants.MSG_PETICION_RECHAZADA, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e){
            Toast.makeText(this, Constants.MSG_FORMATO_NO_VALIDO + e, Toast.LENGTH_LONG).show();
        } finally {

        }

        try {
            if(usuarioEncontrado){
                if(usu.count("", this) > 0){
                    ContentValues registro=new ContentValues();
                    registro.put("nombre", json_data.getString("nombre"));
                    registro.put("nickname", json_data.getString("nickname"));
                    registro.put("tipo", json_data.getInt("tipo"));
                    registro.put("fk_delegacion", json_data.getLong("fk_delegacion"));
                    registro.put("fk_id", json_data.getInt("fk_id"));
                    usu.actualizar(registro, "id = 1", this);
                }else{
                    Usuario user = new Usuario();
                    user.setNombre(json_data.getString("nombre"));
                    user.setNickname(json_data.getString("nickname"));
                    user.setTipo(json_data.getInt("tipo"));
                    user.setFkDelegacion(json_data.getInt("fk_delegacion"));
                    user.setFkId(json_data.getInt("fk_id"));
                    usu.insertar(user, this);
                }
                se.setFkId(json_data.getInt("fk_id"));
                se.setNombreUsuario(json_data.getString("nombre"));
                progressDialog.dismiss();
                Toast.makeText(this, "Login OK", Toast.LENGTH_SHORT).show();
                Intent intentar = new Intent(this, OperarioActivity.class);
                this.startActivity(intentar);
            } else {
                Usuario usuario = usu.consultar(0, 0, "id=1", this);
                if(usuario != null){
                    if(usuario.getNickname().equals(e_usuario.getText().toString().trim())){
                        se.setFkId(usuario.getFkId());
                        se.setNombreUsuario(usuario.getNombre());
                        Toast.makeText(this, "Verifica tu conexion, no estas Online",
                                Toast.LENGTH_LONG).show();
                        Intent intentar = new Intent(this, OperarioActivity.class);
                        this.startActivity(intentar);
                    }
                }else{
                    Toast.makeText(this, "No se ha encontrado este Usuario en el telefono, Intenta de nuevo",
                            Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            System.out.println("Exception: "+e);
            Toast.makeText(this, Constants.MSG_LEYENDO_DATOS + "de sesion. " + e, Toast.LENGTH_LONG).show();
        } finally {
            progressDialog.dismiss();
        }
    }

    public void resetearContrasenaAdmin(Activity activity){
        SharedPreferences preferencias = activity.getSharedPreferences(Constants.CONFIGURACION,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString(Constants.PASSWORDADMIN, "0000");
        editor.commit();
    }

    private void preferenciasDeInicio() {
        PASA_LOGIN = false;
        SharedPreferences preferencias = getSharedPreferences(Constants.CONFIGURACION,
                Context.MODE_PRIVATE);
        SesionSingleton sesion = SesionSingleton.getInstance();
        SharedPreferences.Editor editor = preferencias.edit();
        if(preferencias.getString(Constants.IP, "").equals("")){
            editor.putString(Constants.IP, "52.14.94.46");
            //editor.putString("ip", "192.168.100.133");
        }
        if(preferencias.getString(Constants.RUTAWEB, "").equals("")){
            editor.putString(Constants.RUTAWEB, "controloperativo/public");
        }
        editor.commit();

        preferencias = getSharedPreferences(Constants.CONFIGURACION,
                Context.MODE_PRIVATE);
        sesion.setIp(preferencias.getString(Constants.IP, ""));
        sesion.setRuta(preferencias.getString(Constants.RUTAWEB, ""));
        sesion.setRutaMemoriaTelefono(Environment.getExternalStorageDirectory() + File.separator);

        sesion.setPasswordAdmin(preferencias.getString(Constants.PASSWORDADMIN,""));
        sesion.setPasswordServicios(preferencias.getString(Constants.PASSWORDSERVICIOS,""));
        sesion.setEstadoDatos(preferencias.getString(Constants.ESTADODATOS,""));
        sesion.setEstadoEnvio(preferencias.getString(Constants.ESTADOENVIO,""));
        PASA_LOGIN = true;
    }

    private void checkPermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //no es version 6 api 23
        } else {
            int permsRequestCode = Constants.PERMISOS_REQUEST_CODE;
            String[] perms = {
                    Manifest.permission.INTERNET,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA
            };
            int accessInternetPermission = checkSelfPermission(Manifest.permission.INTERNET);
            int accessWiteExternalPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int accessFinePermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            int cameraPermission = checkSelfPermission(Manifest.permission.CAMERA);

            if (accessInternetPermission == PackageManager.PERMISSION_GRANTED &&
                    accessWiteExternalPermission == PackageManager.PERMISSION_GRANTED &&
                    accessFinePermission == PackageManager.PERMISSION_GRANTED &&
                    cameraPermission == PackageManager.PERMISSION_GRANTED) {
                //se realiza metodo si es necesario...
            } else {
                requestPermissions(perms, permsRequestCode);
            }
        }

        return;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISOS_REQUEST_CODE:
                // accion o metodo realizar
                break;
        }
    }
}
