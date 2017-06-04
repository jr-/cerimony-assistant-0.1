package tcc.cerimony_assistant_v01;

import android.content.Intent;
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

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ExecuteStepsFragment extends Fragment {

    public ExecuteStepsFragment() {
    }
    int fstepNumber = 0;
    Step curStep;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_execute_steps, container, false);
//
//        // The detail Activity called via intent.  Inspect the intent for forecast data.
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("PATH")) {
            final String cerimonyPath = intent.getStringExtra("PATH");
            final int stepNumber = intent.getIntExtra("STEPNUMBER", 0);
            final String cerimonyName = intent.getStringExtra("NAME");
            fstepNumber = stepNumber;
            Cerimony selectedCerimony = CCerimonies.getInstance().getCerimonies().get(cerimonyName);
            //Cerimony selectedCerimony = CerimonyXmlPullParser.getCerimonyFromFile(getActivity(), cerimonyPath);
            final List<Step> steps = selectedCerimony.getSteps();
            curStep = steps.get(stepNumber);

            final TextView mTextView = ((TextView) rootView.findViewById(R.id.description_text));
            mTextView.setText(curStep.getDescription());
            final LinearLayout lm = ((LinearLayout) rootView.findViewById(R.id.linear1));

            List<String> inputs = curStep.getInput();
            Log.v("inputs", ""+inputs.size());
            if(inputs.size() > 0){
                for (int i=0; i < inputs.size(); i++) {
                    CheckBox cb = new CheckBox(getActivity());
                    cb.setId(i+1);
                    cb.setText(inputs.get(i));
                    lm.addView(cb);
                }
            }

            Button nextBtn = (Button) rootView.findViewById(R.id.next_step_button);
            Log.v("stepsize", ""+steps.size());
            nextBtn.setOnClickListener(new View.OnClickListener() {
                //            @Override
                public void onClick(View v) {
                    fstepNumber++;

                    Intent intent = new Intent(getActivity(), ExecuteSteps.class);
                    intent.putExtra("PATH", cerimonyPath);
                    intent.putExtra("STEPNUMBER", fstepNumber);
                    intent.putExtra("NAME", cerimonyName);
                    startActivity(intent);
                }
            });
        }

        return rootView;
    }
}
