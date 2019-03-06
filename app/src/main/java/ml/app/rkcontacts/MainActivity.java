package ml.app.rkcontacts;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    Button login;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
            }
        });

        if (loginStatus() == TRUE) {
            Intent i = new Intent(getApplicationContext(), Home.class);
            startActivity(i);
            finish();
        }


//        Intent i=new Intent(this,Home.class);
//        startActivity(i);
//        finish();
    }

    private void signin() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }

    private void handleResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            String email = account.getEmail();
            String profile = account.getPhotoUrl().toString();
//            String gender = account.gen

            SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE).edit();
            editor.putString("username", email);
            editor.apply();


            String JSON_URL = "http://rkuinfo.ml/getbulk.php?data=hkpanchani";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (savedata("bulk", response)) {
                                Intent i = new Intent(getApplicationContext(), Home.class);
                                startActivity(i);
                                finish();
                            } else {
                                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(@NonNull Status status) {
                                        SharedPreferences.Editor logineditor = getSharedPreferences("login", MODE_PRIVATE).edit();
                                        logineditor.clear().commit();
                                        logineditor.apply();

                                        SharedPreferences.Editor dataeditor = getSharedPreferences("data", MODE_PRIVATE).edit();
                                        dataeditor.clear().commit();
                                        dataeditor.apply();
                                        Toast.makeText(MainActivity.this, "No Data Found", Toast.LENGTH_LONG).show();

                                    }
                                });
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Getting error Please Check Network Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);

            String method = "update";
            UpdateProfile updatedp = new UpdateProfile(this);
            updatedp.execute(method, profile, email);


        } else {
            Toast.makeText(this, result.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean savedata(String type, String response) {

        try {
            JSONObject ob = new JSONObject(response);
            JSONArray jsonArray = ob.getJSONArray("faculty");
            if (jsonArray.length()!=0){
                SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putString(type, response);
                editor.apply();
                editor.commit();
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean loginStatus() {
        SharedPreferences prefs = getSharedPreferences("login", Activity.MODE_PRIVATE);
        String username = prefs.getString("username", "");
        if (!username.equals("")) {
            return TRUE;
        }
        return FALSE;
    }
}