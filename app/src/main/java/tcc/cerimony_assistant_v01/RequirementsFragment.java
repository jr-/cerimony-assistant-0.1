package tcc.cerimony_assistant_v01;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequirementsFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "RequirementsFragment";

    public RequirementsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_requirements, container, false);

        //display dinamycally the requirements in a tablelayout
        Cerimony ceremony = CCerimonies.getInstance().getSelectedCerimony();
        List<String> requirements = ceremony.getRequirements();
        TableLayout table = (TableLayout)view.findViewById(R.id.tableLayout);
        CheckBox cb;

        final ArrayList<CheckBox> cbData = new ArrayList<CheckBox>();
        for(int i = 0; i < requirements.size(); i++) {
            TableRow row = (TableRow)LayoutInflater.from(getActivity()).inflate(R.layout.attrib_row_requirements, null);
            ((TextView)row.findViewById(R.id.text_requirement)).setText(requirements.get(i));
            cb = (CheckBox)row.findViewById(R.id.check_box_requirement);
            cbData.add(cb);
            table.addView(row);
        }
        table.requestLayout();

        Button btn = (Button) view.findViewById(R.id.confirm_requirements_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;
                for(int i = 0; i < cbData.size(); i++) {
                    if (cbData.get(i).isChecked()) {
                        count++;
                    }
                }
                if(cbData.size() == count) {
                    CCerimonies.getInstance().getSelectedCerimony().setConfirmRequirements(true);
                    //display feedback message
                    Context context = getContext();
                    CharSequence text = "Requisitos confirmados com sucesso!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.vp);
                    viewPager.setCurrentItem(0);
                } else {
                    CCerimonies.getInstance().getSelectedCerimony().setConfirmRequirements(false);
                    Context context = getContext();
                    CharSequence text = "Falta confirmar alguns requisitos!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });
        return view;
    }

}
