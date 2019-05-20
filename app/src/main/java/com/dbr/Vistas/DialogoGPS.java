package com.dbr.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dbr.R;

public class DialogoGPS{

	private OnGPSIntent callback = null;
	String latitud, longitud, acurracy;
	Activity actividad;
	public DialogoGPS(OnGPSIntent callback, String latitud, String longitud, String acurracy , Activity actividad){
		this.callback = callback;
		this.latitud = latitud;
		this.longitud = longitud;
		this.acurracy = acurracy;
		this.actividad = actividad;
	}
    public interface OnGPSIntent {
        public void onGuardarConPuntoElegido(String latitud, String longitud, String acurracy);
        public void onSeguirIntentandoGPS();
    }
	TextView gps_texto;

	public Dialog showMyDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(actividad);
		View view = actividad.getLayoutInflater().inflate(
				R.layout.gps_intentar, null);
		gps_texto = (TextView) view.findViewById(R.id.gps_texto);
		gps_texto.setText(
				"¿Desea guardar estas Coordenadas?\nLatitud: "+latitud+
				"\nLongitud: "+longitud+
				"\nPrecisión: "+acurracy+" Metros");
		builder.setView(view);
		builder.setTitle("Punto GPS");
		builder.setNegativeButton("Guardar",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						/**/
					}
				});
		builder.setPositiveButton("Intentar con Otro",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						/**/
					}
				});
		builder.setCancelable(true);
		AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {
                final DialogInterface d = dialog;
                Button buttonOK = ((AlertDialog)dialog).getButton( DialogInterface.BUTTON_POSITIVE );
                buttonOK.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                    	callback.onSeguirIntentandoGPS();
                    	dialog.dismiss();
                    }
                });
                Button buttonNegative = ((AlertDialog)dialog).getButton( DialogInterface.BUTTON_NEGATIVE );
                buttonNegative.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                    	callback.onGuardarConPuntoElegido(latitud, longitud, acurracy);
						dialog.dismiss();
                    }
                });
            }
        });
		return dialog;
	}
}
