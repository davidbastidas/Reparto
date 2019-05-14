package com.dbl.Vistas.Adaptader;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.dbl.Modelos.Servicio;
import com.dbl.R;

import java.util.ArrayList;

public class AdapterServicios extends BaseAdapter implements Filterable {
	private final Activity actividad;
	private ArrayList<Servicio> servicios;
	private ArrayList<Servicio> serviciosFiltrados;

	View view;
	LayoutInflater inflater;
	TextView titulo, barrio;

	public AdapterServicios(Activity actividad, ArrayList<Servicio> servicios){
		super();
		this.actividad = actividad;
		this.servicios = servicios;
		this.serviciosFiltrados = servicios;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		inflater = actividad.getLayoutInflater();
		view = inflater.inflate(R.layout.adapter_servicios, null, true);

		titulo = view.findViewById(R.id.t_titulo);
		barrio = view.findViewById(R.id.t_subtitulo);
		titulo.setText(serviciosFiltrados.get(position).getTitulo());
		barrio.setText(serviciosFiltrados.get(position).getSubtitulo());
		return view;
	}

	public int getCount() {
		return serviciosFiltrados.size();
	}

	public Object getItem(int arg0) {
		return serviciosFiltrados.get(arg0);
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

				serviciosFiltrados = (ArrayList<Servicio>) results.values;
				if (results.count > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				FilterResults results = new FilterResults();
				ArrayList<Servicio> FilteredArrayNames = new ArrayList<>();

				// perform your search here using the searchConstraint String.

				constraint = constraint.toString().toLowerCase();
				for (int i = 0; i < servicios.size(); i++) {
					Servicio servicio = servicios.get(i);
					if ((servicio.getTitulo() + " " + servicio.getSubtitulo()).toLowerCase().contains(constraint.toString()))  {
						FilteredArrayNames.add(servicio);
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
