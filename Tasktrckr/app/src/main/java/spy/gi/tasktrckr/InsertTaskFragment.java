package spy.gi.tasktrckr;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by giannaks on 01/05/16.
 */
public class InsertTaskFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static InsertTaskFragment newInstance(int page, String title) {
        InsertTaskFragment fragmentFirst = new InsertTaskFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insert_task_fragment, container, false);
        switch (page) {
            case 0:
                view.findViewById(R.id.button).setBackgroundColor(getResources().getColor(R.color.engTask));
                break;
            case 1:
                view.findViewById(R.id.button).setBackgroundColor(getResources().getColor(R.color.teamTask));
                break;
            case 2:
                view.findViewById(R.id.button).setBackgroundColor(getResources().getColor(R.color.mgmtTask));
                break;
            default:
                view.findViewById(R.id.button).setBackgroundColor(getResources().getColor(R.color.miscTask));
                break;
        }

        return view;
    }
}
