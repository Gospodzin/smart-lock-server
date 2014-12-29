package com.stak.smartlockserver.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gospo on 29.12.14.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "smart-lock";
    private static final int DATABASE_VERSION = 1;
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
        db.execSQL(DICTIONARY_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
