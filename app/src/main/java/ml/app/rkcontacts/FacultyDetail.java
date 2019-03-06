package ml.app.rkcontacts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class FacultyDetail extends Fragment {
    String name;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.faculty_detail, container, false);
        name = getArguments().getString("name");
        Toast.makeText(getContext(), name, Toast.LENGTH_LONG).show();


        super.getActivity().setTitle(name);

        return view;
    }
}