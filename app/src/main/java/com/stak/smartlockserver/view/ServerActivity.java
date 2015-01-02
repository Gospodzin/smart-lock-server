package com.stak.smartlockserver.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.stak.smartlockserver.R;
import com.stak.smartlockserver.rest.ServerService;
import com.stak.smartlockserver.SmartLockApp;
import com.stak.smartlockserver.security.SecurityHelper;

import java.io.IOException;

import javax.inject.Inject;

import static com.stak.smartlockserver.security.Constants.KEY_STORE_FILE;


public class ServerActivity extends ActionBarActivity implements View.OnClickListener {

    AlertDialog createUserDialog;
    Button createUserButton;
    ListView usersList;

    String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
            "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
            "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
            "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
            "Android", "iPhone", "WindowsMobile" };


    @Inject
    SecurityHelper securityHelper;

    private void init() {
        SmartLockApp.inject(this);

        usersList = (ListView) findViewById(R.id.usersListView);
        usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values));

        createUserDialog = createDialog();

        createUserButton = (Button) findViewById(R.id.addUserButton);
        createUserButton.setOnClickListener(this);


        initKeyStore();
        startService(new Intent(this, ServerService.class));
    }

    private void initKeyStore() {
        try {
            final String keyStorePath = getFilesDir() + "/" + KEY_STORE_FILE;
            if(!securityHelper.isKeyStoreFileExistent(keyStorePath))
            securityHelper.generateKeyStoreFile(keyStorePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private AlertDialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("title");
        final EditText usernameEditText = new EditText(this);
        builder.setView(usernameEditText);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String username = usernameEditText.getText().toString();
                securityHelper.register(username);
            }
        });
        builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) { }
        });

        return builder.create();
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

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.addUserButton:
                createUserDialog.show();
                break;
        }
    }
}

