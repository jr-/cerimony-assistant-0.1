package tcc.cerimony_assistant_v01;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private boolean first_time = true;
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
            String rgDate = cal.get(Calendar.YEAR) + "." + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.DAY_OF_MONTH);

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
            //getActivity().setTitle(selectedCerimony.getShortName() + " - " + step_name);

            final TextView description_tv = ((TextView) rootView.findViewById(R.id.description_text));
            description_tv.setText("\u2022 " + curStep.getDescription());

            final TextView input_tv = ((TextView) rootView.findViewById(R.id.text_input));
            input_tv.setText("\u2022 " + curStep.getInput());

            final TextView output_tv = ((TextView) rootView.findViewById(R.id.text_output));
            output_tv.setText("\u2022 " + curStep.getOutput());

            final TextView observation_tv = ((TextView) rootView.findViewById(R.id.text_observation));
            final TextView obs_title_tv = ((TextView) rootView.findViewById(R.id.textView4));
            String observation_text = curStep.getObservation();
            if (!"".equals(observation_text)) {
                obs_title_tv.setText("Observações");
                observation_tv.setText("\u2022 " + observation_text);
            } else {
                obs_title_tv.setText("");
                observation_tv.setText("");
            }


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
                    selectedCerimony.setCurrentStepNumber(stepNumber);
                    if (stepNumber < steps.size()) {
                        curStep = steps.get(stepNumber);

                        String step_name = curStep.getSName();
                        //getActivity().setTitle(selectedCerimony.getShortName() + " - " + step_name);
                        ((ExecuteSteps) getActivity()).title_toolbar.setText(selectedCerimony.getShortName() + " - " + step_name);
                        int steps_size = steps.size();
                        int display_step_number = stepNumber + 1;
                        ((ExecuteSteps) getActivity()).status_toolbar.setText("Passo " + display_step_number + " de " + steps_size);

                        description_tv.setText("\u2022 " + curStep.getDescription());
                        input_tv.setText("\u2022 " + curStep.getInput());
                        output_tv.setText("\u2022 " + curStep.getOutput());
                        String observation_text = curStep.getObservation();
                        if (!"".equals(observation_text)) {
                            obs_title_tv.setText("Observações");
                            observation_tv.setText("\u2022 " + observation_text);
                        } else {
                            obs_title_tv.setText("");
                            observation_tv.setText("");
                        }

                        if (stepNumber == steps.size() - 1) {
                            nextBtn.setBackgroundColor(Color.GREEN);
                            nextBtn.setText("Finalizar");
                        }
                    }

                    if (stepNumber == steps.size()) {
                        //end of ceremony, save, feedbackmessage in the initial activity?
//                        File file;
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

                                Intent intent2 = new Intent(getActivity(), EndCeremonyActivity.class);
                                startActivity(intent2);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            });

            final Button abort_btn = (Button) rootView.findViewById(R.id.abort_button);
            abort_btn.setOnClickListener(new View.OnClickListener() {
                //            @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Abortar Cerimônia");
                    builder.setMessage("Deseja abortar a cerimônia por qual motivo?");

                    View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.abort_dialog, (ViewGroup) getView(), false);
                    final EditText input = (EditText) viewInflated.findViewById(R.id.input);
                    builder.setView(viewInflated);

                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Cerimony ceremony = CCerimonies.getInstance().getSelectedCerimony();

                            AbortedCeremony ac = new AbortedCeremony();
                            ac.setReason(input.getText().toString());
                            ac.setStep_number(stepNumber);

                            ceremony.setAborted(true);
                            ceremony.setAbortedCeremony(ac);

                            File file2;

                            long msTime2 = System.currentTimeMillis();
                            Date curDateTime2 = new Date(msTime2);
                            Calendar cal2 = Calendar.getInstance();
                            cal2.setTime(curDateTime2);

                            String curTime2 = cal2.get(Calendar.HOUR_OF_DAY) + ":" + cal2.get(Calendar.MINUTE);
                            String curDate2 = cal2.get(Calendar.DAY_OF_MONTH) + "/" + (cal2.get(Calendar.MONTH) + 1) + "/" + cal2.get(Calendar.YEAR);

                            selectedCerimony.setFinalDate(curDate2);
                            selectedCerimony.setFinalTime(curTime2);

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

                                    Intent intent2 = new Intent(getActivity(), EndCeremonyActivity.class);
                                    startActivity(intent2);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            Intent intent = new Intent(getActivity(), EndCeremonyActivity.class);
                            startActivity(intent);

                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            });
        }
        return rootView;
    }

    public void onStart() {
        super.onStart();
        if(first_time) {
            Cerimony ceremony = CCerimonies.getInstance().getSelectedCerimony();
            List<Step> steps = ceremony.getSteps();
            String step_name = steps.get(0).getSName();
            int steps_size = steps.size();
            ((ExecuteSteps) getActivity()).title_toolbar.setText(ceremony.getShortName() + " - " + step_name);

            ((ExecuteSteps) getActivity()).status_toolbar.setText("Passo " + 1 + " de " + steps_size);

            first_time = false;
        }
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
