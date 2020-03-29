package com.marc.multimedia.secuencias;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.marc.multimedia.R;

import java.io.IOException;

public class CapturaAudio extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private static String fitxer = null;
    private static final int PERMISSION_REQUEST_RECORD_AUDIO = 4000;
    private boolean permisCapturaAudio = false;
    private LinearLayout grabarpausa;
    private boolean grabant = false;
    private ImageView micro;
    private Chronometer crono;
    private MediaRecorder recorder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captura_audio);

        grabarpausa = findViewById(R.id.grabarpausa);
        micro = findViewById(R.id.micropausa);
        crono = findViewById(R.id.chronometer);

        demanarPermisos();

        fitxer = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        fitxer += "/Gravació.3gp";

        grabarpausa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grabarPausar();
            }
        });


    }

    private void grabarPausar() {

        if (permisCapturaAudio){
            if (grabant){
                micro.setImageResource(R.drawable.mic);
                micro.setContentDescription(getResources().getText(R.string.play));
                grabant = false;
                crono.setBase(SystemClock.elapsedRealtime());
                crono.stop();
                aturar();
            }
            else{
                crono.start();
                micro.setImageResource(R.drawable.pausa);
                micro.setContentDescription(getResources().getText(R.string.stop));
                grabant = true;
                grabar();
            }
        }
    }

    private void aturar() {
        if (recorder != null){
            recorder.stop();
            recorder.release();
            recorder = null;
        }

        // Guardado con exito
        Toast.makeText(this,
                R.string.guardat,
                Toast.LENGTH_SHORT).show();
    }

    private void grabar() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fitxer);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setAudioEncodingBitRate(16);
        recorder.setAudioSamplingRate(44100);

        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }


    }

    private void demanarPermisos() {

        // Permisos
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            // Mostrar avís
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(this, "Per utilitzar la grabadora són necessaris permissos", Toast.LENGTH_LONG).show();
            }
            else {
                // Demana permissos
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_RECORD_AUDIO);
            }
        } else {
            permisCapturaAudio = true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_RECORD_AUDIO) {
            // permisos concedidos
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permisCapturaAudio = true;
            }
            else {
                Toast.makeText(CapturaAudio.this, "Per utilitzar la grabadora són necessaris permissos", Toast.LENGTH_LONG).show();
            }
        }
    }
}
