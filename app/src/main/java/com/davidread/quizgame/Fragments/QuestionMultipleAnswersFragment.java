package com.davidread.quizgame.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.davidread.quizgame.Activities.MainActivity;
import com.davidread.quizgame.R;

/**
 * This fragment class represents a screen with a multiple answers question with a text view for the
 * question, four checkboxes for options to choose from, and a button to increment the quiz. A
 * confirmation dialog is shown to confirm the increment of the quiz.
 */
public class QuestionMultipleAnswersFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener {

    // Intent extra constants.
    public static final String EXTRA_QUESTION = "question";
    public static final String EXTRA_OPTION_1 = "option_1";
    public static final String EXTRA_OPTION_2 = "option_2";
    public static final String EXTRA_OPTION_3 = "option_3";
    public static final String EXTRA_OPTION_4 = "option_4";

    // Fragment objects.
    private CheckBox checkBoxOption1;
    private CheckBox checkBoxOption2;
    private CheckBox checkBoxOption3;
    private CheckBox checkBoxOption4;
    private AlertDialog alertDialog;

    /**
     * Returns a view that represents a multiple answers question.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate a layout for a multiple answers question.
        View rootView = inflater.inflate(R.layout.fragment_question_multiple_answers, container, false);

        // Initialize text view, checkboxes, and button from the layout.
        TextView textViewQuestion = rootView.findViewById(R.id.text_view_question);
        checkBoxOption1 = rootView.findViewById(R.id.checkbox_option_1);
        checkBoxOption2 = rootView.findViewById(R.id.checkbox_option_2);
        checkBoxOption3 = rootView.findViewById(R.id.checkbox_option_3);
        checkBoxOption4 = rootView.findViewById(R.id.checkbox_option_4);
        Button buttonNext = rootView.findViewById(R.id.button_next);

        // Get arguments from main activity.
        Bundle args = getArguments();

        // Populate text view with question text and checkboxes with options text.
        if (args != null) {
            textViewQuestion.setText(args.getString(EXTRA_QUESTION));
            checkBoxOption1.setText(args.getString(EXTRA_OPTION_1));
            checkBoxOption2.setText(args.getString(EXTRA_OPTION_2));
            checkBoxOption3.setText(args.getString(EXTRA_OPTION_3));
            checkBoxOption4.setText(args.getString(EXTRA_OPTION_4));
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
        if (checkBoxOption1.isChecked() || checkBoxOption2.isChecked() || checkBoxOption3.isChecked() || checkBoxOption4.isChecked())
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
        ((MainActivity)getActivity()).incrementQuiz(checkBoxOption1.isChecked(), checkBoxOption2.isChecked(), checkBoxOption3.isChecked(), checkBoxOption4.isChecked());
    }
}
