package com.example.retroverse;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.retroverse.Fragments.CollectionFragment;
import com.example.retroverse.Fragments.HomeFragment;
import com.example.retroverse.Fragments.ListaFavoritosFragment;
import com.example.retroverse.Fragments.ProfileFragment;
import com.example.retroverse.Listeners.NavigationListener;
import com.example.retroverse.Singleton.Singleton;
import com.example.retroverse.Utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationListener {

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

        carregarInformacoesApi();
    }
    private boolean carregarFragmentoInicial() {
        Menu menu = navigationView.getMenu();
        MenuItem item = menu.getItem(0);
        item.setChecked(true);
        return onNavigationItemSelected(item);
    }


    private void carregarInformacoesApi(){
        Singleton.getInstance(this).getPerfilAPI(Utils.getToken(this), this);

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
        } else if(item.getItemId() == R.id.navCollection) {
            setTitle(item.getTitle());
            fragment = new CollectionFragment(); }

        if (fragment != null) {


            fragmentManager.beginTransaction()
                    .replace(R.id.contentFragmentMenu, fragment)
                    .commit();
        }

        return true;
    }

    @Override
    public void navigateTo(int menuItemId) {
        MenuItem menuItem = navigationView.getMenu().findItem(menuItemId);
        if (menuItem != null) {
            menuItem.setChecked(true);
            onNavigationItemSelected(menuItem);
        }
    }
}