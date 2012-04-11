package com.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.apache.http.conn.ssl.SSLSocketFactory;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

public class CertificadoActual extends SherlockActivity {
    String nombre="";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.certificadoactual);
        SharedPreferences settings = getSharedPreferences("certificado", MODE_PRIVATE);
        nombre = settings.getString("nombre", "");
        try {
            FileInputStream fis=new FileInputStream(new File(nombre));
            ((TextView)findViewById(R.id.textView11)).setText("");
            ((TextView)findViewById(R.id.textView21)).setText("");
            ((TextView)findViewById(R.id.textView31)).setText("");
            ((TextView)findViewById(R.id.textView41)).setText("");
            ((TextView)findViewById(R.id.textView51)).setText("");
            ((TextView)findViewById(R.id.textView61)).setText("");
            ((TextView)findViewById(R.id.textView71)).setText("");
            ((TextView)findViewById(R.id.textView81)).setText("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
    }
}
