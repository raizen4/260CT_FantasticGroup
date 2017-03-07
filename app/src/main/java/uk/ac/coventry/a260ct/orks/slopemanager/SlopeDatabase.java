package uk.ac.coventry.a260ct.orks.slopemanager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * Created by boldurbogdan on 28/02/2017.
 *
 * This is a supposed to be a database that would be run on a server. It would store attributes
 * about the users and the sessions.
 *
 * We store the database locally in this prototype as a proof of concept
 */

public class SlopeDatabase extends SQLiteOpenHelper {

    private final String TAG = this.getClass().getSimpleName();

    private static final String DATABASE_NAME = "SBC_System_Database.db";
    private static final int DATABASE_VERSION = 2;

    // Credentials table constants
    private static final String CREDENTIALS_TABLE = "credentials";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";
    private static final String COL_USER_ID = "user_id";

    // Users table constants
    private static final String USERS_TABLE = "users";
    private static final String COL_ID = "id";
    private static final String COL_FIRST_NAME = "first_name";
    private static final String COL_LAST_NAME = "last_name";
    private static final String COL_EMAIL = "email";
    private static final String COL_PHONE = "phone";
    private static final String COL_MEMBERSHIP = "membership";

    // Permission table constants
    private static final String PERMISSIONS_TABLE =  "permissions";
    public static final String COL_VIEW_BOOKINGS_PERMISSION = "view_bookings_permission";
    public static final String COL_VIEW_DETAILS_PERMISSION = "view_details_permission";
    public static final String COL_VIEW_ALL_DETAILS_PERMISSION = "view_all_details_permission";
    public static final String COL_VIEW_ALL_BOOKINGS_PERMISSION = "view_all_bookings_permission";
    public static final String COL_REGISTER_USER_PERMISSION = "register_user_permission";
    public static final String COL_VIEW_SESSIONS_PERMISSION = "view_sessions_permission";
    public static final String COL_VIEW_INSTRUCTORS_PERMISSION = "view_instructors_permission";
    public static final String COL_CHANGE_SESSIONS_PERMISSION = "change_sessions_permission";

    // Session table constants
    private static final String SESSIONS_TABLE = "sessions";
    private static final String COL_SESSION_ID = "session_id";
    private static final String COL_INSTRUCTOR_ID = "instructor_id";
    private static final String COL_DATE = "date";
    private static final String COL_SLOT = "slot";

    // Bookings table constants
    private static final String BOOKINGS_TABLE = "bookings";
    private static final String COL_BOOKING_ID = "booking_id";
    private static final String COL_PAID = "paid";




    private static final String[] ALL_TABLES =
            new String[]{
                    USERS_TABLE,
                    BOOKINGS_TABLE,
                    PERMISSIONS_TABLE,
                    SESSIONS_TABLE
            };

    private SQLiteDatabase db;


    public SlopeDatabase(Context c) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
        db = getReadableDatabase();

        if (getUserIdFromCredentials("lol", "lol") == -1) {
            Log.v(TAG, "Adding test data");
            addUser(21312432, "Jim", "Jiggles", "test@test.com", "07283929394", 3);
            setPermissions(21312432, true, true, true, true, true, true, true, true);
            registerCredentials(21312432, "lol", "lol");
            addSession(new Date(), 0);
        }

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
                        COL_USERNAME + " varchar(255) NOT NULL PRIMARY KEY, " +
                        COL_PASSWORD + " varchar(255) NOT NULL, " +
                        COL_USER_ID + " INTEGER NOT NULL" +
                        ");";

        String createUsers =
                "CREATE TABLE IF NOT EXISTS "+ USERS_TABLE + "(" +
                        COL_ID + " INTEGER NOT NULL PRIMARY KEY, " +
                        COL_FIRST_NAME + " varchar(255) NOT NULL, " +
                        COL_LAST_NAME + " varchar(255), " +
                        COL_EMAIL + " varchar(255) NOT NULL, " +
                        COL_PHONE + " varchar(255) NOT NULL, " +
                        COL_MEMBERSHIP + " membership int NOT NULL " +
                        ");";

        /**
         * permissions is used to hold what permissions the User has access to
         */

        String createPermissions =
                "CREATE TABLE IF NOT EXISTS "+ PERMISSIONS_TABLE + "(" +
                        COL_USER_ID + " INTEGER NOT NULL PRIMARY KEY, " +
                        COL_VIEW_BOOKINGS_PERMISSION + " BOOLEAN, " + // User can view their bookings
                        COL_VIEW_DETAILS_PERMISSION + " BOOLEAN, " + // User can view their details
                        COL_VIEW_ALL_DETAILS_PERMISSION + " BOOLEAN, " + // User can view anyone's details
                        COL_VIEW_ALL_BOOKINGS_PERMISSION +" BOOLEAN, " + // User can view anyone's bookings
                        COL_REGISTER_USER_PERMISSION + " BOOLEAN, " + // User can register other users
                        COL_VIEW_SESSIONS_PERMISSION + " BOOLEAN, " + // User can view details of all possible sessions
                        COL_VIEW_INSTRUCTORS_PERMISSION + " BOOLEAN, " + // User can view and edit instructors
                        COL_CHANGE_SESSIONS_PERMISSION + " BOOLEAN" + // User can make edits to sessions
                        ");";

        String createSessions =
                "CREATE TABLE IF NOT EXISTS "+ SESSIONS_TABLE + "(" +
                        COL_SESSION_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        COL_INSTRUCTOR_ID + " INTEGER, " +
                        COL_DATE + " DATE NOT NULL, " +
                        COL_SLOT + " INTEGER NOT NULL " +
                        ");";

        String createBookings =
                "CREATE TABLE IF NOT EXISTS "+ BOOKINGS_TABLE + "(" +
                        COL_BOOKING_ID + " INTEGER NOT NULL PRIMARY KEY, " +
                        COL_USER_ID + " INTEGER NOT NULL, " +
                        COL_PAID + " BOOLEAN NOT NULL, " +
                        COL_SESSION_ID + " INTEGER NOT NULL" +
                        ");";

        db.execSQL(createUsers);
        db.execSQL(createPermissions);
        db.execSQL(createSessions);
        db.execSQL(createBookings);
        db.execSQL(createCredentials);
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
                "SELECT " + COL_USER_ID + " FROM " + CREDENTIALS_TABLE +
                        " WHERE " + COL_USERNAME + "=? AND " + COL_PASSWORD +"=?";

        Log.d(TAG, query);

        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                user_id = cursor.getInt(cursor.getColumnIndex(COL_USER_ID));
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
        values.put(COL_USER_ID, id);
        values.put(COL_VIEW_BOOKINGS_PERMISSION, viewBookings);
        values.put(COL_VIEW_DETAILS_PERMISSION, viewDetails);
        values.put(COL_VIEW_ALL_DETAILS_PERMISSION, viewAllDetails);
        values.put(COL_VIEW_ALL_BOOKINGS_PERMISSION, viewAllBookings);
        values.put(COL_REGISTER_USER_PERMISSION, registerUser);
        values.put(COL_VIEW_SESSIONS_PERMISSION, viewSessions);
        values.put(COL_VIEW_INSTRUCTORS_PERMISSION, viewInstructors);
        values.put(COL_CHANGE_SESSIONS_PERMISSION, changeSessions);

        db.insertWithOnConflict(PERMISSIONS_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }


    public HashMap<String, Boolean> getPermissionsForId(int id) {

        String query = "SELECT * FROM " + PERMISSIONS_TABLE + " WHERE " + COL_USER_ID + "=?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        HashMap<String, Boolean> permissions = new HashMap<>();

        if (cursor != null) {
            if (cursor.moveToFirst()) {

                permissions.put(COL_VIEW_BOOKINGS_PERMISSION,
                        cursor.getInt(cursor.getColumnIndex(COL_VIEW_BOOKINGS_PERMISSION)) > 0);

                permissions.put(COL_VIEW_DETAILS_PERMISSION,
                        cursor.getInt(cursor.getColumnIndex(COL_VIEW_DETAILS_PERMISSION)) > 0);

                permissions.put(COL_VIEW_ALL_DETAILS_PERMISSION,
                        cursor.getInt(cursor.getColumnIndex(COL_VIEW_ALL_DETAILS_PERMISSION)) > 0);

                permissions.put(COL_VIEW_ALL_BOOKINGS_PERMISSION,
                        cursor.getInt(cursor.getColumnIndex(COL_VIEW_ALL_BOOKINGS_PERMISSION)) > 0);

                permissions.put(COL_REGISTER_USER_PERMISSION,
                        cursor.getInt(cursor.getColumnIndex(COL_REGISTER_USER_PERMISSION)) > 0);

                permissions.put(COL_VIEW_SESSIONS_PERMISSION,
                        cursor.getInt(cursor.getColumnIndex(COL_VIEW_SESSIONS_PERMISSION)) > 0);

                permissions.put(COL_VIEW_INSTRUCTORS_PERMISSION,
                        cursor.getInt(cursor.getColumnIndex(COL_VIEW_INSTRUCTORS_PERMISSION)) > 0);

                permissions.put(COL_CHANGE_SESSIONS_PERMISSION,
                        cursor.getInt(cursor.getColumnIndex(COL_CHANGE_SESSIONS_PERMISSION)) > 0);

                cursor.close();
            }
        }

        return permissions;
    }

    public void addUser(int id,
                        String firstName,
                        String lastName,
                        String email,
                        String phone,
                        int membership) {

        ContentValues values = new ContentValues();
        values.put(COL_ID, id);
        values.put(COL_FIRST_NAME, firstName);
        values.put(COL_LAST_NAME, lastName);
        values.put(COL_EMAIL, email);
        values.put(COL_PHONE, phone);
        values.put(COL_MEMBERSHIP, membership);

        db.insert(
                USERS_TABLE,
                null,
                values
        );

        setPermissions(id, true, true, false, false, false, false, false, false);
    }

    public void createBooking(int userId, boolean paid, int sessionId) {

        ContentValues values = new ContentValues();
        values.put(COL_USER_ID, userId);
        values.put(COL_PAID, paid);
        values.put(COL_SESSION_ID, sessionId);

        db.insert(
                BOOKINGS_TABLE,
                null,
                values
        );
    }

    public void setBookingPaidStatus(int bookingId, boolean paid) {
        ContentValues values = new ContentValues();
        values.put(COL_PAID, paid);

        db.update(
                BOOKINGS_TABLE,
                values,
                COL_BOOKING_ID + " =?",
                new String[]{String.valueOf(bookingId)}
        );
    }

    public void registerCredentials(int id,
                                    String username,
                                    String password) {
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);
        values.put(COL_USER_ID, id);

        Log.v(TAG, "inserting credential");
        db.insertWithOnConflict(CREDENTIALS_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    /**
     * Adds a session and returns the session ID
     * @param date date of the session
     * @param slot slot of the session
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public int addSession(Date date, int slot) {
        ContentValues values = new ContentValues();

        values.put(COL_SLOT, slot);
        values.put(COL_DATE, new SimpleDateFormat("yyyy-mm-dd").format(date));

        return (int) db.insert(SESSIONS_TABLE, null, values);
    }

    public User getUserFromId(int id) {
        String query = "SELECT * FROM " + USERS_TABLE + " WHERE " + COL_ID + "=?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        User user = null;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                user = new User(
                        id,
                        cursor.getString(cursor.getColumnIndex(COL_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndex(COL_LAST_NAME)),
                        cursor.getString(cursor.getColumnIndex(COL_PHONE)),
                        cursor.getString(cursor.getColumnIndex(COL_EMAIL)),
                        cursor.getInt(cursor.getColumnIndex(COL_MEMBERSHIP)));

                cursor.close();
            }
        }

        return user;
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
