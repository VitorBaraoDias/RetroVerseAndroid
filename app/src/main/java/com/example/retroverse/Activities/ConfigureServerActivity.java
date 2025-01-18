package com.example.retroverse.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.retroverse.Listeners.AuthListener;
import com.example.retroverse.MainActivity;
import com.example.retroverse.MenuActivity;
import com.example.retroverse.Modals.Modal;
import com.example.retroverse.R;
import com.example.retroverse.Singleton.Singleton;
import com.google.android.material.snackbar.Snackbar;

public class ConfigureServerActivity extends AppCompatActivity {

    private EditText txtServer;
    private Button btnSaveConfiguration;
    private ImageView backMainActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_configure_menu);

        txtServer = findViewById(R.id.txtServer);
        btnSaveConfiguration = findViewById(R.id.btnSaveConfiguration);
        backMainActivity = findViewById(R.id.imgBackMainActivity);

        SharedPreferences sharedPreferences = getSharedPreferences("serverip", Context.MODE_PRIVATE);
        String savedIp = sharedPreferences.getString("serverip", null);

        if (savedIp != null) {
            txtServer.setText(savedIp);
        }

        btnSaveConfiguration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serverIp = txtServer.getText().toString();

                if (isValidIP(serverIp)) {
                    Toast.makeText(ConfigureServerActivity.this, "Server IP is valid!", Toast.LENGTH_SHORT).show();

                    SharedPreferences sharedPreferences = getSharedPreferences("serverip", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("serverip", serverIp);
                    editor.apply();

                    backMainActivity();
                } else {

                    Toast.makeText(ConfigureServerActivity.this, "Invalid Server IP!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backMainActivity();
            }
        });

    }

    //regex que valida o ip inserido
    private boolean isValidIP(String ip) {
        String ipRegex = "^((25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)$";
        return ip.matches(ipRegex);
    }


    public void backMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}