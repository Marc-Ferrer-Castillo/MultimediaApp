package com.example.aplicaciomultimediamarc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

public class EditarImagen extends AppCompatActivity {

    private ImageView imagen;
    private ImageView btn_recortar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editar_imagen);
        btn_recortar = findViewById(R.id.botonrecortar);

        cargarImagen(getIntent().getData());

        // Click en el icono recortar
        btn_recortar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start cropping activity for pre-acquired image saved on the device
                CropImage.activity(getIntent().getData()).start(EditarImagen.this);
            }
        });
    }

    private void cargarImagen(Uri data) {
        imagen = findViewById(R.id.mostrarimagen);
        imagen.setImageURI(data);
    }

    // Al haber recortado
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult resultado = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = resultado.getUri();
                Intent intent = new Intent (EditarImagen.this, Dialogo.class);
                intent.setData(resultUri);
                startActivity(intent);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = resultado.getError();
                Toast.makeText(EditarImagen.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
