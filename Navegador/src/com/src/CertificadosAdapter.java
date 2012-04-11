package com.src;

import java.util.*;

import android.content.*;
import android.view.*;
import android.widget.*;

public class CertificadosAdapter extends ArrayAdapter<String> {
	private Context context;
	private ArrayList<String> certificados;
	private String nombre;
	
	public CertificadosAdapter(Context context, ArrayList<String> certificados, String nombre) {
		super(context, R.layout.listacertificados, certificados);
//		super(context, android.R.layout.simple_list_item_single_choice, certificados);
		this.context = context;
		this.certificados = certificados;
		this.nombre=nombre;
	}

	@Override
	public int getCount() {
		return certificados.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.radio_group, parent, false);

		// Obtención de los elementos de la UI de la fila
		RadioButton rb=(RadioButton) rowView.findViewById(R.id.radioButton11);
		rb.setText("       "+certificados.get(position));
		if(rb.getText().toString().compareTo(nombre)==0){
			rb.setChecked(true);
		}

		return rowView;
	}

}
