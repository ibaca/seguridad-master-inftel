
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
import java.util.ArrayList;

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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListActivity;

public class RevocarCertificados extends SherlockListActivity {
    private static final String PROD_URL = "https://home.bacamt.com:83/secure/list.phtml";
    private static final String LOGTAG = "https";
    private static final String RUTA_CERT = "certificados";
    private String nombre;
    private static final String URL_DELETE="https://home.bacamt.com:83/secure/revoke.phtml?cert=";
    private ListView list;
    private String[] names;
    ArrayAdapter<String> adapter;
    ArrayList<String> usuarios=new ArrayList<String>();
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences("certificado", MODE_PRIVATE);
        nombre = settings.getString("nombre", "");
        Log.d("MIO", "nombre " + nombre);
        try {
            names= connect();
        } catch (Exception e) {
            Log.d("MIO", "dentro de navegador " + nombre, e);
        }
        list=getListView();
        list.setBackgroundResource(R.color.blanco);
        Log.d("MIO", "dentro de navegador " + names.length);
        for(int i=0; i< names.length;i++){
            Log.i(LOGTAG, "names: " + names[i]);
        }
        adapter = new ArrayAdapter<String>(this, R.layout.radio_group, names);
        list.setAdapter(adapter);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.d("MIO","He pulsado "+ names[position]);
        final int poscion=position;
        borrarCertificado(names[position]);
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);  
        dialogo1.setTitle(getResources().getString(R.string.Importante));  
        dialogo1.setMessage(getResources().getString(R.string.borrar));            
        dialogo1.setCancelable(false);  
        dialogo1.setPositiveButton(getResources().getString(R.string.Confirmar), new DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialogo1, int id) { 
                try{
                final File borra=new File(getDir(RUTA_CERT, MODE_PRIVATE).getAbsolutePath() + "/"+names[poscion]);
                borra.delete();
                }
                catch(Exception e){}
                
            }  
        });  
        dialogo1.setNegativeButton(getResources().getString(R.string.Cancelar), new DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialogo1, int id) {  
               dialogo1.dismiss();
            }  
        });            
        dialogo1.show();
        usuarios.remove(position);
        adapter=new ArrayAdapter<String>(this, R.layout.radio_group, ListToArray());
        list.setAdapter(adapter);
       
        
    }
    
    public void borrarCertificado(String borra){
        try {
            Log.d("MIO", "dentro del metodo " + URL_DELETE+borra+"a");
            borra=borra.trim();
            Log.d("MIO", "dentro del metodo " + URL_DELETE+borra+"a");
            HttpGet request = new HttpGet(new URI(URL_DELETE+borra));
            KeyStore trusted =  KeyStore.getInstance("BKS");;
            KeyStore clientCert= KeyStore.getInstance("pkcs12");
            FileInputStream fis = new FileInputStream(getDir(RUTA_CERT, MODE_PRIVATE).getAbsolutePath() + "/" + nombre);
            clientCert.load(fis, "inftel".toCharArray());
            trusted.load(getResources().openRawResource(R.raw.truststore), "inftel".toCharArray());
            SSLSocketFactory sslf = new SSLSocketFactory(clientCert, null, trusted);
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("https", sslf, 443));
            HttpClient client = new DefaultHttpClient(new ThreadSafeClientConnManager(request.getParams(), schemeRegistry), request.getParams());
            client.execute(request);
        } catch (Exception e) {
            Log.d("MIO", "borra certificado " + nombre, e);
        } 
    }
    
    /* Metodo encargado de conectar con Apache mediante SSL */
    public String[] connect() throws URISyntaxException, NoSuchAlgorithmException,
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
        String usuario;
        
        contenido=contenido.substring(contenido.indexOf("$")+1);
        while(contenido.length()>0){
            try{
                usuario=contenido.substring(0, contenido.indexOf("$"));
                contenido=contenido.substring(contenido.indexOf("$")+1);
                Log.i(LOGTAG, "Usuario: " + usuario);
                Log.i(LOGTAG, "Content: " + contenido.length());
                Log.i(LOGTAG, "Content: " + contenido);
                usuarios.add(usuario);
            }
            catch(Exception e){
                break;
            }
        }
        
        return ListToArray();
        
    }
    public String[] ListToArray(){
        String[] namess=new String[usuarios.size()-1];
        for(int i=0; i< usuarios.size()-1;i++){
            namess[i]=usuarios.get(i);
        }
        return namess;
    }

    public String convertinputStreamToString(InputStream ists) throws IOException {

        if (ists != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader r1 = new BufferedReader(new InputStreamReader(ists, "UTF-8"));
                while ((line = r1.readLine()) != null) {
                    // Ya que es codigo HTML metemos saltos de linea
                    sb.append(line).append("$");
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
