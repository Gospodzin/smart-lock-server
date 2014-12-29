package com.stak.smartlockserver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.stak.smartlockserver.security.SecurityHelper;

import java.io.IOException;

import static com.stak.smartlockserver.security.Constants.KEY_STORE_FILE;


public class ServerActivity extends ActionBarActivity {

    private String keyStorePath;

    SecurityHelper securityHelper = new SecurityHelper();

    private void init() {
        keyStorePath = getFilesDir() + "/" + KEY_STORE_FILE;
        initKeyStore();
        startService(new Intent(this, ServerService.class));
    }

    private void initKeyStore() {
        try {
            if(!securityHelper.isKeyStoreFileExistent(keyStorePath))
            securityHelper.generateKeyStoreFile(keyStorePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_server, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
