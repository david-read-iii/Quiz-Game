package com.davidread.quizgame.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.davidread.quizgame.Fragments.ManageUserFragment;
import com.davidread.quizgame.Fragments.QuestionMultipleAnswersFragment;
import com.davidread.quizgame.Fragments.QuestionMultipleChoiceFragment;
import com.davidread.quizgame.Fragments.RulesFragment;
import com.davidread.quizgame.Fragments.ResultsFragment;
import com.davidread.quizgame.Models.Question;
import com.davidread.quizgame.Models.Quiz;
import com.davidread.quizgame.Models.User;
import com.davidread.quizgame.R;
import com.davidread.quizgame.Utilities.DatabaseHelper;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This activity class represents a user interface the user can access once they're logged in. It
 * has a navigation drawer interface that switches between several fragments.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Intent extra constants.
    public static final String EXTRA_LOGGED_IN_ID = "logged_in_id";

    // Activity objects.
    private DatabaseHelper db;
    private User loggedInUser;
    private DrawerLayout drawerLayout;
    private Quiz quiz;

    /**
     * Called when the activity is initially created. Inflates a layout with a custom action bar and
     * a fragment container. It also sets up database objects, custom action bar objects, navigation
     * drawer objects. Lastly, it starts a fragment.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize database helper.
        db = new DatabaseHelper(this);

        // Initialize custom action bar and use it instead of the default action bar.
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        // Initialize navigation drawer helper objects.
        drawerLayout = findViewById(R.id.drawer_layout_main);
        NavigationView navigationView = findViewById(R.id.navigation_view_main);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_menu_main_open, R.string.drawer_menu_main_closed);

        // Initialize text views in navigation drawer header.
        TextView textViewFullName = navigationView.getHeaderView(0).findViewById(R.id.text_view_drawer_header_main_name);
        TextView textViewEmail = navigationView.getHeaderView(0).findViewById(R.id.text_view_drawer_header_main_email);

        // Inflate items in navigation drawer and handle item clicks in onNavigationItemSelected().
        navigationView.inflateMenu(R.menu.drawer_menu_main);
        navigationView.setNavigationItemSelectedListener(this);

        // Add navigation drawer toggle to the action bar.
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        // Get information about the logged in user.
        Intent intent = getIntent();
        loggedInUser = db.getUser(intent.getLongExtra(EXTRA_LOGGED_IN_ID, -1));

        // Populate navigation drawer header with information about the logged in user.
        textViewFullName.setText(getString(R.string.format_full_name, loggedInUser.getFirstName(), loggedInUser.getLastName()));
        textViewEmail.setText(loggedInUser.getEmail());

        // Setup quiz questions in an array list.
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(new Question(
                "What types of Fragments are in Android?",
                "Connected, sparse",
                "Homologous, heterogeneous",
                "Synchronous, asynchronous",
                "Dynamic, static",
                false,
                false,
                false,
                true
        ));
        questions.add(new Question(
                "Please check all of the traits that make fragments useful.",
                "They are reusable",
                "You can dynamically add or remove them during runtime",
                "They can be used to build a flexible UI",
                "Fragments have the same lifecycle as their host activity",
                true,
                true,
                false,
                true
        ));
        questions.add(new Question(
                "In what version of Android was the concept of a Fragment introduced?",
                "Android 3.4",
                "Android 2.3",
                "Android 3.0",
                "Android 4.0",
                false,
                false,
                true,
                false
        ));
        questions.add(new Question(
                "What is not a benefit of using Fragment components?",
                "Fragment components simplify testing needs",
                "Fragment components greatly improve code reuse",
                "Fragment components make publication and application package management much less cumbersome",
                "Fragment components improve battery life",
                false,
                false,
                false,
                true
        ));
        questions.add(new Question(
                "An update or modification to a Fragment is performed using what?",
                "A FragmentActivity",
                "A FragmentTransaction",
                "A FragmentView",
                "A FragmentEdit",
                false,
                true,
                false,
                false
        ));

        // Initialize the quiz object with the questions in the array list.
        quiz = new Quiz(questions, loggedInUser.getId(), this);

        // Start the rules fragment by default.
        startFragment(new RulesFragment(), getString(R.string.fragment_label_rules));
    }

    /**
     * Handles navigation drawer item clicks.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // Close the navigation drawer.
        closeNavigationDrawer();

        // Get id of the clicked navigation drawer item.
        int id = item.getItemId();

        // Start the appropriate fragment given the item id.
        if (id == R.id.drawer_menu_main_rules) {
            startFragment(new RulesFragment(), getString(R.string.fragment_label_rules));
            return true;
        } else if (id == R.id.drawer_menu_main_quiz) {
            startQuestionFragment(quiz.getCurrentQuestion());
            return true;
        } else if (id == R.id.drawer_menu_main_results) {
            startFragment(new ResultsFragment(), getString(R.string.fragment_label_results));
            return true;
        } else if (id == R.id.drawer_menu_main_manage_user) {
            Bundle args = new Bundle();
            args.putLong(ManageUserFragment.EXTRA_USER_ID, loggedInUser.getId());
            startFragmentWithArgs(new ManageUserFragment(), getString(R.string.fragment_label_manage_user), args);
            return true;
        } else if (id == R.id.drawer_menu_main_log_out){
            finish();
            return true;
        }

        return false;
    }

    /**
     * Disable the hardware back button in this activity so the user cannot go back to the login
     * activity without logging out.
     */
    @Override
    public void onBackPressed() {
    }

    /**
     * Close the navigation drawer with an animation.
     */
    private void closeNavigationDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    /**
     * Starts a given fragment in the fragment container.
     *
     * @param fragment       Fragment to be started in the fragment container.
     * @param actionBarLabel Label to display in the action bar.
     */
    private void startFragment(Fragment fragment, String actionBarLabel) {
        startFragmentWithArgs(fragment, actionBarLabel, null);
    }

    /**
     * Starts a given fragment in the fragment container with the specified arguments.
     *
     * @param fragment       Fragment to be started in the fragment container.
     * @param actionBarLabel Label to display in the action bar.
     * @param args           Arguments to be passed to the fragment class.
     */
    private void startFragmentWithArgs(Fragment fragment, String actionBarLabel, Bundle args) {

        // Set action bar label.
        Objects.requireNonNull(getSupportActionBar()).setTitle(actionBarLabel);

        // Attach the arguments to the fragment.
        if (args != null)
            fragment.setArguments(args);

        // Start the specified fragment.
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_main, fragment).commit();
    }

    /**
     * Starts a question fragment in the fragment container for the specified question object.
     *
     * @param question   Question object to be displayed in fragment.
     */
    private void startQuestionFragment(Question question) {

        Fragment fragment;
        Bundle args = new Bundle();

        // Select the appropriate fragment and arguments based on the number of correct answers in the current question.
        if (question.getNumberOfCorrectAnswers() == 1) {

            fragment = new QuestionMultipleChoiceFragment();

            args.putString(QuestionMultipleChoiceFragment.EXTRA_QUESTION, question.getQuestion());
            args.putString(QuestionMultipleChoiceFragment.EXTRA_OPTION_1, question.getOption1());
            args.putString(QuestionMultipleChoiceFragment.EXTRA_OPTION_2, question.getOption2());
            args.putString(QuestionMultipleChoiceFragment.EXTRA_OPTION_3, question.getOption3());
            args.putString(QuestionMultipleChoiceFragment.EXTRA_OPTION_4, question.getOption4());
        } else {

            fragment = new QuestionMultipleAnswersFragment();

            args.putString(QuestionMultipleAnswersFragment.EXTRA_QUESTION, question.getQuestion());
            args.putString(QuestionMultipleAnswersFragment.EXTRA_OPTION_1, question.getOption1());
            args.putString(QuestionMultipleAnswersFragment.EXTRA_OPTION_2, question.getOption2());
            args.putString(QuestionMultipleAnswersFragment.EXTRA_OPTION_3, question.getOption3());
            args.putString(QuestionMultipleAnswersFragment.EXTRA_OPTION_4, question.getOption4());
        }

        // Start the selected question fragment with arguments.
        startFragmentWithArgs(
                fragment,
                getString(R.string.fragment_label_question, Integer.toString(quiz.getIndexCurrentQuestion() + 1), Integer.toString(quiz.getCountTotalQuestions())),
                args
        );
    }

    /**
     * First it has the quiz object grade the current question and increment to the next question.
     * Then, it starts a new question fragment for the new current question in the fragment
     * container. If there are no further questions in the quiz, it has the quiz object record the
     * quiz result and reset the quiz. Then, it starts a results fragment in the fragment container.
     *
     * @param userSelectionOption1  User selection for option 1 of the question object to be graded.
     * @param userSelectionOption2  User selection for option 2 of the question object to be graded.
     * @param userSelectionOption3  User selection for option 3 of the question object to be graded.
     * @param userSelectionOption4  User selection for option 4 of the question object to be graded.
     */
    public void incrementQuiz(boolean userSelectionOption1, boolean userSelectionOption2, boolean userSelectionOption3, boolean userSelectionOption4) {

        // Have quiz object grade the current question and increment to the next question.
        quiz.gradeCurrentQuestion(userSelectionOption1, userSelectionOption2, userSelectionOption3, userSelectionOption4);
        quiz.incrementCurrentQuestion();

        /* If the quiz is not complete, start a question fragment for the new current question.
         * Otherwise, have quiz object record the quiz result and reset the quiz. Then, start a
         * results fragment. */
        if (!quiz.isQuizComplete()) {
            startQuestionFragment(quiz.getCurrentQuestion());
        } else {
            quiz.recordQuizResult();
            quiz.resetQuiz();
            startFragment(new ResultsFragment(), getString(R.string.fragment_label_results));
        }
    }
}
