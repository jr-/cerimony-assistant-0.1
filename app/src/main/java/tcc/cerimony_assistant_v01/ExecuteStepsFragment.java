package tcc.cerimony_assistant_v01;

import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ExecuteStepsFragment extends Fragment {


//    private ArrayAdapter<String> mListStepsAdapter;
    private StepsListAdapter mStepsListAdapter;
    private ListView mListStepsView;

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
            try {
                this.copy(new File(Environment.getExternalStorageDirectory(),"ceremony-assistant/new/" + cerimonyName), new File(Environment.getExternalStorageDirectory(), "ceremony-assistant/final/" + folderName + "/original-" + cerimonyName));
            } catch (IOException e) {
                e.printStackTrace();
            }

            //get steps and curStep = steps(0)
            final List<Step> steps = selectedCerimony.getSteps();
            curStep = steps.get(stepNumber);

            //dinamically modify GUI to step(0)

            final EditText description_et = ((EditText) rootView.findViewById(R.id.description_text));
            description_et.setText(curStep.getDescription());

            final EditText input_et = ((EditText) rootView.findViewById(R.id.text_input));
            input_et.setText(curStep.getInput());

            final EditText output_et = ((EditText) rootView.findViewById(R.id.text_output));
            output_et.setText(curStep.getOutput());


            final EditText observation_et = ((EditText) rootView.findViewById(R.id.text_observation));
            String observation_text = curStep.getObservation();

            //TODO para todos tratar observation_text colocar bullet depois de cada nova linha e no começo do arquivo
            observation_et.setText(observation_text);


            final List<StepVisual> stepsVisualList = new ArrayList<>();
            for ( int i=0; i < steps.size(); i++) {
                String stepName = steps.get(i).getSName();
                StepVisual cur_step_visual = new StepVisual(stepName, false);
                stepsVisualList.add(cur_step_visual);
            }

//            mListStepsAdapter =
//                    new ArrayAdapter<String>(
//                            getActivity(),
//                            R.layout.list_item_steps,
//                            R.id.list_item_steps_textview,
//                            stepsList
//                    );
            mStepsListAdapter = new StepsListAdapter(stepsVisualList, getActivity());

            mListStepsView = (ListView) rootView.findViewById(R.id.listview_steps);
            mListStepsView.setAdapter(mStepsListAdapter);


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
                    String description = description_et.getText().toString();
                    String input = input_et.getText().toString();
                    String output = output_et.getText().toString();
                    String observation = observation_et.getText().toString();

                    //modify the step content
                    curStep.setTime(curTime);
                    curStep.setDescription(description);
                    curStep.setInput(input);
                    curStep.setOutput(output);
                    curStep.setObservation(observation);

                    //dinamically modify GUI to step(1) at step(size-1)
                    stepsVisualList.get(stepNumber).setExecuted(true);
                    mStepsListAdapter.notifyDataSetChanged();
                    stepNumber++;
                    selectedCerimony.setCurrentStepNumber(stepNumber);
                    if (stepNumber < steps.size()) {
                        curStep = steps.get(stepNumber);

                        String step_name = curStep.getSName();
                        ((ExecuteSteps) getActivity()).title_toolbar.setText(selectedCerimony.getShortName() + " - " + step_name);
                        int steps_size = steps.size();
                        int display_step_number = stepNumber + 1;
                        ((ExecuteSteps) getActivity()).status_toolbar.setText("Passo " + display_step_number + " de " + steps_size);

                        description_et.setText(curStep.getDescription());
                        input_et.setText(curStep.getInput());
                        output_et.setText(curStep.getOutput());
                        String observation_text = curStep.getObservation();
                        observation_et.setText(observation_text);

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
                                File file = new File(root_sd + "/ceremony-assistant/new/", selectedCerimony.getFileName());
                                file.createNewFile();
                                FileOutputStream f = new FileOutputStream(file, false);
                                String xmlData = selectedCerimony.toXML();
                                f.write(xmlData.getBytes());
                                f.close();

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
                                //Log.v("error", e.printStackTrace());
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

            final Button prev_btn = (Button) rootView.findViewById(R.id.prev_step_button);
            prev_btn.setOnClickListener(new View.OnClickListener() {
                //            @Override
                public void onClick(View v) {
                    if(stepNumber > 0) {
                        //modify the step content
                        String description = description_et.getText().toString();
                        String input = input_et.getText().toString();
                        String output = output_et.getText().toString();
                        String observation = observation_et.getText().toString();
                        curStep.setDescription(description);
                        curStep.setInput(input);
                        curStep.setOutput(output);
                        curStep.setObservation(observation);

                        //dinamically modify GUI to step(1) at step(size-1)
                        stepNumber--;
                        stepsVisualList.get(stepNumber).setExecuted(false);
                        mStepsListAdapter.notifyDataSetChanged();
                        mListStepsView.getChildAt(stepNumber).setBackgroundColor(android.R.drawable.btn_default);
                        selectedCerimony.setCurrentStepNumber(stepNumber);
                        if (stepNumber < steps.size()) {
                            curStep = steps.get(stepNumber);

                            String step_name = curStep.getSName();
                            ((ExecuteSteps) getActivity()).title_toolbar.setText(selectedCerimony.getShortName() + " - " + step_name);
                            int steps_size = steps.size();
                            int display_step_number = stepNumber + 1;
                            ((ExecuteSteps) getActivity()).status_toolbar.setText("Passo " + display_step_number + " de " + steps_size);

                            //bullet code "\u2022 "
                            description_et.setText(curStep.getDescription());
                            input_et.setText(curStep.getInput());
                            output_et.setText(curStep.getOutput());
                            String observation_text = curStep.getObservation();
                            observation_et.setText(observation_text);

                            if (stepNumber == steps.size() - 2) {
                                nextBtn.setBackgroundColor(Color.LTGRAY);
                                nextBtn.setText("Avançar");
                            }
                        }
                    }

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

    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }
}
