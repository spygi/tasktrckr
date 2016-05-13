package spy.gi.tasktrckr;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
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
    public void bindView(View view, Context context, Cursor cursor) {
        TextView viewStart = (TextView) view.findViewById(R.id.start);
        TextView viewEnd = (TextView) view.findViewById(R.id.end);

        Date start = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.TASK_START)));
        Date end = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.TASK_END)));
        String type = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.TASK_TYPE));

        // Populate fields in the view with extracted properties from cursor
        view.setBackgroundColor(context.getResources().getColor(TaskType.getTypeFromName(type).getColor()));
        viewStart.setText(start.toString());
        viewEnd.setText(end.toString());
    }
}
