package com.verisence.zoackadventures.UI;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.verisence.zoackadventures.Constants;
import com.verisence.zoackadventures.R;
import com.verisence.zoackadventures.adapters.FirebaseDestinationViewHolder;
import com.verisence.zoackadventures.models.Destination;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";




    private DatabaseReference destinationsReference;

    private FirebaseRecyclerAdapter<Destination, FirebaseDestinationViewHolder> firebaseAdapter;


    RecyclerView mRecyclerView;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    public MainFragment() {
        // Required empty public constructor
    }


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = getInstance().getReference();
        destinationsReference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_DESTINATIONS);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView  = view.findViewById(R.id.mainRecyclerView);
        setUpFireBaseAdapter();
        firebaseAdapter.startListening();
        return view;
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(firebaseAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (firebaseAdapter != null){
            firebaseAdapter.stopListening();
        }
    }


}