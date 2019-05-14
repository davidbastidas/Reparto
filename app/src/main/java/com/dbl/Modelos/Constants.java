package com.dbl.Modelos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.Toast;

import java.io.File;

public final class Constants {

    private static SesionSingleton sesion = SesionSingleton.getInstance();

    public static final int PERMISOS_REQUEST_CODE = 100;
    public static final int SERVICIO_REQUEST_CODE = 999;
    public static final int FOTO_REQUEST_CODE = 998;

    /**anomalias*/
    public static final int AN001_001 = 2;
    public static final int AN088 = 44;


    public static final int EXTRA_SERVICIO_TIPO_AUDITORIA = 1;
    public static final int EXTRA_SERVICIO_TIPO_PCI = 2;
    public static final String EXTRA_SERVICIO_ID = "servicio_id";
    public static final String EXTRA_SERVICIO_TIPO_ID = "servicio_tipo_id";
    public static final String EXTRA_CT = "ct";
    public static final String EXTRA_MT = "mt";
    public static final String EXTRA_NIC = "nic";
    public static final String EXTRA_MEDIDOR = "medidor";
    public static final String EXTRA_DIRECCION = "direccion";
    public static final String EXTRA_REALIZADO = "realizado";
    public static final String EXTRA_BARRIO = "barrio";

    public static final String DB_NAME = "db_lectura";
    public static final String TABLA_USUARIOS = "usuarios";
    public static final String TABLA_AUDITORIAS = "auditorias";
    public static final String TABLA_PCI = "pci";
    public static final String TABLA_ANOMALIAS = "anomalias";
    public static final String TABLA_OBSERVACION_RAPIDA = "observacion_rapida";

    /** nombre de la configuracion base*/
    public static final String CONFIGURACION = "configuracion";
    public static final String PASSWORDADMIN = "passwordAdmin";
    public static final String PASSWORDSERVICIOS = "passwordServicios";
    public static final String ESTADODATOS = "estado_datos";
    public static final String ESTADOENVIO = "estado_envio";
    public static final String IP = "ip";
    public static final String RUTAWEB = "ruta";

    /** url*/
    //ip/control/api/public/--ruta de el controlador--/
    private static String URL_BASE = "http://" + sesion.getIp() + File.separator + sesion.getRuta() + File.separator +  "api" + File.separator;
    public static final String ROUTE_LOGIN = URL_BASE + "login";
    public static final String ROUTE_SERVICIOS = URL_BASE + "servicios/getServicios";
    public static final String ROUTE_ACTUALIZAR_AUDITORIA = URL_BASE + "servicios/actualizarAuditoria";
    public static final String ROUTE_ACTUALIZAR_PCI = URL_BASE + "servicios/actualizarPci";

    public static ProgressDialog dialogIndeterminate(Context context, String mensaje){
        ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Procesando...");
        pd.setMessage(mensaje);
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
        return pd;
    }

    /** mensajes estandar*/
    public static final String MSG_FORMATO_NO_VALIDO = "La peticion no se realizo. Verifique su conexion o Contacte al administrador.";
    public static final String MSG_SINCRONIZACION = "La lectura no se pudo enviar.";
    public static final String MSG_PETICION_RECHAZADA = "Peticion Rechazada.";
    public static final String MSG_LEYENDO_DATOS = "Ocurrio un error leyendo los datos ";
    public static final String MSG_ENVIO_SERVICIO = "Lectura enviada con exito!.";

    public static boolean isGpsActivo(Activity actividad){
        boolean status = false;
        LocationManager locManager = null;
        locManager = (LocationManager) actividad.getSystemService(Context.LOCATION_SERVICE);
        if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            status = true;
        }
        return status;
    }

    public static void ActivarGPS(Activity actividad) {
        Toast.makeText(actividad, "Por favor active su GPS", Toast.LENGTH_SHORT)
                .show();
        Intent settingsIntent = new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        actividad.startActivityForResult(settingsIntent, 1);
    }
}
