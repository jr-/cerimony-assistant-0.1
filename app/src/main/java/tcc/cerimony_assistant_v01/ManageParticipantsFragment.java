package tcc.cerimony_assistant_v01;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ManageParticipantsFragment extends Fragment {


    public ManageParticipantsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_manage_participants, container, false);
        String jString = ParticipantsJSONParser.convertJSONFiletoString();
        if ("".equals(jString)){
            //cria a o jsonArray participants, faz com que getParticipants nunca seja null
            jString = ParticipantsJSONParser.instantiateJson();
        }
        final List<Participant> participants = ParticipantsJSONParser.getParticipantsFromJSON(jString);

        TableLayout table = (TableLayout)view.findViewById(R.id.tableLayout);

        for(int i = 0; i < participants.size(); i++) {
            TableRow row = (TableRow)LayoutInflater.from(getActivity()).inflate(R.layout.attrib_row_manage_participants, null);
            ((TextView)row.findViewById(R.id.text_name)).setText(participants.get(i).getPName());
            ((TextView)row.findViewById(R.id.text_email)).setText(participants.get(i).getEmail());
            ((TextView)row.findViewById(R.id.text_unit)).setText(participants.get(i).getUnidade());

            table.addView(row, i+1);
        }
        table.requestLayout();

        Button btn = (Button) view.findViewById(R.id.button_add_participant);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validate = true;
                Participant nParticipant = new Participant();
                String name = ((EditText) view.findViewById(R.id.textName)).getText().toString();
                String email = ((EditText) view.findViewById(R.id.textEmail)).getText().toString();;
                String unit = ((EditText) view.findViewById(R.id.textUnit)).getText().toString();;
                //if name || email || unit == "" não permitir adicionar, mostrar toast com o campo que não possou da validação
                if ("".equals(name) || "".equals(email) || "".equals(unit)) {
                    validate = false;
                }

                if (validate) {
                    //add participant on json
                    nParticipant.setUnidade(unit);
                    nParticipant.setPName(name);
                    nParticipant.setEmail(email);
                    String json = ParticipantsJSONParser.addParticipantToJson(nParticipant);
                    ParticipantsJSONParser.writeJson(json);

                    //clean the edit texts
                    ((EditText) view.findViewById(R.id.textName)).setText("");
                    ((EditText) view.findViewById(R.id.textEmail)).setText("");
                    ((EditText) view.findViewById(R.id.textUnit)).setText("");

                    TableLayout table = (TableLayout)view.findViewById(R.id.tableLayout);

                    TableRow row = (TableRow)LayoutInflater.from(getActivity()).inflate(R.layout.attrib_row_manage_participants, null);
                    ((TextView)row.findViewById(R.id.text_name)).setText(nParticipant.getPName());
                    ((TextView)row.findViewById(R.id.text_email)).setText(nParticipant.getEmail());
                    ((TextView)row.findViewById(R.id.text_unit)).setText(nParticipant.getUnidade());

                    table.addView(row, table.getChildCount()-1);
                    table.requestLayout();
                } else {
                    Context context = getContext();
                    int duration = Toast.LENGTH_SHORT;
                    CharSequence text = "Nenhum campo pode estar vazio";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });
        return view;
    }
}
