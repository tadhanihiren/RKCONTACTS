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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ml.app.rkcontacts.BranchList;
import ml.app.rkcontacts.R;

public class NavDashboardFragment  extends Fragment implements View.OnClickListener {
    ImageView soeb,sptb,sosb,sopb,achb,somb,sdsb,sasb,rkub;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_fragment_dashboard, container, false);

        soeb=view.findViewById(R.id.soebtn);
        sdsb=view.findViewById(R.id.sdsbtn);
        somb=view.findViewById(R.id.sombtn);
        sptb=view.findViewById(R.id.sptbtn);
        sosb=view.findViewById(R.id.sosbtn);
        sopb=view.findViewById(R.id.sopbtn);
        achb=view.findViewById(R.id.achbtn);
        sasb=view.findViewById(R.id.sasbtn);
        rkub=view.findViewById(R.id.rkubtn);

        soeb.setOnClickListener(this);
        sdsb.setOnClickListener(this);
        somb.setOnClickListener(this);
        sptb.setOnClickListener(this);
        sosb.setOnClickListener(this);
        sopb.setOnClickListener(this);
        achb.setOnClickListener(this);
        sasb.setOnClickListener(this);
        rkub.setOnClickListener(this);



        getActivity().setTitle(R.string.groups);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.soebtn:
                GetSchool("SOE");
                break;
            case R.id.sdsbtn:
                GetSchool("SDS");
                break;
            case R.id.sombtn:
                GetSchool("SOM");
                break;
            case R.id.sopbtn:
                GetSchool("SOP");
                break;
            case R.id.sosbtn:
                GetSchool("SOS");
                break;
            case R.id.sasbtn:
                GetSchool("SAS");
                break;
            case R.id.sptbtn:
                GetSchool("SPT");
                break;
            case R.id.achbtn:
                GetSchool("ACH");
                break;
            case R.id.rkubtn:
                GetSchool("RKU");
//                replaceFragment();
                break;
        }
    }

    private void GetSchool(String school) {
        BranchList fragment =new BranchList();
        Bundle arguments = new Bundle();
        arguments.putString("school", school);
        fragment.setArguments(arguments);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();

    }

    private void replaceFragment (){
        Fragment fragment=new BranchList();
        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }


}