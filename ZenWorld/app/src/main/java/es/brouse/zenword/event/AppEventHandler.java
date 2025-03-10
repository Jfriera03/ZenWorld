package es.brouse.zenword.event;

import android.util.Log;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Clase AppEventHandler que maneja los eventos de la aplicación.
 *
 * @param <E> el tipo de evento que maneja este manejador de eventos.
 */
public class AppEventHandler<E> {
    private static final String TAG = "CustomEventHandler";
    private final Set<Consumer<E>> listeners = new HashSet<>();

    /**
     * Registra un nuevo oyente en el manejador de eventos.
     *
     * @param listener el oyente a registrar.
     */
    public void register(Consumer<E> listener) {
        listeners.add(listener);
        Log.d(TAG, "Registered listener: " + listener);
    }

    /**
     * Ejecuta todos los oyentes registrados.
     *
     * @param context el contexto del evento.
     */
    public void handleEvent(E context) {
        synchronized (listeners) {
            Iterator<Consumer<E>> iterator = listeners.iterator();
            while (iterator.hasNext()) {
                Consumer<E> listener = iterator.next();
                listener.accept(context);
            }
        }
    }
}
