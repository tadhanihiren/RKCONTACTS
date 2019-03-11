package ml.app.rkcontacts.navigation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.KeyEvent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import ml.app.rkcontacts.ListViewAdapter;
import ml.app.rkcontacts.Model;
import ml.app.rkcontacts.R;

import static android.content.Context.MODE_PRIVATE;

public class NavContactsFragment extends Fragment {
    ListView listView;
    private SpotsDialog progressDialog;
    ListViewAdapter adapter;

    String jsondata;

    ArrayList<Model> arrayList = new ArrayList<Model>();

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.nav_fragment_contacts, container, false);

        progressDialog = new SpotsDialog(getContext(), R.style.Custom);
        progressDialog.setCancelable(false);

        SharedPreferences prefsjsn = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        jsondata = prefsjsn.getString("bulk", "");
//        UpdateData("auto");
        getActivity().setTitle("Contacts");


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

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setTitle("Confirm Exit...");
                    alertDialog.setMessage("Are you sure you want exit application?");
                    alertDialog.setIcon(R.drawable.ic_exit_to_app_black_24dp);
                    alertDialog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();
                        }
                    });

                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    alertDialog.show();

                }
                return false;
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
        super.onCreateOptionsMenu(menu, inflater);
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
        String JSON_URL = "http://rkuinfo.ml/getbulk.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SaveData("bulk", response);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void RefreshFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    private boolean SaveData(String type, String response) {
        try {
            JSONObject ob = new JSONObject(response);
            JSONArray jsonArray = ob.getJSONArray("faculty");
            if (jsonArray.length() != 0) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("data", MODE_PRIVATE).edit();
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
}