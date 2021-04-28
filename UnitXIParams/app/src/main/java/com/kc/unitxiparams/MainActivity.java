package com.kc.unitxiparams;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity<val> extends AppCompatActivity {
    private StorageReference mStorageRef;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.fstTxt);
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://unitxiparams.appspot.com");
        StorageReference data_file_ref = mStorageRef.child("data.txt");
        Log.e("data_file_ref", String.valueOf(data_file_ref));
        File local_file = null;
        try {
            local_file = File.createTempFile("data", "txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final File finalLocalFile = local_file;
        data_file_ref.getFile(local_file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("firebase ", ";local tem file created  created " + finalLocalFile.toString());
                String openFiletoopen = (finalLocalFile.toString());
                try {
                    FileInputStream fis = new FileInputStream (new File(openFiletoopen));
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader bufferedReader = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }
                    textView.setText(sb);
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebase ", ";local tem file not created  created " + exception.toString());
            }
        });
    }
}