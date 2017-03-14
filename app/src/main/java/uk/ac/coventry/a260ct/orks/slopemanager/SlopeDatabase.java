package uk.ac.coventry.a260ct.orks.slopemanager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

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
    private static final int DATABASE_VERSION = 11;

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
    private static final String COL_DOB = "dob";
    private static final String COL_MEMBERSHIP = "membership";

    // User Type table constants
    private static final String USER_TYPES_TABLE =  "user_types";
    private static final String COL_USER_TYPE_ID = "user_type_id";
    private static final String COL_NAME = "user_type_name";

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
                    CREDENTIALS_TABLE,
                    USERS_TABLE,
                    BOOKINGS_TABLE,
                    USER_TYPES_TABLE,
                    SESSIONS_TABLE
            };

    private SQLiteDatabase db;


    public SlopeDatabase(Context c) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
        db = getReadableDatabase();

        if (getUserIdFromCredentials("lol", "lol") == -1) {
            Log.v(TAG, "Adding test data");

            try {
                addUser(21312432,
                        "Jim",
                        "Jiggles",
                        "test@test.com",
                        "07283929394",
                        new SimpleDateFormat("yyyy-mm-dd", Locale.UK).parse("1996-11-28"),
                        1,
                        3);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            registerCredentials(21312432, "lol", "lol"); // Login credentials

            addSession(new Date(), 0); // Add a session today;
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
                        ")";

        String createUsers =
                "CREATE TABLE IF NOT EXISTS "+ USERS_TABLE + "(" +
                        COL_ID + " INTEGER NOT NULL PRIMARY KEY, " +
                        COL_FIRST_NAME + " varchar(255) NOT NULL, " +
                        COL_LAST_NAME + " varchar(255), " +
                        COL_EMAIL + " varchar(255) NOT NULL, " +
                        COL_PHONE + " varchar(255) NOT NULL, " +
                        COL_DOB + " varchar(255) NOT NULL, " +
                        COL_MEMBERSHIP + " INTEGER NOT NULL, " +
                        COL_USER_TYPE_ID + " INTEGER NOT NULL" +
                        ")";

        /**
         * Holds the user type names
         */
        String createUserTypes =
                "CREATE TABLE IF NOT EXISTS "+ USER_TYPES_TABLE + "(" +
                        COL_USER_TYPE_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        COL_NAME + " VARCHAR(255) NOT NULL" +
                        ")";

        String createSessions =
                "CREATE TABLE IF NOT EXISTS "+ SESSIONS_TABLE + "(" +
                        COL_SESSION_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        COL_INSTRUCTOR_ID + " INTEGER, " +
                        COL_DATE + " DATE NOT NULL, " +
                        COL_SLOT + " INTEGER NOT NULL " +
                        ")";

        String createBookings =
                "CREATE TABLE IF NOT EXISTS "+ BOOKINGS_TABLE + "(" +
                        COL_BOOKING_ID + " INTEGER NOT NULL PRIMARY KEY, " +
                        COL_USER_ID + " INTEGER NOT NULL, " +
                        COL_PAID + " BOOLEAN NOT NULL, " +
                        COL_SESSION_ID + " INTEGER NOT NULL" +
                        ")";

        db.execSQL(createUsers);
        db.execSQL(createUserTypes);
        db.execSQL(createSessions);
        db.execSQL(createBookings);
        db.execSQL(createCredentials);

        this.db = db;

        addUserType("User");
        addUserType("Slope Operator");
        addUserType("Instructor");
        addUserType("Slope Manager");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v(TAG, "Upgrading");
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
     * Adds a user type to the table. Not normally edited after created.
     * @param name User Type name
     */
    public void addUserType(String name) {

        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);

        db.insertWithOnConflict(USER_TYPES_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }
    ///overloaded addUser to make the adding more flexible
    public void addUser(User user){
        ContentValues values=new ContentValues();
        values.put(COL_ID, user.getID());
        values.put(COL_FIRST_NAME,user.getFirstName() );
        values.put(COL_LAST_NAME, user.getSurname());
        values.put(COL_EMAIL, user.getEmail());
        values.put(COL_PHONE, user.getPhone());
        values.put(COL_DOB, new SimpleDateFormat("yyyy-mm-dd", Locale.UK).format(user.getDob()));
        values.put(COL_MEMBERSHIP, user.getMembership());
        values.put(COL_USER_TYPE_ID, UserFactory.getUserType(user));
        db.insert(
                USERS_TABLE,
                null,
                values
        );

        Log.v(TAG, "Added user");


    }

    //overloaded this method so we can add a user using only the hashmaap given that we use it to construct users
    public void addUser(HashMap<User.ATTRIBUTES,String>details){
        ContentValues values=new ContentValues();
        values.put(COL_ID, details.get(User.ATTRIBUTES.ID));
        values.put(COL_FIRST_NAME,details.get(User.ATTRIBUTES.FIRST_NAME) );
        values.put(COL_LAST_NAME, details.get(User.ATTRIBUTES.SURNAME));
        values.put(COL_EMAIL, details.get(User.ATTRIBUTES.EMAIL));
        values.put(COL_PHONE, details.get(User.ATTRIBUTES.PHONE));
        values.put(COL_DOB, new SimpleDateFormat("yyyy-mm-dd", Locale.UK).format(User.ATTRIBUTES.DOB));
        values.put(COL_MEMBERSHIP, details.get(User.ATTRIBUTES.MEMBERSHIP));
        values.put(COL_USER_TYPE_ID, details.get(User.ATTRIBUTES.USER_TYPE_ID));
        db.insert(
                USERS_TABLE,
                null,
                values
        );

        Log.v(TAG, "Added user");


    }
    public void addUser(int id,
                        String firstName,
                        String lastName,
                        String email,
                        String phone,
                        Date dob,
                        int membership,
                        int userType) {

        ContentValues values = new ContentValues();
        values.put(COL_ID, id);
        values.put(COL_FIRST_NAME, firstName);
        values.put(COL_LAST_NAME, lastName);
        values.put(COL_EMAIL, email);
        values.put(COL_PHONE, phone);
        values.put(COL_DOB, new SimpleDateFormat("yyyy-mm-dd", Locale.UK).format(dob));
        values.put(COL_MEMBERSHIP, membership);
        values.put(COL_USER_TYPE_ID, userType);

        db.insert(
                USERS_TABLE,
                null,
                values
        );

        Log.v(TAG, "Added user");
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
    public int addSession(Date date, int slot) {
        ContentValues values = new ContentValues();

        values.put(COL_SLOT, slot);
        values.put(COL_DATE, new SimpleDateFormat("yyyy-mm-dd", Locale.UK).format(date));

        return (int) db.insert(SESSIONS_TABLE, null, values);
    }

    public User getUserFromId(int id) {
        String query = "SELECT * FROM " + USERS_TABLE + " WHERE " + COL_ID + "=?";

        UserFactory factory=new UserFactory();
        HashMap<User.ATTRIBUTES,String>map=new HashMap<>();


        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
        int userType=-1;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
               //get the right permission so that factory can generate the correct user
                userType = cursor.getInt(cursor.getColumnIndex(COL_USER_TYPE_ID));
                //put details of the user in hashmap.
                map.put(User.ATTRIBUTES.ID,cursor.getString(cursor.getColumnIndex(COL_ID)));
                map.put(User.ATTRIBUTES.FIRST_NAME,cursor.getString(cursor.getColumnIndex(COL_FIRST_NAME)));
                map.put(User.ATTRIBUTES.SURNAME,cursor.getString(cursor.getColumnIndex(COL_LAST_NAME)));
                map.put(User.ATTRIBUTES.PHONE,cursor.getString(cursor.getColumnIndex(COL_PHONE)));
                map.put(User.ATTRIBUTES.EMAIL,cursor.getString(cursor.getColumnIndex(COL_EMAIL)));
                map.put(User.ATTRIBUTES.DOB, cursor.getString(cursor.getColumnIndex(COL_DOB)));
                map.put(User.ATTRIBUTES.MEMBERSHIP, String.valueOf(cursor.getInt(cursor.getColumnIndex(COL_MEMBERSHIP))));
                cursor.close();
            }
        }
       // Log.v(TAG, "Test");
        //Log.v(TAG, map.get(User.ATTRIBUTES.MEMBERSHIP));
        return UserFactory.getUser(userType,map);
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
