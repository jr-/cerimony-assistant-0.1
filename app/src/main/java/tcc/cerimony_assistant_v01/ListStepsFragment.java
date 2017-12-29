package tcc.cerimony_assistant_v01;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListStepsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListStepsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListStepsFragment extends Fragment {


    private ArrayAdapter<String> mListStepsAdapter;

    public ListStepsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        Cerimony cerimony = CCerimonies.getInstance().getSelectedCerimony();
        List<String> stepsList = new ArrayList<>();
//        List<Step> steps = cerimony.getSteps();
//        for ( int i=0; i < steps.size(); i++) {
//            stepsList.add(steps.get(i).getSName());
//        }
        stepsList.add("teste");

        mListStepsAdapter =
                new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.list_item_steps,
                        R.id.list_item_steps_textview,
                        stepsList
                );

        View rootView = inflater.inflate(R.layout.fragment_list_steps, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_steps);
        listView.setAdapter(mListStepsAdapter);

        return rootView;
    }
}
