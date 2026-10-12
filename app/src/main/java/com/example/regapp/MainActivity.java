package com.example.regapp;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private EditText nameE, surnameE, emailE, passE;
    private TextView msg, lastUser;
    private String FILE_NAME = "data_base.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        nameE = findViewById(R.id.nameE);
        surnameE = findViewById(R.id.surnameE);
        emailE = findViewById(R.id.emailE);
        passE = findViewById(R.id.passE);
        msg = findViewById(R.id.msg);
        lastUser = findViewById(R.id.lastUser);
        Button button = findViewById(R.id.button);
        Button loadUser = findViewById(R.id.loadUser);

        button.setOnClickListener(v -> validate());
        loadUser.setOnClickListener(v -> loadNote());
    }

    private void validate() {
        String name = nameE.getText().toString().trim();
        String surname = surnameE.getText().toString().trim();
        String email = emailE.getText().toString().trim();
        String password = passE.getText().toString();

        // wyczyść stare błędy
        nameE.setError(null);
        surnameE.setError(null);
        emailE.setError(null);
        passE.setError(null);

        boolean ok = true;

        if (checkValues(name)) {
            nameE.setError("Imię nie może być puste");
            ok = false;
        }
        if (checkValues(surname)) {
            surnameE.setError("surname nie może być puste");
            ok = false;
        }
        if (checkValues(email)) {
            emailE.setError("E-mail nie może być pusty");
            ok = false;
        } else if (checkEmails(email)) {
            emailE.setError("Niepoprawny adres e-mail");
            ok = false;
        }

        if (checkValues(password)) {
            passE.setError("Hasło nie może być puste");
            ok = false;
        } else {
            String blad = checkPassword(password);
            if (blad != null) {
                passE.setError(blad);
                ok = false;
            }
        }

        if (ok) {
            msg.setTextColor(Color.parseColor("#2E7D32"));
            nameE.setText("");
            surnameE.setText("");
            emailE.setText("");
            passE.setText("");
            msg.setText("Rejestracja zakończona sukcesem!");
            saveToFile(new String[]{name, surname,email,password});
        } else {
            msg.setTextColor(Color.RED);
            msg.setText("Popraw błędy w formularzu");
        }
    }
    private boolean checkValues (String text){
        return text.isEmpty();
    }
    private boolean checkEmails(String text){
        return !Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }
    private String checkPassword(String password) {
        if (password.length() < 8) {
            return "Hasło musi mieć min. 8 znaków";
        }
        if (!password.matches(".*[A-Z].*")) {
            return "Hasło musi zawierać wielką literę";
        }
        if (!password.matches(".*[a-z].*")) {
            return "Hasło musi zawierać małą literę";
        }
        if (!password.matches(".*\\d.*")) {
            return "Hasło musi zawierać cyfrę";
        }
        if (!password.matches(".*[^A-Za-z0-9].*")) {
            return "Hasło musi zawierać znak specjalny";
        }
        return null;
    }
    private void saveToFile(String[] args) {
        FileOutputStream fos = null;

        try {
            // Otwórz strumień wyjściowy do pliku w pamięci wewnętrznej
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            String text = "Imie: "+args[0]+"\n"
                    +"Nazwisko: "+args[1]+"\n"
                    +"Email: "+args[2]+"\n"
                    +"Haslo: "+args[3]+"\n";
            // Zapisz tekst jako tablicę bajtów
            fos.write(text.getBytes());

            Toast.makeText(this, "Zapisano do pliku " + FILE_NAME, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Błąd zapisu!", Toast.LENGTH_SHORT).show();
        } finally {
            if (fos != null) {
                try {
                    fos.close(); // Zawsze zamykaj strumień!
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void loadNote() {
        FileInputStream fis = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            // Otwórz strumień wejściowy z pliku w pamięci wewnętrznej
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            lastUser.setText(stringBuilder.toString());
            Toast.makeText(this, "Wczytano z pliku " + FILE_NAME, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Błąd odczytu lub plik nie istnieje!", Toast.LENGTH_SHORT).show();
        } finally {
            if (fis != null) {
                try {
                    fis.close(); // Zawsze zamykaj strumień!
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}