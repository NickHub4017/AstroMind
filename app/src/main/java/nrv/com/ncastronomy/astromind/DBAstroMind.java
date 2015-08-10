package nrv.com.ncastronomy.astromind;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by NRV on 8/2/2015.
 */
public class DBAstroMind extends SQLiteOpenHelper {
    public DBAstroMind(Context context) {
        super(context, "astro.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_temptable="CREATE TABLE imagedata (fbid varchar(255),link text)";
        db.execSQL(create_temptable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addOrUpdateLink(String fid,String lnk){
        SQLiteDatabase db=this.getWritableDatabase();
        String query="insert or replace into imagedata (fbid, link) values (" +
                fid+","+
                    lnk+
                "    );";
        db.execSQL(query);
    }

    public String getLink(String fid){
        SQLiteDatabase db=this.getWritableDatabase();
        String query="Select link from imagedata where fbid ='"+fid+"'";
        Cursor c=db.rawQuery(query, null);
        if(c.moveToFirst() & c.getCount()==1){
            return c.getString(c.getColumnIndex("link"));
        }
        else{
            return  null;
        }
    }

    public void getAllLink(){
        SQLiteDatabase db=this.getWritableDatabase();
        String query="Select * from imagedata";
        Cursor c=db.rawQuery(query, null);
        if(c.moveToFirst() ){
            do{
                Log.d("data -> ",c.getString(c.getColumnIndex("fbid"))+"->"+c.getString(c.getColumnIndex("link")));
            }while (c.moveToNext());
        }
        else{
            Log.d("data -> ","nodata");
        }
    }



}
