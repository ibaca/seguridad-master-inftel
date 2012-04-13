
package com.src;

import java.io.File;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListActivity;

public class ListaCertificados extends SherlockListActivity {
    ListView lv;
    String[] names;
    String nombre = "";
    private static final String RUTA_CERT = "certificados";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MIO", "he creado la lista");
        File dir = new File(getDir(RUTA_CERT, MODE_PRIVATE).getAbsolutePath() + "/");
        names = dir.list();
        lv = getListView();
        lv.setCacheColorHint(Color.TRANSPARENT);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.radio_group, names);
        lv.setAdapter(adapter);
        lv.setBackgroundResource(R.color.blanco);
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
