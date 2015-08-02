package nrv.com.ncastronomy.astromind;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by NRV on 8/2/2015.
 */
public class DBAstroMind extends SQLiteOpenHelper {
    public DBAstroMind(Context context) {
        super(context, "astro.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_temptable="CREATE TABLE imagedata";
        db.execSQL(create_temptable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
