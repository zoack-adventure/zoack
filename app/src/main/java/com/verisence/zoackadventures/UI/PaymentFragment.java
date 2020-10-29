package com.verisence.zoackadventures.UI;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import com.verisence.zoackadventures.Constants;
import com.verisence.zoackadventures.R;
import com.verisence.zoackadventures.adapters.FirebasePaymentViewHolder;
import com.verisence.zoackadventures.models.Payment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentFragment extends Fragment {


    private DatabaseReference paymentsReference;
    private FirebaseRecyclerAdapter<Payment, FirebasePaymentViewHolder> firebaseAdapter;

    @BindView(R.id.hotelsRecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.errorRelativeLayout)
    RelativeLayout errorRelativeLayout;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    public PaymentFragment() {
        // Required empty public constructor
    }


    public static PaymentFragment newInstance() {
        return new PaymentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        ButterKnife.bind(this,view);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        paymentsReference = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_TRANSACTIONS)
                .child(uid);



        setUpFireBaseAdapter();
        setUpUI();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = getInstance().getReference();
        return view;
    }

    private void setUpUI() {
        paymentsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    errorRelativeLayout.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }else {
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    errorRelativeLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUpFireBaseAdapter() {


        Query query = paymentsReference;
        FirebaseRecyclerOptions<Payment> options = new FirebaseRecyclerOptions.Builder<Payment>()
                .setQuery(query, Payment.class)
                .build();
        firebaseAdapter = new FirebaseRecyclerAdapter<Payment, FirebasePaymentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FirebasePaymentViewHolder holder, int position, @NonNull Payment payment) {
                holder.bindTransaction(payment);
            }

            @NonNull
            @Override
            public FirebasePaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_item, parent, false);
                return new FirebasePaymentViewHolder(view);
            }
        };
//        mRecyclerView.addItemDecoration(new OverlapDecoration());
        StackLayoutManager manager = new StackLayoutManager();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(manager);
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