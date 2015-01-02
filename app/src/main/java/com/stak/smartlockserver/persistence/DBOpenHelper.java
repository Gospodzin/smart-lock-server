package com.stak.smartlockserver.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.stak.smartlockserver.security.model.AuthToken;
import com.stak.smartlockserver.security.model.Registration;

import java.sql.SQLException;

/**
 * Created by gospo on 29.12.14.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "smart-lock";
    private static final int DATABASE_VERSION = 3;
    private static final String TOKENS_TABLE_NAME = "auth_tokens";
    private static final String TOKEN_COLUMN = "token";
    private static final String DICTIONARY_TABLE_CREATE =
            "CREATE TABLE " + TOKENS_TABLE_NAME + " (" +
                    TOKEN_COLUMN + " TEXT);";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            TableUtils.createTable(new AndroidConnectionSource(db), AuthToken.class);
            TableUtils.createTable(new AndroidConnectionSource(db), Registration.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
