package ml.app.rkcontacts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FacultyDetail extends Fragment {
    String name, email, icon, mobile, ext, gender, school, branch;
    private static final String VCF_DIRECTORY = "/RKCONTACTS";
    ImageView iconev;
    LinearLayout callll, smsll, emailll, lnrcall, lnremail;
    TextView nametv, emailtv, mobiletv, exttv, gendertv, schooltv, branchtv, aboutsection;
    private File vcfFile;


    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.faculty_detail, container, false);
        name = getArguments().getString("name");
        email = getArguments().getString("email");
        icon = getArguments().getString("icon");
        mobile = getArguments().getString("mobile");
        ext = getArguments().getString("ext");
        gender = getArguments().getString("gender");
        school = getArguments().getString("school");
        branch = getArguments().getString("branch");
        callll = view.findViewById(R.id.callicon);
        smsll = view.findViewById(R.id.texticon);
        emailll = view.findViewById(R.id.emailicon);
        lnrcall = view.findViewById(R.id.lnrcall);
        lnremail = view.findViewById(R.id.lnremail);

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

        nametv = view.findViewById(R.id.title);
        emailtv = view.findViewById(R.id.lnrtvemail);
        mobiletv = view.findViewById(R.id.lnrtvcall);
        exttv = view.findViewById(R.id.lnrtvext);
        aboutsection = view.findViewById(R.id.aboutsection);
        gendertv = view.findViewById(R.id.lnrtvgen);
        schooltv = view.findViewById(R.id.lnrtvsch);
        branchtv = view.findViewById(R.id.lnrtvbra);
        iconev = view.findViewById(R.id.profile);

        nametv.setText(name);
        emailtv.setText(email);
        mobiletv.setText(mobile);
        exttv.setText(ext);
        aboutsection.setText("About " + name);
        gendertv.setText(gender);
        schooltv.setText(school);
        branchtv.setText(branch);
        if (!icon.equals(""))
            Picasso.get().load(icon).into(iconev);
        else
            iconev.setImageResource(R.drawable.profile);
//        Toast.makeText(getContext(), name, Toast.LENGTH_LONG).show();


        super.getActivity().setTitle(name);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.shrcntct) {
            ShareContact();
        }
        return super.onOptionsItemSelected(item);
    }

    private void ShareContact() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            Toast.makeText(getContext(), "Storage Permission is required to share a contact!!!", Toast.LENGTH_SHORT).show();
        } else {
            if (CreateVcf()) {
                Intent intent = new Intent(); //this will import vcf in contact list
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(android.content.Intent.ACTION_SEND);
                Uri myUri = FileProvider.getUriForFile(getContext(), "ml.app.rkcontacts.provider", vcfFile);
                intent.setDataAndType(myUri, "text/x-vcard");
                intent.putExtra(Intent.EXTRA_STREAM, myUri);
                startActivity(Intent.createChooser(intent, "Share Contact"));
            }
        }
    }

    private boolean CreateVcf() {

        File dir = new File(Environment.getExternalStorageDirectory() + "/RKCONTACTS");
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }
        try {
            // File vcfFile = new File(this.getExternalFilesDir(null), "generated.vcf");
            File vdfdirectory = new File(
                    Environment.getExternalStorageDirectory() + VCF_DIRECTORY);
            // have the object build the directory structure, if needed.
            if (!vdfdirectory.exists()) {
                vdfdirectory.mkdirs();
            }

            vcfFile = new File(vdfdirectory, name + ".vcf");

            FileWriter fw = null;
            fw = new FileWriter(vcfFile);
            fw.write("BEGIN:VCARD\r\n");
            fw.write("VERSION:3.0\r\n");
            // fw.write("N:" + p.getSurname() + ";" + p.getFirstName() + "\r\n");
            fw.write("FN:" + name + "\r\n");
            //  fw.write("ORG:" + p.getCompanyName() + "\r\n");
            //  fw.write("TITLE:" + p.getTitle() + "\r\n");
            fw.write("TEL;TYPE=WORK,VOICE:" + mobile + "\r\n");
            //   fw.write("TEL;TYPE=HOME,VOICE:" + p.getHomePhone() + "\r\n");
            //   fw.write("ADR;TYPE=WORK:;;" + p.getStreet() + ";" + p.getCity() + ";" + p.getState() + ";" + p.getPostcode() + ";" + p.getCountry() + "\r\n");
            fw.write("EMAIL;TYPE=PREF,WORK:" + email + "\r\n");
            fw.write("END:VCARD\r\n");
            fw.close();

            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void Sms() {

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("smsto:" + mobile));
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 1);
            Toast.makeText(getContext(), "SMS Permission is required to make a call!!!", Toast.LENGTH_SHORT).show();
        } else {
            if (!mobile.equals("NULL")) {
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
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
            Toast.makeText(getContext(), "Call Permission is required to make a call!!!", Toast.LENGTH_SHORT).show();
        } else {
            if (!mobile.equals("NULL")) {
                getContext().startActivity(intent);
            }
        }
    }
}