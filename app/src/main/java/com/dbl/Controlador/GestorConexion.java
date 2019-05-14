package com.dbl.Controlador;


import com.dbl.Modelos.Constants;
import com.dbl.Modelos.Auditorias;
import com.dbl.Modelos.Pci;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GestorConexion {
    public String login(String user, String password){
        JSONObject obj = new JSONObject();
        try {
            obj.put("user", user);
            obj.put("password", password);
            return peticionWeb(Constants.ROUTE_LOGIN, obj);
        } catch (JSONException e) {
            return "" + e;
        }
    }

    public String descargarServicios(int userId){
        JSONObject obj = new JSONObject();
        try {
            obj.put("user", userId);
            return peticionWeb(Constants.ROUTE_SERVICIOS, obj);
        } catch (JSONException e) {
            return "" + e;
        }
    }

    public String enviarAuditoria(Auditorias audioria, int userId){
        JSONObject obj = new JSONObject();
        try {
            obj.put("user", userId);
            obj.put("id", audioria.getId());
            obj.put("lectura", audioria.getLectura());
            obj.put("anomalia", audioria.getAnomalia());
            obj.put("habitado", audioria.getHabitado());
            obj.put("visible", audioria.getVisible());
            obj.put("observacion_rapida", audioria.getObservacionRapida());
            obj.put("observacion_analisis", audioria.getObservacionAnalisis());
            obj.put("latitud", audioria.getLatitud());
            obj.put("longitud", audioria.getLongitud());
            obj.put("orden_realizado", audioria.getOrden());
            obj.put("fecha_realizado", audioria.getFechaRealizado());
            obj.put("foto", audioria.getFoto());
            return peticionWeb(Constants.ROUTE_ACTUALIZAR_AUDITORIA, obj);
        } catch (JSONException e) {
            return "" + e;
        }
    }

    public String enviarPci(Pci pci, int userId){
        JSONObject obj = new JSONObject();
        try {
            obj.put("user", userId);
            obj.put("id", pci.getId());
            obj.put("lectura", pci.getLectura());
            obj.put("anomalia", pci.getAnomalia());
            obj.put("observacion_analisis", pci.getObservacionAnalisis());
            obj.put("latitud", pci.getLatitud());
            obj.put("longitud", pci.getLongitud());
            obj.put("orden_realizado", pci.getOrden());
            obj.put("fecha_realizado", pci.getFechaRealizado());
            obj.put("foto", pci.getFoto());
            return peticionWeb(Constants.ROUTE_ACTUALIZAR_PCI, obj);
        } catch (JSONException e) {
            return "" + e;
        }
    }

    public String peticionWeb(String urlWeb, JSONObject obj){
        String respuesta="";
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(urlWeb);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/json");
            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(30000);
            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.write(obj.toString().getBytes("UTF-8"));
            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuffer response = new StringBuffer();
            while((respuesta = rd.readLine()) != null) {
                response.append(respuesta);
                response.append('\r');
            }
            rd.close();
            respuesta = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "" +e;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
        return respuesta;
    }
}
