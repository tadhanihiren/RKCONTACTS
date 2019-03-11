package ml.app.rkcontacts.navigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import ml.app.rkcontacts.R;

public class NavProfileFragment extends Fragment {
    String email;
    TextView emailtv;
    EditText nameet,mobileet,extet,genderet,schoolet,branchet;
    ImageView profileev;
    private SpotsDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_fragment_profile, container, false);

        progressDialog = new SpotsDialog(getContext(), R.style.Custom);
        progressDialog.setCancelable(false);

        emailtv=view.findViewById(R.id.emailtv);
        nameet=view.findViewById(R.id.nameet);
        mobileet=view.findViewById(R.id.mobileev);
        extet=view.findViewById(R.id.extet);
        genderet=view.findViewById(R.id.genderet);
        schoolet=view.findViewById(R.id.schoolet);
        branchet=view.findViewById(R.id.branchet);
        profileev=view.findViewById(R.id.profileev);



        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct != null) {
            email = acct.getEmail();
        }

        if (!email.equals("")) {
            progressDialog.show();
            String JSON_URL = "http://rkuinfo.ml/update_profile.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject ob = new JSONObject(response);
                                JSONArray jsonArray = ob.getJSONArray("faculty");
                                for (int i = 0; i < 1; i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    emailtv.setText(jsonObject.getString("email"));
                                    nameet.setText(jsonObject.getString("fullname"));
                                    mobileet.setText(jsonObject.getString("mobile"));
                                    extet.setText(jsonObject.getString("ext"));
                                    genderet.setText(jsonObject.getString("gender"));
                                    schoolet.setText(jsonObject.getString("school"));
                                    branchet.setText(jsonObject.getString("branch"));
                                    if (!jsonObject.getString("profile").equals("http://blms.ml/defaultuser.png"))
                                        Picasso.get().load(jsonObject.getString("profile")).into(profileev);
                                    else
                                        profileev.setImageResource(R.drawable.profile);
                                }
                            progressDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            DetachFragment();
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Unable to Update Profile at the Moment.", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("task", "fetch");
                    params.put("email", email);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        }
        getActivity().setTitle(R.string.profile);
        return view;
    }
    private void DetachFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
    }
}