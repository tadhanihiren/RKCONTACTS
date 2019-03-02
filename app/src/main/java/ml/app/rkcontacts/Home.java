package ml.app.rkcontacts;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ml.app.rkcontacts.navigation.NavDashboardFragment;
import ml.app.rkcontacts.navigation.NavFlatFragment;
import ml.app.rkcontacts.navigation.NavPersonalFragment;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    TextView dispname, dispemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.s_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);

        dispemail = headerView.findViewById(R.id.dispemail);
        dispname = headerView.findViewById(R.id.dispname);
        dispemail.setText("h@h");
        dispname.setText("Hiren");

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new NavDashboardFragment()).commit();
            navigationView.setCheckedItem(R.id.dshrd);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dshrd:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NavDashboardFragment()).commit();
                break;
            case R.id.flt:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NavFlatFragment()).commit();
                break;
            case R.id.prsnl:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NavPersonalFragment()).commit();
                break;
            case R.id.shr:

                break;
            case R.id.updt:

                break;
            case R.id.s_lgt:

                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 1000);
    }
}
