package com.example.aplicaciomultimediamarc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class EditarVideo extends AppCompatActivity {

    public static final int RECORTE_MINIMO = 1000;
    private VideoView videoView;
    private CountDownTimer countDownTimer;
    private long tiempoExtra = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_video);

         videoView = findViewById(R.id.videoeditor);

         Uri uri = getIntent().getData();
         String rutaFitxer = getIntent().getStringExtra("RUTA");

        editarVideo(videoView, uri, rutaFitxer );

    }

    // Editor de video
    @SuppressLint("ClickableViewAccessibility")
    private void editarVideo(final VideoView videoView, final Uri uri, final String ruta) {


        // El videoView usa la Uri recibida por parametro
        videoView.setVideoURI( uri );

        // Botón de guardar
        final ImageView guardarBoton = findViewById(R.id.recortar);
        // Botón de play
        final ImageView playBoton = findViewById(R.id.playboton);

        // Contadores
        final TextView contadorInicio = findViewById(R.id.tiempoinicio);
        final TextView tiempo         = findViewById(R.id.tiempoactual);
        final TextView contadorFin    = findViewById(R.id.tiempofin);

        // Seekbars
        final SeekBar controlIzquierdo = findViewById(R.id.marcaizquierda);
        final SeekBar controlDerecho   = findViewById(R.id.marcaderecha);

        // Obtiene la duración del video
        final int duracion = extraerDuracion(uri);

        // La duración máxima será la del video
        controlIzquierdo.setMax(duracion);
        controlDerecho.setMax(duracion);

        // Tiempo del contador derecho inicial
        contadorFin.setText(convertirTiempo(duracion));

        // Temporizador
        countDownTimer = new CountDownTimer(duracion, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                tiempo.setText( convertirTiempo(videoView.getCurrentPosition()) );

                // Si el tiempo del video coincide con la posición del control derecho se terminará el tiempo
                if (convertirTiempo(videoView.getCurrentPosition()).equals(convertirTiempo(controlDerecho.getProgress()))){
                    videoView.pause();
                    playBoton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFinish() {
            }
        };

        // El control derecho empezará en la posición equivalente a la duración del video
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            controlDerecho.setProgress(duracion, true);
        }

        // Al hacer click sobre el video
        videoView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent)
            {

                if (videoView.isPlaying())
                {
                    playBoton.setVisibility(View.VISIBLE);
                    videoView.pause();
                    countDownTimer.cancel();
                }
                else
                {
                    playBoton.setVisibility(View.INVISIBLE);
                    videoView.seekTo(controlIzquierdo.getProgress());
                    videoView.start();
                    countDownTimer.start();
                }
                return false;
            }
        });

        // Al finalizar el video
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                countDownTimer.cancel();
            }
        });

        // Al deslizar el control izquierdo
        controlIzquierdo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress <= controlDerecho.getProgress() - RECORTE_MINIMO){
                    videoView.seekTo(progress);
                    tiempo.setText(convertirTiempo(progress));
                    contadorInicio.setText(convertirTiempo(progress));
                }
                // La posición del control izquierdo no podrá ser mayor a la del derecho
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        controlIzquierdo.setProgress(controlDerecho.getProgress() - RECORTE_MINIMO, true);
                    }
                    videoView.seekTo(controlDerecho.getProgress() - RECORTE_MINIMO);
                    tiempo.setText(convertirTiempo(controlDerecho.getProgress() - RECORTE_MINIMO));
                    contadorInicio.setText(convertirTiempo(controlDerecho.getProgress() - RECORTE_MINIMO));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tiempoExtra = seekBar.getProgress();
                playBoton.setVisibility(View.INVISIBLE);
                videoView.start();
                countDownTimer.start();
            }
        });

        // Al deslizar el control derecho
        controlDerecho.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Si el progreso del control es superior al del control izquierdo
                if (progress >= controlIzquierdo.getProgress() + RECORTE_MINIMO){
                    contadorFin.setText(convertirTiempo(progress));
                    tiempo.setText(convertirTiempo(progress));
                    videoView.seekTo(progress);
                }
                else{
                    // La posición del control derecho no podrá ser menor a la del izquierdo
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        controlDerecho.setProgress(controlIzquierdo.getProgress() + RECORTE_MINIMO, true);
                    }
                    contadorFin.setText(convertirTiempo(controlIzquierdo.getProgress() + RECORTE_MINIMO));
                    tiempo.setText(convertirTiempo(controlIzquierdo.getProgress() + RECORTE_MINIMO));
                    videoView.seekTo(controlIzquierdo.getProgress() + RECORTE_MINIMO);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                videoView.seekTo(controlIzquierdo.getProgress());
                videoView.pause();
            }
        });

        guardarBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // recortar y guardar video
                try {

                    Toast.makeText(EditarVideo.this, "Exportant video...", Toast.LENGTH_LONG).show();

                    MediaUtil.cortarVideo(ruta, getExternalFilesDir(null) + "newvideo", controlIzquierdo.getProgress(), controlDerecho.getProgress(),
                            true, true);
                } catch (IOException e) {
                    Toast.makeText(EditarVideo.this, "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    // Convierte los milisegundos recibidos por parametro en un string con minutos y segundos
    @SuppressLint("DefaultLocale")
    private String convertirTiempo (long milisegundos){
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milisegundos),
                TimeUnit.MILLISECONDS.toSeconds(milisegundos) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milisegundos))
        );
    }

    // Extrae la duración del video
    private int extraerDuracion(Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(EditarVideo.this, uri);

        String tiempo = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        int duracion = Integer.parseInt(tiempo);

        retriever.release();

        return duracion;
    }

}
