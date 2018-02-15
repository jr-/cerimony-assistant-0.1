package tcc.cerimony_assistant_v01;


import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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
        ArrayList<Participant> participants = ParticipantsJSONParser.getParticipantsFromJSON(jString);

        //display dinamycally the participants in a tablelayout
        TableLayout table = (TableLayout)view.findViewById(R.id.tableLayout);
        for(int i = 0; i < participants.size(); i++) {
            TableRow row = (TableRow)LayoutInflater.from(getActivity()).inflate(R.layout.attrib_row_participants, null);
            ((TextView)row.findViewById(R.id.text_participant)).setText(participants.get(i).getPName());
            table.addView(row);
        }
        table.requestLayout();
        return view;
    }

}
