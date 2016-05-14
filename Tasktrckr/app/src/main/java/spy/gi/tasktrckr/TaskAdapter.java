package spy.gi.tasktrckr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;


public class TaskAdapter extends CursorAdapter {
    public TaskAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.task_row, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        EditText durationView = (EditText) view.findViewById(R.id.duration);
        TextView descriptionView = (TextView) view.findViewById(R.id.description);

        durationView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText newDuration = (EditText) v.findViewById(R.id.duration);
                String rowId = (String) v.getTag();
                if (!hasFocus) {
                    ContentValues updatedTask = new ContentValues();
                    updatedTask.put(TaskDatabaseHelper.TASK_DURATION, "20");

                    SQLiteDatabase taskDb = new TaskDatabaseHelper(context).getWritableDatabase();
                    taskDb.update(TaskDatabaseHelper.DICTIONARY_TABLE_NAME, updatedTask, String.format("%s = ?", "rowid"), new String[]{rowId});
                }
            }
        });

        long start = cursor.getLong(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.TASK_START));
        long end = cursor.getLong(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.TASK_END));
        int durationInSeconds = (int) (end - start) / 1000;
        int mins = durationInSeconds / 60;
        int hours = 0;
        if (mins >= 60) {
            hours = durationInSeconds / 60 / 60;
            mins = durationInSeconds % 60;
        }

        String type = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.TASK_TYPE));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.TASK_DESCRIPTION));
        String rowId = cursor.getString(cursor.getColumnIndexOrThrow("rowid"));

        // Populate fields in the view with extracted properties from cursor
        view.setBackgroundColor(context.getResources().getColor(TaskType.getTypeFromName(type).getColor()));
        durationView.setText(hours + "h " + mins + "m");
        descriptionView.setText(description);
        durationView.setTag(rowId);
    }
}
