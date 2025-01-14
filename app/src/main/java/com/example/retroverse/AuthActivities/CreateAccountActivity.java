package com.example.retroverse.AuthActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.retroverse.Listeners.AuthCreatAccountListener;
import com.example.retroverse.MainActivity;
import com.example.retroverse.Modals.Modal;
import com.example.retroverse.R;
import com.example.retroverse.Singleton.Singleton;
import com.google.android.material.snackbar.Snackbar;

public class CreateAccountActivity extends AppCompatActivity implements AuthCreatAccountListener {

    private EditText txtUsername, txtEmail, txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);

        txtUsername = findViewById(R.id.txtUsernameCreatAccount);
        txtEmail = findViewById(R.id.txtPasswordCreateAccount);
        txtPassword = findViewById(R.id.txtPasswordCreateAccount);

        Singleton.getInstance(this).setCreatAccountListener(this);

    }

    public void clickCreatAccount(View view) {
        String username = txtUsername.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        if (!username.isEmpty() && !email.isEmpty() &&  !password.isEmpty()) {
            Modal.showDialogProgress(this);

            Singleton.getInstance(this).createAccountAPI(username, email, password, this);
        }
    }

    @Override
    public void onValidateCreatAccount(String token, Context context) {
        Modal.hideLoading();
        Snackbar.make(this.getCurrentFocus(), "Conta Realizada", Snackbar.LENGTH_SHORT).show();

        SharedPreferences sharedPreferences = getSharedPreferences("token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
        finish();
    }

    public void backMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}