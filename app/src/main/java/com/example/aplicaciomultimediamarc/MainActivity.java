package com.example.aplicaciomultimediamarc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int BUSCAR_MEDIA = 1;
    private boolean edicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView capturar = findViewById(R.id.capturar);
        ImageView galeria = findViewById(R.id.galeria);
        ImageView editar = findViewById(R.id.editar);



        // Click en capturar
        capturar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hiHaCamara()) {
                    startActivity(new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA));
                }
            }
        });

        // Click en galeria
        galeria.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                edicio = false;
                seleccioTipus(MainActivity.this);
            }
        });

        // Click en editar
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edicio = true;
                seleccioTipus(MainActivity.this);
            }
        });


    }

    // Seleccio del tipus d´arxiu multimedia
    // Utilitza un intent diferent segons el tipus
    public void seleccioTipus(Activity contexto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        String[] opciones = {"Imatge", "Video", "Audio"};

        builder.setTitle("Selecciona un tipus de fitxer").setItems(opciones, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int seleccio) {
                        Intent intent = null;

                        switch (seleccio){
                            case 0:
                                // Seleccio d´imatge
                                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                break;
                            case 1:
                                // Seleccio de video
                                intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                                break;
                            case 2:
                                // Seleccio d´audio
                                intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                                break;
                        }

                        // Obre activity si el intent no es null
                        if (intent != null){
                            startActivityForResult(intent, BUSCAR_MEDIA);
                        }
                    }
                }).create().show();
    }

    // Un cop seleccionat el tipus i l´arxiu
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                // Intent
                Intent intent = null;

                // Si l 'arxiu seleccionat és una imatge
                if (uri.toString().contains("image")) {

                    if (edicio){
                        intent = new Intent (this, EditarImagen.class);
                    }
                    else{
                        intent = new Intent (this, MostrarImagen.class);
                    }
                }
                // Si l 'arxiu seleccionat és un vídeo
                else if (uri.toString().contains("video")) {
                    // Si es vol editar
                    if (edicio){
                        intent = new Intent (this, EditarVideo.class);
                    }
                    else{
                        intent = new Intent (this, MostrarVideo.class);
                    }
                }
                // Obre activity si el intent no es null
                if (intent != null){
                    intent.setData(uri);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this, "Error al obrir el fitxer", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    // Busca hardware de camara al dispositiu, retorna true si existeix
    private boolean hiHaCamara () {
        boolean retorno = false;
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            retorno = true;
        } else {
            Toast.makeText(this, "No s'ha trovat cap hardware de càmara", Toast.LENGTH_SHORT).show();
        }
        return retorno;
    }

}
