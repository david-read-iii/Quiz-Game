package com.davidread.quizgame.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.davidread.quizgame.Models.Result;
import com.davidread.quizgame.Models.User;
import com.davidread.quizgame.R;
import com.davidread.quizgame.Utilities.DatabaseHelper;
import com.davidread.quizgame.Utilities.ResultsAdapter;

import java.util.ArrayList;

/**
 * This fragment class represents a screen with a list of quiz results of past attempts.
 */
public class ResultsFragment extends Fragment {

    /**
     * Returns a view that represents a list of quiz results.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate a layout for a list of quiz results.
        View rootView = inflater.inflate(R.layout.fragment_results, container, false);

        // Initialize recycler view and empty text view from the layout.
        RecyclerView recyclerViewResults = rootView.findViewById(R.id.recycler_view_results);
        TextView textViewRecyclerViewResultsEmpty = rootView.findViewById(R.id.text_view_recycler_view_results_empty);

        // Get array list of results and users from the database.
        DatabaseHelper db = new DatabaseHelper(getContext());
        ArrayList<Result> results = db.getAllResults();
        ArrayList<User> users = db.getAllUsers();

        // Setup results adapter for recycler view.
        ResultsAdapter resultsAdapter = new ResultsAdapter(getContext(), results, users);
        recyclerViewResults.setAdapter(resultsAdapter);

        // Setup other aspects of the recycler view.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        recyclerViewResults.setLayoutManager(layoutManager);
        recyclerViewResults.setItemAnimator(new DefaultItemAnimator());
        recyclerViewResults.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        // Change the visibility of the empty text view depending on whether there are results in the array list.
        if (results.size() > 0)
            textViewRecyclerViewResultsEmpty.setVisibility(View.GONE);
        else
            textViewRecyclerViewResultsEmpty.setVisibility(View.VISIBLE);

        return rootView;
    }
}
