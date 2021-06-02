package com.davidread.quizgame.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.davidread.quizgame.R;
import com.davidread.quizgame.Utilities.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;

/**
 * This activity class represents a login screen with fields for email and password, and buttons for
 * logging in and registering.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // Activity objects.
    private DatabaseHelper db;
    private TextInputEditText textInputEditTextEmail, textInputEditTextPassword;

    /**
     * Called when activity is initially created. Inflates a login form layout, sets up XML
     * objects, sets up database objects, and sets up click listeners for the login and register
     * buttons.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Setup XML element references.
        textInputEditTextEmail = findViewById(R.id.text_field_email);
        textInputEditTextPassword = findViewById(R.id.text_field_password);
        Button buttonLogin = findViewById(R.id.button_login);
        Button buttonRegister = findViewById(R.id.button_register);

        // Setup database helper.
        db = new DatabaseHelper(this);

        // Setup click listeners for the login and register buttons.
        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
    }

    /**
     * Called when activity comes to foreground. Clears text fields and text field errors.
     */
    @Override
    protected void onResume() {
        super.onResume();
        textInputEditTextEmail.setText("");
        textInputEditTextPassword.setText("");
        textInputEditTextEmail.setError(null);
        textInputEditTextPassword.setError(null);
    }

    /**
     * Handles clicks for login and register buttons.
     */
    @Override
    public void onClick(View v) {

        int id = v.getId();

        /* For button login, it starts the main activity if a valid email/password combo is in the
         * text fields. */
        if (id == R.id.button_login) {

            boolean invalidTextFieldDetected = false;
            long userId = -1;
            String email = textInputEditTextEmail.getText().toString();
            String password = textInputEditTextPassword.getText().toString();

            // Validate email field.
            if (email.isEmpty()) {
                textInputEditTextEmail.setError(getString(R.string.toast_error_field_empty));
                invalidTextFieldDetected = true;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                textInputEditTextEmail.setError(getString(R.string.toast_error_email_field_invalid));
                invalidTextFieldDetected = true;
            }

            // Validate password field.
            if (password.isEmpty()) {
                textInputEditTextPassword.setError(getString(R.string.toast_error_field_empty));
                invalidTextFieldDetected = true;
            }

            // Check for correctness of email/password combo. If correct, start the main activity.
            if (!invalidTextFieldDetected) {

                userId = db.getUserId(email, password);

                if (userId == -1) {
                    textInputEditTextEmail.setError(getString(R.string.toast_error_login_invalid));
                    textInputEditTextPassword.setError(getString(R.string.toast_error_login_invalid));
                } else {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra(MainActivity.EXTRA_LOGGED_IN_ID, userId);
                    startActivity(intent);
                }
            }
        }

        // For the register button, it starts the register activity.
        else if (id == R.id.button_register) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Disable the hardware back button in this activity so the user cannot go back to the splash
     * screen activity.
     */
    @Override
    public void onBackPressed() {
    }
}
