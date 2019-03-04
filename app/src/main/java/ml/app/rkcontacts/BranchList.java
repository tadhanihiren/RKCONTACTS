package ml.app.rkcontacts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BranchList extends Fragment {
    @Nullable
    ListView listView;
    TextView branch;
    String school;

    String jasonstring= "{\"branch\":[{\"id\":\"CE\",\"name\":\"Computer Engineering\",\"school\":\"SDS\"},{\"id\":\"EE\",\"name\":\"Electrical Engineering\",\"school\":\"SDS\"}]}";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.branch_list, container, false);
        school = getArguments().getString("school");

//        Toast.makeText(getContext(), school, Toast.LENGTH_SHORT).show();

        listView=view.findViewById(R.id.listview);
        branch=view.findViewById(R.id.text);
        branch.setText(school+" Departments");
        ArrayList schoollist = new ArrayList();
        schoollist.add("ALL - All Departments");
        try {
            JSONObject ob = new JSONObject(jasonstring);
            JSONArray jsonArray = ob.getJSONArray("branch");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String id = jsonObject.getString("id");
                String schoolid = jsonObject.getString("school");
                if (schoolid.equals(school)){
                    schoollist.add(id+" - "+name);
                }
            }
            ArrayAdapter<String> customAdapter=new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1,schoollist);
            listView.setAdapter(customAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String strName = listView.getItemAtPosition(position).toString();
                strName =strName.substring(0, strName.indexOf(' '));
                FacultyList fragment =new FacultyList();
                Bundle arguments = new Bundle();
                arguments.putString("branch", strName);
                fragment.setArguments(arguments);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            }
        });

        getActivity().setTitle("Select Branch");
        return view;
    }
}