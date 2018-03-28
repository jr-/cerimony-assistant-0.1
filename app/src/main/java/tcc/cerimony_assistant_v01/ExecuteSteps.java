package tcc.cerimony_assistant_v01;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
            String folderName = CCerimonies.getInstance().getSelectedCerimony().getFolderName();
            String title = (String) this.getTitle();
            File photo = new File(Environment.getExternalStorageDirectory(),  "ceremony-assistant/final/" + "evidencia-" + folderName + "/" + title + ".jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(photo));
            imageUri = Uri.fromFile(photo);
            startActivityForResult(intent, TAKE_PICTURE);
        }

        return super.onOptionsItemSelected(item);
    }

}
