package com.davidread.quizgame.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.davidread.quizgame.Activities.MainActivity;
import com.davidread.quizgame.R;
import com.davidread.quizgame.Utilities.DatabaseHelper;

/**
 * This fragment class represents a settings screen for a single logged-in user.
 */
public class ManageUserFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener, DialogInterface.OnClickListener {

    // Intent extra constants.
    public static final String EXTRA_USER_ID = "user_id";

    // Preference key constants.
    private static final String PREFERENCE_KEY_DELETE_RESULTS = "delete_results";
    private static final String PREFERENCE_KEY_DELETE_USER = "delete_user";

    // Fragment objects.
    private long userId;
    private DatabaseHelper db;
    private AlertDialog alertDialogDeleteResults;
    private AlertDialog alertDialogDeleteUser;

    /**
     * Called when the fragment is initially created. It sets up the preference screen and several
     * helper objects used to fulfill preference clicks.
     */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        // Get id of the logged in user passed from the main activity.
        Bundle args = getArguments();

        if (args != null) {
            userId = args.getLong(EXTRA_USER_ID);
        }

        // Add preferences from resource file.
        addPreferencesFromResource(R.xml.fragment_manage_user);

        // Set click listeners for each preference.
        Preference preferenceDeleteResults = findPreference(PREFERENCE_KEY_DELETE_RESULTS);
        Preference preferenceDeleteUser = findPreference(PREFERENCE_KEY_DELETE_USER);
        preferenceDeleteResults.setOnPreferenceClickListener(this);
        preferenceDeleteUser.setOnPreferenceClickListener(this);

        // Initialize the database helper.
        db = new DatabaseHelper(getContext());

        // Setup confirmation dialogs for each preference.
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setPositiveButton(getString(R.string.dialog_confirm_positive_button), this);
        builder.setNegativeButton(getString(R.string.dialog_confirm_negative_button), null);

        builder.setTitle(getString(R.string.preference_delete_results_title));
        builder.setMessage(getString(R.string.dialog_confirm_delete_results_message));
        alertDialogDeleteResults = builder.create();

        builder.setTitle(getString(R.string.preference_delete_user_title));
        builder.setMessage(getString(R.string.dialog_confirm_delete_user_message));
        alertDialogDeleteUser = builder.create();
    }

    /**
     * Handles preference view clicks. Each click shows a confirmation dialog for that preference.
     */
    @Override
    public boolean onPreferenceClick(Preference preference) {

        switch (preference.getKey()) {
            case PREFERENCE_KEY_DELETE_RESULTS:
                alertDialogDeleteResults.show();
                return true;
            case PREFERENCE_KEY_DELETE_USER:
                alertDialogDeleteUser.show();
                return true;
        }

        return false;
    }

    /**
     * Handles confirmation dialog positive button clicks.
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {

        // Deletes results for the logged in user.
        if (dialog == alertDialogDeleteResults) {
            db.deleteResultsForUser(userId);
        }

        /* Deletes results for the logged-in user, deletes the logged-in user, and logs the
         * logged-in user out of the app. */
        else if (dialog == alertDialogDeleteUser) {
            db.deleteResultsForUser(userId);
            db.deleteUser(userId);
            ((MainActivity)getActivity()).finish();
        }
    }
}
