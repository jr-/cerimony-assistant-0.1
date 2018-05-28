package tcc.cerimony_assistant_v01;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zr on 14/05/18.
 */

public class StepsListAdapter extends BaseAdapter {
    private final List<StepVisual> steps;
    private final Activity activity;

    public StepsListAdapter(List<StepVisual> steps, Activity activity) {
        this.steps = steps;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return steps.size();
    }

    @Override
    public Object getItem(int position) {
        return steps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StepVisual cur_step_visual = steps.get(position);
        String stepName = cur_step_visual.getName();
        View view = activity.getLayoutInflater()
                .inflate(R.layout.list_item_steps, parent, false);

        TextView stepName_tv = (TextView) view.findViewById(R.id.list_item_steps_textview);
        stepName_tv.setText(stepName);
        if(cur_step_visual.isExecuted()){
            stepName_tv.setBackgroundColor(Color.LTGRAY);
        } else {
            stepName_tv.setBackgroundColor(android.R.drawable.btn_default);
        }

        return view;
    }
}
