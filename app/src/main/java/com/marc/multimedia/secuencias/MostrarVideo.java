package com.marc.multimedia.secuencias;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.marc.multimedia.R;

/** Clase para mostrar video, se abre desde MainActivity.java*/
public class MostrarVideo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_video);

        // ViewView para visualizar el video
        final VideoView videoView = findViewById(R.id.video);

        // Fuente del video que se recibe por intent
        videoView.setVideoURI( getIntent().getData() );

        // Controles de video instanciados y asignados al videoView
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Reproduce el video
        videoView.start();
    }
}
