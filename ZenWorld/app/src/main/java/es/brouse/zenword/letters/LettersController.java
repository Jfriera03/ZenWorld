package es.brouse.zenword.letters;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import es.brouse.zenword.utils.MultiPath;
import es.brouse.zenword.R;
import es.brouse.zenword.utils.Utils;

/**
 * Clase LettersController que controla la lógica de las letras en la vista.
 */
public class LettersController implements View.OnTouchListener {
    private final ImageView parent;
    private final int radius;
    private final View view;
    private MultiPath multiPath;
    private List<Button> buttons;
    private boolean disabled = false;

    /**
     * Interfaz que define los métodos que la vista debe implementar.
     */
    public interface View {
        void init();
        List<Button> loadButtons(int nButtons);
        void changeColor();
        void setDisabled(boolean b);
        void clearButtons();
        void setDragListener(android.view.View.OnTouchListener listener);
        void createConnections(MultiPath path);
        void clearConnections();
    }

    /**
     * Constructor del controlador LettersController, inicializa las variables y la vista.
     *
     * @param view la vista asociada al controlador.
     * @param context el contexto de la aplicación.
     */
    public LettersController(View view, AppCompatActivity context) {
        this.view = view;
        this.parent = context.findViewById(R.id.letters_container);
        this.radius = Utils.getIntPx(R.dimen.letters_container) / 2;
        view.init();
    }

    /**
     * Método que carga los botones en la vista.
     *
     * @param nButtons el número de botones a cargar.
     */
    public void loadButtons(int nButtons) {
        multiPath = new MultiPath(nButtons + 1);

        // We load the button and then shuffle them
        buttons = view.loadButtons(nButtons);
        shuffleButtons();

        // We set the visuals
        view.changeColor();
        view.setDragListener(this);
    }

    /**
     * Método que reinicia los botones en la vista.
     */
    public void resetButtons() {
        view.clearButtons();
    }

    /**
     * Método que mezcla los botones en la vista.
     */
    public void shuffleButtons() {
        Collections.shuffle(buttons);
    }

    /**
     * Método que maneja los eventos de toque en la vista.
     *
     * @param v la vista que recibió el evento de toque.
     * @param event el evento de toque.
     * @return un booleano que indica si el evento de toque fue manejado.
     */
    @Override
    public boolean onTouch(android.view.View v, MotionEvent event) {
        // We don't want to handle the event if the buttons are disabled
        if (disabled) return false;

        int posX = (int) event.getX();
        int posY = (int) event.getY();

        // We only want to handle buttons
        if (!(v instanceof Button)) return false;

        Button button = (Button) v;
        posX += button.getLeft() - parent.getLeft();
        posY += button.getTop() - parent.getTop();

        // Check if the touch is inside the circle
        if (!Utils.isInRadius(posX, posY, radius, radius, radius)) {
            view.clearConnections();
            v.invalidate();
            return false;
        }

        // Handle the event
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Point pos = Utils.getAbsPosition(
                        v,
                        parent,
                        Utils.getIntPx(R.dimen.letters_circle) / 2
                );
                multiPath.start(pos, button.getId());
                break;
            case MotionEvent.ACTION_MOVE:
                handleDrag(posX, posY, button);
                this.view.clearConnections();
                this.view.createConnections(multiPath);
                break;
            case MotionEvent.ACTION_UP:
                this.view.clearConnections();
                multiPath.clear();
                break;
            default:
                return false;
        }

        v.performClick();
        parent.invalidate();
        return true;
    }

    /**
     * Método que deshabilita o habilita los botones en la vista.
     *
     * @param b un booleano que indica si los botones deben ser deshabilitados.
     */
    public void setDisabled(boolean b) {
        disabled = b;
        view.setDisabled(b);
    }

    /**
     * Método privado que obtiene el botón en una posición específica.
     *
     * @param x la posición x.
     * @param y la posición y.
     * @param id el identificador del botón.
     * @return el botón en la posición especificada.
     */
    private Button getButton(int x, int y, int id) {
        int radius = Utils.getIntPx(R.dimen.letters_circle) / 2;

        Iterator<Button> iterator = buttons.iterator();
        while (iterator.hasNext()) {
            Button button = iterator.next();
            // Skip same button
            if (button.getId() == id) continue;

            Point pos = Utils.getAbsPosition(button, parent, radius);

            if (Utils.isInRadius(x, y, pos.x, pos.y, radius)) return button;
        }
        return null;
    }

    /**
     * Método privado que maneja el arrastre de los botones en la vista.
     *
     * @param posX la posición x del arrastre.
     * @param posY la posición y del arrastre.
     * @param button el botón que se está arrastrando.
     */
    private void handleDrag(int posX, int posY, Button button) {
        // We handle disabled on parent, but just in case
        if (disabled) return;

        Button targetBtn = getButton(posX, posY, button.getId());

        if (targetBtn == null) {
            multiPath.setEnd(new Point(posX, posY));
        } else {
            Point pos = Utils.getAbsPosition(
                    targetBtn,
                    parent,
                    Utils.getIntPx(R.dimen.letters_circle) / 2
            );
            multiPath.addPath(pos, targetBtn.getId());
        }
    }
}
