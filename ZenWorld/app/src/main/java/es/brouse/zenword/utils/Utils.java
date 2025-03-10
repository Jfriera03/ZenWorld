package es.brouse.zenword.utils;

import android.graphics.Point;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Clase Utils que proporciona metodos de utilidad para la aplicacion.
 */
public final class Utils {
    /**
     * Contexto de la aplicacion.
     */
    private static AppCompatActivity context;
    /**
     * Metodo para establecer el contexto de la aplicacion.
     *
     * @param context el contexto de la aplicacion.
     * @throws IllegalStateException si el contexto ya ha sido definido.
     */
    public static void setContext(AppCompatActivity context) {
        if (Utils.context != null) {
            throw new IllegalStateException("Cannot redefine singleton context.");
        }
        Utils.context = context;
    }

    /**
     * Metodo para verificar si un punto esta dentro de un radio dado.
     *
     * @param posX la posicion x del punto.
     * @param posY la posicion y del punto.
     * @param centerX la posicion x del centro del circulo.
     * @param centerY la posicion y del centro del circulo.
     * @param radius el radio del circulo.
     * @return un booleano que indica si el punto está dentro del circulo.
     */
    public static boolean isInRadius(int posX, int posY, int centerX, int centerY, int radius) {
        return Math.pow(posX - centerX, 2) + Math.pow(posY - centerY, 2) <= radius * radius;
    }

    /**
     * Metodo para obtener la posicion absoluta de una vista.
     *
     * @param object la vista de la que obtener la posicion.
     * @param parent la vista padre de la vista.
     * @param offset el desplazamiento a aplicar a la posicion.
     * @return un objeto Point que representa la posicion de la vista.
     */
    public static Point getAbsPosition(View object, View parent, int offset) {
        int posX = object.getLeft() - parent.getLeft() + offset;
        int posY = object.getTop() - parent.getTop() + offset;

        return new Point(posX, posY);
    }

    /**
     * Metodo para obtener un valor de dimensión en pixeles como un entero.
     *
     * @param id el identificador del recurso de dimension.
     * @return el valor de la dimension en pixeles.
     */
    public static int getIntPx(int id) {
        return context.getResources().getDimensionPixelSize(id);
    }

    /**
     * Metodo para obtener un valor de dimension en pixeles como un flotante.
     *
     * @param id el identificador del recurso de dimension.
     * @return el valor de la dimension en pixeles.
     */
    public static float getFloatPx(int id) {
        return context.getResources().getDimensionPixelSize(id);
    }
}
