package com.marc.multimedia;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.marc.multimedia.imagen.EditarImagen;
import com.marc.multimedia.imagen.MostrarImagen;
import com.marc.multimedia.secuencias.EditarVideo;
import com.marc.multimedia.secuencias.MostrarVideo;
import com.marc.multimedia.secuencias.RingdroidEditActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /**Constantes REQUEST_CODE*/
    private static final int REQUEST_PERMISSIONS_CODE = 2909;
    private static final int ERROR_REQUEST_CODE = -1;
    private static final int BUSCAR_IMATGE_REQUEST_CODE = 0;
    private static final int BUSCAR_VIDEO_REQUEST_CODE = 1;
    private static final int BUSCAR_SO_REQUEST_CODE = 2;

    /** Si edicio es true se abren actividades diferentes */
    private boolean edicio;
    /** Si permisosConcedidos es false no se utilizar funciones de lectura/escritura */
    private boolean permisosConcedidos = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views de los botones
        ImageView capturar = findViewById(R.id.capturar);
        ImageView galeria = findViewById(R.id.galeria);
        ImageView editar = findViewById(R.id.editar);

        // Permisos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS_CODE);

            }
            else {
                permisosConcedidos = true;
            }
        }
        else {
            permisosConcedidos = true;
        }

        // Click en capturar
        capturar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre la cámara si se encuntra hardware de cámara
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            // permisos concedidos
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permisosConcedidos = true;
            }
            else {
                Toast.makeText(MainActivity.this, "Permissos denegats", Toast.LENGTH_LONG).show();
            }
        }
    }

    /** Seleccio del tipus d´arxiu multimedia
     *  Utilitza un intent diferent segons el tipus
     *  @param contexto Contexto de la actividad*/
    public void seleccioTipus(Activity contexto) {

        // Objeto para crear diálogos
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        // Posibles opciones de tipo de archivo a escoger
        String[] opciones = {"Imatge", "Video", "Audio"};

        // Si se han concedido los permisos previamente
        if (permisosConcedidos){

            // Crea diálogo preguntado por el tipo de fichero deseado
            builder.setTitle("Selecciona un tipus de fitxer").setItems(opciones, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int seleccio) {

                    // Intent para la actividad a abrir
                    Intent intent = null;

                    // Según el tipo de fichero seleccionado
                    switch (seleccio){

                        // Seleccio d´imatge
                        case BUSCAR_IMATGE_REQUEST_CODE:
                            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, BUSCAR_IMATGE_REQUEST_CODE);
                            break;

                        // Seleccio d´audio
                        case BUSCAR_SO_REQUEST_CODE:
                            // Custom File Picker
                            intent = new Intent(MainActivity.this, FilePickerActivity.class);

                            // Configuración del filePicker
                            intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                                    .setCheckPermission(true)
                                    .setShowAudios(true) // Solo muestra audio
                                    .setShowFiles(false)
                                    .setShowImages(false)
                                    .setShowVideos(false)
                                    .setSingleChoiceMode(true) // Solo 1 único archivo seleccionable
                                    .setSkipZeroSizeFiles(true)
                                    .build());

                            startActivityForResult(intent, BUSCAR_SO_REQUEST_CODE);
                            break;

                        // Selecció de video
                        case BUSCAR_VIDEO_REQUEST_CODE:
                            // Custom File Picker
                            intent = new Intent(MainActivity.this, FilePickerActivity.class);
                            // Configuración del filePicker
                            intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                                    .setShowAudios(false)
                                    .setShowFiles(false)
                                    .setShowImages(false)
                                    .setShowVideos(true) // Solo muestra video
                                    .setCheckPermission(true)
                                    .setSingleChoiceMode(true) // Solo 1 único archivo seleccionable
                                    .setSkipZeroSizeFiles(true)
                                    .build());

                            startActivityForResult(intent, BUSCAR_VIDEO_REQUEST_CODE);
                            break;
                    }

                }}).create().show();

        }

    }

    // Un cop seleccionat el tipus i l´arxiu
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Si s´ha seleccionat un fitxer
        if (resultCode == RESULT_OK) {

            // Uri del fitxer seleccionat
            Uri uri = data.getData();
            // Fitxers seleccionats amb FilePickerActivity
            ArrayList<MediaFile> fitxers = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
            // Intent
            Intent intent = null;

            // Si no es selecciona cap fitxer amb el FilePickerActivity
            if (fitxers != null && fitxers.size() == 0) {
                requestCode = ERROR_REQUEST_CODE;
            }

            switch(requestCode){

                // Si l 'arxiu seleccionat és una imatge
                case BUSCAR_IMATGE_REQUEST_CODE:
                    if (edicio){
                        intent = new Intent (this, EditarImagen.class).setData(uri);
                    }
                    else{
                        intent = new Intent (this, MostrarImagen.class).setData(uri);
                    }
                    break;

                // Si l 'arxiu seleccionat és d'audio
                case BUSCAR_SO_REQUEST_CODE:

                    // Si es vol editar
                    if (edicio){
                        intent = new Intent (this, RingdroidEditActivity.class);
                        intent.putExtra("RUTA", fitxers.get(0).getPath());
                    }
                    // Només visualitzat
                    else{
                        intent = new Intent (this, MostrarVideo.class).setData(fitxers.get(0).getUri());
                    }
                    break;

                // Si l 'arxiu seleccionat és de video
                case BUSCAR_VIDEO_REQUEST_CODE:

                    // Si es vol editar
                    if (edicio){
                        intent = new Intent (this, EditarVideo.class).setData(fitxers.get(0).getUri());

                    }
                    // Només visualitzat
                    else{
                        intent = new Intent (this, MostrarVideo.class).setData(fitxers.get(0).getUri());
                    }
                    break;

                default:
                    Toast.makeText(MainActivity.this, "No s'ha pogut obrir el fitxer", Toast.LENGTH_LONG).show();
                    break;
            }
            // Inicia l´activitat corresponent
            if (intent != null){
                startActivity(intent);
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
