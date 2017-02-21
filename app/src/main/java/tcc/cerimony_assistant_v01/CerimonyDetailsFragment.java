package tcc.cerimony_assistant_v01;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class CerimonyDetailsFragment extends Fragment {

    public CerimonyDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cerimony_details, container, false);

        // The detail Activity called via intent.  Inspect the intent for forecast data.
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String cerimonyFileName = intent.getStringExtra(Intent.EXTRA_TEXT);
            Cerimony selectedCerimony = CerimonyXmlPullParser.getCerimonyFromFile(getActivity(), "new/"+cerimonyFileName);
            ((TextView) rootView.findViewById(R.id.cerimonydetails_name))
            .setText(selectedCerimony.getCName());
        }

        return rootView;
    }
}
