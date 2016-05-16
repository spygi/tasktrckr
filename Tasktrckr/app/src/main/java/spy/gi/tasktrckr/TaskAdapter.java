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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        TextView dateOfTask = (TextView) view.findViewById(R.id.dateOfTask);
        EditText taskStart = (EditText) view.findViewById(R.id.taskStart);
        EditText taskEnd = (EditText) view.findViewById(R.id.taskEnd);
        EditText descriptionView = (EditText) view.findViewById(R.id.description);
        TextView deleteView = (TextView) view.findViewById(R.id.deleteTask);

        // listeners
        TaskTimeOnFocusChangeListener startListener = new TaskTimeOnFocusChangeListener();
        taskStart.setOnFocusChangeListener(startListener);
        taskEnd.setOnFocusChangeListener(startListener);

        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parent = (View) v.getParent();
                String rowId = (String) parent.getTag(R.string.tag_row_id);
                SQLiteDatabase taskDb = TaskDatabaseHelper.getInstance(context).getWritableDatabase();
                taskDb.delete(TaskDatabaseHelper.DICTIONARY_TABLE_NAME, String.format("%s = ?", "rowid"), new String[]{rowId});

                cursor.requery();
                notifyDataSetChanged();
            }
        });

        Date startDate = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.TASK_START)));
        Date endDate = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.TASK_END)));

        String type = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.TASK_TYPE));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.TASK_DESCRIPTION));
        String rowId = cursor.getString(cursor.getColumnIndexOrThrow("rowid"));

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
        view.setTag(R.string.tag_row_id, rowId);
        view.setTag(R.string.tag_task_date, startDate);
        view.setBackgroundColor(context.getResources().getColor(TaskType.getTypeFromName(type).getColor()));
        if (shouldUseTheStartDateOfTheTask(cursor, startDate)) { // start and end dates should be the same anyhow
            dateOfTask.setText(dateFormatter.format(startDate));
        }
        taskStart.setText(timeFormatter.format(startDate));
        taskStart.setTag(R.string.tag_database_field_to_update, TaskDatabaseHelper.TASK_START);
        taskEnd.setText(timeFormatter.format(endDate));
        taskEnd.setTag(R.string.tag_database_field_to_update, TaskDatabaseHelper.TASK_END);
        descriptionView.setText(description);
        deleteView.bringToFront();
    }

    private boolean shouldUseTheStartDateOfTheTask(Cursor cursor, Date startDate) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        boolean useStartDate = true;

        if (cursor.getPosition() > 0) {
            cursor.moveToPrevious();
            Date startOfPreviousTask = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.TASK_START)));
            if (dateFormatter.format(startDate).equals(dateFormatter.format(startOfPreviousTask))) { // hacky: compare dates based on their string representation
                useStartDate = false;
            }
            cursor.moveToNext(); // fix the cursor's position
        }

        return useStartDate;
    }

    private void calculateDistanceInHourAndMinute(long start, long end) {
        int durationInSeconds = (int) (end - start) / 1000;
        int mins = durationInSeconds / 60;
        int hours = 0;
        if (mins >= 60) {
            hours = durationInSeconds / 60 / 60;
            mins = durationInSeconds % 60;
        }
    }
}
