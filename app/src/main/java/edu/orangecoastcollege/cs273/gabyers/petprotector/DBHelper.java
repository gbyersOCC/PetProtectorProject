package edu.orangecoastcollege.cs273.gabyers.petprotector;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;

/**
 * @author Grant Byers
 * 10/23/2016 Class creates SQLiteDatabse, and methods for acccessing and upddating that database
 */

public class DBHelper extends SQLiteOpenHelper{
    static final String DBASE_NAME = "PetProtector";
    private static final String DBASE_TABLE = "PetProfile";
    private static final int DBASE_VERSION = 1;

    //define Column Headers for database table
    private static final String KEY_FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_DETAILS = "details";
    private static final String FIELD_PHONENUMBER = "phoneNumber";
    private static final String FIELD_IMAGEPATH = "imagePath";

    public DBHelper(Context context) {
        super(context, DBASE_NAME, null, DBASE_VERSION);
    }

    /**
     * Method allows for creation of SQLiteDatabase with fields such as: id, name, details, phone and image path.
     * @param database
     */
    public void onCreate(SQLiteDatabase database) {
        String table = "CREATE TABLE " + DBASE_TABLE + "(" +
                KEY_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FIELD_NAME + " TEXT, " +
                FIELD_DETAILS + " TEXT, " +
                FIELD_PHONENUMBER + " TEXT, " +
                FIELD_IMAGEPATH + " TEXT" + ")";
        database.execSQL(table);
    }

    /**
     * Method quries SQLiteDatabase table named PetProfile
     * Method allows for dump of all saved objects into the returned arrayList
     * @return
     */
    public ArrayList<PetProfile> getAllProfiles(){
        ArrayList<PetProfile> profileList = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();

        Cursor results = database.query(DBASE_TABLE, new String[]{KEY_FIELD_ID,
                        FIELD_NAME,
                        FIELD_DETAILS,
                        FIELD_PHONENUMBER,
                        FIELD_IMAGEPATH},
                null, null, null, null, null,null);
        if (results.moveToFirst()){

            do{
                int id = results.getInt(0);
                String name = results.getString(1);
                String details = results.getString(2);
                String phoneNumber = results.getString(3);
                String imagePath = results.getString(4);
                Uri imageUriPath = Uri.parse(imagePath);
                PetProfile petPro = new PetProfile(id, name, details, phoneNumber, imageUriPath);
                profileList.add(petPro);

            }while(results.moveToNext());
        }
        database.close();
        return profileList;
    }

    /**
     * Method adds (provided) PetProfile object to Database table named "PetProfile"
     * @param profile
     */
    public void addProfile(PetProfile profile){
        SQLiteDatabase database = getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put(FIELD_NAME, profile.getName());
        content.put(FIELD_DETAILS, profile.getDetails());
        content.put(FIELD_PHONENUMBER, profile.getPhoneNumber());
        content.put(FIELD_IMAGEPATH, profile.getImagePath().toString());
//now just insert and close
        database.insert(DBASE_TABLE,null,content);

        database.close();


    }

    /**
     * Method updates the (provided) object in database with the most current object accessors
     * @param profile
     */
    public void updateProfile(PetProfile profile){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put(FIELD_NAME,profile.getName());
        content.put(FIELD_DETAILS,profile.getDetails());
        content.put(FIELD_PHONENUMBER, profile.getPhoneNumber());
        content.put(FIELD_IMAGEPATH, profile.getImagePath().toString());

        database.update(DBASE_TABLE, content, KEY_FIELD_ID+"=?",new String[]{String.valueOf(profile.getId())});
        database.close();

    }

    /**
     * Method queries the database looking for the given integer id and returns the associated object
     * @param id
     * @return
     */
    public PetProfile getProfile(int id){
        //open a readable database
        SQLiteDatabase database = getReadableDatabase();
        //load up Cursor object with query
        Cursor content = database.query(DBASE_TABLE, new String[]{KEY_FIELD_ID,FIELD_NAME,
                        FIELD_DETAILS,FIELD_PHONENUMBER,FIELD_IMAGEPATH},
                KEY_FIELD_ID+"=?", new String[]{String.valueOf(id)},
                null, null, null, null);

        //make variables with content
        int objInt = content.getInt(0);
        String name = content.getString(1);
        String details = content.getString(2);
        String phone = content.getString(3);
        String imagePath = content.getString(4);
        Uri imageUriPath = Uri.parse(imagePath);
        PetProfile profile = new PetProfile(objInt,name, details,phone,imageUriPath);

        database.close();
        return profile;
    }

    /**
     * Method deletes the saved table in database
     */
    public void deleteAllProfiles(){
        //attach writable database
        SQLiteDatabase database = this.getWritableDatabase();
        //delete database TABLE
        database.delete(DBASE_TABLE,null, null);
        database.close();
    }

    /**
     * Method allows for updating to most current version of database by dropping the
     * old one and creating a new one with new version number
     * @param database
     * @param oldVersion
     * @param newVersion
     */
    public void onUpgrade(SQLiteDatabase database,
                          int oldVersion,
                          int newVersion) {
        // TODO:  Execute the SQL statment to drop the table
        database.execSQL("DROP TABLE IF EXISTS " + DBASE_TABLE);
        // TODO:  Recreate the database
        onCreate(database);
    }

}
