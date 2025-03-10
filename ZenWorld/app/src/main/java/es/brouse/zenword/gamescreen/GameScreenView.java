package es.brouse.zenword.gamescreen;

import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import es.brouse.zenword.utils.AppColors;
import es.brouse.zenword.R;
import es.brouse.zenword.utils.Utils;

/**
 * Clase GameScreenView que implementa la interfaz de la vista del controlador GameScreenController.
 */
public class GameScreenView implements GameScreenController.View {
    private final AppCompatActivity context;
    private final Map<Integer, TextView[]> rows;
    private int lastID = -1;

    /**
     * Constructor de la vista GameScreenView, inicializa el contexto y las filas.
     *
     * @param context el contexto de la aplicación.
     */
    public GameScreenView(AppCompatActivity context) {
        this.context = context;
        rows = new HashMap<>();
    }

    /**
     * Método que establece las palabras y la palabra actual en la vista.
     *
     * @param words las palabras a establecer.
     * @param word la palabra actual.
     * @param solsCount el número total de soluciones.
     * @param alert indica si se debe mostrar un diálogo de alerta.
     */
    @Override
    public void setWords(Map<String, String> words, String word, int solsCount, boolean alert) {
        int currentWords = 0;
        TextView wordsCount = context.findViewById(R.id.wordsCount);
        TextView allWords = context.findViewById(R.id.allWords);

        StringBuilder wordBuilder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = words.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            currentWords++;

            // Add the word to the list with the specific color
            if (entry.getValue().equals(word)) {
                wordBuilder.append(String.format("<font color='red'>%s</font>, ", entry.getValue()));
            } else {
                wordBuilder.append(String.format("%s, ", entry.getValue()));
            }
        }

        if (wordBuilder.length() > 0) wordBuilder.delete(wordBuilder.length() - 2, wordBuilder.length());

        if (alert) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(String.format(
                    context.getString(R.string.words_found)
                    , currentWords, solsCount));
            builder.setPositiveButton("OK", null);

            builder.setMessage(Html.fromHtml(wordBuilder.toString()));
            builder.create().show();
        } else {
            wordsCount.setText(String.format(
                    context.getString(R.string.words_found)
                    , currentWords, solsCount)
            );
            allWords.setText(Html.fromHtml(wordBuilder.toString()));
        }
    }

    /**
     * Método que crea una fila en la vista con un número específico de letras.
     *
     * @param id el identificador de la fila.
     * @param numberLetters el número de letras en la fila.
     */
    @Override
    public void createRow(int id, int numberLetters) {
        // Add the LinearLayout to the parent
        ConstraintLayout parent = context.findViewById(R.id.main_layout);

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setId(View.generateViewId());
        linearLayout.setGravity(Gravity.CENTER);

        // Generate the layout parameters
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setLayoutDirection(LinearLayout.HORIZONTAL);

        // Set the constraints for the LinearLayout
        layoutParams.topToBottom = lastID != -1 ? lastID : R.id.guide_game_content;
        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;

        layoutParams.setMargins(20, 25, 20, 0);

        // Update the layout
        linearLayout.setLayoutParams(layoutParams);
        lastID = linearLayout.getId();

        // Start generating the letters
        TextView[] textViews = new TextView[numberLetters];
        for (int i = 0; i < numberLetters; i++) {
            TextView textView = new TextView(context);
            textView.setId(View.generateViewId());
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.getIntPx(R.dimen.letter_textSize));
            textView.setTextColor(Color.BLACK);
            textView.setBackground(AppCompatResources.getDrawable(
                    context,
                    R.drawable.round_square_shape
            ));
            textView.getBackground().setColorFilter(
                    AppColors.getPrimaryColor(),
                    android.graphics.PorterDuff.Mode.SRC_IN
            );

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    Utils.getIntPx(R.dimen.hidden_letter),
                    Utils.getIntPx(R.dimen.hidden_letter)
            );
            params.setMargins(10, 0, 0, 0);
            textView.setLayoutParams(params);
            textViews[i] = textView;
            linearLayout.addView(textView);
        }

        // Add to view
        rows.put(id, textViews);
        parent.addView(linearLayout);
    }

    /**
     * Método que elimina todas las filas de la vista.
     */
    @Override
    public void deleteRows() {
        // Delete the created textViews
        Iterator<Map.Entry<Integer, TextView[]>> iterator = rows.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, TextView[]> entry = iterator.next();
            LinearLayout parent = (LinearLayout) entry.getValue()[0].getParent();
            parent.removeAllViews();

            ConstraintLayout rootParent = ((ConstraintLayout) parent.getParent());
            if (rootParent.getId() != R.id.main_layout) rootParent.removeView(parent);
        }

        // Perform the logic remove
        lastID = -1;
        rows.clear();
    }

    /**
     * Método que carga una palabra en la vista.
     *
     * @param id el identificador de la fila.
     * @param word la palabra a cargar.
     */
    @Override
    public void loadWord(int id, char[] word) {
        TextView[] textViews = rows.get(id);

        // Error, word doesn't fit on space
        assert textViews != null;
        if (word.length > textViews.length) {
            Log.e("GameScreenView", "Word doesn't fit on space (" +
                    word.length + " " + textViews.length + ")");
            return;
        }

        // Call loadChar for each character in the word
        for (int i = 0; i < word.length; i++) loadChar(id, word[i], i);
    }

    /**
     * Método que muestra un mensaje en la vista.
     *
     * @param text el mensaje a mostrar.
     */
    @Override
    public void displayToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * Método que carga un carácter en una posición específica en la vista.
     *
     * @param id el identificador de la fila.
     * @param character el carácter a cargar.
     * @param position la posición en la que cargar el carácter.
     */
    @Override
    public void loadChar(int id, char character, int position) {
        TextView[] textViews = rows.get(id);

        // Error, position is greater than the number of textViews
        assert textViews != null;
        if (position >= textViews.length) {
            Log.d("GameScreenView", "Position is greater than the number of textViews");
            return;
        }

        textViews[position].setText(String.valueOf(character).toUpperCase());
    }

    /**
     * Método que establece el número de pistas en la vista.
     *
     * @param hints el número de pistas a establecer.
     */
    @Override
    public void setHints(int hints) {
        TextView textHint = context.findViewById(R.id.hintsInfo);
        RelativeLayout.LayoutParams layout =
                ((RelativeLayout.LayoutParams) textHint.getLayoutParams());

        layout.setMarginEnd(hints >= 10 ? 57 : 67);
        textHint.setLayoutParams(layout);

        textHint.setText(String.valueOf(hints));
    }
}
