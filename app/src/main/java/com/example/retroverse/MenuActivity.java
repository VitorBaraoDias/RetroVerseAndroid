package com.example.retroverse;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.retroverse.Fragments.HomeFragment;
import com.example.retroverse.Fragments.ListaFavoritosFragment;
import com.example.retroverse.Fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        fragmentManager = getSupportFragmentManager();
        navigationView = findViewById(R.id.bottomNavigationMenu);

        navigationView.setOnNavigationItemSelectedListener(this);
        carregarFragmentoInicial();
    }
    private boolean carregarFragmentoInicial() {
        Menu menu = navigationView.getMenu();
        MenuItem item = menu.getItem(0);
        item.setChecked(true);
        return onNavigationItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        if(item.getItemId() == R.id.navHome) {
            setTitle(item.getTitle());
            fragment = new HomeFragment();
        }
        else if(item.getItemId() == R.id.navHeart) {
            setTitle(item.getTitle());
            fragment = new ListaFavoritosFragment();
        }else if(item.getItemId() == R.id.navProfile) {
            setTitle(item.getTitle());
            fragment = new ProfileFragment();
        }

        if(fragment != null)
            fragmentManager.beginTransaction().replace(R.id.contentFragmentMenu ,fragment).commit();

        return true;
    }
}