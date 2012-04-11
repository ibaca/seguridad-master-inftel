
package com.src;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class NavegadorActivity extends SherlockActivity {
    private WebView wv;
    private static final String PROD_URL = "http://home.bacamt.com:83/cert/index.phtml";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        wv = (WebView) findViewById(R.id.webView1);
        wv.loadUrl(PROD_URL);
        SharedPreferences settings = getSharedPreferences("certificado", MODE_PRIVATE);
        String nombre = settings.getString("nombre", " ");
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
                Log.d("RETURN", contents);
                // Handle successful scan
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }
}
