package com.stak.smartlockserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.stak.smartlockserver.rest.RegistrationResource;

import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.Parameter;
import org.restlet.data.Protocol;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.restlet.util.Series;

import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import static com.stak.smartlockserver.Constants.*;

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
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        serverComponent = new Component();
        Server server = new Server(Protocol.HTTPS, null, SMART_LOCK_PORT, null);
        serverComponent.getServers().add(server);
        Series<Parameter> parameters = server.getContext().getParameters();
        parameters.add("keystorePath", getFilesDir() + "/keystore");
        parameters.add("keystorePassword", "");
        parameters.add("keyPassword", "");
        parameters.add("keystoreType", KeyStore.getDefaultType());
        parameters.add("keyManagerAlgorithm", KeyManagerFactory.getDefaultAlgorithm());
        parameters.add("trustManagerAlgorithm", TrustManagerFactory.getDefaultAlgorithm());

        final Router router = new Router(serverComponent.getContext().createChildContext());
        router.setDefaultMatchingMode(Template.MODE_STARTS_WITH);
        router.attach("/register", RegistrationResource.class);
        serverComponent.getDefaultHost().attach(router);
        try {
            serverComponent.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
