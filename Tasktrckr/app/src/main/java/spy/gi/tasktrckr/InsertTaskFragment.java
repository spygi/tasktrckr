package spy.gi.tasktrckr;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class InsertTaskFragment extends Fragment {
    private TaskType type;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static InsertTaskFragment newInstance(int page) {
        InsertTaskFragment fragmentFirst = new InsertTaskFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        fragmentFirst.setArguments(args);

        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("page", 0);
        type = TaskType.getTypeFromIndex(page);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insert_task_fragment, container, false);
        switch (type) {
            case ENG:
                view.findViewById(R.id.button).setBackgroundColor(getResources().getColor(TaskType.ENG.getColor()));
                break;
            case TEAM:
                view.findViewById(R.id.button).setBackgroundColor(getResources().getColor(TaskType.TEAM.getColor()));
                break;
            case MGMT:
                view.findViewById(R.id.button).setBackgroundColor(getResources().getColor(TaskType.MGMT.getColor()));
                break;
            default:
                view.findViewById(R.id.button).setBackgroundColor(getResources().getColor(TaskType.MISC.getColor()));
                break;
        }

        Button button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues newTask = new ContentValues();
                View parentView = (ViewGroup) view.getParent();

                TimePicker startPicker = (TimePicker) parentView.findViewById(R.id.startPicker);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, startPicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, startPicker.getCurrentMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                newTask.put(TaskDatabaseHelper.TASK_START, calendar.getTimeInMillis());

                TimePicker endPicker = (TimePicker) parentView.findViewById(R.id.endPicker);
                calendar.set(Calendar.HOUR_OF_DAY, endPicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, endPicker.getCurrentMinute());
                newTask.put(TaskDatabaseHelper.TASK_END, calendar.getTimeInMillis());

                TextView description = (TextView) parentView.findViewById(R.id.description);
                newTask.put(TaskDatabaseHelper.TASK_DESCRIPTION, description.getText().toString());
                newTask.put(TaskDatabaseHelper.TASK_TYPE, type.name());

                SQLiteDatabase taskDb =  TaskDatabaseHelper.getInstance(getActivity().getBaseContext()).getWritableDatabase();
                long row = taskDb.insert(TaskDatabaseHelper.DICTIONARY_TABLE_NAME, null, newTask);

                getActivity().finish();
            }
        });
        return view;
    }
}
