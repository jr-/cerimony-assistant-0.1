package tcc.cerimony_assistant_v01;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zr on 15/02/18.
 */

public class ParticipantsJSONParser {
    private static final String TAG_PARTICIPANTS = "participants";
    private static final String TAG_SNAME = "sname";
    private static final String TAG_SEMAIL = "semail";
    private static final String TAG_SORGANIZATION = "sorganization";
    public static ArrayList<Participant> getParticipantsFromJSON(String json) {
        if (json != null) {
            try {

                ArrayList<Participant> participantList = new ArrayList<Participant>();
                JSONObject jsonObj = new JSONObject(json);

// Getting JSON Array node
                JSONArray participants = jsonObj.getJSONArray(TAG_PARTICIPANTS);

// looping through All Participants
                for (int i = 0; i < participants.length(); i++) {
                    JSONObject c = participants.getJSONObject(i);

                    String sname = c.getString(TAG_SNAME);
                    String email = c.getString(TAG_SEMAIL);
                    String sorganization = c.getString(TAG_SORGANIZATION);


// tmp hashmap for single student
                    Participant participant = new Participant();
                    participant.setUnidade(sorganization);
                    participant.setPName(sname);
                    participant.setEmail(email);


// adding student to students list
                    participantList.add(participant);
                }
                return participantList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("JSONNULL", "No participants added in participants.JSON");
            return null;
        }
    }
}

