package ml.app.rkcontacts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class FacultyList extends Fragment {
    String branch;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.faculty_list, container, false);
        branch = getArguments().getString("branch");
        Toast.makeText(getContext(), branch, Toast.LENGTH_LONG).show();

        getActivity().setTitle("Contacts");

        return view;
    }
}