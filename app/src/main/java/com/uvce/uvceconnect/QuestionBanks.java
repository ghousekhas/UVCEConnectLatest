package com.uvce.uvceconnect;


import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionBanks extends Fragment {
    private Spinner branchspinner,semesterspinner,subjectspinner;
    private Button button;
    DatabaseReference databaseReference;
    List<String> subjects=new ArrayList<>();
    ArrayAdapter<String> subjectAdapter;
    String[] branches={"ARCH","CSE","CE","ECE","EEE","ISE","ME"};
    String[] semesters={"Sem_1","Sem_2","Sem_3","Sem_4","Sem_5","Sem_6","Sem_7","Sem_8","Sem_9","Sem_10"};
    String subject="";



    public QuestionBanks() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_question_banks, container, false);




        branchspinner=v.findViewById(R.id.branchSpinner);
        semesterspinner=v.findViewById(R.id.semesterSpinner);
        subjectspinner=v.findViewById(R.id.subjectspinner);
        button=v.findViewById(R.id.download_button);

        subjectAdapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, subjects);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectspinner.setAdapter(subjectAdapter);
        subjectspinner.setSaveEnabled(false);
        subjectspinner.setSelected(true);

        semesterspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                createSubjectsList(branches[branchspinner.getSelectedItemPosition()],semesters[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        branchspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                createSubjectsList(branches[position],semesters[semesterspinner.getSelectedItemPosition()]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject=subjects.get(subjectspinner.getSelectedItemPosition());
                Toast.makeText(getContext(),"Question_Banks/"+branches[branchspinner.getSelectedItemPosition()]+"/"+semesters[semesterspinner.getSelectedItemPosition()]+"/"+subject,Toast.LENGTH_LONG).show();
                checkStoragePermission();
            }
        });

















        return v;
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

    private void startDownload() {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Question_Banks/"+branches[branchspinner.getSelectedItemPosition()]+"/"+semesters[semesterspinner.getSelectedItemPosition()]);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            String url, id, downloadURL = "https://docs.google.com/uc?id=[FILE_ID]&export=download";
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               url=dataSnapshot.child(subject).getValue().toString();
                if (url.equals("-1")) {
                    Toast.makeText(getContext(), "Resource not found\n" +
                                    "Will be added soon!\n" +
                                    "Please contribute if available.",
                            Toast.LENGTH_LONG).show();

                    return;
                }

                id = getID(url);
                downloadURL = downloadURL.replace("[FILE_ID]", id);
                Download md = new Download(getContext(), branches[branchspinner.getSelectedItemPosition()]+semesters[semesterspinner.getSelectedItemPosition()]+".pdf", downloadURL);
                md.start();

                ref.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


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

    public void createSubjectsList(String branch,String semester){
        subjects.clear();
        databaseReference=FirebaseDatabase.getInstance().getReference().child("Question_Banks/"+branch+"/"+semester);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childsnapshot: dataSnapshot.getChildren())
                    subjectAdapter.add(childsnapshot.getKey());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        subjectAdapter.notifyDataSetChanged();

        



    }

}