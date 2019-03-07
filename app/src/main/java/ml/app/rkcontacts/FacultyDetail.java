package ml.app.rkcontacts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class FacultyDetail extends Fragment {
    String name,email,icon,mobile,ext,gender,school,branch;
    TextView nametv,emailtv,mobiletv,exttv,gendertv,schooltv,branchtv;
    ImageView iconev;
    LinearLayout callll,smsll,emailll,lnrcall,lnremail;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.faculty_detail, container, false);
        name = getArguments().getString("name");
        email = getArguments().getString("email");
        icon = getArguments().getString("icon");
        mobile = getArguments().getString("mobile");
        ext = getArguments().getString("ext");
        gender = getArguments().getString("gender");
        school = getArguments().getString("school");
        branch = getArguments().getString("branch");
        callll=view.findViewById(R.id.callicon);
        smsll=view.findViewById(R.id.texticon);
        emailll=view.findViewById(R.id.emailicon);
        lnrcall=view.findViewById(R.id.lnrcall);
        lnremail=view.findViewById(R.id.lnremail);

        lnrcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call();
            }
        });

        lnremail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email();
            }
        });

        callll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call();
            }
        });

        emailll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email();
            }
        });
        
        smsll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sms();
            }
        });

        nametv=view.findViewById(R.id.title);
        emailtv=view.findViewById(R.id.lnrtvemail);
        mobiletv=view.findViewById(R.id.lnrtvcall);
        exttv=view.findViewById(R.id.lnrtvext);
        gendertv=view.findViewById(R.id.lnrtvgen);
        schooltv=view.findViewById(R.id.lnrtvsch);
        branchtv=view.findViewById(R.id.lnrtvbra);
        iconev=view.findViewById(R.id.profile);

        nametv.setText(name);
        emailtv.setText(email);
        mobiletv.setText(mobile);
        exttv.setText(ext);
        gendertv.setText(gender);
        schooltv.setText(school);
        branchtv.setText(branch);
        Picasso.get().load(icon).into(iconev);
//        Toast.makeText(getContext(), name, Toast.LENGTH_LONG).show();


        super.getActivity().setTitle(name);

        return view;
    }

    private void Sms() {

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("smsto:" + mobile));
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 1);
            Toast.makeText(getContext(), "SMS Permission is required to make a call!!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (!mobile.equals("NULL")){
                getContext().startActivity(intent);
            }
        }

    }

    private void Email() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

    private void Call() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +mobile));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
            Toast.makeText(getContext(), "Call Permission is required to make a call!!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (!mobile.equals("NULL")){
                getContext().startActivity(intent);
            }
        }
    }
}