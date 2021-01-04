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

public class ContactsActivity extends AppCompatActivity implements  View.OnClickListener{



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



//        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);


        web.setOnClickListener(this);
        email.setOnClickListener(this);
        phone.setOnClickListener(this);



    }




    @Override
    public void onClick(View v) {

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
