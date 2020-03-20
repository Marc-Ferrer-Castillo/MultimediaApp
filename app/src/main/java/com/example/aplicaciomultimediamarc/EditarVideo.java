package com.example.aplicaciomultimediamarc;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.deep.videotrimmer.DeepVideoTrimmer;
import com.deep.videotrimmer.interfaces.OnTrimVideoListener;

public class EditarVideo extends AppCompatActivity implements OnTrimVideoListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_video);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
            } else {
                // continue with your code
            }
        } else {
            // continue with your code
        }


        DeepVideoTrimmer videoTrimmer = findViewById(R.id.videorecorte);
        videoTrimmer.setMaxDuration(500);
        videoTrimmer.setMaxFileSize(30);
        videoTrimmer.setOnTrimVideoListener(this);
        videoTrimmer.setVideoURI(getIntent().getData());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2909: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission", "Granted");
                } else {
                    Log.e("Permission", "Denied");
                }
                return;
            }
        }
    }
    @Override
    public void getResult(Uri uri) {
        savefile(uri);
    }

    @Override
    public void cancelAction() {

    }

    void savefile(Uri sourceuri)
    {
        Toast.makeText(EditarVideo.this, "Video guardat correctament", Toast.LENGTH_LONG).show();

    }
}
