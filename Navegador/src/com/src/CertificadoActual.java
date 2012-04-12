package com.src;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
            KeyStore clientCert = KeyStore.getInstance("pkcs12");
            FileInputStream fis = new FileInputStream(new File("/sdcard/certificados/" + nombre));
            clientCert.load(fis, "inftel".toCharArray());
            try{
                Enumeration<String> es=clientCert.aliases();
                String s = "";
                while(es.hasMoreElements()){
                    s=es.nextElement();
                    Log.d("MIO", s);
                }
                X509Certificate x509=(X509Certificate) clientCert.getCertificate(s);
                String issuerNombre=x509.getIssuerDN().getName();
                issuerNombre=issuerNombre.substring(issuerNombre.indexOf("L"),issuerNombre.length());
                String ciudad=issuerNombre.substring(issuerNombre.indexOf("L")+2, issuerNombre.indexOf(","));
                issuerNombre=issuerNombre.substring(issuerNombre.indexOf("O"),issuerNombre.length());
                String org=issuerNombre.substring(issuerNombre.indexOf("O")+2, issuerNombre.indexOf(","));
                issuerNombre=issuerNombre.substring(issuerNombre.indexOf("CN"),issuerNombre.length());
                String cn=issuerNombre.substring(issuerNombre.indexOf("CN")+3, issuerNombre.length());
                ((TextView)findViewById(R.id.textView11)).setText(nombre);
                ((TextView)findViewById(R.id.textView21)).setText(cn);
                ((TextView)findViewById(R.id.textView31)).setText(org);
                ((TextView)findViewById(R.id.textView41)).setText(cn);
                ((TextView)findViewById(R.id.textView51)).setText(x509.getNotBefore().toGMTString());
                ((TextView)findViewById(R.id.textView61)).setText(x509.getNotAfter().toGMTString());
                ((TextView)findViewById(R.id.textView71)).setText(ciudad);
                ((TextView)findViewById(R.id.textView81)).setText(x509.getSigAlgOID());
            }
            catch (Exception e) {
                Log.d("MIO", "a saltado la excepcion" + nombre, e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
