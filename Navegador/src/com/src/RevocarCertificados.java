
package com.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.SherlockListActivity;

public class RevocarCertificados extends SherlockListActivity {
    private static final String PROD_URL = "https://home.bacamt.com:83/secure/list.phtml";
    private static final String LOGTAG = "https";
    private static final String RUTA_CERT = "certificados";
    String nombre;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences("certificado", MODE_PRIVATE);
        nombre = settings.getString("nombre", "");
        try {
            connect();
        } catch (Exception e) {
            Log.d("MIO", "dentro de navegador " + nombre, e);
        }
        // CONNECT GENERA EN LA VARIABLE CONTENIDO EL LISTADO CON LOS
        // CERTIFICADOS
    }

    /* Metodo encargado de conectar con Apache mediante SSL */
    public void connect() throws URISyntaxException, NoSuchAlgorithmException,
            CertificateException, IOException, KeyStoreException, KeyManagementException,
            UnrecoverableKeyException {

        HttpGet request = new HttpGet(new URI(PROD_URL));

        // Certificado de la Autoridad Certificador (CA)
        KeyStore trusted = KeyStore.getInstance("BKS");
        trusted.load(getResources().openRawResource(R.raw.truststore), "inftel".toCharArray());

        String FILENAME = getDir(RUTA_CERT, MODE_PRIVATE).getAbsolutePath() + "/" + nombre;
        Log.d(LOGTAG, FILENAME);

        // Certificado del cliente
        KeyStore clientCert = KeyStore.getInstance("pkcs12");
        FileInputStream fis = new FileInputStream(new File(FILENAME));
        clientCert.load(fis, "inftel".toCharArray());

        SSLSocketFactory sslf = new SSLSocketFactory(clientCert, null, trusted);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("https", sslf, 443));

        HttpClient client = new DefaultHttpClient(new ThreadSafeClientConnManager(
                request.getParams(), schemeRegistry), request.getParams());

        HttpResponse result = client.execute(request);

        /* Comprobamos que la respuesta que obtenemos es valida */
        final int statusCode = result.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            Log.w(LOGTAG, "Error " + statusCode + " for URL "
                    + PROD_URL);
        }
        HttpEntity getResponseEntity = result.getEntity();
        InputStream is = getResponseEntity.getContent();
        String contenido = convertinputStreamToString(is);
        Log.i(LOGTAG, "Content: " + contenido);
    }

    public String convertinputStreamToString(InputStream ists) throws IOException {

        if (ists != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader r1 = new BufferedReader(new InputStreamReader(ists, "UTF-8"));
                while ((line = r1.readLine()) != null) {
                    // Ya que es codigo HTML metemos saltos de linea
                    sb.append(line).append("\n");
                }
            } finally {
                ists.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }
}
