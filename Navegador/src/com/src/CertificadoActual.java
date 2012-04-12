package com.src;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.cert.CertificateFactory;

import javax.security.cert.X509Certificate;

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
            FileInputStream fin = new FileInputStream(new File("/sdcard/certificados/"+nombre));
            X509Certificate cert= X509Certificate.getInstance(fin);
//            X509Certificate.getInstance(fin);
            ((TextView)findViewById(R.id.textView11)).setText(cert.getIssuerDN().getName());
            ((TextView)findViewById(R.id.textView21)).setText(cert.getSubjectDN().getName());
            ((TextView)findViewById(R.id.textView31)).setText("");
            ((TextView)findViewById(R.id.textView41)).setText("");
            ((TextView)findViewById(R.id.textView51)).setText(cert.getNotBefore().toString());
            ((TextView)findViewById(R.id.textView61)).setText(cert.getNotAfter().toString());
            ((TextView)findViewById(R.id.textView71)).setText("");
            ((TextView)findViewById(R.id.textView81)).setText(cert.getSigAlgOID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
