package com.stak.smartlockserver.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.stak.smartlockserver.R;
import com.stak.smartlockserver.rest.ServerService;
import com.stak.smartlockserver.SmartLockApp;
import com.stak.smartlockserver.security.SecurityHelper;
import com.stak.smartlockserver.security.model.Registration;

import java.io.IOException;

import javax.inject.Inject;

import static com.stak.smartlockserver.security.Constants.KEY_STORE_FILE;


public class ServerActivity extends ActionBarActivity implements View.OnClickListener {

    AlertDialog createUserDialog;
    Button createUserButton;
    ListView usersList;
    ArrayAdapter<UserDTO> usersAdapter;

    public static final int DELETE_MENU_ITEM = 0;

    @Inject
    ViewHelper viewHelper;

    @Inject
    SecurityHelper securityHelper;

    private void init() {
        SmartLockApp.inject(this);

        usersList = (ListView) findViewById(R.id.usersListView);
        usersAdapter = new ArrayAdapter<UserDTO>(this, android.R.layout.simple_list_item_1, viewHelper.getUsersList());
        usersList.setAdapter(usersAdapter);

        createUserDialog = createDialog();

        createUserButton = (Button) findViewById(R.id.addUserButton);
        createUserButton.setOnClickListener(this);

        registerForContextMenu(usersList);


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
        builder.setTitle("Utwórz");
        final EditText usernameEditText = new EditText(this);
        builder.setView(usernameEditText);
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) { }
        });

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface d) {
                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String username = usernameEditText.getText().toString();
                        try {
                            Registration registration = securityHelper.register(username);
                            usersAdapter.add(new UserDTO(registration.getUsername(), registration.getPin()));
                            usernameEditText.getText().clear();
                            dialog.dismiss();
                        } catch (SecurityHelper.UserExistsException e) {
                            e.printStackTrace();
                            usernameEditText.getText().clear();
                        }
                    }
                });
            }
        });

        return dialog;
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
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.addUserButton:
                createUserDialog.show();
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        switch(v.getId()) {
            case R.id.usersListView:
                MenuItem menuItem = menu.add(Menu.NONE, DELETE_MENU_ITEM, 0, "Usuń");
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_MENU_ITEM:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                UserDTO user = usersAdapter.getItem(info.position);
                securityHelper.deleteUser(user.getUsername());
                usersAdapter.remove(user);
                break;
        }
        return true;
    }
}

