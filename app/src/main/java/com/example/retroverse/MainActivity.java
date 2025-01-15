package com.example.retroverse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.retroverse.AuthActivities.CreateAccountActivity;
import com.example.retroverse.AuthActivities.LoginActivity;

public class MainActivity extends AppCompatActivity {


    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isTokenValid(this)){
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void openLoginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void openCreateAccountActivity(View view) {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }
    public boolean isTokenValid(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("token", Context.MODE_PRIVATE);

        String token = sharedPreferences.getString("token", null);

        return token != null && !token.isEmpty();
    }
}