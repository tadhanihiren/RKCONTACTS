package ml.app.rkcontacts;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class GlobalFunctions {
    Context mContext;
    String jsondata;

    // constructor
    public GlobalFunctions(Context context) {
        this.mContext = context;
        SharedPreferences prefsjsn = mContext.getSharedPreferences("data", MODE_PRIVATE);
        jsondata = prefsjsn.getString("bulk", "");
    }

    public String getSchoolName(String schoolid) {
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
}
