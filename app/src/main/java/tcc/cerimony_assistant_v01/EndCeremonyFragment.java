package tcc.cerimony_assistant_v01;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A placeholder fragment containing a simple view.
 */
public class EndCeremonyFragment extends Fragment {
    private static final String TAG = "EndCeremonyFragment";

    public EndCeremonyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_end_ceremony, container, false);

        return rootView;
    }
}