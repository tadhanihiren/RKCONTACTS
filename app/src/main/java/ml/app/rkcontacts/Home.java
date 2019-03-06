package ml.app.rkcontacts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

import ml.app.rkcontacts.navigation.NavContactsFragment;
import ml.app.rkcontacts.navigation.NavDashboardFragment;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {
    private DrawerLayout drawer;
    TextView dispname, dispemail;
    String Gmail,Gname;
    Uri Gprofile;
    ImageView Gimage;
    private GoogleApiClient googleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.s_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);

        GoogleSignInAccount acct= GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct !=null){
            Gname=acct.getDisplayName();
            Gmail=acct.getEmail();
            Gprofile=acct.getPhotoUrl();


            dispemail = headerView.findViewById(R.id.dispemail);
            dispname = headerView.findViewById(R.id.dispname);
            Gimage=headerView.findViewById(R.id.profile);
            dispemail.setText(Gmail);
            dispname.setText(Gname);

            Picasso.get().load(Gprofile).into(Gimage);

        }



        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new NavContactsFragment()).commit();
            navigationView.setCheckedItem(R.id.cntct);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dshrd:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NavDashboardFragment()).addToBackStack(null).commit();
                break;
            case R.id.cntct:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NavContactsFragment()).addToBackStack(null).commit();
                break;
//            case R.id.prsnl:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new NavPersonalFragment()).addToBackStack(null).commit();
//                break;
            case R.id.shr:

                break;
            case R.id.updt:

                break;
            case R.id.s_lgt:
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        SharedPreferences.Editor logineditor = getSharedPreferences("login", MODE_PRIVATE).edit();
                        logineditor.clear().commit();
                        logineditor.apply();

                        SharedPreferences.Editor dataeditor = getSharedPreferences("data", MODE_PRIVATE).edit();
                        dataeditor.clear().commit();
                        dataeditor.apply();

                        finish();
                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(i);
                    }
                });
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

//    @Override
//    public void onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            return;
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce = false;
//            }
//        }, 1000);
//    }
}
