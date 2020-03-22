package com.marc.multimedia.imagen;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.marc.multimedia.R;

/** Recibe Uri de imagen por parámetro desde MainActivity
 *  y lo muestra en el ZoomageView*/
public class MostrarImagen extends AppCompatActivity {

    private ImageView imagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_imagen);

        imagen = findViewById(R.id.imagen);
        imagen.setImageURI(getIntent().getData());
    }

}
