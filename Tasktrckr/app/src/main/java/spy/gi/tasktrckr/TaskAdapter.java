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

import java.text.SimpleDateFormat;
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
    public void bindView(View view, final Context context, final Cursor cursor) {
        EditText durationView = (EditText) view.findViewById(R.id.duration);
        TextView descriptionView = (TextView) view.findViewById(R.id.description);
        TextView dateOfTask = (TextView) view.findViewById(R.id.dateOfTask);

        durationView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText newDuration = (EditText) v.findViewById(R.id.duration);
                View parent = (View) v.getParent();
                String rowId = (String) parent.getTag();
                if (!hasFocus) {
                    ContentValues updatedTask = new ContentValues();
                    updatedTask.put(TaskDatabaseHelper.TASK_DURATION, "20");

                    SQLiteDatabase taskDb = new TaskDatabaseHelper(context).getWritableDatabase();
                    taskDb.update(TaskDatabaseHelper.DICTIONARY_TABLE_NAME, updatedTask, String.format("%s = ?", "rowid"), new String[]{rowId});
                }
            }
        });

        TextView deleteView = (TextView) view.findViewById(R.id.deleteTask);
        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parent = (View) v.getParent();
                String rowId = (String) parent.getTag();
                SQLiteDatabase taskDb = new TaskDatabaseHelper(context).getWritableDatabase();
                taskDb.delete(TaskDatabaseHelper.DICTIONARY_TABLE_NAME, String.format("%s = ?", "rowid"), new String[]{rowId});

                cursor.requery();
                notifyDataSetChanged();
            }
        });

        // calculate duration
        long start = cursor.getLong(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.TASK_START));
        long end = cursor.getLong(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.TASK_END));
        int durationInSeconds = (int) (end - start) / 1000;
        int mins = durationInSeconds / 60;
        int hours = 0;
        if (mins >= 60) {
            hours = durationInSeconds / 60 / 60;
            mins = durationInSeconds % 60;
        }

        // put the date if it's new
        Date startDate = new Date(start);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (cursor.getPosition() > 0) {
            cursor.moveToPrevious();
            Date startOfPreviousTask = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.TASK_START)));
            if (sdf.format(startDate).equals(sdf.format(startOfPreviousTask))) { // hacky: compare dates based on their string representation
                startDate = null;
            }
            cursor.moveToNext(); // fix the cursor's position
        }

        String type = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.TASK_TYPE));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.TASK_DESCRIPTION));
        String rowId = cursor.getString(cursor.getColumnIndexOrThrow("rowid"));

        view.setTag(rowId);
        view.setBackgroundColor(context.getResources().getColor(TaskType.getTypeFromName(type).getColor()));
        durationView.setText(hours + "h " + mins + "m");
        descriptionView.setText(description);
        if (startDate != null) {
            dateOfTask.setText(sdf.format(startDate));
        }
    }
}
