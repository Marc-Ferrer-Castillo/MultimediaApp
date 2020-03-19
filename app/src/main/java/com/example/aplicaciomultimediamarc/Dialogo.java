package com.example.aplicaciomultimediamarc;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Dialogo extends Activity {

    private ImageView imagenRecortada;
    private Uri recorte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogo);

        // Titulo de la actividad
        setTitle("Guardar retall?");

        imagenRecortada = findViewById(R.id.imagenrecortada);
        Button guardar = findViewById(R.id.guardarimagen);

        recorte = getIntent().getData();
        imagenRecortada.setImageURI(recorte);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Guarda la imatge
                SaveImage(convertirABitmap());
                finish();
            }
        });
    }

    // Converteix Uri a Bitmap i retorna el bitmap
    private Bitmap convertirABitmap(){

        Bitmap bm = null;

        try {
            InputStream input = Dialogo.this.getContentResolver().openInputStream(recorte);
            bm = BitmapFactory.decodeStream(input);
        }
        catch (FileNotFoundException e) {
            Toast.makeText(Dialogo.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return bm;
    }

    // Guarda la imatge a la galeria
    private void SaveImage(Bitmap bitmap) {

        // Guarda la imatge a la galeria
        String savedImageURL = MediaStore.Images.Media.insertImage(
                getContentResolver(),
                bitmap,
                "Retall",
                "Imatge Retallada"
        );

        Toast.makeText(Dialogo.this, "Imatge guardada correctament\n"+ savedImageURL, Toast.LENGTH_LONG).show();
    }
}
