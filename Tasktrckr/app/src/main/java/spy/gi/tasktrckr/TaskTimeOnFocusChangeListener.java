package spy.gi.tasktrckr;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskTimeOnFocusChangeListener implements View.OnFocusChangeListener {

    public TaskTimeOnFocusChangeListener() {
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            EditText editText = (EditText) v;
            View parent = (View) v.getParent();
            Date taskDate = (Date) parent.getTag(R.string.tag_task_date);
            String rowId = (String) parent.getTag(R.string.tag_row_id);
            String dbFieldToUpdate = (String) v.getTag(R.string.tag_database_field_to_update);

            // combine the new time and the existing date
            SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
            Date newStartTime = new Date();
            try {
                newStartTime = timeFormatter.parse(editText.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(taskDate);
            cal.set(Calendar.HOUR_OF_DAY, newStartTime.getHours());
            cal.set(Calendar.MINUTE, newStartTime.getMinutes());

            ContentValues updatedTask = new ContentValues();
            updatedTask.put(dbFieldToUpdate, cal.getTime().getTime());
            SQLiteDatabase taskDb = TaskDatabaseHelper.getInstance(v.getContext()).getWritableDatabase();
            taskDb.update(TaskDatabaseHelper.DICTIONARY_TABLE_NAME, updatedTask, String.format("%s = ?", "rowid"), new String[]{rowId});
        }
    }
}
