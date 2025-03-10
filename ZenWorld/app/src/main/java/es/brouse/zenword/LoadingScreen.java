package es.brouse.zenword;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import es.brouse.zenword.utils.DictionaryReader;

/**
 * Clase LoadingScreen que representa la pantalla de carga de la aplicacion.
 */
public class LoadingScreen extends AppCompatActivity {
    /**
     * Texto de carga en la pantalla.
     */
    private TextView loadingText;

    /**
     * Metodo que se llama cuando se crea la actividad.
     *
     * @param savedInstanceState el estado guardado de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        loadingText = findViewById(R.id.loading_info);

        loadApp();
    }

    /**
     * Metodo para cargar la aplicacion.
     */
    private void loadApp() {
        loadingText.setText(getString(R.string.loading));

        new Handler().post(() -> {
            DictionaryReader.readDictionary(this);
            startActivity(new Intent(LoadingScreen.this, MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
    }
}