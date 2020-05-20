package com.verisence.zoackadventures.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.verisence.zoackadventures.adapters.FirebasePaymentViewHolder;
import com.verisence.zoackadventures.models.Payment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class PaymentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    private DrawerLayout drawer;
    NavigationView navigationView;
    private DatabaseReference paymentReference;

    private FirebaseRecyclerAdapter<Payment, FirebasePaymentViewHolder> firebaseRecyclerAdapter;

    @BindView(R.id.hotelsRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.contactDrawer)
    TextView contactDrawer;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);

        user = FirebaseAuth.getInstance().getCurrentUser();

        paymentReference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_TRANSACTIONS).child(user.getUid());
        setUpFireBaseAdapter();
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        View view = navigationView.inflateHeaderView(R.layout.nav_header);
        TextView navUsername = view.findViewById(R.id.nav_header_name);
        TextView navEmail = view.findViewById(R.id.nav_header_mail);
        ImageView navImage = view.findViewById(R.id.nav_header_photo);



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
                    String image = "" + ds.child("image").getValue();

                    navUsername.setText(name);
                    navEmail.setText(email);
                    try {
                        Picasso.get().load(image).into(navImage);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.ic_face_black_24dp).into(navImage);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        navigationView.setNavigationItemSelectedListener(this);

        contactDrawer.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_call_black_24dp,0,0,0);
        contactDrawer.setOnClickListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getColor(R.color.black));
        toggle.syncState();

        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_main);
        }
    }

    private void setUpFireBaseAdapter() {

        FirebaseRecyclerOptions<Payment> options = new FirebaseRecyclerOptions.Builder<Payment>()
                .setQuery(paymentReference, Payment.class)
                .build();
        System.out.println(">>>>>>>>>>>>>>>>>>>>> I AM OUTSIDE HERE");
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Payment, FirebasePaymentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FirebasePaymentViewHolder holder, int position, @NonNull Payment payment) {
                System.out.println(">>>>>>>>>>>>>>>>>>>>> I AM HERE");
                holder.bindTransaction(payment);
            }

            @NonNull
            @Override
            public FirebasePaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_item, parent, false);
                return new FirebasePaymentViewHolder(view);
            }
        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
