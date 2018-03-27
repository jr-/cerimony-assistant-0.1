package tcc.cerimony_assistant_v01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


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
        Cerimony ceremony = CCerimonies.getInstance().getSelectedCerimony();

        //dinamically modify GUI
        getActivity().setTitle("Fechamento da Ata - " + ceremony.getShortName());
        TextView status_tv = (TextView) rootView.findViewById(R.id.status);

        if(ceremony.isAborted()) {
            AbortedCeremony ac = ceremony.getAbortedCeremony();
            String reason = ac.getReason();
            int step_number = ac.getStep_number();
            status_tv.setText("A cerimônia foi abortada pelo motivo: \"" + reason +"\" no passo " + step_number);
        } else {
            status_tv.setText("A Cerimônia terminou com sucesso!");
        }


        Button btn = (Button) rootView.findViewById(R.id.return_main);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListNewCerimonies.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
