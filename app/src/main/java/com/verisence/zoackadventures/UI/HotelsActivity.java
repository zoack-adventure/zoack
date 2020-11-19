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
import com.littlemango.stacklayoutmanager.StackLayoutManager;
import com.squareup.picasso.Picasso;
import com.verisence.zoackadventures.Constants;
import com.verisence.zoackadventures.R;
import com.verisence.zoackadventures.adapters.FirebaseHotelViewHolder;
import com.verisence.zoackadventures.models.Hotel;
import com.verisence.zoackadventures.zoack;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class HotelsActivity extends AppCompatActivity {


    private DatabaseReference hotelsReference;
    private FirebaseRecyclerAdapter<Hotel, FirebaseHotelViewHolder> firebaseAdapter;

    @BindView(R.id.hotelsRecyclerView)
    RecyclerView mRecyclerView;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotels);
        ButterKnife.bind(this);

        hotelsReference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_HOTELS);



        setUpFireBaseAdapter();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = getInstance().getReference();



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



    }

    private void setUpFireBaseAdapter() {
        Bundle bundle = getIntent().getExtras();
        String location = null;
        if (bundle != null) {
            location = bundle.getString("current location");
        }else{
            location = zoack.currentLoc;
        }
        Log.d("HOTELS ACTIVITY", "setUpFireBaseAdapter: "+location);
        Query query = hotelsReference.orderByChild("location").equalTo(location);
        FirebaseRecyclerOptions<Hotel> options = new FirebaseRecyclerOptions.Builder<Hotel>()
                .setQuery(query, Hotel.class)
                .build();
        firebaseAdapter = new FirebaseRecyclerAdapter<Hotel, FirebaseHotelViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FirebaseHotelViewHolder holder, int position, @NonNull Hotel hotel) {
                holder.bindHotel(hotel);
            }

            @NonNull
            @Override
            public FirebaseHotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_list_item, parent, false);
                return new FirebaseHotelViewHolder(view);
            }
        };
//        mRecyclerView.addItemDecoration(new OverlapDecoration());
        StackLayoutManager manager = new StackLayoutManager();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(manager);
//        mRecyclerView.smoothScrollToPosition(3);
        mRecyclerView.setAdapter(firebaseAdapter);
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

