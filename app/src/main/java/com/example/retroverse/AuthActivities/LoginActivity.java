package com.example.retroverse.AuthActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.retroverse.Listeners.AuthListener;
import com.example.retroverse.MainActivity;
import com.example.retroverse.MenuActivity;
import com.example.retroverse.Modals.Modal;
import com.example.retroverse.R;
import com.example.retroverse.Singleton.Singleton;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity implements AuthListener {

    private EditText txtUsername, txtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        txtUsername = findViewById(R.id.txtServer);
        txtPassword = findViewById(R.id.txtPassword);



        Singleton.getInstance(this).setLoginListener(this);

    }

    public void backMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void clickLogin(View view) {
        String username = txtUsername.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();


        if (!username.isEmpty() && !password.isEmpty()) {
            Modal.showDialogProgress(this);

            Singleton.getInstance(LoginActivity.this).loginAPI(username, password, this);
        }
    }
    @Override
    public void onValidateLogin(final String token, final Context context) {
        // Armazenar token e email no Shared Preferences

        Modal.hideLoading();
        Snackbar.make(this.getCurrentFocus(), "Login Realizado", Snackbar.LENGTH_SHORT).show();

        SharedPreferences sharedPreferences = getSharedPreferences("token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();

        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();

    }
    // Método para mostrar o loading

}