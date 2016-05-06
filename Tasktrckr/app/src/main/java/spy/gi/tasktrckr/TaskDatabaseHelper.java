package spy.gi.tasktrckr;

import android.content.ContentUris;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

public class TaskDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 5;
    private static final String DICTIONARY_TABLE_NAME = "tasks";
    private static final String TASK_START = "start";
    private static final String TASK_END = "end";
    private static final String TASK_TYPE = "type";
    private static final String TASK_DESCRIPTION = "description";

    private static final String DICTIONARY_TABLE_CREATE =
            "CREATE TABLE " + DICTIONARY_TABLE_NAME + " (" +
                    BaseColumns._ID + " INTEGER, " +
                    TASK_TYPE + " TEXT, " +
                    TASK_DESCRIPTION + " TEXT, " +
                    TASK_START + " TEXT, " +
                    TASK_END + " TEXT);";

    TaskDatabaseHelper(Context context) {
        super(context, context.getString(R.string.app_name), null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DICTIONARY_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE " + DICTIONARY_TABLE_NAME);
            db.execSQL(DICTIONARY_TABLE_CREATE);
        }
    }
}