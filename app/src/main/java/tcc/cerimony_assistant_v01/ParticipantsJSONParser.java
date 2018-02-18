package tcc.cerimony_assistant_v01;

import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by zr on 15/02/18.
 */

public class ParticipantsJSONParser {
    private static final String TAG_PARTICIPANTS = "participants";
    private static final String TAG_SNAME = "sname";
    private static final String TAG_SEMAIL = "semail";
    private static final String TAG_SORGANIZATION = "sorganization";
    public static String convertJSONFiletoString() {
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
        return jString;
    }
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

