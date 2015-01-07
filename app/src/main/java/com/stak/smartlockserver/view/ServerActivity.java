package com.stak.smartlockserver.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.stak.smartlockserver.R;
import com.stak.smartlockserver.SmartLockApp;
import com.stak.smartlockserver.rest.ServerService;
import com.stak.smartlockserver.security.SecurityHelper;
import com.stak.smartlockserver.security.model.Registration;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import javax.inject.Inject;

import static com.stak.smartlockserver.security.Constants.KEY_STORE_FILE;


public class ServerActivity extends ActionBarActivity {

    AlertDialog createUserDialog;
    ListView usersList;
    ArrayAdapter<UserDTO> usersAdapter;

    public static final int DELETE_MENU_ITEM = 0;

    @Inject
    ViewHelper viewHelper;

    @Inject
    SecurityHelper securityHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        init();
    }

    private void init() {
        SmartLockApp.inject(this);

        usersList = (ListView) findViewById(R.id.usersListView);
        usersAdapter = new ArrayAdapter<UserDTO>(this, android.R.layout.simple_list_item_1, viewHelper.getUsersList());
        usersList.setAdapter(usersAdapter);

        createUserDialog = createDialog();

        registerForContextMenu(usersList);

        TextView ipTextView = (TextView) findViewById(R.id.ipTextView);
        ipTextView.setText(getIp());

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

    public String getIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                    .hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().contains("wlan")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                            .hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()
                                && (inetAddress.getAddress().length == 4)) {
                            Log.d("WIFIIP", inetAddress.getHostAddress());
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
                Log.e("WIFIIP", e.toString());
        }
        return null;
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
                        String username = usernameEditText.getText().toString().trim();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_server, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(item.getItemId()) {
            case R.id.action_refresh:
                usersAdapter.clear();
                for(UserDTO user : viewHelper.getUsersList())
                    usersAdapter.add(user);
                return true;

            case R.id.action_new_user:
                createUserDialog.show();
                return true;
        }

        return super.onOptionsItemSelected(item);
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

