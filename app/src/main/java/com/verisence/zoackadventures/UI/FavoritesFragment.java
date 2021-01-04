package com.verisence.zoackadventures.UI;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.StorageReference;
import com.littlemango.stacklayoutmanager.StackLayoutManager;
import com.verisence.zoackadventures.Constants;
import com.verisence.zoackadventures.R;
import com.verisence.zoackadventures.adapters.FirebaseHotelFavoritesViewHolder;
import com.verisence.zoackadventures.adapters.FirebaseHotelViewHolder;
import com.verisence.zoackadventures.models.Hotel;
import com.verisence.zoackadventures.zoack;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment {

    private DatabaseReference hotelsReference;
    private FirebaseRecyclerAdapter<Hotel, FirebaseHotelFavoritesViewHolder> firebaseAdapter;

    @BindView(R.id.hotelsRecyclerView)
    RecyclerView mRecyclerView;
  

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    public FavoritesFragment() {
        // Required empty public constructor
    }

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        ButterKnife.bind(this,view);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        hotelsReference = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_SAVED_HOTELS)
                .child(uid);





        setUpFireBaseAdapter();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = getInstance().getReference();


        return view;
    }



    private void setUpFireBaseAdapter() {
        Query query = hotelsReference;
        FirebaseRecyclerOptions<Hotel> options = new FirebaseRecyclerOptions.Builder<Hotel>()
                .setQuery(query, Hotel.class)
                .build();
        firebaseAdapter = new FirebaseRecyclerAdapter<Hotel, FirebaseHotelFavoritesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FirebaseHotelFavoritesViewHolder holder, int position, @NonNull Hotel hotel) {
                holder.bindHotel(hotel);
            }

            @NonNull
            @Override
            public FirebaseHotelFavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.destination_list_item, parent, false);
                return new FirebaseHotelFavoritesViewHolder(view);
            }
        };
//        mRecyclerView.addItemDecoration(new OverlapDecoration());
//        StackLayoutManager manager = new StackLayoutManager();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.smoothScrollToPosition(3);
        mRecyclerView.setAdapter(firebaseAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (firebaseAdapter != null){
            firebaseAdapter.stopListening();
        }
    }
}