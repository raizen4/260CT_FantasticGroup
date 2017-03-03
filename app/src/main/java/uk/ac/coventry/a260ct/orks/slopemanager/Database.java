package uk.ac.coventry.a260ct.orks.slopemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by boldurbogdan on 28/02/2017.
 *
 * This is a supposed to be a database that would be run on a server. It would store attributes
 * about the users and the sessions.
 *
 * We store the database locally in this prototype as a proof of concept
 */

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SBC_System_Database.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREDENTIALS_TABLE = "credentials";
    private static final String USERS_TABLE = "users";
    private static final String BOOKINGS_TABLE = "bookings";
    private static final String PERMISSIONS_TABLE =  "permissions";
    private static final String SESSIONS_TABLE = "sessions";

    private static final String[] ALL_TABLES =
            new String[]{
                    USERS_TABLE,
                    BOOKINGS_TABLE,
                    PERMISSIONS_TABLE,
                    SESSIONS_TABLE
            };

    private SQLiteDatabase db;


    public Database(Context c) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);

    }

    /**
     * Creates the table if they don't exist and stores the database in the instance
     * @param db
     */
    public void onCreate(SQLiteDatabase db) {
        /**
         * User table holds the user details
         */

        String createCredentials =
                "CREATE TABLE IF NOT EXISTS " + CREDENTIALS_TABLE + "(" +
                        "username varchar(255) NOT NULL PRIMARY KEY, " +
                        "password varchar(255) NOT NULL, " +
                        "user_id int NOT NULL" +
                        ");";

        String createUsers =
                "CREATE TABLE IF NOT EXISTS "+ USERS_TABLE + "(" +
                        "id int NOT NULL PRIMARY KEY, " +
                        "first_name varchar(255) NOT NULL, " +
                        "last_name varchar(255), " +
                        "email varchar(255) NOT NULL, " +
                        "phone varchar(255) NOT NULL, " +
                        "membership int NOT NULL, " +
                        ");";

        /**
         * permissions is used to hold what permissions the User has access to
         */

        String createPermissions =
                "CREATE TABLE IF NOT EXISTS "+ PERMISSIONS_TABLE + "(" +
                        "user_id int NOT NULL PRIMARY_KEY, " +
                        "view_bookings BOOLEAN, " + // User can view their bookings
                        "view_details BOOLEAN, " + // User can view their details
                        "view_all_details BOOLEAN, " + // User can view anyone's details
                        "view_all_bookings BOOLEAN, " + // User can view anyone's bookings
                        "register_user BOOLEAN, " + // User can register other users
                        "view_sessions BOOLEAN, " + // User can view details of all possible sessions
                        "view_instructors BOOLEAN, " + // User can view and edit instructors
                        "change_sessions BOOLEAN" + // User can make edits to sessions
                        ");";

        String createSessions =
                "CREATE TABLE IF NOT EXISTS "+ SESSIONS_TABLE + "(" +
                        "session_id int NOT NULL PRIMARY_KEY, " +
                        "instructor_id int NOT NULL, " +
                        "date DATE NOT NULL, " +
                        "slot int NOT NULL, " +
                        ");";

        String createBookings =
                "CREATE TABLE IF NOT EXISTS "+ BOOKINGS_TABLE + "(" +
                        "booking_id int NOT NULL PRIMARY_KEY, " +
                        "user_id int NOT NULL, " +
                        "paid BOOLEAN, " +
                        "session_id int NOT NULL" +
                        ");";

        db.execSQL(createUsers);
        db.execSQL(createPermissions);
        db.execSQL(createSessions);
        db.execSQL(createBookings);
        db.execSQL(createCredentials);

        addUser(21312432, "Jim", "Jiggles", "test@test.com", "07283929394", 2);
        setPermissions(21312432, true, true, true, true, true, true, true, true);


        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String table: ALL_TABLES) {
            db.execSQL("DROP TABLE IF EXISTS " + table + ";");
        }

        onCreate(db);
    }

    /**
     * Used for login, returns the user_id from username and password
     * which can be used to get the user object from the table
     * @param username
     * @param password
     * @return
     */

    public int getUserIdFromCredentials(String username, String password) {
        int user_id = -1;

        String query =
                "SELECT user_id FROM " + CREDENTIALS_TABLE +
                        "WHERE username=? & password=?";

        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                user_id = cursor.getInt(cursor.getColumnIndex("user_id"));
            }
            cursor.close();
        }

        return user_id;
    }

    /**
     * Updates the given user ID permissions, if the user doesn't exist in the permissions table,
     * it will create the entry
     * @param id user Id
     * @param viewBookings User can view their bookings
     * @param viewDetails User can view their details
     * @param viewAllDetails User can view anyone's details
     * @param viewAllBookings User can view anyone's bookings
     * @param registerUser User can register other users
     * @param viewSessions User can view details of all possible sessions
     * @param viewInstructors User can view and edit instructors
     * @param changeSessions User can make edits to sessions
     */
    public void setPermissions(int id,
                               boolean viewBookings,
                               boolean viewDetails,
                               boolean viewAllDetails,
                               boolean viewAllBookings,
                               boolean registerUser,
                               boolean viewSessions,
                               boolean viewInstructors,
                               boolean changeSessions) {

        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("view_bookings", viewBookings);
        values.put("view_details", viewDetails);
        values.put("view_all_details", viewAllDetails);
        values.put("view_all_bookings", viewAllBookings);
        values.put("register_user", registerUser);
        values.put("view_sessions", viewSessions);
        values.put("view_instructors", viewInstructors);
        values.put("change_sessions", changeSessions);

        db.insertWithOnConflict(PERMISSIONS_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void addUser(int id,
                        String firstName,
                        String lastName,
                        String email,
                        String phone,
                        int membership) {

        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("first_name", firstName);
        values.put("last_name", lastName);
        values.put("email", email);
        values.put("phone", phone);
        values.put("membership", membership);

        db.insert(
                USERS_TABLE,
                null,
                values
        );

        setPermissions(id, true, true, false, false, false, false, false, false);
    }

    public void registerCredentials(int id,
                                    String username,
                                    String password) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("id", id);

        db.insert(CREDENTIALS_TABLE, null, values);
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
