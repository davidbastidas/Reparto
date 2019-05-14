package com.dbl.Vistas.Adaptader;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.dbl.Modelos.Anomalias;
import com.dbl.R;

import java.util.ArrayList;

public class AdapterAnomalias extends BaseAdapter implements Filterable {
	private final Activity actividad;
	private ArrayList<Anomalias> anomaliasOriginal;
	private ArrayList<Anomalias> anomaliasFiltrado;

	View view;
	LayoutInflater inflater;
	TextView titulo;

	public AdapterAnomalias(Activity actividad, ArrayList<Anomalias> anomalias){
		super();
		this.actividad = actividad;
		this.anomaliasOriginal = anomalias;
		this.anomaliasFiltrado = anomalias;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		inflater = actividad.getLayoutInflater();
		view = inflater.inflate(R.layout.adapter_anomalias, null, true);

		titulo = view.findViewById(R.id.t_titulo);
		titulo.setText(anomaliasFiltrado.get(position).getNombre().toUpperCase());
		return view;
	}

	public int getCount() {
		return anomaliasFiltrado.size();
	}

	public Object getItem(int arg0) {
		return anomaliasFiltrado.get(arg0);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public Filter getFilter() {

		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {

				anomaliasFiltrado = (ArrayList<Anomalias>) results.values;
				if (results.count > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				FilterResults results = new FilterResults();
				ArrayList<Anomalias> FilteredArrayNames = new ArrayList<Anomalias>();

				// perform your search here using the searchConstraint String.

				constraint = constraint.toString().toLowerCase();
				for (int i = 0; i < anomaliasOriginal.size(); i++) {
					Anomalias anomalia = AdapterAnomalias.this.anomaliasOriginal.get(i);
					if (anomalia.getNombre().toLowerCase().contains(constraint.toString()))  {
						FilteredArrayNames.add(anomalia);
					}
				}

				results.count = FilteredArrayNames.size();
				results.values = FilteredArrayNames;

				return results;
			}
		};

		return filter;
	}
}
