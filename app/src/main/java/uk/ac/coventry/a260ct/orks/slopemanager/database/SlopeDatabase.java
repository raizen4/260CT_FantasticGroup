package uk.ac.coventry.a260ct.orks.slopemanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import uk.ac.coventry.a260ct.orks.slopemanager.SlopeManagerApplication;

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
    private static final int DATABASE_VERSION = 17;

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
    private static final String COL_DATE = "date";
    private static final String COL_SLOT = "slot";

    // Bookings table constants
    private static final String BOOKINGS_TABLE = "bookings";
    private static final String COL_BOOKING_ID = "booking_id";
    private static final String COL_PAID = "paid";
    private static final String COL_WANTS_INSTRUCTOR = "wants_instructor";


    private static final String[] ALL_TABLES =
            new String[]{
                    CREDENTIALS_TABLE,
                    USERS_TABLE,
                    BOOKINGS_TABLE,
                    USER_TYPES_TABLE,
                    SESSIONS_TABLE
            };

    private SQLiteDatabase db;


    public SlopeDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getReadableDatabase();

        if (getUserIdFromCredentials("lol", "lol") == -1) {
            Log.v(TAG, "Adding test data");

            addUser(21312432,
                    "Jim",
                    "Jiggles",
                    "test@test.com",
                    "07283929394",
                    SlopeManagerApplication.stringToDate("1996-11-28"),
                    1,
                    3);

            addUser(87817382,
                    "Sege",
                    "Tong",
                    "test@lol.com",
                    "08273782737",
                    SlopeManagerApplication.stringToDate("1997-09-3"),
                    1,
                    0);

            registerCredentials(21312432, "lol", "lol"); // Login credentials
            registerCredentials(87817382, "lol1", "lol"); // Login credentials
            Calendar calendar = Calendar.getInstance();
            Random random = new Random();

            for (int j = 0; j < 100; j++) {
                calendar.setTime(new Date());
                calendar.add(Calendar.DATE, j);  // number of days to add

                if (random.nextInt(20) > 0 && j != 1) {
                    for (int i = 0; i < 10; i++) {
                        if (random.nextInt(100) != 0) {
                            addSession(calendar.getTime(), i); // Add a session today;
                        }
                    }
                }
            }
            SkiSession session = getSessionFromDateAndSlot(new Date(), 0); // Get that session
            createBooking(session.getId(), 21312432, false, false);
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
                        COL_DATE + " DATE NOT NULL, " +
                        COL_SLOT + " INTEGER NOT NULL " +
                        ")";

        String createBookings =
                "CREATE TABLE IF NOT EXISTS "+ BOOKINGS_TABLE + "(" +
                        COL_BOOKING_ID + " INTEGER NOT NULL PRIMARY KEY, " +
                        COL_USER_ID + " INTEGER NOT NULL, " +
                        COL_WANTS_INSTRUCTOR + " INTEGER NOT NULL," +
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
        addUser(user.getId(),
                user.getFirstName(),
                user.getSurname(),
                user.getEmail(),
                user.getPhone(),
                user.getDob(),
                user.getMembership(),
                UserFactory.getUserType(user));
    }

    //overloaded this method so we can add a user using only the hashmaap given that we use it to construct users
    public void addUser(HashMap<User.ATTRIBUTES, String> details) throws ParseException {
        addUser(
                UserFactory.getUser(
                        Integer.parseInt(details.get(User.ATTRIBUTES.USER_TYPE_ID)),
                        details)
        );
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
        values.put(COL_DOB, SlopeManagerApplication.dateToString(dob));
        values.put(COL_MEMBERSHIP, membership);
        values.put(COL_USER_TYPE_ID, userType);

        db.insert(
                USERS_TABLE,
                null,
                values
        );

        Log.v(TAG, "Added user");
    }

    public int createBooking(int sessionId, int userId, boolean paid, boolean wantsInstructor) {

        ContentValues values = new ContentValues();
        values.put(COL_USER_ID, userId);
        values.put(COL_PAID, paid);
        values.put(COL_WANTS_INSTRUCTOR, wantsInstructor);

        values.put(COL_SESSION_ID, sessionId);

        return (int) db.insert(
                BOOKINGS_TABLE,
                null,
                values
        );
    }

    public boolean removeBooking(int bookingId) {
        return db.delete(
                BOOKINGS_TABLE,
                COL_BOOKING_ID + "=?",
                new String[]{String.valueOf(bookingId)}
        ) > 0;
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

    public Booking[] getBookingsForUser(User user) {
        ArrayList<Booking> bookings = new ArrayList<>();


        String query = "SELECT * FROM " + BOOKINGS_TABLE + " WHERE " + COL_USER_ID + "=?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(user.getId())});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    bookings.add(
                            new Booking(
                                    cursor.getInt(
                                            cursor.getColumnIndex(COL_BOOKING_ID)
                                    ),
                                    getSessionFromId(
                                            cursor.getInt(cursor.getColumnIndex(COL_SESSION_ID)
                                            )
                                    ),
                                    cursor.getInt(
                                            cursor.getColumnIndex(COL_WANTS_INSTRUCTOR)
                                    ) != 0,
                                    cursor.getInt(
                                            cursor.getColumnIndex(COL_PAID)
                                    ) != 0
                            )
                    );
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return bookings.toArray(new Booking[bookings.size()]);
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
        values.put(COL_DATE, SlopeManagerApplication.dateToString(date));

        return (int) db.insert(SESSIONS_TABLE, null, values);
    }

    public SkiSession getSessionFromDateAndSlot(Date date, int slot) {
        SkiSession session = null;
        String query =
                "SELECT * FROM " + SESSIONS_TABLE + " WHERE " + COL_DATE + "=? AND " + COL_SLOT + "=?";

        Cursor cursor =
                db.rawQuery(
                    query,
                    new String[]{SlopeManagerApplication.dateToString(date), String.valueOf(slot)}
                );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                session = buildSessionFromCursor(cursor);
            }
            cursor.close();
        }

        return session;
    }

    private SkiSession buildSessionFromCursor(Cursor cursor) {
        Log.v(TAG, "Session date: " + cursor.getString(
                cursor.getColumnIndex(COL_DATE)
        ));
        return
                new SkiSession(
                        cursor.getInt(
                                cursor.getColumnIndex(COL_SESSION_ID)
                        ),
                        SlopeManagerApplication.stringToDate(
                                cursor.getString(
                                        cursor.getColumnIndex(COL_DATE)
                                )
                        ),
                        cursor.getInt(
                                cursor.getColumnIndex(COL_SLOT)
                        )
                );
    }

    public SkiSession getSessionFromId(int sessionId) {
        String query = "SELECT * FROM " + SESSIONS_TABLE + " WHERE " + COL_SESSION_ID + "=?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(sessionId)});

        SkiSession session = null;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                session = buildSessionFromCursor(cursor);

            }
            cursor.close();
        }

        return session;
    }

    public User getUserFromId(int id) {
        String query = "SELECT * FROM " + USERS_TABLE + " WHERE " + COL_ID + "=?";

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

    public SkiSession[] getSessionsForDate(Date sessionDate) {
        String query = "SELECT * FROM " + SESSIONS_TABLE + " WHERE " + COL_DATE + "=?";
        Log.v(TAG, query);
        Log.v(TAG, SlopeManagerApplication.dateToString(sessionDate));

        Cursor cursor = db.rawQuery(query, new String[]{SlopeManagerApplication.dateToString(sessionDate)});
        ArrayList<SkiSession> sessions = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    sessions.add(buildSessionFromCursor(cursor));
                    Log.v(TAG, "Found a session");
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return sessions.toArray(new SkiSession[sessions.size()]);
    }

    public Booking getBookingFromId(int bookingId) {
        Booking booking = null;
        String query = "SELECT * FROM " + BOOKINGS_TABLE + " WHERE " + COL_BOOKING_ID + "=?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(bookingId)});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                    booking = new Booking(
                            cursor.getInt(
                                    cursor.getColumnIndex(COL_BOOKING_ID)
                            ),
                            getSessionFromId(
                                    cursor.getInt(cursor.getColumnIndex(COL_SESSION_ID)
                                    )
                            ),
                            cursor.getInt(
                                    cursor.getColumnIndex(COL_WANTS_INSTRUCTOR)
                            ) != 0,
                            cursor.getInt(
                                    cursor.getColumnIndex(COL_PAID)
                            ) != 0
                    );
            }
            cursor.close();
        }

        return booking;
    }
}
