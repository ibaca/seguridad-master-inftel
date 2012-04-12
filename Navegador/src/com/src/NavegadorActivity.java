
package com.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
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
import org.apache.http.impl.conn.SingleClientConnManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class NavegadorActivity extends SherlockActivity {
    private WebView wv;
    private static final String PROD_URL = "https://home.bacamt.com:83/cert/showme.phtml";
    private static final String LOGTAG = "https";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        File dir = new File("sdcard/certificados/");
        if (dir.list() == null || dir.list().length == 0) {
            Log.d("CARPETA", "He creado la carpeta");
            dir.mkdir();
            metodoParaLlamarAlQR();
        }
        wv = (WebView) findViewById(R.id.webView1);
        wv.loadUrl(PROD_URL);
        SharedPreferences settings = getSharedPreferences("certificado", MODE_PRIVATE);
        String nombre = settings.getString("nombre", "");
        Log.d("MIO", "dentro de navegador " + nombre);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean h = super.onCreateOptionsMenu(menu);
        menu.add(0, 0, 0, "Ver Certificados");
        menu.add(0, 1, 1, "Certificado Actual");
        return h;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        boolean h = super.onMenuItemSelected(featureId, item);
        if (item.getItemId() == 0) {
            startActivity(new Intent(this, new ListaCertificados().getClass()));
        }
        else if (item.getItemId() == 1) {
            startActivity(new Intent(this, CertificadoActual.class));
        }
        return h;
    }

    public void metodoParaLlamarAlQR() {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                try {
                    downloadCert(contents);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    /* Metodo para generar certificados a partir del contenido de un String */
    public void createCert() throws IOException {
        String FILENAME = "cert";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = prefs.edit();

        int numCerts = prefs.getInt("numCertificados", 1);
        FILENAME += numCerts;
        String string = "hello world!";

        FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
        fos.write(string.getBytes());
        fos.close();

        editor.putInt("numCertificados", numCerts + 1);
        editor.commit();
    }

    public void downloadCert(String url) throws URISyntaxException, KeyStoreException,
            NoSuchAlgorithmException, CertificateException, NotFoundException, IOException,
            KeyManagementException, UnrecoverableKeyException {
        HttpGet request = new HttpGet(new URI(url));

        KeyStore trusted = KeyStore.getInstance("BKS");
        trusted.load(getResources().openRawResource(R.raw.truststore), "inftel".toCharArray());

        MySSLSocketFactory sslf = new MySSLSocketFactory(trusted);
        sslf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("https", sslf, 443));
        SingleClientConnManager cm = new SingleClientConnManager(request.getParams(),
                schemeRegistry);

        HttpClient client = new DefaultHttpClient(cm, request.getParams());
        HttpResponse result = client.execute(request);

        /* Comprobamos que la respuesta que obtenemos es valida */
        final int statusCode = result.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            Log.w(LOGTAG, "Error " + statusCode + " for URL "
                    + PROD_URL);
        }
        HttpEntity getResponseEntity = result.getEntity();
        InputStream is = getResponseEntity.getContent();
        Log.i(LOGTAG, "Content: " + convertinputStreamToString(is));
    }

    /* Metodo encargado de conectar con Apache mediante SSL */
    public void connect() throws URISyntaxException, NoSuchAlgorithmException,
            CertificateException, IOException, KeyStoreException, KeyManagementException,
            UnrecoverableKeyException {

        HttpGet request = new HttpGet(new URI(PROD_URL));

        KeyStore trusted = KeyStore.getInstance("BKS");
        trusted.load(getResources().openRawResource(R.raw.truststore), "inftel".toCharArray());

        // De momento usamos el certificado directamente del raw. Posteriormente
        // habra que cambiarlo
        KeyStore clientCert = KeyStore.getInstance("pkcs12");
        clientCert.load(getResources().openRawResource(R.raw.clientpass),
                "inftel".toCharArray());

        SSLSocketFactory sslf = new SSLSocketFactory(clientCert, null, trusted);
        sslf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("https", sslf, 443));
        SingleClientConnManager cm = new SingleClientConnManager(request.getParams(),
                schemeRegistry);

        HttpClient client = new DefaultHttpClient(cm, request.getParams());
        HttpResponse result = client.execute(request);

        /* Comprobamos que la respuesta que obtenemos es valida */
        final int statusCode = result.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            Log.w(LOGTAG, "Error " + statusCode + " for URL "
                    + PROD_URL);
        }
        HttpEntity getResponseEntity = result.getEntity();
        InputStream is = getResponseEntity.getContent();
        Log.i(LOGTAG, "Content: " + convertinputStreamToString(is));

    }

    public String convertinputStreamToString(InputStream ists) throws IOException {

        if (ists != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader r1 = new BufferedReader(new InputStreamReader(ists, "UTF-8"));
                while ((line = r1.readLine()) != null) {
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
