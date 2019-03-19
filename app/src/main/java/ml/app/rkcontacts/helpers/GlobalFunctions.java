package ml.app.rkcontacts.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ml.app.rkcontacts.R;

import static android.content.Context.MODE_PRIVATE;

public class GlobalFunctions {
    Context mContext;
    String jsondata;

    // constructor
    public GlobalFunctions(Context context) {
        this.mContext = context;
    }

    public String getSchoolName(String schoolid) {
        SharedPreferences prefsjsn = mContext.getSharedPreferences("data", MODE_PRIVATE);
        jsondata = prefsjsn.getString("bulk", "");
        try {
            JSONObject ob = new JSONObject(jsondata);
            JSONArray jsonArray = ob.getJSONArray("school");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("id").equals(schoolid)) {
                    String name = jsonObject.getString("name");
                    return name;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return schoolid;
    }

    public String getBranchName(String branchid) {
        SharedPreferences prefsjsn = mContext.getSharedPreferences("data", MODE_PRIVATE);
        jsondata = prefsjsn.getString("bulk", "");
        try {
            JSONObject ob = new JSONObject(jsondata);
            JSONArray jsonArray = ob.getJSONArray("branch_master");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("id").equals(branchid)) {
                    String name = jsonObject.getString("name");
                    return name;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return branchid;
    }

    public void AlertMessage(Context amContext, String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(amContext);
        alert.setIcon(R.drawable.ic_info_black_24dp);
        alert.setTitle("Info!!!");
        alert.setMessage(msg);
        alert.setPositiveButton("OK", null);
        alert.show();
    }

    public void UpdateData() {
        String JSON_URL = mContext.getText(R.string.url) + "/getbulk.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SaveData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("data", "hkpanchani");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(mContext));
        requestQueue.add(stringRequest);
    }

    private void SaveData(String response) {
        try {
            JSONObject ob = new JSONObject(response);
            JSONArray jsonArray = ob.getJSONArray("faculty");
            if (jsonArray.length() != 0) {
                SharedPreferences.Editor editor = Objects.requireNonNull(mContext).getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putString("bulk", response);
                editor.apply();
                editor.commit();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
