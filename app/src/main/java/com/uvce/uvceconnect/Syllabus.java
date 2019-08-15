package com.uvce.uvceconnect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Syllabus extends Fragment implements View.OnClickListener {
    private Spinner branchSpinner, semesterSpinner;
    private String fileName;
    private TextView noteTextView;
    private String branch, year;
    private TextView department_header, semester_header;
    private Typeface mycustomfont;
    private final String SYLLABUS = "Syllabus";

    //Ghouse
    SharedPreferences sharedPreferences;
    String registeredBefore="";
    int yeara;
    String brancha;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_syllabus, container, false);

        //Ghouse
        sharedPreferences=getActivity().getApplicationContext().getSharedPreferences("lol", Context.MODE_PRIVATE);
        registeredBefore=sharedPreferences.getString("registeredBefore","false");



        Button button = (Button) view.findViewById(R.id.download_button);
        button.setOnClickListener(this);
        mycustomfont = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/adobe_font.otf");
        department_header = view.findViewById(R.id.branchTextView);
        semester_header = view.findViewById(R.id.yearTextView);
        department_header.setTypeface(mycustomfont);
        semester_header.setTypeface(mycustomfont);
        branchSpinner = (Spinner) view.findViewById(R.id.branchSpinner);
        semesterSpinner = (Spinner) view.findViewById(R.id.semesterSpinner);
        noteTextView = (TextView) view.findViewById(R.id.noteTextView);

        brancha=sharedPreferences.getString("studentBranch","");
        yeara=sharedPreferences.getInt("studentyear",0);
        branchspinnerselect();



        return view;
    }

    @Override
    public void onClick(View v) {

            downloadButtonClicked();


    }

    public void downloadButtonClicked() {
        noteTextView.setText("");
        String b = getBranch();
        Integer y = getYear();

        if (y == 5 && !b.equals(getContext().getString(R.string.arch))) {
            Toast.makeText(getContext(), "Invalid Selection", Toast.LENGTH_SHORT).show();
            return;
        }

        setBranchYear(b, y);
        fileName = b.replaceAll("\\s+", "-") + '-' + String.valueOf(y) + ".pdf";

        checkStoragePermission();
    }

    private void setBranchYear(String b, Integer y) {
        Map<String, String> branchMapper = new HashMap<>();
        Map<Integer, String> yearMapper = new HashMap<>();

        final String arch = getContext().getString(R.string.arch),
                cse = getContext().getString(R.string.cse),
                ise = getContext().getString(R.string.ise),
                ce = getContext().getString(R.string.ce),
                me = getContext().getString(R.string.me),
                eee = getContext().getString(R.string.eee),
                ece = getContext().getString(R.string.ece);

        branchMapper.put(arch, "ARCH");
        branchMapper.put(cse, "CSE");
        branchMapper.put(ise, "ISE");
        branchMapper.put(eee, "EEE");
        branchMapper.put(ece, "ECE");
        branchMapper.put(ce, "CE");
        branchMapper.put(me, "ME");

        yearMapper.put(1, "Year_1");
        yearMapper.put(2, "Year_2");
        yearMapper.put(3, "Year_3");
        yearMapper.put(4, "Year_4");
        yearMapper.put(5, "Year_5");

        branch = branchMapper.get(b);
        year = yearMapper.get(y);
    }

    private String getBranch() {
        return branchSpinner.getSelectedItem().toString();
    }

    private Integer getYear() {
        return Integer.parseInt(semesterSpinner.getSelectedItem().toString());
    }

    protected void checkStoragePermission() {
        // if android version >= 6.0
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(
                    getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // if permission was not granted initially, ask the user again.
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            } else {
                // if android >= 6.0 and permission already granted, continue to download.
                startDownload();
            }
        } else {

            // if android < 6.0 continue to download, no need to ask permission again.
            startDownload();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // if permission allowed by the user, continue to download.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startDownload();
                } else {

                    // permission not granted, download can't take place.
                    Toast.makeText(getContext(),
                            "Permission denied to read External storage",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // execute the AsyncTask to download Files.
    private void startDownload() {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(SYLLABUS);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            String url, id, downloadURL = "https://docs.google.com/uc?id=[FILE_ID]&export=download";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (year.equals("Year_1") && !branch.equals("ARCH")) {
                    url = dataSnapshot.child("Year_1").getValue().toString();
                } else {
                    url = dataSnapshot.child(branch).child(year).getValue().toString();
                }

                if (url.equals("-1")) {
                    Toast.makeText(getContext(), "Resource not found\n" +
                                    "Will be added soon!\n" +
                                    "Please contribute if available.",
                            Toast.LENGTH_LONG).show();

                    return;
                }

                id = getID(url);
                downloadURL = downloadURL.replace("[FILE_ID]", id);
                Download md = new Download(getContext(), fileName, downloadURL);
                md.start();

                ref.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // returns the ID from the google drive URL.
    private String getID(String url) {
        String ID = "";

        for (int i = url.length() - 1; i >= 0; --i) {
            if (url.charAt(i) != '=') {
                ID = url.charAt(i) + ID;
            } else {
                break;
            }
        }

        return ID;
    }
    //ghouse selects branch spinner based on registration, will select branch, adding year selection as well


    private void branchspinnerselect(){
        switch(brancha){
            case "CSE": branchSpinner.setSelection(1);
                    break;
            case "ISE": branchSpinner.setSelection(5);
            break;
            case "ME": branchSpinner.setSelection(6);
            break;
            case "ECE": branchSpinner.setSelection(4);
            break;
            case "EEE": branchSpinner.setSelection(3);
            break;
            case "CE":branchSpinner.setSelection(2);
            break;
            case "ARCH": branchSpinner.setSelection(0);
            break;

        }
        switch(yeara){
            case 1: semesterSpinner.setSelection(1);
                break;
            case 2: semesterSpinner.setSelection(2);
            break;
            case 3: semesterSpinner.setSelection(3);
            break;
            case 4: semesterSpinner.setSelection(4);
            break;
            case 5: semesterSpinner.setSelection(5);
            break;
            case 6: semesterSpinner.setSelection(6);
                break;
            case 7: semesterSpinner.setSelection(7);
                break;
            case 8: semesterSpinner.setSelection(8);
                break;
            case 9: semesterSpinner.setSelection(9);
                break;
            case 10: semesterSpinner.setSelection(10);
                break;
        }

    }
    public int redirectToRegister(){
        Toast.makeText(getActivity().getApplicationContext(),"Kindly register to download. ", Toast.LENGTH_LONG).show();
        Intent intent=new Intent(getActivity(),Register.class);
        startActivityForResult(intent,1);
        brancha=sharedPreferences.getString("studentBranch","");
        registeredBefore=sharedPreferences.getString("registeredBefore","false");
        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        registeredBefore=sharedPreferences.getString("registeredBefore","false");
    }
}
