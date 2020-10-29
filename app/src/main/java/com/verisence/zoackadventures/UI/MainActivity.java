package com.verisence.zoackadventures.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.verisence.zoackadventures.Constants;
import com.verisence.zoackadventures.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.fragment_container)
    FrameLayout container;

    @BindView(R.id.bottom_nav)
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        Bundle extras = getIntent().getExtras();

        if(extras==null){
            bottomNavigationView.setSelectedItemId(R.id.nav_main);
        }else {
            String navigation = extras.getString(Constants.NAVIGATION);
            if(navigation.equals(Constants.PAYMENTS_PAGE)){
                bottomNavigationView.setSelectedItemId(R.id.nav_payments);
            }
        }


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_main:
                MainFragment mainFragment = MainFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,mainFragment).commit();
                break;
            case R.id.nav_profile:
                ProfileFragment fragment = ProfileFragment.newInstance("bl","bl");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
                break;
            case R.id.nav_favorites:
                FavoritesFragment favoritesFragment = FavoritesFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,favoritesFragment).commit();
                break;
            case R.id.nav_payments:
                PaymentFragment paymentFragment = PaymentFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,paymentFragment).commit();
                break;
            case R.id.nav_logout:
                logout();
                break;
        }
        return true;
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

}

