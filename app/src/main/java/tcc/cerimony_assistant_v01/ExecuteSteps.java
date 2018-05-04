package tcc.cerimony_assistant_v01;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;

public class ExecuteSteps extends AppCompatActivity {

    public TextView title_toolbar;
    public TextView status_toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_steps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        title_toolbar = (TextView) toolbar.findViewById(R.id.title_toolbar);
        status_toolbar = (TextView) toolbar.findViewById(R.id.status_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_execute_steps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        final int TAKE_PICTURE = 1;
        Uri imageUri;


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Cerimony selectedCeremony = CCerimonies.getInstance().getSelectedCerimony();
            String folderName = selectedCeremony.getFolderName();
            int currentStepNumber = selectedCeremony.getCurrentStepNumber();
            String evidenceName = "evidencia-passo" + currentStepNumber + ".jpg";
            File photo = new File(Environment.getExternalStorageDirectory(),  "ceremony-assistant/final/" + folderName + "/" + evidenceName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(photo));
            imageUri = Uri.fromFile(photo);
            startActivityForResult(intent, TAKE_PICTURE);

        }

        if (id == R.id.notes) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Escreva suas anotações:");
            ViewGroup view = (ViewGroup) findViewById(android.R.id.content);
            View viewInflated = LayoutInflater.from(this).inflate(R.layout.abort_dialog, view, false);
            final EditText input = (EditText) viewInflated.findViewById(R.id.input);
            builder.setView(viewInflated);

            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Cerimony selectedCeremony = CCerimonies.getInstance().getSelectedCerimony();
                    int currentStepNumber = selectedCeremony.getCurrentStepNumber();
                    String note = input.getText().toString();
                    selectedCeremony.getSteps().get(currentStepNumber).setNotes(note);

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

        return super.onOptionsItemSelected(item);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK) {
            Cerimony selectedCeremony = CCerimonies.getInstance().getSelectedCerimony();
            int currentStepNumber = selectedCeremony.getCurrentStepNumber();
            String evidenceName = "evidencia-passo" + currentStepNumber + ".jpg";
            selectedCeremony.getSteps().get(currentStepNumber).setEvidence(evidenceName);
        }
    }

}
