package tcc.cerimony_assistant_v01;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ParticipantsFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "ParticipantsFragment";

    public ParticipantsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_participants, container, false);

        return view;
    }

}
