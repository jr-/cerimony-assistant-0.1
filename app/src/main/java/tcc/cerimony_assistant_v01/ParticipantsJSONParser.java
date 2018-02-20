package tcc.cerimony_assistant_v01;

import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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
        //se o arquivo não existe, cria o arquivo
        File file = new File(Environment.getExternalStorageDirectory(), "ceremony-assistant/participants.json");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public static String instantiateJson() {
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            jsonObj.put("participants", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jParticipants = jsonObj.toString();
        writeJson(jParticipants);

        return jParticipants;
    }

    public static boolean writeJson(String json) {
        String path = Environment.getExternalStorageDirectory() + "/ceremony-assistant/participants.json";
        try (FileWriter file = new FileWriter(path)) {
            file.write(json);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
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

    public static String ParticipantsToJson(ArrayList<Participant> pList) {
        return "";
    }

    public static String addParticipantToJson(Participant participant) {
        String json = convertJSONFiletoString();

        if (json != null) {
            try {

                ArrayList<Participant> participantList = new ArrayList<Participant>();
                JSONObject jsonObj = new JSONObject(json);

// Getting JSON Array node
                JSONArray jParticipants = jsonObj.getJSONArray(TAG_PARTICIPANTS);
                JSONObject jParticipant = new JSONObject();
                jParticipant.put("sname", participant.getPName());
                jParticipant.put("semail", participant.getEmail());
                jParticipant.put("sorganization", participant.getUnidade());
                jParticipants.put(jParticipant);

                return jsonObj.toString();
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

