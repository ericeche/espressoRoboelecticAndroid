package com.vidatak.vidatalk;

/**
 * Created by ericecheverri on 9/5/16.
 */
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vidatak.vidatalk.data.Gender;
import com.vidatak.vidatalk.database.ButtonsDatabase;
import com.vidatak.vidatalk.database.DatabaseHelper;
import com.vidatak.vidatalk.database.LanguagesDatabase;
import com.vidatak.vidatalk.database.StringsDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.Locale;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class DatabaseUnitTest {

    private static final String VIDATALK_DATABASE_NAME = "vidatalk.db";
    private DatabaseHelper dbHelper;

    /*****************************************
     *  Strngs Database Variables
     *****************************************/

    private static String mStringsDatabaseColKey = "homecold";
    private static String mStringsDatabaseColText = "I am cold";
    private static String mStringsDatabaseColLongText = "I am cold";

    // New record to be inserted
    private static String mStringsDatabaseNewColKey = "homenotcold";
    private static String mStringsDatabaseNewColText = "I am not cold";
    private static String mStringsDatabaseNewColLongText = "I am not cold";
    private static long mStringsDBAssignId;


    /*****************************************
     *  Buttons Database Variables:
     *
     *   0;;homecold;homecold;images/cold.png;cold.mp3
     *****************************************/


    private static final String COL_BUTTON_NAME = "homecold";
    private static final String COL_STRING_NAME = "homecold";
    private static final String COL_PIC = "cold.png";
    private static final String COL_AUDIO = "cold.mp3";
    private static final String COL_SCREEN = "0";
    private static final String COL_FOLDER = "19";


    private static final String NEW_COL_BUTTON_NAME = "homenotcold";
    private static final String NEW_COL_STRING_NAME = "homenotcold";
    private static final String NEW_COL_PIC = "cold.png";
    private static final String NEW_COL_AUDIO = "cold.mp3";
    private static final String NEW_COL_SCREEN = "0";

    private static long mButtonDBAssignId;

    ShadowApplication context;

    @Before
    public void setUp() {
        context = Shadows.shadowOf(RuntimeEnvironment.application);
        dbHelper = new DatabaseHelper(context.getApplicationContext());
    }

    @Test
    public void whenTheDBHelperIsCreatedThenTheDatabaseNameShouldBeSet() {
        assertEquals(VIDATALK_DATABASE_NAME, dbHelper.getDatabaseName());
    }

    @Test
    public void testCreateDB() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertTrue(db.isOpen());
        db.close();
        Log.d("DBTesting", "testCreateDB Pass");
        System.out.println("DBTesting" + "testCreateDB Pass");
    }

    @Test
    public void testLoadDefaultDataDB() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertTrue(db.isOpen());
        dbHelper.loadDefaultData(db);
        db.close();
        Log.d("DBTesting", "testLoadDefaultDataDB Pass");
        System.out.println("DBTesting" + "testLoadDefaultDataDB Pass");
    }

    /*@Test
    public void testUpgradeDB() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertTrue(db.isOpen());
        dbHelper.onUpgrade(db, 0, 1);
        db.close();
        Log.d("DBTesting", "testUpgradeDB Pass");
        System.out.println("DBTesting" + "testUpgradeDB Pass");
    }*/


    @Test
    public void testUpgradeOnStringsDB() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertTrue(db.isOpen());
        StringsDatabase.onUpgrade(db,0,1);
        db.close();
        Log.d("DBTesting", "testUpgradeOnStringsDB Pass");
        System.out.println("DBTesting" + "testUpgradeOnStringsDB Pass");
    }

    @Test
    public void testUpgradeOnButtonsDB() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertTrue(db.isOpen());
        ButtonsDatabase.onUpgrade(db, 0, 1);
        db.close();
        Log.d("DBTesting", "testUpgradeOnButtonsDB Pass");
        System.out.println("DBTesting" + "testUpgradeOnButtonsDB Pass");
    }

    @Test
    public void testUpgradeOnLanguagesDB() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertTrue(db.isOpen());
        LanguagesDatabase.onUpgrade(db, 0, 1);
        db.close();
        Log.d("DBTesting", "testUpgradeOnButtonsDB Pass");
        System.out.println("DBTesting" + "testUpgradeOnButtonsDB Pass");
    }


    @Test
    public void testStringDatabaseInsertDataAndtestIsDataCorrectInDB(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertTrue(db.isOpen());

        LanguagesDatabase languagesDatabase = new LanguagesDatabase(context.getApplicationContext());
        languagesDatabase.open(db);
        String languageCode = languagesDatabase.getLanguageCodeForLocale(new Locale("en"));

        //  Assert an existing record ( Previously defined in assets/strings.csv )
        ContentValues contentValues = new ContentValues();

        // Assert a non existing record
        contentValues.put(StringsDatabase.COL_KEY, mStringsDatabaseNewColKey);
        contentValues.put(StringsDatabase.COL_TEXT, mStringsDatabaseNewColText);
        contentValues.put(StringsDatabase.COL_LONGTEXT, mStringsDatabaseNewColLongText);
        contentValues.put(StringsDatabase.COL_LANG, languageCode);
        contentValues.put(StringsDatabase.COL_GENDER, Gender.MALE.ordinal());
        mStringsDBAssignId = db.insert(StringsDatabase.TBLNAME, null, contentValues);
        assertTrue(mStringsDBAssignId != -1);


        // Assert an existing record
        contentValues.put(StringsDatabase.COL_KEY, mStringsDatabaseNewColKey);
        contentValues.put(StringsDatabase.COL_TEXT, mStringsDatabaseNewColText);
        contentValues.put(StringsDatabase.COL_LONGTEXT, mStringsDatabaseNewColLongText);
        contentValues.put(StringsDatabase.COL_LANG, languageCode);
        contentValues.put(StringsDatabase.COL_GENDER, Gender.MALE.ordinal());
        mStringsDBAssignId = db.insert(StringsDatabase.TBLNAME, null, contentValues);
        assertTrue(mStringsDBAssignId == -1);

        db.close();

        SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();
        assertTrue(readableDatabase.isOpen());

        Cursor cursor = readableDatabase.query(StringsDatabase.TBLNAME, null, StringsDatabase.COL_KEY + "=? AND " + StringsDatabase.COL_LANG + "=?", new String[]{mStringsDatabaseNewColKey, languageCode}, null, null, null);
        assertTrue(cursor.moveToNext());

        int idColumnIndex = cursor.getColumnIndex(StringsDatabase.COL_ID);
        int dbId = cursor.getInt(idColumnIndex);

        int keyColumnIndex = cursor.getColumnIndex(StringsDatabase.COL_KEY);
        String dbKey = cursor.getString(keyColumnIndex);

        int langColumnIndex = cursor.getColumnIndex(StringsDatabase.COL_LANG);
        String dbLanguage = cursor.getString(langColumnIndex);

        int textColumnIndex = cursor.getColumnIndex(StringsDatabase.COL_TEXT);
        String dbText = cursor.getString(textColumnIndex);

        int longtextColumnIndex = cursor.getColumnIndex(StringsDatabase.COL_LONGTEXT);
        String dbLongText = cursor.getString(longtextColumnIndex);

        assertEquals(mStringsDatabaseNewColKey, dbKey);
        assertEquals(mStringsDatabaseNewColText, dbText);
        assertEquals(mStringsDatabaseNewColLongText, dbLongText);
        assertEquals(languageCode, dbLanguage);

        cursor.close();
        cursor = null;
        readableDatabase.close();

        Log.d("DBTesting", "testIsDataCorrectInDB");
    }

    @Test
    public void testButtonsDatabaseInsertDataAndtestIsDataCorrectInDB(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertTrue(db.isOpen());

        LanguagesDatabase languagesDatabase = new LanguagesDatabase(context.getApplicationContext());
        languagesDatabase.open(db);
        String languageCode = languagesDatabase.getLanguageCodeForLocale(new Locale("en"));


        //  Assert an existing record ( Previously defined in assets/strings.csv )
        ContentValues contentValues = new ContentValues();

        // Assert a non existing record
        contentValues.put(ButtonsDatabase.COL_PIC, NEW_COL_PIC);
        contentValues.put(ButtonsDatabase.COL_SCREEN, NEW_COL_SCREEN);
        contentValues.put(ButtonsDatabase.COL_AUDIO, NEW_COL_AUDIO);
        contentValues.put(ButtonsDatabase.COL_STRING_NAME, NEW_COL_STRING_NAME);
        contentValues.put(ButtonsDatabase.COL_BUTTON_NAME, NEW_COL_BUTTON_NAME);
        mButtonDBAssignId = db.insert(ButtonsDatabase.TBLNAME, null, contentValues);
        assertTrue(mButtonDBAssignId != -1);

        // Assert an existing record
        contentValues.put(ButtonsDatabase.COL_PIC, NEW_COL_PIC);
        contentValues.put(ButtonsDatabase.COL_SCREEN, NEW_COL_SCREEN);
        contentValues.put(ButtonsDatabase.COL_AUDIO, NEW_COL_AUDIO);
        contentValues.put(ButtonsDatabase.COL_STRING_NAME, NEW_COL_STRING_NAME);
        contentValues.put(ButtonsDatabase.COL_BUTTON_NAME, NEW_COL_BUTTON_NAME);
        mButtonDBAssignId = db.insert(ButtonsDatabase.TBLNAME, null, contentValues);
        assertTrue(mButtonDBAssignId == -1);

        db.close();

        SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();
        assertTrue(readableDatabase.isOpen());

        String query =  ButtonsDatabase.COL_BUTTON_NAME + "=? ;";

        Cursor cursor = readableDatabase.query(ButtonsDatabase.TBLNAME,null, query, new String[]{NEW_COL_BUTTON_NAME}, null, null, null);


        assertTrue(cursor.moveToNext());

        int idColumnIndex = cursor.getColumnIndex(ButtonsDatabase.COL_ID);
        int dbId = cursor.getInt(idColumnIndex);

        int keyColumnIndex = cursor.getColumnIndex(ButtonsDatabase.COL_PIC);
        String dbKey = cursor.getString(keyColumnIndex);

        int screenColumnIndex = cursor.getColumnIndex(ButtonsDatabase.COL_SCREEN);
        String dbScreen = cursor.getString(screenColumnIndex);

        int stringNameColumnIndex = cursor.getColumnIndex(ButtonsDatabase.COL_STRING_NAME);
        String dbStringName = cursor.getString(stringNameColumnIndex);

        int buttonNameColumnIndex = cursor.getColumnIndex(ButtonsDatabase.COL_BUTTON_NAME);
        String dbButtonName = cursor.getString(buttonNameColumnIndex);

        assertEquals(NEW_COL_PIC, dbKey);
        assertEquals(NEW_COL_SCREEN, dbScreen);
        assertEquals(NEW_COL_STRING_NAME, dbStringName);
        assertEquals(NEW_COL_BUTTON_NAME, dbButtonName);

        cursor.close();
        cursor = null;
        readableDatabase.close();

        Log.d("DBTesting", "testButtonsDatabaseInsertDataAndtestIsDataCorrectInDB");
    }

    @Test
    public void testButtonsDatabaseInsertDataISCorrectInDBForFolderRecord(){

        final String COL_KEY = "homenotcoldfolder";
        final String COL_TEXT = "homenotcoldfolder";
        final String COL_LONGTEXT = "I am not cold, coz I am inside of a folder";

        final String NEW_COL_BUTTON_NAME = "homenotcoldfolder";
        final String NEW_COL_STRING_NAME = "homenotcoldfolder";
        final String NEW_COL_PIC = "cold.png";
        final Integer NEW_COL_SCREEN = 0;
        final Integer NEW_COL_FOLDER = 9;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertTrue(db.isOpen());

        LanguagesDatabase languagesDatabase = new LanguagesDatabase(context.getApplicationContext());
        languagesDatabase.open(db);
        String languageCode = languagesDatabase.getLanguageCodeForLocale(new Locale("en"));

        ContentValues contentValuesStringDB = new ContentValues();
        // Assert a non existing record
        contentValuesStringDB.put(StringsDatabase.COL_KEY, COL_KEY);
        contentValuesStringDB.put(StringsDatabase.COL_TEXT, COL_TEXT);
        contentValuesStringDB.put(StringsDatabase.COL_LONGTEXT, COL_LONGTEXT);
        contentValuesStringDB.put(StringsDatabase.COL_LANG, languageCode);
        mStringsDBAssignId = db.insert(StringsDatabase.TBLNAME, null, contentValuesStringDB);
        assertTrue(mStringsDBAssignId != -1);


        // Assert a non existing record
        ContentValues contentValues = new ContentValues();
        contentValues.put(ButtonsDatabase.COL_PIC, NEW_COL_PIC);
        contentValues.put(ButtonsDatabase.COL_SCREEN, NEW_COL_SCREEN);
        contentValues.put(ButtonsDatabase.COL_STRING_NAME, NEW_COL_STRING_NAME);
        contentValues.put(ButtonsDatabase.COL_BUTTON_NAME, NEW_COL_BUTTON_NAME);
        contentValues.put(ButtonsDatabase.COL_FOLDER, NEW_COL_FOLDER);
        mButtonDBAssignId = db.insert(ButtonsDatabase.TBLNAME, null, contentValues);
        assertTrue(mButtonDBAssignId != -1);
        db.close();

        Log.d("DBTesting", "testButtonsDatabaseInsertDataISCorrectInDBForAllMethod1");
    }

    @Test
    public void testButtonsDatabaseInsertDataISCorrectInDBForAudioRecord(){

        final String COL_KEY = "homenotcoldfolder";
        final String COL_TEXT = "homenotcoldfolder";
        final String COL_LONGTEXT = "I am not cold, coz I am inside of a folder";

        final String NEW_COL_BUTTON_NAME = "homenotcoldfolder";
        final String NEW_COL_STRING_NAME = "homenotcoldfolder";
        final String NEW_COL_PIC = "cold.png";
        final String NEW_COL_AUDIO = "audio.mp3";
        final Integer NEW_COL_SCREEN = 0;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertTrue(db.isOpen());

        LanguagesDatabase languagesDatabase = new LanguagesDatabase(context.getApplicationContext());
        languagesDatabase.open(db);
        String languageCode = languagesDatabase.getLanguageCodeForLocale(new Locale("en"));

        ContentValues contentValuesStringDB = new ContentValues();
        // Assert a non existing record
        contentValuesStringDB.put(StringsDatabase.COL_KEY, COL_KEY);
        contentValuesStringDB.put(StringsDatabase.COL_TEXT, COL_TEXT);
        contentValuesStringDB.put(StringsDatabase.COL_LONGTEXT, COL_LONGTEXT);
        contentValuesStringDB.put(StringsDatabase.COL_LANG, languageCode);
        mStringsDBAssignId = db.insert(StringsDatabase.TBLNAME, null, contentValuesStringDB);
        assertTrue(mStringsDBAssignId != -1);


        // Assert a non existing record
        ContentValues contentValues = new ContentValues();
        contentValues.put(ButtonsDatabase.COL_PIC, NEW_COL_PIC);
        contentValues.put(ButtonsDatabase.COL_SCREEN, NEW_COL_SCREEN);
        contentValues.put(ButtonsDatabase.COL_STRING_NAME, NEW_COL_STRING_NAME);
        contentValues.put(ButtonsDatabase.COL_AUDIO, NEW_COL_AUDIO);
        contentValues.put(ButtonsDatabase.COL_BUTTON_NAME, NEW_COL_BUTTON_NAME);
        mButtonDBAssignId = db.insert(ButtonsDatabase.TBLNAME, null, contentValues);
        assertTrue(mButtonDBAssignId != -1);
        db.close();

        Log.d("DBTesting", "testButtonsDatabaseInsertDataISCorrectInDBForAudioRecord");
    }

    @Test
    public void testButtonsDatabaseInsertDataISCorrectInDBForFolderAndAudioRecord(){

        final String COL_KEY = "homenotcoldfolder";
        final String COL_TEXT = "homenotcoldfolder";
        final String COL_LONGTEXT = "I am not cold, coz I am inside of a folder";

        final String NEW_COL_BUTTON_NAME = "homenotcoldfolder";
        final String NEW_COL_STRING_NAME = "homenotcoldfolder";
        final String NEW_COL_PIC = "cold.png";
        final String NEW_COL_AUDIO = "audio.mp3";
        final Integer NEW_COL_FOLDER = 9;
        final Integer NEW_COL_SCREEN = 0;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertTrue(db.isOpen());

        LanguagesDatabase languagesDatabase = new LanguagesDatabase(context.getApplicationContext());
        languagesDatabase.open(db);
        String languageCode = languagesDatabase.getLanguageCodeForLocale(new Locale("en"));

        ContentValues contentValuesStringDB = new ContentValues();
        // Assert a non existing record
        contentValuesStringDB.put(StringsDatabase.COL_KEY, COL_KEY);
        contentValuesStringDB.put(StringsDatabase.COL_TEXT, COL_TEXT);
        contentValuesStringDB.put(StringsDatabase.COL_LONGTEXT, COL_LONGTEXT);
        contentValuesStringDB.put(StringsDatabase.COL_LANG, languageCode);
        mStringsDBAssignId = db.insert(StringsDatabase.TBLNAME, null, contentValuesStringDB);
        assertTrue(mStringsDBAssignId != -1);


        // Assert a non existing record
        ContentValues contentValues = new ContentValues();
        contentValues.put(ButtonsDatabase.COL_PIC, NEW_COL_PIC);
        contentValues.put(ButtonsDatabase.COL_SCREEN, NEW_COL_SCREEN);
        contentValues.put(ButtonsDatabase.COL_STRING_NAME, NEW_COL_STRING_NAME);
        contentValues.put(ButtonsDatabase.COL_AUDIO, NEW_COL_AUDIO);
        contentValues.put(ButtonsDatabase.COL_BUTTON_NAME, NEW_COL_BUTTON_NAME);
        contentValues.put(ButtonsDatabase.COL_FOLDER, NEW_COL_FOLDER);
        mButtonDBAssignId = db.insert(ButtonsDatabase.TBLNAME, null, contentValues);
        assertTrue(mButtonDBAssignId != -1);
        db.close();

        Log.d("DBTesting", "testButtonsDatabaseInsertDataISCorrectInDBForFolderAndAudioRecord");
    }

    @Test
    public void testLanguagesDatabaseInsertDataISCorrect(){

        final String ENG_NAME = "Spanish";
        final String NAME = "español";
        final String CODE = "es";
        Locale loc = new Locale(CODE);


        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertTrue(db.isOpen());

        LanguagesDatabase languagesDatabase = new LanguagesDatabase(context.getApplicationContext());
        languagesDatabase.open(db);
        languagesDatabase.addLanguage(loc,false);
        String languageCode = languagesDatabase.getLanguageCodeForLocale(new Locale("es"));
        assertFalse("insert language to database",languageCode.isEmpty());
       /* // Assert a non existing record
        ContentValues cv = new ContentValues();
        cv.put(languagesDatabase.COL_CODE,CODE);
        cv.put(languagesDatabase.COL_ENGLISH_NAME,ENG_NAME);
        cv.put(languagesDatabase.COL_NAME,NAME);
        cv.put(languagesDatabase.COL_FLAGGENDER, 0);
        mStringsDBAssignId = db.insert(LanguagesDatabase.TBLNAME, null, cv);
        assertTrue("Assigning new language to db",mStringsDBAssignId != -1);*/
        db.close();

        SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();
        assertTrue("Readable database open",readableDatabase.isOpen());

        String query =  LanguagesDatabase.COL_CODE + "=? ;";

        Cursor cursor = readableDatabase.query(LanguagesDatabase.TBLNAME,LanguagesDatabase.ALL_COLS, query, new String[]{CODE}, null, null, null);

        assertTrue("cursor is non-empty",cursor.moveToNext());


        int idColumnIndex = cursor.getColumnIndex(LanguagesDatabase.COL_ID);
        int dbId = cursor.getInt(idColumnIndex);

        int keyColumnIndex = cursor.getColumnIndex(LanguagesDatabase.COL_CODE);
        String dbKey = cursor.getString(keyColumnIndex);

        int englishNameColumnIndex = cursor.getColumnIndex(LanguagesDatabase.COL_ENGLISH_NAME);
        String dbEnglishName = cursor.getString(englishNameColumnIndex);

        int columnNameColumnIndex = cursor.getColumnIndex(LanguagesDatabase.COL_NAME);
        String dbColumnName = cursor.getString(columnNameColumnIndex);

        assertEquals(CODE, dbKey);
        assertEquals(ENG_NAME, dbEnglishName);
        assertEquals(NAME, dbColumnName);
        readableDatabase.close();

        Log.d("DBTesting", "testLanguagesDatabaseInsertDataISCorrect");
    }
}


