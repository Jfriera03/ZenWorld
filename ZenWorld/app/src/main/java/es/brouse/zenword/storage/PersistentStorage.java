package es.brouse.zenword.storage;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Clase PersistentStorage que proporciona metodos para almacenar y recuperar datos de las preferencias compartidas.
 */
public class PersistentStorage {
    private final SharedPreferences preferences;

    /**
     * Constructor de PersistentStorage, inicializa las preferencias compartidas.
     *
     * @param context el contexto de la aplicacion.
     */
    public PersistentStorage(AppCompatActivity context) {
        preferences = context.getPreferences(Context.MODE_PRIVATE);
    }

    /**
     * Metodo que obtiene una cadena de las preferencias compartidas.
     *
     * @param key la clave de la cadena a obtener.
     * @return la cadena obtenida.
     */
    public String getString(String key) {
        return preferences.getString(key, null);
    }

    /**
     * Metodo que obtiene un entero de las preferencias compartidas.
     *
     * @param key la clave del entero a obtener.
     * @return el entero obtenido.
     */
    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    /**
     * Metodo que establece una cadena en las preferencias compartidas.
     *
     * @param key la clave de la cadena a establecer.
     * @param value el valor de la cadena a establecer.
     */
    public void setString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    /**
     * Metodo que establece un entero en las preferencias compartidas.
     *
     * @param key la clave del entero a establecer.
     * @param value el valor del entero a establecer.
     */
    public void setInt(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    /**
     * Metodo que verifica si una clave existe en las preferencias compartidas.
     *
     * @param key la clave a verificar.
     * @return un booleano que indica si la clave existe.
     */
    public boolean existsKey(String key) {
        return preferences.contains(key);
    }
}
