package uk.ac.coventry.a260ct.orks.slopemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by boldurbogdan on 28/02/2017.
 */

public class Database extends SQLiteOpenHelper {
    Context context;
    private final static String DatabaseName = "SBC_System_Database.db";
    private final static String CUSTOMER_TABLE="customer";
    private static final String BOOKING_TABLE="booking";
    private static final String PERMISSION_TABLE="permission";
    //private final static String DatabaseName = "playlists"; that is how you define the database j
    private final static int DATABASE_VERSION = 1;
    private SQLiteDatabase db;


    public Database(Context c) {
        super(c, DatabaseName, null, DATABASE_VERSION);

    }

    public void onCreate(SQLiteDatabase db) {
        String createCustomer = "create table if not exists playlist(name TEXT primary key not null," +
                "notrack INTEGER not null);";
        String createPermissions="";
        String createBookings="";


       /* String create2 = "create table if not exists song(" +
                "id INTEGER primary key not null," +
                "name TEXT not null," +
                "duration Integer not null," +
                "path TEXT not null,art TEXT not null," +
                "artist TEXT not null);";

                db.execSQL(create2);///create the table in database*/

        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*String deletetablesong = "drop table if exists " + DatabaseTableSong + ";"; //select table to delete and remake

        db.execSQL(deletetablesongplaylist);//execute the sql query*/

        this.onCreate(db);
    }
    public String getId(int id){
        String permissionForUser=null;
        db=this.getReadableDatabase();
        String query="Select permission from "+CUSTOMER_TABLE+" where ID=?;";
        Cursor cursor=db.rawQuery(query,new String[]{String.valueOf(id)});
        if(cursor!=null)
            permissionForUser=cursor.getString(cursor.getColumnIndex("permission"));
        cursor.close();

        return permissionForUser;
    }


    /*
    public void updateNumberOfTracks(String playlistName,int newNumberOfTracks){
        db=this.getWritableDatabase();
        ContentValues valueToInsert=new ContentValues();
        valueToInsert.put("notrack",newNumberOfTracks);
        db.update(DatabaseTablePlaylist,valueToInsert,"name=?",new String[]{playlistName});
    }
    public void insertPlaylist(Playlist playlist) {
        db = this.getWritableDatabase();
        ContentValues valuetoinsert = new ContentValues();
        valuetoinsert.put("name", playlist.getName());
        valuetoinsert.put("notrack", playlist.getNumber_of_tracks());
        db.insert(DatabaseTablePlaylist, null, valuetoinsert);
    }

    public void insertSong(Song song) {
        db = this.getWritableDatabase();
        ContentValues valuesToInsert = new ContentValues();
        valuesToInsert.put("id", song.getId());
        valuesToInsert.put("name", song.getName());
        valuesToInsert.put("duration", song.getDuration());
        valuesToInsert.put("path", song.getPath());
        valuesToInsert.put("art", String.valueOf(song.getArt()));
        valuesToInsert.put("artist", song.getArtist());
        db.insert("song", null, valuesToInsert);
    }

    public ArrayList<String> retrivesongnames() {
        db = this.getReadableDatabase();
        ArrayList<String> names = new ArrayList<>();
        String select = "select * from (select  from " + DatabaseTableSong + ";";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                names.add(name);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return names;
    }*/

    /// LOOK ON THE METHODS ABOVE FOR SEEING HOW TO USE CURSOR, CONTENTVALUES to add, delete rows in the database
}
