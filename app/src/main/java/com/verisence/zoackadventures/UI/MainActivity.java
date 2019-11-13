package com.verisence.zoackadventures.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.verisence.zoackadventures.Constants;
import com.verisence.zoackadventures.R;
import com.verisence.zoackadventures.adapters.FirebaseDestinationViewHolder;
import com.verisence.zoackadventures.models.Destination;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private DatabaseReference destinationsReference;

    private FirebaseRecyclerAdapter<Destination, FirebaseDestinationViewHolder> firebaseAdapter;

    @BindView(R.id.mainRecyclerView)
    RecyclerView mRecyclerView;
    NavigationView navigationView;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        View view = navigationView.inflateHeaderView(R.layout.nav_header);
        TextView navUsername = view.findViewById(R.id.nav_header_name);
        TextView navEmail = view.findViewById(R.id.nav_header_mail);
        ImageView navImage = view.findViewById(R.id.nav_header_photo);
        destinationsReference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_DESTINATIONS);
        setUpFireBaseAdapter();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = getInstance().getReference();

        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    //get data
                    String name = "" + ds.child("name").getValue();
                    String email = "" + ds.child("email").getValue();
//                    String phone = "" + ds.child("phone").getValue();
                    String image = "" + ds.child("image").getValue();
//                    String cover = "" + ds.child("cover").getValue();

                    //set data
                    navUsername.setText(name);
                    navEmail.setText(email);
//                    phoneTv.setText(phone);
                    try {
                        Picasso.get().load(image).into(navImage);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.ic_face_black_24dp).into(navImage);
                    }
//                    try {
//                        Picasso.get().load(cover).into(coverIv);
//                    } catch (Exception e) {
//                        Picasso.get().load(R.drawable.ic_add_image_foreground).into(avatarIv);
//                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        mAuthListener = firebaseAuth -> {
//          FirebaseUser user = firebaseAuth.getCurrentUser();
//            if (user != null) {
//                navUsername.setText(user.getDisplayName());
//            }
//        };

//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_main);
        }
    }

    private void setUpFireBaseAdapter() {
        FirebaseRecyclerOptions<Destination> options = new FirebaseRecyclerOptions.Builder<Destination>()
                .setQuery(destinationsReference, Destination.class)
                .build();
        firebaseAdapter = new FirebaseRecyclerAdapter<Destination, FirebaseDestinationViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FirebaseDestinationViewHolder holder, int position, @NonNull Destination destination) {
                holder.bindDestination(destination);
            }

            @NonNull
            @Override
            public FirebaseDestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.destination_list_item, parent, false);
                return new FirebaseDestinationViewHolder(view);
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(firebaseAdapter);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_main:
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                break;
            case R.id.nav_profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;
            case R.id.nav_logout:
                logout();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAdapter != null){
            firebaseAdapter.stopListening();
        }
    }
}

