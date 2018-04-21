package com.knwedu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author ritwik.rai
 */
public class CPSDBHelper extends SQLiteOpenHelper implements DBConstants {

    private static final String TAG = "CPSDBHelper";
    private static CPSDBHelper mDatabase;
    private SQLiteDatabase mDb;

    public CPSDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static final CPSDBHelper getInstance(Context context) {
        if (mDatabase == null) {
            mDatabase = new CPSDBHelper(context);
            mDatabase.getWritableDatabase();
        }
        return mDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.i(TAG, "oncreate tables");
        // create table
        String[] createStatements = getCreatetableStatements();
        int total = createStatements.length;
        for (int i = 0; i < total; i++) {
            Log.i(TAG, "executing create query " + createStatements[i]);
            Log.i("Database", "Offline History Database created");
            db.execSQL(createStatements[i]);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // on upgrade drop older tables
        Log.i("Tag", "Old version" + oldVersion + " New version: " + newVersion + "Constant variable version name: " + DB_VERSION);
        db.execSQL("DROP TABLE IF EXISTS " + NOTIFICATION_TABLE);
        db.setVersion(DB_VERSION);

        // create new tables
        onCreate(db);
    }

    private String[] getCreatetableStatements() {

        String[] create = new String[1];

        // THREADS table -> _id , threadID, formName, formData , parent_cat_id
        String threadsTableStatement = CREATE_TABLE_BASE + NOTIFICATION_TABLE + START_COLUMN + _ID + INTEGER + PRIMARY_KEY
                + AUTO_ICNREMENT + COMMA + NOTIFICATION_TITLE + TEXT + COMMA + NOTIFICATION_MESSAGE + TEXT + COMMA + ROLE + TEXT
                + COMMA + MODULE + TEXT + COMMA + ORGANIZATIONID + TEXT + COMMA + TIMESTAMP + TEXT + FINISH_COLUMN;
        create[0] = threadsTableStatement;
        return create;
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {

        return mDb != null ? mDb : (mDb = super.getWritableDatabase());
    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {

        return mDb != null ? mDb : (mDb = super.getReadableDatabase());
    }

    public void startmanagingcursor() {
        mDatabase.startmanagingcursor();
    }

}
