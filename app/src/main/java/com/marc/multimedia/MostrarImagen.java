package com.marc.multimedia;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;


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
