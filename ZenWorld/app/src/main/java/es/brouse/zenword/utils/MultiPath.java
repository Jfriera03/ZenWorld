package es.brouse.zenword.utils;

import android.graphics.Point;

import java.util.Arrays;
import java.util.function.BiConsumer;

import es.brouse.zenword.event.ButtonPressEvent;
import es.brouse.zenword.event.ButtonReleaseEvent;

/**
 * Clase MultiPath que representa un camino compuesto por multiples puntos.
 */
public class MultiPath {
    /**
     * Array de puntos que representan el camino.
     */
    private final Point[] points;
    /**
     * Punto final del camino.
     */
    private Point end;
    /**
     * Array de identificadores de los puntos.
     */
    private final int[] ids;
    /**
     * Indice actual en el array de puntos.
     */
    private int index = 0;

    /**
     * Constructor de MultiPath, inicializa el array de puntos y los identificadores.
     *
     * @param size el tamaño del camino.
     */
    public MultiPath(int size) {
        this.points = new Point[size];
        this.ids = new int[size];
        Arrays.fill(ids, -1);
    }

    /**
     * Metodo que inicia un nuevo camino.
     *
     * @param start el punto de inicio del camino.
     * @param id el identificador del punto de inicio.
     */
    public void start(Point start, int id) {
        // Set the new values
        points[0] = start;
        ids[0] = id;

        index = 1;

        // Call the event for the first value
        ButtonPressEvent.getHandler().handleEvent(new ButtonPressEvent(ids[0]));
    }

    /**
     * Metodo que limpia el camino.
     */
    public void clear() {
        Arrays.fill(points, null);
        Arrays.fill(ids, -1);
        index = 0;

        ButtonReleaseEvent.getHandler().handleEvent(new ButtonReleaseEvent(ids));
    }

    /**
     * Metodo que añade un nuevo punto al camino.
     *
     * @param point el nuevo punto a añadir.
     * @param id el identificador del nuevo punto.
     */
    public void addPath(Point point, int id) {
        int posArr = getIndex(id);

        if (posArr == -1) {
            ids[index] = id;
            points[index] = point;

            // Call the event for the button pressed
            ButtonPressEvent.getHandler().handleEvent(new ButtonPressEvent(ids[index++]));
        }else {
            points[posArr] = point;
        }

        end = null;
    }

    /**
     * Metodo que establece el punto final del camino.
     *
     * @param end el punto final del camino.
     */
    public void setEnd(Point end) {
        this.end = end;
    }

    /**
     * Metodo que consume el camino, aplicando una funcion a cada par de puntos.
     *
     * @param consumer la funcion a aplicar a cada par de puntos.
     */
    public void consume(BiConsumer<Point, Point> consumer) {
        if (points[0] == null) return;

        for (int i = 0; i < index; i++) {
            if (points[i + 1] == null) break;

            consumer.accept(points[i], points[i + 1]);
        }

        if (end != null) consumer.accept(points[index - 1], end);
    }

    /**
     * Metodo privado que obtiene el indice de un identificador en el array de identificadores.
     *
     * @param id el identificador a buscar.
     * @return el indice del identificador en el array, o -1 si no se encuentra.
     */
    private int getIndex(int id) {
        for (int i = 0; i < ids.length; i++) if (ids[i] == id) return i;
        return -1;
    }
}
