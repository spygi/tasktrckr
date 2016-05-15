package spy.gi.tasktrckr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class TaskDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 12;
    private static TaskDatabaseHelper mInstance = null;

    public static final String DICTIONARY_TABLE_NAME = "tasks";
    public static final String TASK_START = "start";
    public static final String TASK_END = "end";
    public static final String TASK_DURATION = "duration"; // in case of editing a task
    public static final String TASK_TYPE = "type";
    public static final String TASK_DESCRIPTION = "description";

    private static final String DICTIONARY_TABLE_CREATE =
            "CREATE TABLE " + DICTIONARY_TABLE_NAME + " (" +
                    BaseColumns._ID + " INTEGER, " +
                    TASK_TYPE + " TEXT, " +
                    TASK_DESCRIPTION + " TEXT, " +
                    TASK_START + " TEXT, " +
                    TASK_END + " TEXT);";

    private TaskDatabaseHelper(Context context) {
        super(context, context.getString(R.string.app_name), null, DATABASE_VERSION);
    }

    public static TaskDatabaseHelper getInstance(Context ctx) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new TaskDatabaseHelper(ctx.getApplicationContext());
        }
        return mInstance;
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