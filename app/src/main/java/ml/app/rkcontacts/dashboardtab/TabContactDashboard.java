package ml.app.rkcontacts.dashboardtab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import dmax.dialog.SpotsDialog;
import ml.app.rkcontacts.R;
import ml.app.rkcontacts.UpdateProfile;
import ml.app.rkcontacts.helpers.GlobalFunctions;
import ml.app.rkcontacts.helpers.ListViewAdapter;
import ml.app.rkcontacts.helpers.Model;

import static android.content.Context.MODE_PRIVATE;

public class TabContactDashboard extends Fragment {
    ListView listView;
    private SpotsDialog progressDialog;
    ListViewAdapter adapter;
    GlobalFunctions gf;
    String jsondata;

    ArrayList<Model> arrayList = new ArrayList<>();

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        LayoutInflater lf = Objects.requireNonNull(this.getActivity()).getLayoutInflater();
        View view = lf.inflate(R.layout.tab_contact_dashboard, container, false);
        gf = new GlobalFunctions(getContext());

        progressDialog = new SpotsDialog(getContext(), R.style.Custom);
        progressDialog.setCancelable(false);

        SharedPreferences prefsjsn = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        jsondata = prefsjsn.getString("bulk", "");
        UpdateData("auto");

        SharedPreferences prefup = getActivity().getSharedPreferences("app", MODE_PRIVATE);
        if (prefup.getString("update", "").equals(""))
            gf.UpdateApp("auto");

        listView = view.findViewById(R.id.listView);
        if (arrayList != null) {
            arrayList.clear();
        } else {
            arrayList = new ArrayList<>();
        }
        try {
            JSONObject ob = new JSONObject(jsondata);
            JSONArray jsonArray = ob.getJSONArray("faculty");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("fullname");
                String email = jsonObject.getString("email");
                String mobile = jsonObject.getString("mobile");
                String profile = jsonObject.getString("profile");
                String ext = jsonObject.getString("ext");
                String gender = jsonObject.getString("gender");
                String school = jsonObject.getString("school");
                String branch = jsonObject.getString("branch");
                Model model = new Model(name, email, profile, mobile, ext, gender, school, branch);
                //bind all strings in an array
                arrayList.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //pass results to listViewAdapter class
        adapter = new ListViewAdapter(getContext(), arrayList);

        //bind the adapter to the listview
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)) {
                    adapter.filter("");
                    listView.clearTextFilter();
                } else {
                    adapter.filter(s);
                }
                return true;
            }
        });
//        menu.clear();
//        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.update_contact_data) {
            progressDialog.show();
            UpdateData("manual");
        }
        return super.onOptionsItemSelected(item);
    }

    private void UpdateData(final String type) {
        String JSON_URL = getText(R.string.url) + "/getbulk.php";
//        Toast.makeText(getContext(), JSON_URL, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SaveData(response);
                        if (!type.equals("auto")) {
                            progressDialog.dismiss();
                            RefreshFragment();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (!type.equals("auto")) {
                            Toast.makeText(getContext(), "Getting error Please Check Network Connection", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("data", "hkpanchani");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        requestQueue.add(stringRequest);
    }

    private void RefreshFragment() {
        assert getFragmentManager() != null;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    private void SaveData(String response) {
        try {
            JSONObject ob = new JSONObject(response);
            JSONArray jsonArray = ob.getJSONArray("faculty");
            if (jsonArray.length() != 0) {
                SharedPreferences.Editor editor = Objects.requireNonNull(getContext()).getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putString("bulk", response);
                editor.apply();
                editor.commit();
                CheckProfile(response);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void CheckProfile(String jsondata) {
        String email = "";
        Boolean profilepresent = false;
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct != null) {
            email = acct.getEmail();
        }
        try {
            JSONObject ob = new JSONObject(jsondata);
            JSONArray jsonArray = ob.getJSONArray("faculty");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String tempemail = jsonObject.getString("email");
                if (tempemail.equals(email)) {
                    profilepresent = true;
                    break;
                }
            }
            if (!profilepresent) {
                Intent i = new Intent(getContext(), UpdateProfile.class);
                startActivity(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}