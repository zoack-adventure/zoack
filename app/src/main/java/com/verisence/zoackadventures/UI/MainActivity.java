package com.verisence.zoackadventures.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.squareup.picasso.Picasso;
import com.verisence.zoackadventures.Constants;
import com.verisence.zoackadventures.R;
import com.verisence.zoackadventures.utils.dialogs.TermsDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.fragment_container)
    FrameLayout container;

    @BindView(R.id.bottom_nav)
    BottomNavigationView bottomNavigationView;

    @BindView(R.id.profileImg)
    ImageView profileImg;

    private ActionBar actionBar;
    private MenuItem selectedMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        actionBar = getSupportActionBar();
        AppCenter.start(getApplication(), "be98ca16-5e8f-4eea-8201-974fc32d64bc",
                Analytics.class, Crashes.class);


        Bundle extras = getIntent().getExtras();

        if(extras==null){
            bottomNavigationView.setSelectedItemId(R.id.nav_main);
        }else {
            String navigation = extras.getString(Constants.NAVIGATION);
            if(navigation.equals(Constants.PAYMENTS_PAGE)){
                bottomNavigationView.setSelectedItemId(R.id.nav_payments);
            }
        }

        setupProfileInfo();


    }
    private void setupProfileInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    String image = ds.getKey().equals("image") ? ds.getValue().toString() : "";


                    if(!image.isEmpty()){
                        Glide.with(MainActivity.this)
                                .load(image)
                                .centerCrop()
                                .circleCrop()
                                .into(profileImg);

                    }



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        selectedMenuItem = item;
        switch (item.getItemId()){
            case R.id.nav_main:
                MainFragment mainFragment = MainFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,mainFragment).commit();
                actionBar.setTitle("Destinations");
                break;
            case R.id.nav_profile:
                ProfileFragment fragment = ProfileFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
                actionBar.setTitle("Your Profile");
                break;
            case R.id.nav_favorites:
                FavoritesFragment favoritesFragment = FavoritesFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,favoritesFragment).commit();
                actionBar.setTitle("Favorites");
                break;
            case R.id.nav_payments:
                PaymentFragment paymentFragment = PaymentFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,paymentFragment).commit();
                actionBar.setTitle("Payments");
                break;
        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        this.onNavigationItemSelected(selectedMenuItem);
    }

}

