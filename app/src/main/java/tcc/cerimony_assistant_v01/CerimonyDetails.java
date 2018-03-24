package tcc.cerimony_assistant_v01;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class CerimonyDetails extends AppCompatActivity {

    private static final String TAG = "CerimonyDetails";

    private CeremonyDetailsPageAdapter mCMPageAdapter;

    public ViewPager mViewPager;
    public Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerimony_details);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mCMPageAdapter = new CeremonyDetailsPageAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.vp);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        CeremonyDetailsPageAdapter adapter = new CeremonyDetailsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new CerimonyDetailsFragment(), "Detalhes");
        adapter.addFragment(new RequirementsFragment(), "Requisitos");
        adapter.addFragment(new ParticipantsFragment(), "Participantes");

        viewPager.setAdapter(adapter);
    }

}
