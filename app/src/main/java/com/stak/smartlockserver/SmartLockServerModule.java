package com.stak.smartlockserver;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.stak.smartlockserver.lock.LockManager;
import com.stak.smartlockserver.persistence.DBOpenHelper;
import com.stak.smartlockserver.rest.RegistrationResource;
import com.stak.smartlockserver.rest.SmartLockResource;
import com.stak.smartlockserver.security.KeyStoreGenerator;
import com.stak.smartlockserver.security.model.AuthToken;
import com.stak.smartlockserver.security.model.Registration;
import com.stak.smartlockserver.view.ServerActivity;

import java.sql.SQLException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gospo on 29.12.14.
 */
@Module(
        library = true,
        injects = {RegistrationResource.class, SmartLockResource.class, ServerActivity.class}
)
public class SmartLockServerModule {

    private Application app;

    SmartLockServerModule(Application app) {
        this.app = app;
    }

    @Provides @Singleton public Context provideApplicationContext() {
        return app;
    }

    @Provides @Singleton public SQLiteOpenHelper provideSQLiteOpenHelper(Context appContext) {
        return new DBOpenHelper(appContext);
    }

    @Provides @Singleton public ConnectionSource provideConnectionSource(SQLiteOpenHelper openHelper) {
        return new AndroidConnectionSource(openHelper);
    }

    @Provides @Singleton public Dao<Registration, String> provideRegistrationsDao(ConnectionSource connectionSource){
        try {
            Log.i("da","ads");
            return DaoManager.createDao(connectionSource, Registration.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Provides @Singleton public Dao<AuthToken, String> provideUsersDao(ConnectionSource connectionSource){
        try {
            return DaoManager.createDao(connectionSource, AuthToken.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Provides @Singleton public LockManager provideLockManager() {
        return new LockManager();
    }

    @Provides @Singleton public KeyStoreGenerator keyStoreGenerator() {return new KeyStoreGenerator();}

}
