
package com.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class NavegadorActivity extends SherlockActivity {
    private WebView wv;
    private static final String PROD_URL = "https://home.bacamt.com:83";
    private static final String LOGTAG = "https";
    private static final String RUTA_CERT = "certificados";
    private String nombre;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        File dir = getDir(RUTA_CERT, MODE_PRIVATE);
        SharedPreferences settings = getSharedPreferences("certificado", MODE_PRIVATE);
        nombre = settings.getString("nombre", "");
        Log.d(LOGTAG, "El nombre: " + nombre);
        if (dir.list().length == 0) {
            llamadaQR();
        }
        wv = (WebView) findViewById(R.id.webView1);
        wv.setWebViewClient(new WebViewClient());
        try {
            if (nombre != "") {
                connect();
            }
        } catch (Exception e) {
            Log.d(LOGTAG, "dentro de navegador " + nombre, e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean h = super.onCreateOptionsMenu(menu);
        menu.add(0, 0, 0, "Ver Certificados");
        menu.add(1, 1, 1, "Certificado Actual");
        menu.add(2, 2, 2, "Añadir certificado");
        menu.add(3, 3, 3, "Revocar certificado");
        return h;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        boolean h = super.onMenuItemSelected(featureId, item);
        if (item.getItemId() == 0) {
            startActivityForResult(new Intent(this, new ListaCertificados().getClass()), 1);
        }
        else if (item.getItemId() == 1) {
            startActivity(new Intent(this, CertificadoActual.class));
        }
        else if (item.getItemId() == 2) {
            llamadaQR();
        }
        else if (item.getItemId() == 3) {
            startActivity(new Intent(this, RevocarCertificados.class));
        }
        return h;
    }

    public void llamadaQR() {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                try {
                    Log.d(LOGTAG, "c: " + contents);
                    downloadCert(contents);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == 1 || requestCode == 0) {
            try {
                SharedPreferences settings = getSharedPreferences("certificado", MODE_PRIVATE);
                nombre = settings.getString("nombre", "");
                connect();
            } catch (Exception e) {
                Log.d("MIO", "dentro de reconexion " + nombre, e);
            }
        }
    }

    /* Metodo para generar certificados a partir del contenido de un String */
    public void createCert(InputStream is, String nombre) throws IOException {
        String FILENAME = getDir(RUTA_CERT, MODE_PRIVATE).getAbsolutePath() + "/" + nombre;

        Log.d(LOGTAG, FILENAME);
        // FileOutputStream fos = openFileOutput(FILENAME,
        // Context.MODE_PRIVATE);
        FileOutputStream fos = new FileOutputStream(new File(FILENAME));
        int read = 0;
        byte[] buffer = new byte[1024];
        while ((read = is.read(buffer)) != -1) {
            fos.write(buffer, 0, read);
        }
        fos.close();
    }

    public void downloadCert(String url) throws URISyntaxException, KeyStoreException,
            NoSuchAlgorithmException, CertificateException, NotFoundException, IOException,
            KeyManagementException, UnrecoverableKeyException {
        HttpGet request = new HttpGet(new URI(url));

        KeyStore trusted = KeyStore.getInstance("BKS");
        trusted.load(getResources().openRawResource(R.raw.truststore), "inftel".toCharArray());

        SSLSocketFactory sslf = new SSLSocketFactory(trusted);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("https", sslf, 443));

        HttpClient client = new DefaultHttpClient(new ThreadSafeClientConnManager(
                request.getParams(), schemeRegistry), request.getParams());
        HttpResponse result = client.execute(request);

        /* Comprobamos que la respuesta que obtenemos es valida */
        final int statusCode = result.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            Log.w(LOGTAG, "Error " + statusCode + " for URL " + PROD_URL);
        }
        HttpEntity getResponseEntity = result.getEntity();
        InputStream is = getResponseEntity.getContent();
        int lIndex = url.lastIndexOf("/");
        String name = url.substring(lIndex + 1);
        SharedPreferences settings = getSharedPreferences("certificado", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("nombre", name);

        // Commit the edits!
        editor.commit();
        Log.d(LOGTAG, "Guardado nombre: " + name);
        createCert(is, name);
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

        HttpResponse result = null;
        try {
            result = client.execute(request);
        } catch (IOException e) {
            if (nombre != "") {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
                dialogo1.setTitle(getResources().getString(R.string.Importante));
                dialogo1.setMessage(getResources().getString(R.string.borrar2));
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton(getResources().getString(R.string.Confirmar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                try {
                                    final File borra = new File(getDir(RUTA_CERT, MODE_PRIVATE)
                                            .getAbsolutePath() + "/" + nombre);
                                    borra.delete();
                                }
                                catch (Exception e) {
                                }
                            }
                        });
                dialogo1.setNegativeButton(getResources().getString(R.string.Cancelar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                dialogo1.dismiss();
                            }
                        });
                dialogo1.show();
            }
        }

        /* Comprobamos que la respuesta que obtenemos es valida */
        if (result != null) {
            final int statusCode = result.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w(LOGTAG, "Error " + statusCode + " for URL "
                        + PROD_URL);
            }
            HttpEntity getResponseEntity = result.getEntity();
            InputStream is = getResponseEntity.getContent();
            String contenido = convertinputStreamToString(is);
            Log.i(LOGTAG, "Content: " + contenido);
            wv.loadData(contenido, "text/html", "UTF-8");
        } else {
            wv.loadData("", "text/html", "UTF-8");
        }
    }

    public String convertinputStreamToString(InputStream ists) throws IOException {

        if (ists != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader r1 = new BufferedReader(new InputStreamReader(ists, "UTF-8"));
                while ((line = r1.readLine()) != null) {
                    // Ya que es codigo HTML metemos saltos de linea
                    sb.append(line).append("<br/>");
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
