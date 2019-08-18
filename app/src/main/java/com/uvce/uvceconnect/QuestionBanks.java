package com.uvce.uvceconnect;


import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
    private Spinner branchspinner, semesterspinner, subjectspinner;
    private Button button;
    DatabaseReference databaseReference;
    List<String> subject = new ArrayList<>();

    List<String> semester = new ArrayList<>();
    List<String> branch = new ArrayList<>();
    String selectedbranch = " ", selectedsemester = " ", selectedsubject = " ", selectedsubjectclink = " ";
    ArrayAdapter<String> subjectadapter;
    ArrayAdapter<String> semesteradapter, branchadapter;


    public QuestionBanks() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_question_banks, container, false);


        branchspinner = v.findViewById(R.id.branchSpinner);
        semesterspinner = v.findViewById(R.id.semesterSpinner);
        subjectspinner = v.findViewById(R.id.subjectspinner);
        button = v.findViewById(R.id.download_button);


        branchadapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, branch);
        branchadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branchspinner.setAdapter(branchadapter);

        semesteradapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, semester);
        semesteradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semesterspinner.setAdapter(semesteradapter);

        subjectadapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, subject);
        subjectadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectspinner.setAdapter(subjectadapter);


        branch.add("Select Branch");
        semesteradapter.notifyDataSetChanged();
        subjectadapter.notifyDataSetChanged();


        databaseReference = FirebaseDatabase.getInstance().getReference("Question_Banks");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    branch.clear();
                    branch.add("Select Branch");
                    for (DataSnapshot childsnapshot : dataSnapshot.getChildren())
                        branch.add(childsnapshot.getKey());
                    branchadapter.notifyDataSetChanged();
                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        branchspinner.setSelected(true);


        branchspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedbranch = branch.get(position);
                Log.e("branch", selectedbranch);
                databaseReference.child(selectedbranch).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            semester.clear();
                            semester.add("Select Semester");
                            for (DataSnapshot childsnapshot : dataSnapshot.getChildren()) {
                                semester.add(childsnapshot.getKey());
                                Log.e("sem", childsnapshot.getKey());
                            }
                            semesteradapter.notifyDataSetChanged();

                        } catch (Exception e) {
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        semesterspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedsemester = semester.get(position);
                Log.e("semester", selectedsemester);
                databaseReference.child(selectedbranch).child(selectedsemester).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            subject.clear();
                            subject.add("Select Subject");
                            for (DataSnapshot childsnapshot : dataSnapshot.getChildren())
                                subject.add(childsnapshot.getKey());
                            subjectadapter.notifyDataSetChanged();
                        } catch (Exception e) {
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        subjectspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedsubject = subject.get(position);
                Log.e("topic", selectedsubject);
                databaseReference.child(selectedbranch).child(selectedsemester).child(selectedsubject).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            selectedsubjectclink = dataSnapshot.getValue().toString();
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getContext(),"Question_Banks/"+branches[branchspinner.getSelectedItemPosition()]+"/"+semesters[semesterspinner.getSelectedItemPosition()]+"/"+subject,Toast.LENGTH_LONG).show();
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

    private void startDownload() {


        try {
            String url = "https://docs.google.com/uc?id=[FILE_ID]&export=download";
            String id = getID(selectedsubjectclink);
            url = url.replace("[FILE_ID]", id);
            Download download = new Download(getContext(), selectedsubject + ".pdf", url);
            download.start();
        } catch (SecurityException e) {
            Toast.makeText(getContext(), "Please grant storage permissions to download", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
        }
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
}



        





