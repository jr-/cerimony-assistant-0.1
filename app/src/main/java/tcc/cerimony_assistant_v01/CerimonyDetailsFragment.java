package tcc.cerimony_assistant_v01;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        String cerimonyPath = "";
        String cerimonyFileName = "";
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            cerimonyFileName = intent.getStringExtra(Intent.EXTRA_TEXT);
            cerimonyPath = "new/"+cerimonyFileName;
            Cerimony cerimony = CCerimonies.getInstance().getCerimonies().get(cerimonyFileName);
            cerimony = CerimonyXmlPullParser.getCerimonyFromFile(getActivity(), "new/"+cerimonyFileName, cerimony);
            ((TextView) rootView.findViewById(R.id.cerimonydetails_name))
            .setText(cerimony.getCName());
        }

        Button btn = (Button) rootView.findViewById(R.id.start_cerimony_button);
        final String finalCerimonyPath = cerimonyPath;
        final String finalCerimonyFileName = cerimonyFileName;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExecuteSteps.class);
                intent.putExtra("PATH", finalCerimonyPath);
                intent.putExtra("STEPNUMBER", 0);
                intent.putExtra("NAME", finalCerimonyFileName);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
