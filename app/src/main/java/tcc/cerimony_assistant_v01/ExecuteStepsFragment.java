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
import android.widget.LinearLayout;
import android.widget.TextView;

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
            final String cerimonyNewPath = intent.getStringExtra("NEW_PATH");
            final String cerimonyLoadPath = intent.getStringExtra("LOAD_PATH");
            final String cerimonyName = intent.getStringExtra("NAME");

            //getting timestamp to start_ceremony_date and hour
            long msTime = System.currentTimeMillis();
            Date curDateTime = new Date(msTime);

            //make a copy of the selected ceremony file xml in assets/new to sdcard


            final Cerimony selectedCerimony = CCerimonies.getInstance().getCerimonies().get(cerimonyName);

            //set date to start_ceremony_date and hour
            Calendar cal = Calendar.getInstance();
            cal.setTime(curDateTime);
            String curTime = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
            String curDate = cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.YEAR);

            selectedCerimony.setInitialDate(curDate);
            selectedCerimony.setInitialTime(curTime);

            //get steps and curStep = steps(0)
            final List<Step> steps = selectedCerimony.getSteps();
            curStep = steps.get(stepNumber);

            //dinamically modify GUI to step(0)
            getActivity().setTitle(selectedCerimony.getShortName() + " @ "+ "Passo "+stepNumber);
            final TextView mTextView = ((TextView) rootView.findViewById(R.id.description_text));
            mTextView.setText(curStep.getDescription());

            List<Input> inputs = curStep.getInputs();

            final Button nextBtn = (Button) rootView.findViewById(R.id.next_step_button);
            Log.v("stepsize", ""+steps.size());
            nextBtn.setOnClickListener(new View.OnClickListener() {
                //            @Override
                public void onClick(View v) {
                    //logic to save in the xml
                    long msTime = System.currentTimeMillis();
                    Date curDateTime = new Date(msTime);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(curDateTime);
                    String curTime = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
                    curStep.setTime(curTime);

                    //dinamically modify GUI to step(1) at step(size-1)
                    stepNumber++;
                    if(stepNumber < steps.size()-1){
                        curStep = steps.get(stepNumber);

                        getActivity().setTitle(selectedCerimony.getShortName() + " @ "+ "Passo "+stepNumber);
                        LinearLayout li = ((LinearLayout) rootView.findViewById(R.id.linear_entradas));
                        //deleting previous step view elements
                        if(li.getChildCount() > 0)
                            li.removeAllViews();

                        LinearLayout lo = ((LinearLayout) rootView.findViewById(R.id.linear_saidas));
                        //deleting previous step view elements
                        if(lo.getChildCount() > 0)
                            lo.removeAllViews();

                        TextView mTextView = ((TextView) rootView.findViewById(R.id.description_text));
                        mTextView.setText(curStep.getDescription());

                        List<Input> inputs = curStep.getInputs();
                        Log.v("inputs", ""+inputs.size());

                        if(inputs.size() > 0){
                            for (int i=0; i < inputs.size(); i++) {
                                if("text".equalsIgnoreCase(inputs.get(i).getType())){
                                    TextView tv = new TextView(getActivity());
                                    tv.setId(i);
                                    tv.setText("\u2022 "+inputs.get(i).getText());
                                    li.addView(tv);
                                }
                            }
                        }
                        List<String> outputs = curStep.getOutput();
                        if(outputs.size() > 0){
                            for (int i=0; i < outputs.size(); i++) {
                                TextView tv = new TextView(getActivity());
                                tv.setId(inputs.size()+i);
                                tv.setText("\u2022 "+outputs.get(i));
                                lo.addView(tv);
                            }
                        }
                    } else if(stepNumber == steps.size()-1){
                        nextBtn.setBackgroundColor(Color.GREEN);
                        nextBtn.setText("Finalizar");
                    } else {
                        //end of ceremony, save, feedbackmessage in the initial activity?
                        File file;

                        cal = Calendar.getInstance();
                        cal.setTime(curDateTime);
                        curTime = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
                        String curDate = cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.YEAR);

                        selectedCerimony.setFinalDate(curDate);
                        selectedCerimony.setFinalTime(curTime);

                        if(isExternalStorageWritable()) {
                            String root_sd = Environment.getExternalStorageDirectory().toString();
                            File dir = new File(root_sd + "/ceremony-assistant/load/");
                            dir.mkdirs();
                            try {
                                file = new File(dir, selectedCerimony.getShortName().replaceAll("\\s","") + "-" + selectedCerimony.getFinalDate().replaceAll("/", "-") + ".xml");
                                file.createNewFile();
                                FileOutputStream f = new FileOutputStream(file);
                                String xmlData = selectedCerimony.toXML();
                                f.write(xmlData.getBytes());
                                f.close();
                                //send the information about the new file and folders to scanner
                                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                intent.setData(Uri.fromFile(file));
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
