package ml.app.rkcontacts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FacultyDetail extends AppCompatActivity {
    String name, email, icon, mobile, ext, gender, school, branch;
    private static final String VCF_DIRECTORY = "/RKCONTACTS";
    ImageView iconev;
    LinearLayout callll, smsll, emailll, lnrcall, lnremail;
    TextView nametv, emailtv, mobiletv, exttv, gendertv, schooltv, branchtv, aboutsection;
    private File vcfFile;
    GlobalFunctions gb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_detail);
        gb = new GlobalFunctions(getApplicationContext());

        Intent i = getIntent();

        name = i.getStringExtra("name");
        email = i.getStringExtra("email");
        icon = i.getStringExtra("icon");
        mobile = i.getStringExtra("mobile");
        ext = i.getStringExtra("ext");
        gender = i.getStringExtra("gender");
        school = gb.getSchoolName(i.getStringExtra("school"));
        branch = gb.getBranchName(i.getStringExtra("branch"));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(name);

        callll = findViewById(R.id.callicon);
        smsll = findViewById(R.id.texticon);
        emailll = findViewById(R.id.emailicon);
        lnrcall = findViewById(R.id.lnrcall);
        lnremail = findViewById(R.id.lnremail);

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

        nametv = findViewById(R.id.title);
        emailtv = findViewById(R.id.lnrtvemail);
        mobiletv = findViewById(R.id.lnrtvcall);
        exttv = findViewById(R.id.lnrtvext);
        aboutsection = findViewById(R.id.aboutsection);
        gendertv = findViewById(R.id.lnrtvgen);
        schooltv = findViewById(R.id.lnrtvsch);
        branchtv = findViewById(R.id.lnrtvbra);
        iconev = findViewById(R.id.profile);

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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            return true;
        } else if (id == R.id.shrcntct) {
            ShareContact();
        }
        return super.onOptionsItemSelected(item);
    }

    private void ShareContact() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            Toast.makeText(getApplicationContext(), "Storage Permission is required to share a contact!!!", Toast.LENGTH_SHORT).show();
        } else {
            if (CreateVcf()) {
                Intent intent = new Intent(); //this will import vcf in contact list
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(android.content.Intent.ACTION_SEND);
                Uri myUri = FileProvider.getUriForFile(getApplicationContext(), "ml.app.rkcontacts.provider", vcfFile);
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
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
            Toast.makeText(getApplicationContext(), "SMS Permission is required to make a call!!!", Toast.LENGTH_SHORT).show();
        } else {
            if (!mobile.equals("NULL")) {
                getApplicationContext().startActivity(intent);
            }
        }

    }

    private void Email() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }

    private void Call() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            Toast.makeText(getApplicationContext(), "Call Permission is required to make a call!!!", Toast.LENGTH_SHORT).show();
        } else {
            if (!mobile.equals("NULL")) {
                getApplicationContext().startActivity(intent);
            }
        }
    }
}
