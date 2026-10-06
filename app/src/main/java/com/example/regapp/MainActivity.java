package com.example.regapp;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText imieE, nazwiskoE, emailE, passE;
    private TextView msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        imieE = findViewById(R.id.imieE);
        nazwiskoE = findViewById(R.id.nazwiskoE);
        emailE = findViewById(R.id.emailE);
        passE = findViewById(R.id.passE);
        msg = findViewById(R.id.msg);
        Button button = findViewById(R.id.button);

        button.setOnClickListener(v -> validate());
    }

    private void validate() {
        String imie = imieE.getText().toString().trim();
        String nazwisko = nazwiskoE.getText().toString().trim();
        String email = emailE.getText().toString().trim();
        String haslo = passE.getText().toString();

        // wyczyść stare błędy
        imieE.setError(null);
        nazwiskoE.setError(null);
        emailE.setError(null);
        passE.setError(null);

        boolean ok = true;

        if (imie.isEmpty()) {
            imieE.setError("Imię nie może być puste");
            ok = false;
        }
        if (nazwisko.isEmpty()) {
            nazwiskoE.setError("Nazwisko nie może być puste");
            ok = false;
        }
        if (email.isEmpty()) {
            emailE.setError("E-mail nie może być pusty");
            ok = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailE.setError("Niepoprawny adres e-mail");
            ok = false;
        }

        if (haslo.isEmpty()) {
            passE.setError("Hasło nie może być puste");
            ok = false;
        } else {
            String blad = sprawdzHaslo(haslo);
            if (blad != null) {
                passE.setError(blad);
                ok = false;
            }
        }

        if (ok) {
            msg.setTextColor(Color.parseColor("#2E7D32"));
            imieE.setText("");
            nazwiskoE.setText("");
            emailE.setText("");
            passE.setText("");
            msg.setText("Rejestracja zakończona sukcesem!");
        } else {
            msg.setTextColor(Color.RED);
            msg.setText("Popraw błędy w formularzu");
        }
    }

    private String sprawdzHaslo(String haslo) {
        if (haslo.length() < 8) {
            return "Hasło musi mieć min. 8 znaków";
        }
        if (!haslo.matches(".*[A-Z].*")) {
            return "Hasło musi zawierać wielką literę";
        }
        if (!haslo.matches(".*[a-z].*")) {
            return "Hasło musi zawierać małą literę";
        }
        if (!haslo.matches(".*\\d.*")) {
            return "Hasło musi zawierać cyfrę";
        }
        if (!haslo.matches(".*[^A-Za-z0-9].*")) {
            return "Hasło musi zawierać znak specjalny";
        }
        return null;
    }
}