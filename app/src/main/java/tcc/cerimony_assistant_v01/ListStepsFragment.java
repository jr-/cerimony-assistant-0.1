package tcc.cerimony_assistant_v01;

import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListStepsFragment extends Fragment {

    private ArrayAdapter<String> mListStepsAdapter;

    public ListStepsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        View rootView = null;
        if (intent != null && intent.hasExtra("LOAD_PATH")) {
            final String cerimonyName = intent.getStringExtra("NAME");
            final Cerimony selectedCerimony = CCerimonies.getInstance().getCerimonies().get(cerimonyName);

            List<Step> steps = selectedCerimony.getSteps();
            List<String> stepsNameList = new ArrayList<>();
            for (Step step : steps) {
                stepsNameList.add(step.getSName());
            }
            mListStepsAdapter =
                    new ArrayAdapter<String>(
                            getActivity(), // The current context (this activity)
                            R.layout.list_item_steps, // The name of the layout ID.
                            R.id.list_item_steps_textview, // The ID of the textview to populate.
                            stepsNameList);

            rootView = inflater.inflate(R.layout.fragment_list_steps, container, false);


//            ListView listView = (ListView) rootView.findViewById(R.id.listview_steps);
//            listView.setAdapter(mListStepsAdapter);
//
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                    String cerimonyFileName = mListStepsAdapter.getItem(position);
//
//                    Intent intent = new Intent(getActivity(), CerimonyDetails.class)
//                            .putExtra(Intent.EXTRA_TEXT, cerimonyFileName);
//                    startActivity(intent);
//                }
//            });
        }

        return rootView;
    }
}
