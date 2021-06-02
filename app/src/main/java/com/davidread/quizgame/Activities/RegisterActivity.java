package com.davidread.quizgame.Activities;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.davidread.quizgame.R;
import com.davidread.quizgame.Utilities.DatabaseHelper;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

/**
 * This activity class represents a registration form. It has several data validated fields for the
 * user to fill and a button to register the user.
 */
public class RegisterActivity extends AppCompatActivity implements MaterialPickerOnPositiveButtonClickListener, View.OnClickListener {

    // Activity objects.
    private DatabaseHelper db;
    private TextInputEditText textInputEditTextFirstName, textInputEditTextLastName, textInputEditTextDateOfBirth, textInputEditTextEmail, textInputEditTextPassword, textInputEditTextConfirmPassword;
    private MaterialDatePicker materialDatePicker;
    private Button buttonRegister;

    /**
     * Called when activity is initially created. Inflates a registration form layout, sets up XML
     * references, sets up the date picker and its helper objects, and sets up click listeners for
     * the positive button of the date picker and the register button.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Setup XML element references.
        textInputEditTextFirstName = findViewById(R.id.text_field_first_name);
        textInputEditTextLastName = findViewById(R.id.text_field_last_name);
        textInputEditTextDateOfBirth = findViewById(R.id.text_field_date_of_birth);
        textInputEditTextEmail = findViewById(R.id.text_field_email);
        textInputEditTextPassword = findViewById(R.id.text_field_password);
        textInputEditTextConfirmPassword = findViewById(R.id.text_field_confirm_password);
        buttonRegister = findViewById(R.id.button_register);

        // Setup database helper.
        db = new DatabaseHelper(this);

        /* Setup calendar constraints for date picker. Use it to restrict the date picker to dates
         * before the current date. */
        CalendarConstraints.Builder calendarConstraintsBuilder = new CalendarConstraints.Builder();
        calendarConstraintsBuilder.setValidator(DateValidatorPointBackward.before(System.currentTimeMillis()));
        calendarConstraintsBuilder.setEnd(System.currentTimeMillis());

        // Setup date picker.
        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setCalendarConstraints(calendarConstraintsBuilder.build());
        materialDatePicker = materialDateBuilder.build();
        materialDatePicker.addOnPositiveButtonClickListener(this);

        // Setup click listeners for the date of birth text field and register button.
        textInputEditTextDateOfBirth.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
    }

    /**
     * Set behavior of the action bar back button as the same as the hardware back button. Set so
     * that both have the same activity transition animation.
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Handles positive button clicks from the material date picker. It copies the date selected by
     * the picker to the date of birth text field and removes the text field's error.
     */
    @Override
    public void onPositiveButtonClick(Object selection) {
        textInputEditTextDateOfBirth.setText(materialDatePicker.getHeaderText());
        textInputEditTextDateOfBirth.setError(null);
    }

    /**
     * Handles clicks for the date of birth text field and register button.
     */
    @Override
    public void onClick(View v) {

        int id = v.getId();

        // For the date of birth text field, it shows the material date picker.
        if (id == R.id.text_field_date_of_birth) {
            materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        }

        /* For the register button, inserts a new user into the database and finishes the activity
         * if valid attributes are entered in the text fields. */
        else if (id == R.id.button_register) {

            boolean invalidTextFieldDetected = false;
            String firstName = textInputEditTextFirstName.getText().toString();
            String lastName = textInputEditTextLastName.getText().toString();
            String dateOfBirth = textInputEditTextDateOfBirth.getText().toString();
            String email = textInputEditTextEmail.getText().toString();
            String password = textInputEditTextPassword.getText().toString();
            String confirmPassword = textInputEditTextConfirmPassword.getText().toString();

            // Validate first name field.
            if (firstName.length() < 3 || firstName.length() > 30) {
                textInputEditTextFirstName.setError(getString(R.string.toast_error_name_field_invalid));
                invalidTextFieldDetected = true;
            }

            // Validate last name field.
            if (lastName.length() < 3 || lastName.length() > 30) {
                textInputEditTextLastName.setError(getString(R.string.toast_error_name_field_invalid));
                invalidTextFieldDetected = true;
            }

            // Validate date of birth field.
            if (dateOfBirth.isEmpty()) {
                textInputEditTextDateOfBirth.setError(getString(R.string.toast_error_field_empty));
                invalidTextFieldDetected = true;
            }

            // Validate email field.
            if (email.isEmpty()) {
                textInputEditTextEmail.setError(getString(R.string.toast_error_field_empty));
                invalidTextFieldDetected = true;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                textInputEditTextEmail.setError(getString(R.string.toast_error_email_field_invalid));
                invalidTextFieldDetected = true;
            } else if (db.isEmailTakenByUser(email)) {
                textInputEditTextEmail.setError(getString(R.string.toast_error_email_field_taken));
                invalidTextFieldDetected = true;
            }

            // Validate password field.
            if (password.length() < 8 || password.length() > 30) {
                textInputEditTextPassword.setError(getString(R.string.toast_error_password_field_invalid));
                invalidTextFieldDetected = true;
            } else if (!password.equals(confirmPassword)) {
                textInputEditTextConfirmPassword.setError(getString(R.string.toast_error_confirm_password_field_invalid));
                invalidTextFieldDetected = true;
            }

            // If no invalid fields detected, insert a new user into the database and finish the activity.
            if (!invalidTextFieldDetected) {
                db.insertUser(firstName, lastName, dateOfBirth, email, password);
                Toast.makeText(RegisterActivity.this, R.string.toast_success_user_created, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}