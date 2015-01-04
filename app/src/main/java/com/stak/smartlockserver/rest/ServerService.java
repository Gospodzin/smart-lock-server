package com.stak.smartlockserver.rest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.stak.smartlockserver.rest.di.DIFinder;

import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Server;
import org.restlet.data.Parameter;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.ext.gson.GsonConverter;
import org.restlet.routing.Router;
import org.restlet.util.Series;

import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import static com.stak.smartlockserver.rest.Constants.SMART_LOCK_PORT;
import static com.stak.smartlockserver.security.Constants.KEY_PASSWORD;
import static com.stak.smartlockserver.security.Constants.KEY_STORE_FILE;
import static com.stak.smartlockserver.security.Constants.KEY_STORE_PASSWORD;

/**
 * Created by gospo on 26.12.14.
 */
public class ServerService extends Service {

    Component serverComponent;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            serverComponent.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Engine.getInstance().getRegisteredConverters().clear();
        Engine.getInstance().getRegisteredConverters().add(new GsonConverter());

        serverComponent = new Component();
        Server server = createServer();
        serverComponent.getServers().add(server);
        addParameters(server.getContext().getParameters());
        serverComponent.getDefaultHost().attach(createRouter(serverComponent.getContext().createChildContext()));
        try {
            serverComponent.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Server createServer() {
        Server server = new Server(Protocol.HTTPS, null, SMART_LOCK_PORT, null);

        return server;
    }

    private void addParameters(Series <Parameter> parameters) {
        parameters.add("keystorePath", getFilesDir() + "/" + KEY_STORE_FILE);
        parameters.add("keystorePassword", KEY_STORE_PASSWORD);
        parameters.add("keyPassword", KEY_PASSWORD);
        parameters.add("keystoreType", KeyStore.getDefaultType());
        parameters.add("keyManagerAlgorithm", KeyManagerFactory.getDefaultAlgorithm());
        parameters.add("trustManagerAlgorithm", TrustManagerFactory.getDefaultAlgorithm());
    }

    private Router createRouter(Context context) {
        final Router router = new Router(context);
        router.setFinderClass(DIFinder.class);
        router.attach("/confirm", RegistrationResource.class);
        router.attach("/lock", SmartLockResource.class);
        return router;
    }
}
