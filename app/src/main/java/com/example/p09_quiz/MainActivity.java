package com.example.p09_quiz;

import android.Manifest;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {

    EditText et;
    Button save,read;
    String folderLocation;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = findViewById(R.id.et);
        save = findViewById(R.id.save);
        read = findViewById(R.id.read);
        tv = findViewById(R.id.tv);

        folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/quiz";

        File folder = new File(folderLocation);
        if (folder.exists() == false) {
            boolean result = folder.mkdir();
            if (result == true) {
                Log.d("File Read/Write", "Folder created");
            }
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkPermission()) {
                    String coord = et.getText().toString();
                    try {
                        File file = new File(folderLocation, "quiz.txt");
                        FileWriter filewriter = new FileWriter(file, false);
                        filewriter.write(coord + "\n");
                        filewriter.flush();
                        filewriter.close();

                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Failed to write!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } else {
                    String msg = "Permission not granted to retrieve location info";
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }

            }
        });

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {

                    File file = new File(folderLocation, "quiz.txt");
                    if (file.exists() == true) {
                        String data = "";

                        try {

                            FileReader reader = new FileReader(file);
                            BufferedReader br = new BufferedReader(reader);

                            String line = br.readLine();
                            while (line != null) {
                                data += line + "\n";
                                line = br.readLine();
                            }
                            br.close();
                            reader.close();
                            tv.setText(data);

                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "Failed to write!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                } else {
                    String msg = "Permission not granted to retrieve location info";
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }
            }
        });

    }
    private boolean checkPermission() {
        int permissionCheck_read = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck_write = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck_read == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_write == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

}
