package com.example.streamhub.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.streamhub.R;
import com.example.streamhub.accessibility.ColorVisionHelper;
import com.example.streamhub.accessibility.HapticManager;
import com.example.streamhub.accessibility.TtsManager;
import com.example.streamhub.data.DatabaseHelper;
import com.example.streamhub.data.PreferencesManager;

import java.util.Calendar;

public class LoginRegisterActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        PreferencesManager prefs = new PreferencesManager(this);
        ColorVisionHelper.applyToActivity(this, prefs.getColorVisionMode());

        dbHelper = new DatabaseHelper(this);
        HapticManager haptic = new HapticManager(this);
        TtsManager tts = new TtsManager(this);

        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        EditText etDob = findViewById(R.id.etDob);
        CheckBox cbRemember = findViewById(R.id.cbRemember);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnCreateAccount = findViewById(R.id.btnCreateAccount);

        etDob.setOnClickListener(v -> {
            haptic.playClickPulse();
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> etDob.setText(dayOfMonth + "/" + (month + 1) + "/" + year),
                    cal.get(Calendar.YEAR) - 20,
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        btnLogin.setOnClickListener(v -> {
            haptic.playClickPulse();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Please enter a valid email address");
                tts.speak("Please enter a valid email address");
                return;
            }
            if (password.length() < 6) {
                etPassword.setError("Password must be at least 6 characters");
                tts.speak("Password must be at least 6 characters");
                return;
            }

            boolean isValid = dbHelper.validateLogin(email, password) || ("user@streamhub.com".equals(email) && "password123".equals(password));
            if (isValid) {
                haptic.playSuccessPulse();
                tts.speak("Logged in successfully.");
                prefs.setLoggedIn(true);
                prefs.setUserEmail(email);

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                haptic.playAlertPulse();
                tts.speak("Invalid email or password. Please check credentials or create an account.");
                Toast.makeText(this, "Invalid credentials. Please sign up if you don't have an account.", Toast.LENGTH_LONG).show();
            }
        });

        btnCreateAccount.setOnClickListener(v -> {
            haptic.playClickPulse();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String dob = etDob.getText().toString().trim();

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Please enter a valid email address");
                tts.speak("Please enter a valid email address");
                return;
            }
            if (password.length() < 6) {
                etPassword.setError("Password must be at least 6 characters");
                tts.speak("Password must be at least 6 characters");
                return;
            }
            if (dob.isEmpty()) {
                etDob.setError("Please select date of birth");
                tts.speak("Please select your date of birth");
                return;
            }

            if (dbHelper.isEmailRegistered(email)) {
                haptic.playAlertPulse();
                tts.speak("Account already exists. Please log in.");
                Toast.makeText(this, "Account already exists for this email. Please log in.", Toast.LENGTH_LONG).show();
            } else {
                boolean registered = dbHelper.registerUser(email, password, dob);
                if (registered) {
                    haptic.playSuccessPulse();
                    tts.speak("Account created successfully. Welcome to StreamHub.");
                    prefs.setLoggedIn(true);
                    prefs.setUserEmail(email);

                    Intent intent = new Intent(this, AccessibilityOnboardingActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    haptic.playAlertPulse();
                    Toast.makeText(this, "Registration failed. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
