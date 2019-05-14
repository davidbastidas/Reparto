package com.dbl.Vistas;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dbl.BuildConfig;
import com.dbl.Controlador.ObservacionRapidaController;
import com.dbl.Controlador.AuditoriaController;
import com.dbl.Controlador.PciController;
import com.dbl.Modelos.Constants;
import com.dbl.Modelos.ObservacionRapida;
import com.dbl.Modelos.Pci;
import com.dbl.Modelos.SesionSingleton;
import com.dbl.Modelos.ServicioSesion;
import com.dbl.Modelos.Auditorias;
import com.dbl.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ObservacionActivity extends AppCompatActivity implements DialogoGPS.OnGPSIntent {

    Button b_foto, b_finalizar;
    Spinner s_observacion;
    EditText e_lectura, e_observacion;
    TextView t_lectura, textHabitado, textVisible, textTipoServicio;
    RadioButton rb_hab_si, rb_hab_no;
    RadioButton rb_an_si, rb_an_no;
    RadioGroup rg_habitado, rg_visible;
    ObservacionRapida obsElegida;
    ProgressDialog progressDialog = null;
    Auditorias auditoriaEnviar = null;
    Pci pciEnviar = null;
    int lecturaActual = -1;
    int lecturaDesviada = -1;
    boolean cancelarCritica = false;
    private int desviacionAceptda = 30;

    /** OPCIONES DEL GPS*/
    private ObservacionActivity listenerGps;
    LocationManager locManager = null;
    private boolean guardadoActivo = true, pasarConPuntoelegido = false;
    LocationListener locListener = null;
    private int ESTADO_SERVICE = 0;
    private static final int OUT_OF_SERVICE = 0;
    private static final int TEMPORARILY_UNAVAILABLE = 1;
    private static final int AVAILABLE = 2;
    private String LONGITUD = "0.0", LATITUD = "0.0", ACURRACY = "0";
    private String LONGITUD_FINAL = "0.0", LATITUD_FINAL = "0.0", ACURRACY_FINAL = "0";
    private int limitTimeSecond = 30;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            if(limitTimeSecond == 0){
                if(!b_finalizar.isEnabled()){
                    timerHandler.removeCallbacks(timerRunnable);
                    b_foto.setEnabled(true);
                    b_finalizar.setEnabled(true);
                    b_finalizar.setText("FINALIZAR Y GUARDAR");
                }
            }else{
                limitTimeSecond = limitTimeSecond - 1;
                b_finalizar.setText("Esperando el Punto GPS... " + limitTimeSecond);

                timerHandler.postDelayed(this, 1000);
            }
        }
    };
    private File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observacion);
        setTitle("Lectura y Otros");

        listenerGps = this;

        b_foto = findViewById(R.id.b_foto);
        b_finalizar = findViewById(R.id.b_finalizar);
        s_observacion = findViewById(R.id.s_observacion);
        e_observacion = findViewById(R.id.e_observacion);
        e_lectura = findViewById(R.id.e_lectura);
        t_lectura = findViewById(R.id.textLectura);
        textHabitado = findViewById(R.id.textHabitado);
        textVisible = findViewById(R.id.textVisible);
        textTipoServicio = findViewById(R.id.textTipoServicio);
        rb_hab_si = findViewById(R.id.rb_hab_si);
        rb_hab_no = findViewById(R.id.rb_hab_no);
        rb_an_si = findViewById(R.id.rb_an_si);
        rb_an_no = findViewById(R.id.rb_an_no);
        rg_habitado = findViewById(R.id.rg_habitado);
        rg_visible = findViewById(R.id.rg_visible);

        if (ServicioSesion.getInstance().getTipoServicio() == Constants.EXTRA_SERVICIO_TIPO_PCI) {
            rg_habitado.setVisibility(View.INVISIBLE);
            rg_visible.setVisibility(View.INVISIBLE);
            s_observacion.setVisibility(View.INVISIBLE);
            textHabitado.setVisibility(View.INVISIBLE);
            textVisible.setVisibility(View.INVISIBLE);
            textTipoServicio.setVisibility(View.INVISIBLE);
            desviacionAceptda = ServicioSesion.getInstance().getDesviacionAceptda();
        }

        if (ServicioSesion.getInstance().getPideLectura() == 0) {
            t_lectura.setVisibility(View.INVISIBLE);
            e_lectura.setVisibility(View.INVISIBLE);
            e_lectura.setEnabled(false);
        }

        if(ServicioSesion.getInstance().getPideGps() == 1){
            b_foto.setEnabled(false);
            b_finalizar.setEnabled(false);
            b_finalizar.setText("Esperando el Punto GPS...");
        } else {
            b_foto.setEnabled(true);
            b_finalizar.setEnabled(true);
            b_finalizar.setText("FINALIZAR Y GUARDAR");
        }

        if (ServicioSesion.getInstance().getObservacionAnalisis() != null) {
            e_observacion.setText(ServicioSesion.getInstance().getObservacionAnalisis());
        }

        ObservacionRapidaController obs = new ObservacionRapidaController();
        ArrayList<ObservacionRapida> observaciones = obs.consultar(0, 0, "", this);
        ArrayAdapter<ObservacionRapida> obsAdapter = new ArrayAdapter<ObservacionRapida>(this,
                android.R.layout.simple_spinner_item, observaciones);
        obsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        s_observacion.setAdapter(obsAdapter);
        if (ServicioSesion.getInstance().getObservacionRapida() != 0) {
            ArrayList<ObservacionRapida> obs2 = obs.consultar(0, 0, "id=" + ServicioSesion.getInstance().getObservacionRapida(), this);
            s_observacion.setSelection(getIndex(s_observacion, obs2.get(0).getNombre()));
        } else {
            ObservacionRapida ob = new ObservacionRapida();
            ob.setId(4);//sin observacion en la Base de datos
            s_observacion.setSelection(getIndex(s_observacion, "Sin observación"));
        }

        s_observacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                obsElegida = (ObservacionRapida) parentView.getItemAtPosition(position);
                ServicioSesion.getInstance().setObservacionRapida(obsElegida.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                obsElegida = null;
            }
        });

        b_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        b_finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarGuardar();
            }
        });
        if(ServicioSesion.getInstance().getPideGps() == 1){
            comenzarLocalizacion();
            timerHandler.postDelayed(timerRunnable, 0);
        }
    }

    private void validarGuardar() {
        boolean pasa = true;
        boolean fotoRequerida = false, lecturaRequerida = false, observacionRequerida = false;
        String motivo = "";

        ServicioSesion servSess = ServicioSesion.getInstance();
        if(servSess.getPideFoto() == 1){
            fotoRequerida = true;
        }

        if(ServicioSesion.getInstance().isObservacionObligatoria()){
            observacionRequerida = true;
        }

        if(servSess.getPideLectura() == 1){
            lecturaRequerida = true;
        }

        if(fotoRequerida){
            if (servSess.getFoto() != null) {
                if (servSess.getFoto().equals("")) {
                    pasa = false;
                    motivo = "Debe tomar una foto para soporte.";
                }
            } else {
                pasa = false;
                motivo = "Debe tomar una foto para soporte.";
            }
        }
        if(observacionRequerida){
            if (e_observacion.getText().toString().trim().equals("")) {
                pasa = false;
                switch ((int) servSess.getAnomalia()){
                    case Constants.AN001_001:
                        motivo = "Ingrese el numero del MEDIDOR o NIC unificado.";
                        break;
                    case Constants.AN088:
                        motivo = "Escriba los DIGITOS diferentes.";
                        break;
                    default:
                        motivo = "Debe escribir la observacion.";
                        break;
                }
            }
        }
        if(lecturaRequerida){
            if (e_lectura.getText().toString().trim().equals("")) {
                pasa = false;
                motivo = "Debe escribir la Lectura.";
            }else{
                if(ServicioSesion.getInstance().getTipoServicio() == Constants.EXTRA_SERVICIO_TIPO_PCI){
                    //validar la critica
                    int lectura1 = ServicioSesion.getInstance().getLectura1();
                    int lectura2 = ServicioSesion.getInstance().getLectura2();
                    if(lectura1 == -1 || lectura2 == -1){
                        Toast.makeText(ObservacionActivity.this, "SIN CRITICA.", Toast.LENGTH_LONG).show();
                    }else{
                        lecturaActual = Integer.parseInt(e_lectura.getText().toString());
                        int consumoActual = lecturaActual - lectura1;
                        int consumoAnterior = lectura1 - lectura2;
                        double desviacion = 0;
                        if(consumoAnterior > 0){
                            desviacion = Math.ceil(((consumoAnterior - consumoActual)/consumoAnterior)*100);
                        }
                        if (desviacion > desviacionAceptda || desviacion > -desviacionAceptda){
                            if(lecturaActual == lecturaDesviada){
                                if(!cancelarCritica){
                                    Toast.makeText(ObservacionActivity.this, "LECTURA DESVIADA.", Toast.LENGTH_LONG).show();
                                    cancelarCritica = true;
                                }
                            }else{
                                lecturaDesviada = lecturaActual;
                                pasa = false;
                                motivo = "LECTURA DESVIADA, POR FAVOR VERIFICA LA LECTURA.";
                                cancelarCritica = false;
                            }
                        }
                    }
                }
            }
        }

        if(pasa){
            ServicioSesion.getInstance().setObservacionAnalisis(e_observacion.getText().toString());

            if(ServicioSesion.getInstance().getPideGps() == 1){
                validarGuardarGps();
            } else {
                guardarVisita();
            }
        }else{
            Toast.makeText(ObservacionActivity.this, motivo, Toast.LENGTH_LONG).show();
        }
    }

    //private method of your class
    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

    private void guardarVisita() {
        progressDialog = Constants.dialogIndeterminate(this, "Guardando...");
        if(!LATITUD.equals("0.0")){
            Toast.makeText(this, "SE CAPTURO EL PUNTO.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "SIN PUNTO GPS.", Toast.LENGTH_SHORT).show();
        }
        ServicioSesion sesion = ServicioSesion.getInstance();
        sesion.setLectura(e_lectura.getText().toString());
        if(ServicioSesion.getInstance().getTipoServicio() == Constants.EXTRA_SERVICIO_TIPO_AUDITORIA){
            AuditoriaController audCont = new AuditoriaController();
            int orden = audCont.ultimoOrden(this);
            orden = orden + 1;

            auditoriaEnviar = new Auditorias();
            auditoriaEnviar.setId(sesion.getId());
            auditoriaEnviar.setAnomalia(sesion.getAnomalia());
            auditoriaEnviar.setLectura(sesion.getLectura());
            if(rb_hab_si.isChecked()){
                auditoriaEnviar.setHabitado(1);
            }else{
                auditoriaEnviar.setHabitado(0);
            }
            if(rb_an_si.isChecked()){
                auditoriaEnviar.setVisible(1);
            }else{
                auditoriaEnviar.setVisible(0);
            }
            auditoriaEnviar.setObservacionRapida(sesion.getObservacionRapida());

            auditoriaEnviar.setObservacionAnalisis(sesion.getObservacionAnalisis());
            auditoriaEnviar.setLatitud(LATITUD_FINAL);
            auditoriaEnviar.setLongitud(LONGITUD_FINAL);
            auditoriaEnviar.setOrden(sesion.getOrden());
            auditoriaEnviar.setFoto(sesion.getFoto());

            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new Date());

            ContentValues registro = new ContentValues();
            registro.put("anomalia", auditoriaEnviar.getAnomalia());
            registro.put("lectura", auditoriaEnviar.getLectura());
            registro.put("habitado", auditoriaEnviar.getHabitado());
            registro.put("visible", auditoriaEnviar.getVisible());
            registro.put("observacion_rapida", auditoriaEnviar.getObservacionRapida());
            registro.put("observacion_analisis", auditoriaEnviar.getObservacionAnalisis());
            registro.put("latitud", auditoriaEnviar.getLatitud());
            registro.put("longitud", auditoriaEnviar.getLongitud());
            registro.put("orden", orden);
            registro.put("foto", auditoriaEnviar.getFoto());
            registro.put("fecha_realizado", date);
            registro.put("estado", 1);
            registro.put("lector_realiza_id", SesionSingleton.getInstance().getFkId());
            audCont.actualizar(registro, "id = " + ServicioSesion.getInstance().getId(), this);
        } else {
            PciController pciCont = new PciController();
            int orden = pciCont.ultimoOrden(this);
            orden = orden + 1;

            pciEnviar = new Pci();
            pciEnviar.setId(sesion.getId());
            pciEnviar.setAnomalia(sesion.getAnomalia());
            pciEnviar.setLectura(sesion.getLectura());
            pciEnviar.setObservacionAnalisis(sesion.getObservacionAnalisis());
            pciEnviar.setLatitud(LATITUD_FINAL);
            pciEnviar.setLongitud(LONGITUD_FINAL);
            pciEnviar.setOrden(sesion.getOrden());
            pciEnviar.setFoto(sesion.getFoto());

            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new Date());

            ContentValues registro = new ContentValues();
            registro.put("anomalia", pciEnviar.getAnomalia());
            registro.put("lectura", pciEnviar.getLectura());
            registro.put("observacion_analisis", pciEnviar.getObservacionAnalisis());
            registro.put("latitud", pciEnviar.getLatitud());
            registro.put("longitud", pciEnviar.getLongitud());
            registro.put("orden", orden);
            registro.put("foto", pciEnviar.getFoto());
            registro.put("fecha_realizado", date);
            registro.put("estado", 1);
            registro.put("lector_realiza_id", SesionSingleton.getInstance().getFkId());
            pciCont.actualizar(registro, "id = " + ServicioSesion.getInstance().getId(), this);
        }

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        ServicioSesion.resetSesion();
        progressDialog.dismiss();
        finish();
    }

    private void validarGuardarGps() {
        if (pasarConPuntoelegido) {
            if (guardadoActivo) {
                guardadoActivo = false;
                pasarConPuntoelegido = false;
                guardarVisita();
            }
        } else {
            DialogoGPS dgps = new DialogoGPS(listenerGps, LATITUD, LONGITUD, ACURRACY, this);
            dgps.showMyDialog().show();
        }
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
        }
        Uri outputFileUri = null;
        if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.N) {
            outputFileUri = FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    photoFile);
        } else{
            outputFileUri = Uri.fromFile(photoFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intent, Constants.FOTO_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.FOTO_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Bitmap b = BitmapFactory.decodeFile(mCurrentPhotoPath);
                        Bitmap out = Bitmap.createScaledBitmap(b, 480, 640, false);
                        FileOutputStream fOut = new FileOutputStream(photoFile);
                        out.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                        fOut.flush();
                        fOut.close();
                        b.recycle();
                        out.recycle();

                        ServicioSesion.getInstance().setFoto(getStringFromFile(photoFile));
                    } catch (Exception e) {
                        System.err.println("Error foto: " + e);
                    }

                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ServicioSesion.getInstance().setObservacionAnalisis(e_observacion.getText().toString());
        if (s_observacion.getSelectedItem() != null) {
            ServicioSesion.getInstance().setObservacionRapida(((ObservacionRapida) s_observacion.getSelectedItem()).getId());
        }
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    @Override
    public void onStop() {
        if (locListener != null) {
            locManager.removeUpdates(locListener);
            locListener = null;
            locManager = null;
        }
        super.onStop();
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,80, baos);
        byte [] b = baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private void comenzarLocalizacion() {
        locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if(locListener == null){
                locListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        System.out.println("onLocationChanged");
                        LATITUD = String.valueOf(location.getLatitude());
                        LONGITUD = String.valueOf(location.getLongitude());
                        ACURRACY = String.valueOf(location.getAccuracy());
                        if(!b_finalizar.isEnabled()){
                            timerHandler.removeCallbacks(timerRunnable);
                            b_foto.setEnabled(true);
                            b_finalizar.setEnabled(true);
                            b_finalizar.setText("FINALIZAR Y GUARDAR");
                        }
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        locManager.removeUpdates(locListener);
                        System.out.println("onProviderDisabled");
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    public void onStatusChanged(String provider, int status,
                                                Bundle extras) {
                        ESTADO_SERVICE = status;
                        mostrarEstadoGPS();
                    }
                };
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
        } else {
            ActivarGPS();
        }
    }

    private void mostrarEstadoGPS(){
        if(ESTADO_SERVICE==OUT_OF_SERVICE){
            Toast.makeText(this, "Servicio GPS no disponible", Toast.LENGTH_SHORT).show();
            System.out.println("OUT_OF_SERVICE");
        } else if(ESTADO_SERVICE==TEMPORARILY_UNAVAILABLE){
            System.out.println("TEMPORARILY_UNAVAILABLE");
            Toast.makeText(this, "Servicio GPS no disponible", Toast.LENGTH_SHORT).show();
        } else if(ESTADO_SERVICE==AVAILABLE){
            System.out.println("AVAILABLE");
            Toast.makeText(this, "GPS Disponible", Toast.LENGTH_SHORT).show();
        }
    }

    private void ActivarGPS() {
        Toast.makeText(this, "Por favor active su GPS", Toast.LENGTH_SHORT)
                .show();
        Intent settingsIntent = new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        this.startActivityForResult(settingsIntent, 1);
    }

    @Override
    public void onGuardarConPuntoElegido(String latitud,String longitud,String acurracy) {
        pasarConPuntoelegido = true;
        LONGITUD_FINAL = longitud;
        LATITUD_FINAL = latitud;
        ACURRACY_FINAL = acurracy;
        validarGuardarGps();
    }

    @Override
    public void onSeguirIntentandoGPS() {
        pasarConPuntoelegido = false;
        validarGuardarGps();
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private String getStringFromFile(File file) throws Exception {
        InputStream in = new FileInputStream(file);
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try{
            while ((bytesRead = in.read(buffer)) != -1){
                output.write(buffer, 0, bytesRead);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        String ret = Base64.encodeToString(bytes, Base64.DEFAULT);
        //Make sure you close all streams.
        in.close();
        return ret;
    }
}
