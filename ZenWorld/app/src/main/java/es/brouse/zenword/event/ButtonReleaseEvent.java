package es.brouse.zenword.event;

import android.widget.Button;

/**
 * Clase ButtonReleaseEvent que representa un evento de liberacion de boton.
 */
public class ButtonReleaseEvent {
    private static final AppEventHandler<ButtonReleaseEvent> HANDLER = new AppEventHandler<>();
    private final int[] button;

    /**
     * Constructor del evento ButtonReleaseEvent, recibe un array de identificadores de boton.
     *
     * @param id el array de identificadores de boton.
     */
    public ButtonReleaseEvent(int[] id) {
        this.button = id;
    }

    /**
     * Metodo que devuelve el array de identificadores de boton asociados al evento.
     *
     * @return el array de identificadores de boton.
     */
    public int[] getButton() {
        return button;
    }

    /**
     * Metodo estático que devuelve el manejador de eventos para ButtonReleaseEvent.
     *
     * @return el manejador de eventos para ButtonReleaseEvent.
     */
    public static AppEventHandler<ButtonReleaseEvent> getHandler() {
        return HANDLER;
    }
}
