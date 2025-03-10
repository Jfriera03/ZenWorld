package es.brouse.zenword;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import es.brouse.zenword.gamescreen.GameScreenController;
import es.brouse.zenword.gamescreen.GameScreenView;
import es.brouse.zenword.utils.Utils;

/**
 * Clase MainActivity que representa la actividad principal de la aplicacion.
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Controlador de la pantalla del juego.
     */
    private GameScreenController gameScreenController;
    /**
     * Indicador de si la actividad está ocupada.
     */
    private boolean busy = false;

    /**
     * Metodo que se llama cuando se crea la actividad.
     *
     * @param savedInstanceState el estado guardado de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.setContext(this);

        loadButtons();
    }

    /**
     * Metodo que se llama cuando se inicia la actividad.
     */
    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * Metodo para cargar los botones.
     */
    private void loadButtons() {
        gameScreenController = new GameScreenController(
                new GameScreenView(this),
                this
        );

        gameScreenController.startGame();
    }

    /**
     * Metodo para reiniciar el juego.
     *
     * @param view la vista que inicio el evento.
     */
    public void restartGame(View view) {
        // Avoid multiple clicks
        if (busy) return;

        System.out.println("Restarting game");

        busy = true;
        gameScreenController.restartGame();
        busy = false;
    }

    /**
     * Metodo para mezclar las letras.
     *
     * @param view la vista que inicio el evento.
     */
    public void shuffleLetters(View view) {
        gameScreenController.shuffleLetters();
    }

    /**
     * Metodo para mostrar las pistas.
     *
     * @param view la vista que inicio el evento.
     */
    public void showHints(View view) {
        gameScreenController.showHints();
    }

    /**
     * Metodo para cargar una pista.
     *
     * @param view la vista que inicio el evento.
     */
    public void loadHint(View view) {
        gameScreenController.showHelp();
    }
}