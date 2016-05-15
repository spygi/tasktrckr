package spy.gi.tasktrckr;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.text.SimpleDateFormat;
import java.util.AbstractMap;

public class MainActivityFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    // This is the Adapter being used to display the list's data
    SimpleCursorAdapter mAdapter;

    public MainActivityFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // For the cursor adapter, specify which columns go into which views
        String[] fromColumns = {TaskDatabaseHelper.TASK_TYPE};
        int[] toViews = {android.R.id.text1}; // The TextView in simple_list_item_1

        // Create an empty adapter we will use to display the loaded data.
        // We pass null for the cursor, then update it in onLoadFinished()
        mAdapter = new SimpleCursorAdapter(this.getActivity().getBaseContext(),
                android.R.layout.simple_list_item_1, null,
                fromColumns, toViews, 0);
        setListAdapter(mAdapter);

        return inflater.inflate(R.layout.task_list, container, false);
    }

    // Called when a new Loader needs to be created
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(getActivity().getBaseContext(), null, null, null, null, null) {
            @Override
            public Cursor loadInBackground() {
                return loadInBg();
            }
        };
    }

    private Cursor loadInBg() {
        SQLiteDatabase taskDb = new TaskDatabaseHelper(getActivity().getBaseContext()).getReadableDatabase();
        String[] fromColumns = {BaseColumns._ID, "rowid", TaskDatabaseHelper.TASK_TYPE, TaskDatabaseHelper.TASK_START, TaskDatabaseHelper.TASK_END, TaskDatabaseHelper.TASK_DESCRIPTION}; // _id is required for the SimpleCursorAdapter to work
        // You can use any query that returns a cursor.
        try {
            Cursor cursor = taskDb.query("tasks", fromColumns, null, null, null, null, null, null); // TODO: sort by date
            return cursor;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Called when a previously created loader has finished loading
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Find ListView to populate
        ListView tasks = (ListView) this.getActivity().findViewById(android.R.id.list);
        // Setup cursor adapter using cursor from last step
        TaskAdapter taskAdapter = new TaskAdapter(this.getActivity(), data, 0);
        // Attach cursor adapter to the ListView
        tasks.setAdapter(taskAdapter);

        mAdapter.swapCursor(null);

    }

    // Called when a previously created loader is reset, making the data unavailable
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mAdapter.swapCursor(null);
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
//        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
//    }
}
