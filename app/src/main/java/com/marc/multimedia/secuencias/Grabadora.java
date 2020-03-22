//  The MIT License (MIT)

//  Copyright (c) 2018 Intuz Pvt Ltd.

//  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files
//  (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify,
//  merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:

//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
//  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
//  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
//  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.marc.multimedia.secuencias;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.marc.multimedia.R;

public class Grabadora extends AppCompatActivity {

    private static final int ADD_AUDIO = 1001;
    private Button btnAudioTrim;
    private static final int REQUEST_ID_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grabadora);

        btnAudioTrim = findViewById(R.id.btnaudiotrim);
        btnAudioTrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btnAudioTrim) {
                    //check storage permission before start trimming
                    if (checkStoragePermission()) {
                        startActivityForResult(new Intent(Grabadora.this, AudioTrimmerActivity.class), ADD_AUDIO);
                        overridePendingTransition(0, 0);
                    } else {
                        requestStoragePermission();
                    }
                }
            }
        });
    }



    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(Grabadora.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO},
                REQUEST_ID_PERMISSIONS);
    }

    private boolean checkStoragePermission() {
        return (ActivityCompat.checkSelfPermission(Grabadora.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(Grabadora.this,
                        Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ID_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(Grabadora.this, "Permission granted, Click again", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_AUDIO) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    //audio trim result will be saved at below path
                    String path = data.getExtras().getString("INTENT_AUDIO_FILE");
                    Toast.makeText(Grabadora.this, "Audio stored at " + path, Toast.LENGTH_LONG).show();
                }
            }
        }
    }


}
