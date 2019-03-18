package ml.app.rkcontacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class Groups extends AppCompatActivity {
    String school;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        school = i.getStringExtra("school");

        if (savedInstanceState == null) {
            BranchList fragment = new BranchList();
            Bundle arguments = new Bundle();
            arguments.putString("school", school);
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            onBackPressed();
        return true;

    }
}