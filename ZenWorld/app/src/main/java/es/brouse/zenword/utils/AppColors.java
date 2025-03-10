package es.brouse.zenword.utils;

import android.graphics.Color;

/**
 * Clase AppColors que proporciona metodos para manejar los colores utilizados en la aplicacion.
 */
public class AppColors {
    /**
     * Array de colores primarios utilizados en la aplicacion.
     */
    public static final String[] PRIMARY_COLORS = new String[] {
            "#F9A87B", "#D36675", "#5C03BC", "#46B83D", "#D5CFAC"
    };

    /**
     * Array de colores secundarios utilizados en la aplicacion.
     */
    public static final String[] SECONDARY_COLORS = new String[] {
            "#F97D5B", "#BA4470", "#42048A", "#34852C", "#BEB15B"
    };

    /**
     * Indice del color actualmente seleccionado.
     */
    private static int colorIndex = 0;

    /**
     * Metodo que cambia el color actualmente seleccionado a uno aleatorio.
     */
    public static void changeColor() {
        colorIndex = (int) (Math.random() * PRIMARY_COLORS.length);
    }

    /**
     * Metodo que devuelve el color primario actualmente seleccionado.
     *
     * @return el color primario actualmente seleccionado.
     */
    public static int getPrimaryColor() {
        return Color.parseColor(PRIMARY_COLORS[colorIndex]);
    }

    /**
     * Metodo que devuelve el color secundario actualmente seleccionado.
     *
     * @return el color secundario actualmente seleccionado.
     */
    public static int getSecondaryColor() {
        return Color.parseColor(SECONDARY_COLORS[colorIndex]);
    }
}
