package ml.app.rkcontacts.navigation;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import ml.app.rkcontacts.GlobalFunctions;
import ml.app.rkcontacts.R;

import static android.content.Context.MODE_PRIVATE;

public class NavProfileFragment extends Fragment {
    int branchcache;
    String email, jasonstring, oldschool;
    String[] gender = {"Select Gender", "Male", "Female"};
    TextView emailtv, aboutsection;
    EditText nameet, mobileet, extet;
    Spinner gendersp, schoolsp, branchsp;
    ArrayList<SetArrayAdapterClass> school = new ArrayList<>();
    ArrayList<SetArrayAdapterClass> branch = new ArrayList<>();
    ImageView profileev;
    String editname, editmobile, editext, editgender, editschool, editbranch;
    Button updt_prfl;
    private SpotsDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_profile, container, false);

        SharedPreferences prefsjsn = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        jasonstring = prefsjsn.getString("bulk", "");

        progressDialog = new SpotsDialog(getContext(), R.style.Custom);
        progressDialog.setCancelable(false);

        emailtv = view.findViewById(R.id.emailtv);
        nameet = view.findViewById(R.id.nameet);
        mobileet = view.findViewById(R.id.mobileev);
        extet = view.findViewById(R.id.extet);
        aboutsection = view.findViewById(R.id.aboutsection);
        gendersp = view.findViewById(R.id.gendersp);
        schoolsp = view.findViewById(R.id.schoolsp);
        branchsp = view.findViewById(R.id.branchsp);
        profileev = view.findViewById(R.id.profileev);
        updt_prfl = view.findViewById(R.id.updtprflbtn);

        SetArrayList("school", "");


        final ArrayAdapter<String> genderaa = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, gender);
        genderaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gendersp.setAdapter(genderaa);

        final ArrayAdapter<SetArrayAdapterClass> schoolaa = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, school);
        schoolaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schoolsp.setAdapter(schoolaa);


        gendersp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    editgender = "";
                else
                    editgender = gender[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        schoolsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editschool = school.get(position).toString();
                if (position == 0)
                    editschool = "";
                else
                    editschool = editschool.substring(0, editschool.indexOf(' '));
                SetArrayList("branch", editschool);

                if (editschool != null && oldschool != null) {
                    if (oldschool.equals(editschool))
                        branchsp.setSelection(branchcache);
                    else
                        branchsp.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        branchsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editbranch = branch.get(position).toString();
                if (position == 0)
                    editbranch = "";
                else
                    editbranch = editbranch.substring(0, editbranch.indexOf(' '));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        updt_prfl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidateUpdateData()) {
                    UpdateProfile();
                }
            }
        });


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
                            int spinnerPosition;
                            try {
                                JSONObject ob = new JSONObject(response);
                                JSONArray jsonArray = ob.getJSONArray("faculty");
                                if (jsonArray.length() == 0) {
                                    progressDialog.dismiss();
                                    DetachFragment();
                                    AlertMessage("Email Not Found in the database");

                                } else {
                                    for (int i = 0; i < 1; i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        emailtv.setText(jsonObject.getString("email"));
                                        nameet.setText(jsonObject.getString("fullname"));
                                        mobileet.setText(jsonObject.getString("mobile"));
                                        extet.setText(jsonObject.getString("ext"));
                                        aboutsection.setText("About " + jsonObject.getString("fullname"));
                                        spinnerPosition = genderaa.getPosition(jsonObject.getString("gender"));
                                        gendersp.setSelection(spinnerPosition);
                                        String schoolid = jsonObject.getString("school");
                                        String branchid = jsonObject.getString("branch");

                                        for (int j = 0; j < school.size(); j++) {
                                            String temp = school.get(j).toString();
                                            temp = temp.substring(0, temp.indexOf(' '));
                                            if (temp.equals(schoolid)) {
                                                oldschool = temp;
                                                schoolsp.setSelection(j);
                                                break;
                                            }
                                        }
                                        SetArrayList("branch", schoolid);

                                        for (int j = 0; j < branch.size(); j++) {
                                            String temp = branch.get(j).toString();
                                            temp = temp.substring(0, temp.indexOf(' '));
                                            if (temp.equals(branchid)) {
                                                branchcache = j;
                                                break;
                                            }
                                        }


                                        if (!jsonObject.getString("profile").equals(""))
                                            Picasso.get().load(jsonObject.getString("profile")).into(profileev);
                                        else
                                            profileev.setImageResource(R.drawable.profile);
                                    }
                                    progressDialog.dismiss();
                                }
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
                            AlertMessage("Please check your internet connection.");
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

    private void UpdateProfile() {
        progressDialog.show();
        String JSON_URL = "http://rkuinfo.ml/update_profile.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject ob = new JSONObject(response);
                            JSONArray jsonArray = ob.getJSONArray("response");
                            for (int i = 0; i < 1; i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if (jsonObject.getString("status").equals("success")) {
                                    AlertMessage("Profile updated successfully");
                                    DetachFragment();
                                } else
                                    AlertMessage("Failed to update profile. please try again");
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            AlertMessage("Failed to update profile. please try again");
                            DetachFragment();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        AlertMessage("Please check your internet connection.");
                        DetachFragment();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("task", "update");
                params.put("email", email);
                params.put("name", editname);
                params.put("mobile", editmobile);
                params.put("ext", editext);
                params.put("gender", editgender);
                params.put("school", editschool);
                params.put("branch", editbranch);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private boolean ValidateUpdateData() {
        editname = nameet.getText().toString();
        editmobile = mobileet.getText().toString();
        editext = extet.getText().toString();

        if (!editname.equals("")) {
            if (!editname.matches("^[a-zA-Z]+\\s[a-zA-Z].*")) {
                nameet.requestFocus();
                AlertMessage("Please Write Full Name");
                return false;
            }
        } else {
            nameet.requestFocus();
            AlertMessage("Name Field must not be empty");
            return false;
        }

        if (!editmobile.equals("")) {
            if (editmobile.length() != 10) {
                mobileet.requestFocus();
                AlertMessage("Please Enter 10 - Digit Mobile Number");
                return false;
            }
        } else {
            mobileet.requestFocus();
            AlertMessage("Mobile Field must not be empty");
            return false;
        }

        if (!editext.equals("")) {
            if (editext.length() != 3) {
                extet.requestFocus();
                AlertMessage("Please Enter 3 - Digit Extension Number");
                return false;
            }
        } else {
            extet.requestFocus();
            AlertMessage("Extension Field must not be empty");
            return false;
        }

        if (editgender.equals("")) {
            gendersp.requestFocus();
            AlertMessage("Please select your Gender");
            return false;
        }
        if (editschool.equals("")) {
            schoolsp.requestFocus();
            AlertMessage("Please select your School");
            return false;
        }
        if (editbranch.equals("")) {
            branchsp.requestFocus();
            AlertMessage("Please select your Branch");
            return false;
        }
        return true;
    }

    private void AlertMessage(String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setIcon(R.drawable.ic_info_black_24dp);
        alert.setTitle("Info!!!");
        alert.setMessage(msg);
        alert.setPositiveButton("OK", null);
        alert.show();

//        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
//        updt_prfl.setEnabled(true);
    }

    private void SetArrayList(String type, String param) {
        if (type == "school") {
            school.add(new SetArrayAdapterClass("Select School"));
            try {
                JSONObject ob = new JSONObject(jasonstring);
                JSONArray jsonArray = ob.getJSONArray("school");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String name = jsonObject.getString("name");
                    String id = jsonObject.getString("id");
                    school.add(new SetArrayAdapterClass(id + " - " + name));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (type == "branch") {
            final ArrayAdapter<SetArrayAdapterClass> branchaa = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, branch);
            if (branch != null)
                branch.clear();
            String schoolid = param;
            branch.add(new SetArrayAdapterClass("Select Branch"));
            try {
                JSONObject ob = new JSONObject(jasonstring);
                JSONArray jsonArray = ob.getJSONArray("branch");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.getString("school").equals(schoolid)) {
                        String branchid = jsonObject.getString("branch");
                        GlobalFunctions gb = new GlobalFunctions(getContext());
                        branch.add(new SetArrayAdapterClass(branchid + " - " + gb.getBranchName(branchid)));
                    }
                }
                branch.add(new SetArrayAdapterClass("GEN - General Department"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            branchaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            branchsp.setAdapter(branchaa);
        }
    }

    private void DetachFragment() {
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new NavDashboardFragment()).commit();
    }
}


class SetArrayAdapterClass {
    private String desc;

    public SetArrayAdapterClass(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    @Override
    public String toString() {
        return desc;
    }
}