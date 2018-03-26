package tcc.cerimony_assistant_v01;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class CerimonyDetailsFragment extends Fragment {
    private static final String TAG = "CerimonyDetailsFragment";
    private boolean first_time = true;
    public CerimonyDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cerimony_details, container, false);

        // The detail Activity called via intent.  Inspect the intent for forecast data.
        Intent intent = getActivity().getIntent();
        String cerimonyPath = "";
        String cerimonyFileName = "";
        Cerimony cerimony;
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            cerimonyFileName = intent.getStringExtra(Intent.EXTRA_TEXT);
            cerimonyPath = "new/"+cerimonyFileName;
            CCerimonies.getInstance().setSelectedCerimony(cerimonyFileName);
            cerimony = CCerimonies.getInstance().getCerimonies().get(cerimonyFileName);
            cerimony = CerimonyXmlPullParser.getCerimonyFromFile(getActivity(), "new/"+cerimonyFileName, cerimony);

            //dinamically modify GUI
            String format_title = "Abertura da Ata" + " - " + cerimony.getShortName();
            ((CerimonyDetails) getActivity()).mToolbar.setTitle(format_title);
            //((CerimonyDetails) getActivity()).mViewPager.setCurrentItem(2);

            TextView tv_name = ((TextView) rootView.findViewById(R.id.c_details_name));
            tv_name.setText(Html.fromHtml("<b>Nome da cerimônia:</b> " + cerimony.getCName()));

            TextView tv_participants = ((TextView) rootView.findViewById(R.id.c_details_participants));
            String participants_text;
            List<Participant> participants = cerimony.getParticipants();
            if(participants.size() == 0) {
                participants_text = "<i>Nenhum participante confirmado!</i>";
            } else {
                participants_text = "<b>Participantes confirmados:</b><br />";
                for(int i = 0; i < participants.size(); i++) {
                    participants_text += "\u2022 " + participants.get(i).getPName() + "<i> como </i>" + participants.get(i).getCargo() + "<br />";
                }
            }

            tv_participants.setText(Html.fromHtml(participants_text));

            TextView tv_requirements = ((TextView) rootView.findViewById(R.id.c_details_requirements));
            String requirements_text = "";
            boolean isConfirmRequirements = CCerimonies.getInstance().getSelectedCerimony().isConfirmRequirements();
            if(!isConfirmRequirements) {
                requirements_text = "<i>Os requisitos não foram confirmados!</i>";
            }
            tv_requirements.setText(Html.fromHtml(requirements_text));

        }



        Button btn = (Button) rootView.findViewById(R.id.start_cerimony_button);
        final String finalCerimonyNewPath = cerimonyPath;
        final String finalCerimonyLoadedPath = "load/"+cerimonyFileName;
        final String finalCerimonyFileName = cerimonyFileName;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Participant> participantList = CCerimonies.getInstance().getSelectedCerimony().getParticipants();
                boolean isConfirmRequirements = CCerimonies.getInstance().getSelectedCerimony().isConfirmRequirements();
                if(participantList.size() == 0 || !isConfirmRequirements) {
                    //display feedback message
                    Context context = getContext();
                    CharSequence text = "É necessário confirmar todos os requisitos e confirmar pelo menos 1 participante para iniciar a cerimônia";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    Intent intent = new Intent(getActivity(), ExecuteSteps.class);
                    intent.putExtra("NEW_PATH", finalCerimonyNewPath);
                    intent.putExtra("LOAD_PATH", finalCerimonyLoadedPath);
                    intent.putExtra("NAME", finalCerimonyFileName);
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    public void onStart(){
        super.onStart();
        if(first_time == true) {
            ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.vp);
            viewPager.setCurrentItem(2);
            first_time = false;
        }
    }


}
