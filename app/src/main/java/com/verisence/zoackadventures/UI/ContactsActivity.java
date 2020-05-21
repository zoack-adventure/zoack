package com.verisence.zoackadventures.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.verisence.zoackadventures.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class ContactsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    private DrawerLayout drawer;

    NavigationView navigationView;

    @BindView(R.id.contactUs)
    TextView contactDrawer;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    RelativeLayout web, email, phone;
//    TextView webText, emailText, phoneText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);


        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        web = findViewById(R.id.contactWeb);
        email = findViewById(R.id.contactEmail);
        phone = findViewById(R.id.contactPhone);

//        webText = findViewById(R.id.webText);
//        phoneText = findViewById(R.id.phoneText);
//        emailText = findViewById(R.id.emailText);
//
//        webText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_open_in_browser_black_24dp,0,0,0);
//        phoneText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_call_black_24dp,0,0,0);
//        emailText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email_black_24dp,0,0,0);


        View view = navigationView.inflateHeaderView(R.layout.nav_header);
        TextView navUsername = view.findViewById(R.id.nav_header_name);
        TextView navEmail = view.findViewById(R.id.nav_header_mail);
        ImageView navImage = view.findViewById(R.id.nav_header_photo);
//        setUpFireBaseAdapter();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = getInstance().getReference();

//        drawer = findViewById(R.id.drawer_layout);

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
                    String phone = "" + ds.child("phone").getValue();
                    String image = "" + ds.child("image").getValue();

                    navUsername.setText(name);
                    navEmail.setText(email);
//                    phoneTv.setText(phone);
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

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        contactDrawer.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_call_black_24dp,0,0,0);
        contactDrawer.setOnClickListener(this);
        web.setOnClickListener(this);
        email.setOnClickListener(this);
        phone.setOnClickListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_main);
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_main:
                startActivity(new Intent(ContactsActivity.this, MainActivity.class));
                break;
            case R.id.nav_profile:
                startActivity(new Intent(ContactsActivity.this, ProfileActivity.class));
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
        Intent intent = new Intent(ContactsActivity.this, LoginActivity.class);
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
    public void onClick(View v) {
        if (v==contactDrawer){
            startActivity(new Intent(ContactsActivity.this, ContactsActivity.class));
        }
        if (v==phone){
            Intent phoneIntent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:+254746079970"));
            startActivity(phoneIntent);
        }
        if (v==web){
            Intent webIntent = new Intent(Intent.ACTION_VIEW);
            webIntent.setData(Uri.parse("http://www.zoackadventures.co.ke"));
            startActivity(webIntent);
        }
        if (v==email){
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto","info@zoackadventures.co.ke", null));
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        }
    }
}
