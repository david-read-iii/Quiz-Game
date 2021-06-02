package com.davidread.quizgame.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.davidread.quizgame.Activities.MainActivity;
import com.davidread.quizgame.R;

/**
 * This fragment class represents a screen with a multiple choice question with a text view for
 * the question, four radio buttons for options to choose from, and a button to increment the
 * quiz. A confirmation dialog is shown to confirm the increment of the quiz.
 */
public class QuestionMultipleChoiceFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener {

    // Intent extra constants.
    public static final String EXTRA_QUESTION = "question";
    public static final String EXTRA_OPTION_1 = "option_1";
    public static final String EXTRA_OPTION_2 = "option_2";
    public static final String EXTRA_OPTION_3 = "option_3";
    public static final String EXTRA_OPTION_4 = "option_4";

    // Fragment objects.
    private RadioButton radioButtonOption1;
    private RadioButton radioButtonOption2;
    private RadioButton radioButtonOption3;
    private RadioButton radioButtonOption4;
    private AlertDialog alertDialog;

    /**
     * Returns a view that represents a multiple choice question.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate a layout for a multiple choice question.
        View rootView = inflater.inflate(R.layout.fragment_question_multiple_choice, container, false);

        // Initialize text view, radio buttons, and button from the layout.
        TextView textViewQuestion = rootView.findViewById(R.id.text_view_question);
        radioButtonOption1 = rootView.findViewById(R.id.radio_option_1);
        radioButtonOption2 = rootView.findViewById(R.id.radio_option_2);
        radioButtonOption3 = rootView.findViewById(R.id.radio_option_3);
        radioButtonOption4 = rootView.findViewById(R.id.radio_option_4);
        Button buttonNext = rootView.findViewById(R.id.button_next);

        // Get arguments from main activity.
        Bundle args = getArguments();

        // Populate text view with question text and radio buttons with options text.
        if (args != null) {
            textViewQuestion.setText(args.getString(EXTRA_QUESTION));
            radioButtonOption1.setText(args.getString(EXTRA_OPTION_1));
            radioButtonOption2.setText(args.getString(EXTRA_OPTION_2));
            radioButtonOption3.setText(args.getString(EXTRA_OPTION_3));
            radioButtonOption4.setText(args.getString(EXTRA_OPTION_4));
        }

        // Setup button with listener.
        buttonNext.setOnClickListener(this);

        // Setup confirmation dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.dialog_confirm_answer_title));
        builder.setMessage(getString(R.string.dialog_confirm_answer_message));
        builder.setPositiveButton(getString(R.string.dialog_confirm_positive_button), this);
        builder.setNegativeButton(getString(R.string.dialog_confirm_negative_button), null);
        alertDialog = builder.create();

        return rootView;
    }

    /**
     * Click listener for the next button. It shows the confirmation dialog if an answer selection
     * has been made.
     */
    @Override
    public void onClick(View v) {
        if (radioButtonOption1.isChecked() || radioButtonOption2.isChecked() || radioButtonOption3.isChecked() || radioButtonOption4.isChecked())
            alertDialog.show();
        else
            Toast.makeText(getContext(), getString(R.string.toast_error_answer_selection_empty), Toast.LENGTH_SHORT).show();
    }

    /**
     * Click listener for the positive button in the confirmation dialog. It calls incrementQuiz()
     * in the main activity.
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        ((MainActivity)getActivity()).incrementQuiz(radioButtonOption1.isChecked(), radioButtonOption2.isChecked(), radioButtonOption3.isChecked(), radioButtonOption4.isChecked());
    }
}
