package tcc.cerimony_assistant_v01;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


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
        final View view = inflater.inflate(R.layout.fragment_participants, container, false);

        //convert participants.json in a string
        File file = new File(Environment.getExternalStorageDirectory(), "ceremony-assistant/participants.json");
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String jString = "";
        try {
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = null;
            try {
                bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            } catch (IOException e) {
                e.printStackTrace();
            }
            /* Instead of using default, pass in a decoder. */
            jString = Charset.defaultCharset().decode(bb).toString();
        }
        finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //parse participants.json string to participants object
        final ArrayList<Participant> participants = ParticipantsJSONParser.getParticipantsFromJSON(jString);

        //display dinamycally the participants in a tablelayout
        TableLayout table = (TableLayout)view.findViewById(R.id.tableLayout);
        CheckBox cb;
        Spinner spinner;
        final ArrayList<CheckBox> cbData = new ArrayList<CheckBox>();
        final ArrayList<Spinner> spinnerData = new ArrayList<Spinner>();
        for(int i = 0; i < participants.size(); i++) {
            TableRow row = (TableRow)LayoutInflater.from(getActivity()).inflate(R.layout.attrib_row_participants, null);
            ((TextView)row.findViewById(R.id.text_participant)).setText(participants.get(i).getPName());
            cb = (CheckBox)row.findViewById(R.id.check_box_participant);
            spinner = (Spinner)row.findViewById(R.id.spinner_participant);
            spinnerData.add(spinner);
            cbData.add(cb);
            table.addView(row);
        }
        table.requestLayout();

        Button btn = (Button) view.findViewById(R.id.confirm_participants_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Participant participant;
            List<Participant> cer_participants = new ArrayList<Participant>();

            for(int i = 0; i < cbData.size(); i++) {
                if (cbData.get(i).isChecked()) {
                    participant = participants.get(i);
                    String pRole = spinnerData.get(i).getSelectedItem().toString();
                    participant.setCargo(pRole);
                    cer_participants.add(participant);
                }
            }
            if(cer_participants.size() == 0) {
                //display feedback message
                Context context = getContext();
                CharSequence text = "Nenhum participante foi confirmado";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            } else {
                CCerimonies.getInstance().getSelectedCerimony().setParticipants(cer_participants);
            }
            }
        });
        return view;
    }

}
