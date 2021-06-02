package com.davidread.quizgame.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.davidread.quizgame.R;

/**
 * This fragment class represents a screen that explains the rules of the quiz.
 */
public class RulesFragment extends Fragment {

    /**
     * Returns a view representing a rules screen.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rules, container, false);
        return rootView;
    }
}
