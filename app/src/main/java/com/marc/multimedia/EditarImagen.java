package com.marc.multimedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.theartofdev.edmodo.cropper.CropImage;

/** Clase que se usa para previsualizar imagen
 * con opción a edición mediante el boton superior derecho.
 *
 * Se abre y recibe Uri de la imagen desde MainActivity */
public class EditarImagen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_imagen);

        // Botón para abrir el editor
        ImageView btn_recortar = findViewById(R.id.botonrecortar);

        cargarImagen(getIntent().getData());

        // Click en el icono recortar
        btn_recortar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre editor de imagen con el Uri
                CropImage.activity(getIntent().getData()).start(EditarImagen.this);
            }
        });
    }

    /**Carga la imagen en el ImageView
     * @param data Uri con imagen a mostrar en el imageView*/
    private void cargarImagen(Uri data) {
        ImageView imagen = findViewById(R.id.mostrarimagen);
        imagen.setImageURI(data);
    }

    // Al haber recortado
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            // Recibe el resultado del recorte
            CropImage.ActivityResult resultado = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                // Recibe el Uri del resultado
                Uri resultUri = resultado.getUri();
                // Abre dialogo personalizado (clase Dialogo) preguntando si se desea guardar
                // el recorte mostrando una vista previa
                Intent intent = new Intent (EditarImagen.this, Dialogo.class);
                intent.setData(resultUri);
                startActivity(intent);

            }
            // En aso de error
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = resultado.getError();
                Toast.makeText(EditarImagen.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
