package tcc.cerimony_assistant_v01;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ExecuteStepsFragment extends Fragment {

    public ExecuteStepsFragment() {
    }
    int stepNumber = 0;
    Step curStep;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_execute_steps, container, false);
//
//        // The detail Activity called via intent.  Inspect the intent for forecast data.
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("LOAD_PATH")) {
            final String cerimonyName = intent.getStringExtra("NAME");

            //getting timestamp to start_ceremony_date and hour
            long msTime = System.currentTimeMillis();
            Date curDateTime = new Date(msTime);


            final Cerimony selectedCerimony = CCerimonies.getInstance().getCerimonies().get(cerimonyName);

            //set date to start_ceremony_date and hour
            Calendar cal = Calendar.getInstance();
            cal.setTime(curDateTime);
            String curTime = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
            String curDate = cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
            String rgDate = cal.get(Calendar.YEAR) + "." +  (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.DAY_OF_MONTH);

            selectedCerimony.setInitialDate(curDate);
            selectedCerimony.setInitialTime(curTime);

            //create folder for started ceremony
            final String folderName = rgDate + " - " + curTime + " - " + selectedCerimony.getShortName();
            selectedCerimony.setFolderName(folderName);
            File folder = new File(Environment.getExternalStorageDirectory(), "ceremony-assistant/final/" + folderName);
            folder.mkdirs();

            //get steps and curStep = steps(0)
            final List<Step> steps = selectedCerimony.getSteps();
            curStep = steps.get(stepNumber);

            //dinamically modify GUI to step(0)
            String step_name = curStep.getSName();
            getActivity().setTitle(selectedCerimony.getShortName() + " - " + step_name);
            //getActivity().setTitle(selectedCerimony.getShortName() + " - " + "Passo " + stepNumber);
            final TextView description_tv = ((TextView) rootView.findViewById(R.id.description_text));
            description_tv.setText("\u2022 " + curStep.getDescription());

            final TextView input_tv = ((TextView) rootView.findViewById(R.id.text_input));
            input_tv.setText("\u2022 " + curStep.getInput());

            final TextView output_tv = ((TextView) rootView.findViewById(R.id.text_output));
            output_tv.setText("\u2022 " + curStep.getOutput());

            final TextView observation_tv = ((TextView) rootView.findViewById(R.id.text_observation));
            String observation_text = curStep.getObservation();
           // if(!"".equals(observation_text)) {
            observation_tv.setText("\u2022 " + observation_text);
           // }

            final Button nextBtn = (Button) rootView.findViewById(R.id.next_step_button);
            Log.v("stepsize", "" + steps.size());
            nextBtn.setOnClickListener(new View.OnClickListener() {
                //            @Override
                public void onClick(View v) {
                    //logic to save in the xml
                    long msTime = System.currentTimeMillis();
                    Date curDateTime = new Date(msTime);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(curDateTime);
                    String curTime = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
                    String observation = observation_tv.getText().toString();
                    curStep.setTime(curTime);
                    curStep.setObservation(observation);

                    //dinamically modify GUI to step(1) at step(size-1)
                    stepNumber++;
                    if (stepNumber < steps.size()) {
                        curStep = steps.get(stepNumber);

                        String step_name = curStep.getSName();
                        getActivity().setTitle(selectedCerimony.getShortName() + " - " + step_name);

                        description_tv.setText("\u2022 " + curStep.getDescription());
                        input_tv.setText("\u2022 " + curStep.getInput());
                        output_tv.setText("\u2022 " + curStep.getOutput());
                        String observation_text = curStep.getObservation();
                       // if(!"".equals(observation_text)) {
                        observation_tv.setText("\u2022 " + observation_text);
                        //}

                        if (stepNumber == steps.size() - 1) {
                            nextBtn.setBackgroundColor(Color.GREEN);
                            nextBtn.setText("Finalizar");
                        }
                    }

                    if (stepNumber == steps.size()) {
                        //end of ceremony, save, feedbackmessage in the initial activity?
                        File file;
                        File file2;

                        cal = Calendar.getInstance();
                        cal.setTime(curDateTime);
                        curTime = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
                        String curDate = cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);

                        selectedCerimony.setFinalDate(curDate);
                        selectedCerimony.setFinalTime(curTime);

                        if (isExternalStorageWritable()) {
                            String root_sd = Environment.getExternalStorageDirectory().toString();
                            File dir = new File(root_sd + "/ceremony-assistant/final/" + folderName);
                            dir.mkdirs();
                            try {
//                                    file = new File(dir, selectedCerimony.getShortName().replaceAll("\\s", "") + "-" + selectedCerimony.getFinalDate().replaceAll("/", "-") + ".xml");
//                                    file.createNewFile();
//                                    FileOutputStream f = new FileOutputStream(file);
//                                    String xmlData = selectedCerimony.toXML();
//                                    f.write(xmlData.getBytes());
//                                    f.close();

                                file2 = new File(dir, selectedCerimony.getShortName().replaceAll("\\s", "") + "-" + selectedCerimony.getFinalDate().replaceAll("/", "-") + ".txt");
                                file2.createNewFile();
                                FileOutputStream f2 = new FileOutputStream(file2);
                                String txtData = selectedCerimony.toTXT();
                                f2.write(txtData.getBytes());
                                f2.close();

                                //send the information about the new file and folders to scanner
                                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                intent.setData(Uri.fromFile(file2));
                                getActivity().sendBroadcast(intent);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            });
        }
        return rootView;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
