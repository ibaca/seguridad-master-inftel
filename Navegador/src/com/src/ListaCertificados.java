package com.src;

import java.util.ArrayList;

import com.google.android.maps.ItemizedOverlay;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;

public class ListaCertificados extends ListActivity {
	ListView lv;
	String[] names = new String[] { "Issues", "Request for Information",
			"Contracts", "Purchase Orders", "Change Orders", "Proposals",
			"Submittals" };
	String nombre = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("MIO", "he creado la lista");
		lv = getListView();
		lv.setCacheColorHint(Color.TRANSPARENT);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, names);
		lv.setAdapter(adapter);

	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Log.d("MIO", Integer.toString(position));
		SharedPreferences settings = getSharedPreferences("certificado", MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("nombre", names[position]);
		editor.commit();
		Log.d("MIO", names[position]);
		this.finish();
	}
}