package com.davidread.quizgame.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.davidread.quizgame.Models.Result;
import com.davidread.quizgame.Models.User;
import com.davidread.quizgame.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * This utility class defines how result objects in the array list should be adapted to be displayed
 * in a recycler view. An array list of users is also required to be passed so that full names may
 * be displayed for each result instead of their id.
 */
public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultViewHolder> {

    // Utility variables.
    private Context context;
    private ArrayList<Result> results;
    private ArrayList<User> users;

    /**
     * This class represents a holder for the views used in a single view of the recycler view.
     */
    public class ResultViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewResult;
        public TextView textViewResultDetails;

        /**
         * Constructs a view holder.
         */
        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewResult = itemView.findViewById(R.id.text_view_result);
            textViewResultDetails = itemView.findViewById(R.id.text_view_result_details);
        }
    }

    /**
     * Constructs a results adapter.
     */
    public ResultsAdapter(Context context, ArrayList<Result> results, ArrayList<User> users) {
        this.context = context;
        this.results = results;
        this.users = users;
    }

    /**
     * Called for each view holder to initialize them by inflating the layout for a single view of
     * the recycler view.
     */
    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_result, parent, false);
        return new ResultViewHolder(itemView);
    }

    /**
     * Called for each view holder to bind each to the adapter. Data about the corresponding result
     * object is passed here.
     */
    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {

        // Get corresponding result object.
        Result result = results.get(position);

        // Populate text views with the result and details about the result.
        holder.textViewResult.setText(context.getString(R.string.format_result, result.getCountQuestionsCorrect(), result.getCountTotalQuestions()));
        holder.textViewResultDetails.setText(context.getString(R.string.format_result_details, getNameFromUserId(result.getUserId()), formatTime(result.getTimestamp()), formatDate(result.getTimestamp())));
    }

    /**
     * Returns the size of the array list.
     */
    @Override
    public int getItemCount() {
        return results.size();
    }

    /**
     * Returns a user's full name given their user id. It checks the array list of users passed to
     * this class.
     */
    private String getNameFromUserId(long userId) {

        Iterator<User> iterator = users.iterator();

        while(iterator.hasNext()) {
            User user = iterator.next();
            if (user.getId() == userId)
                return context.getString(R.string.format_full_name, user.getFirstName(), user.getLastName());
        }

        return context.getString(R.string.text_view_user_unknown);
    }

    /**
     * Returns a formatted time string (hh:mm aa) given an epoch timestamp.
     */
    private String formatTime(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
        return simpleDateFormat.format(new Date(timestamp));
    }

    /**
     * Returns a formatted date string (M/d/yyyy) given an epoch timestamp.
     */
    private String formatDate(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M/d/yyyy");
        return simpleDateFormat.format(new Date(timestamp));
    }
}