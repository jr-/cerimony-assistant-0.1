package tcc.cerimony_assistant_v01;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListNewCerimoniesFragment extends Fragment {

    private ArrayAdapter<String> mNewCerimoniesAdapter;

    public ListNewCerimoniesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String[] fileNames = new String[0];
        try {
            fileNames = getActivity().getAssets().list("new");
        } catch (IOException e) {
            e.printStackTrace();
        }


        List<String> newCerimoniesList = new ArrayList<String>(Arrays.asList(fileNames));
        //instancia o singleton
        CCerimonies.getInstance().setCerimonies(newCerimoniesList);

        // Now that we have some dummy forecast data, create an ArrayAdapter.
        // The ArrayAdapter will take data from a source (like our dummy forecast) and
        // use it to populate the ListView it's attached to.
        mNewCerimoniesAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_newcerimonies, // The name of the layout ID.
                        R.id.list_item_newcerimonies_textview, // The ID of the textview to populate.
                        newCerimoniesList);

        View rootView = inflater.inflate(R.layout.fragment_list_new_cerimonies, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_newcerimonies);
        listView.setAdapter(mNewCerimoniesAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String cerimonyFileName = mNewCerimoniesAdapter.getItem(position);

                Intent intent = new Intent(getActivity(), CerimonyDetails.class)
                        .putExtra(Intent.EXTRA_TEXT, cerimonyFileName);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
