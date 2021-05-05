package com.verisence.zoackadventures.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.verisence.zoackadventures.Constants;
import com.verisence.zoackadventures.R;
import com.verisence.zoackadventures.adapters.TransactionsAdapter;
import com.verisence.zoackadventures.models.Payment;
import com.verisence.zoackadventures.models.Transaction;
import com.verisence.zoackadventures.services.PaymentService;
import com.verisence.zoackadventures.utils.Helpers;
import com.verisence.zoackadventures.utils.dialogs.CommunicationDialogs;
import com.verisence.zoackadventures.utils.dialogs.DialogInfo;
import com.verisence.zoackadventures.utils.dialogs.DialogType;

import org.parceler.Parcels;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PaymentInquiryFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.hotelImageView)
    ImageView mImageLabel;
    @BindView(R.id.amountRem)
    TextView amountRemaining;

    @BindView(R.id.days)
    TextView days;
    @BindView(R.id.user)
    TextView user;
    @BindView(R.id.paymentHistory)
    TextView paymentHistory;
    @BindView(R.id.getDetails)
    Button getDetails;
    @BindView(R.id.addPaymentTwo)
    TextView addPaymentTwo;
    @BindView(R.id.transactions)
    RecyclerView transactions;


    //Details dialog elements
    TextView start_date;
    TextView end_date;
    TextView adult_count;
    TextView child_count;
    TextView total_price;
    TextView closeBtn;


    TextView payBtn;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    TransactionsAdapter adapter;
    public static final String DATE_FORMAT = "d - M - yyyy";


    AlertDialog.Builder descDialog;

    private Payment mPayment;
    private CommunicationDialogs communicationDialogs;

    String location;

    public static Fragment newInstance(Payment payment) {
        // Required empty public constructor
        PaymentInquiryFragment paymentInquiryFragment = new PaymentInquiryFragment();
        Bundle args = new Bundle();
        args.putParcelable("payment", Parcels.wrap(payment));
        paymentInquiryFragment.setArguments(args);
        return paymentInquiryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        mPayment = Parcels.unwrap(getArguments().getParcelable("payment"));

    }

    @Override
    public void onResume() {
        super.onResume();
        communicationDialogs = new CommunicationDialogs(getActivity());
        refreshTransactions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_list, container, false);
        ButterKnife.bind(this, view);
        Picasso.get().load(mPayment.getHotel().getImageUrl()).into(mImageLabel);
        amountRemaining.setText(mPayment.getAmount());
        String startdate = mPayment.getArrivalDate();
        days.setText(String.valueOf(getDaysBetweenDates(startdate)));

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();



        getDetails.setOnClickListener(this);
        addPaymentTwo.setOnClickListener(this);

        return view;
    }


    public long getDaysBetweenDates(String start) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        Date endDate = new Date();
        long numberOfDays = 0;
        try {
            Date startDate = dateFormat.parse(start);
            numberOfDays = getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numberOfDays;
    }
    private long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = startDate.getTime() - endDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onClick(View view) {
        if(view.equals(addPaymentTwo)){
            final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
            dialog.setContentView(R.layout.payment_dialog);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.roundbcg);
            TextInputLayout cash  = dialog.findViewById(R.id.amount);
            TextInputLayout number = dialog.findViewById(R.id.phoneNumber);
            payBtn = dialog.findViewById(R.id.payBtn);
            progressBar = dialog.findViewById(R.id.payment_progress);
            payButtonState(payBtn,progressBar,false);
            payBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!validatePhoneNumber(number) | !validateAmount(cash)){
                        return;
                    }else {
                        payButtonState(payBtn,progressBar,true);
                        ArrayList<Transaction> transactions = mPayment.getTransactions();
                        if(transactions == null){
                            transactions = new ArrayList<>();
                        }


                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        assert user != null;
                        String uid = user.getUid();
                        Transaction transaction = new Transaction();
                        transaction.setValue("0");
                        transaction.setStatus("pending");
                        transaction.setDate(new Date());

                        DatabaseReference savedPaymentRef = FirebaseDatabase
                                .getInstance()
                                .getReference(Constants.FIREBASE_CHILD_TRANSACTIONS).child(uid).child(mPayment.getPushID());
                        transactions.add(transaction);
                        mPayment.setTransactions(transactions);
                        mPayment.setTransactionDate(new Date().toString());
                        savedPaymentRef.setValue(mPayment);

                        PaymentService.pay(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Error "+e);
                                DialogInfo dialogInfo = Helpers.addFailureHandler(e);
                                if(dialogInfo == null){
                                    DialogInfo dialogInfoTwo  = new DialogInfo(DialogType.TRANSACTION_NOT_PROCESSED,null);
                                    communicationDialogs.init(dialogInfoTwo);
                                    payButtonState(payBtn,progressBar,false);
                                }else {
                                    dialog.cancel();
                                    payButtonState(payBtn,progressBar,false);
                                    communicationDialogs.init(dialogInfo);
                                }
                            }

                            @Override
                            public void onResponse(Call call, Response response) {
                                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Response "+response);


                            }
                        },Integer.parseInt(cash.getEditText().getText().toString().trim()),formatNumber(number.getEditText().getText().toString()),uid,mPayment.getPushID(),String.valueOf(transactions.size()-1));

                        DatabaseReference databaseref = FirebaseDatabase.getInstance().getReference()
                                .child(Constants.FIREBASE_CHILD_TRANSACTIONS)
                                .child(uid)
                                .child(mPayment.getPushID())
                                .child(Constants.FIREBASE_CHILD_TRANSACTIONS);

                        databaseref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                    if(ds.getKey().equals(String.valueOf(dataSnapshot.getChildrenCount()-1))){
                                        Transaction transactionToUpdate = ds.getValue(Transaction.class);
                                        if(transactionToUpdate != null){
                                            if(transactionToUpdate.getStatus().equalsIgnoreCase("success")) {
                                                dialog.cancel();
                                                DialogInfo dialogInfo  = new DialogInfo(DialogType.SUCCESSFUL_PAYMENT,"You have made a successful payment.");
                                                communicationDialogs.init(dialogInfo);


                                            }else if(transactionToUpdate.getStatus().equalsIgnoreCase("failed")){
                                                dialog.cancel();
                                                DialogInfo dialogInfo  = new DialogInfo(DialogType.TRANSACTION_NOT_PROCESSED,null);
//                                                CommunicationDialogs dialogs = new CommunicationDialogs(getActivity());
                                                communicationDialogs.init(dialogInfo);
                                            }
                                        }

                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }
            });

            dialog.show();
        }
        if(view.equals(getDetails)){
            final Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.details_dialog);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.roundbcg);
            start_date = dialog.findViewById(R.id.start_date);
            end_date = dialog.findViewById(R.id.end_date);
            adult_count = dialog.findViewById(R.id.adult_count);
            child_count = dialog.findViewById(R.id.child_count);
            total_price = dialog.findViewById(R.id.total_price);
            closeBtn = dialog.findViewById(R.id.closeBtn);

            start_date.setText(mPayment.getArrivalDate());
            end_date.setText(mPayment.getDepartureDate());
            adult_count.setText(mPayment.getAdults());
            child_count.setText(mPayment.getChildren());
            total_price.setText(mPayment.getAmount());



            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }
    }

    private void refreshTransactions() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        String uid = currentUser.getUid();

        DatabaseReference databaseref = FirebaseDatabase.getInstance().getReference()
                .child(Constants.FIREBASE_CHILD_TRANSACTIONS)
                .child(uid)
                .child(mPayment.getPushID())
                .child(Constants.FIREBASE_CHILD_TRANSACTIONS);
        databaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Transaction> newTransactions = new ArrayList<>();
                for(DataSnapshot ds :dataSnapshot.getChildren()){
                    Transaction transaction = ds.getValue(Transaction.class);

                    if(transaction.getStatus().equalsIgnoreCase("success")){
                        newTransactions.add(transaction);
                    }

                }
                setupTransactionsView(newTransactions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void setupTransactionsView(ArrayList<Transaction> newTransactions) {

        if(newTransactions.size() > 0){
            user.setVisibility(View.GONE);
            paymentHistory.setVisibility(View.GONE);
            transactions.setVisibility(View.VISIBLE);

            float remainingAmount = Float.parseFloat(Helpers.removeCommas(mPayment.getAmount().split(" ")[1]));
            for (Transaction t: newTransactions) {
                remainingAmount  = remainingAmount - Float.parseFloat(t.getValue().split(" ")[1]);
            }
            amountRemaining.setText(Helpers.numberWithCommas(remainingAmount));

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
            transactions.setLayoutManager(layoutManager);
            adapter = new TransactionsAdapter(getContext(),newTransactions);
            transactions.setAdapter(adapter);
        }else {
            user.setText("Hello "+currentUser.getDisplayName()+",");

        }

    }

    public boolean validatePhoneNumber(TextInputLayout number){
        String numberInput = number.getEditText().getText().toString().trim();
        if(numberInput.isEmpty()){
            number.setError("Number field can not be empty");
            return false;
        }else if(!(numberInput).matches("^(?:254|\\+254|0)?(7(?:(?:[129][0-9])|(?:0[0-8])|(4[0-1]))[0-9]{6})$")){
            number.setError("Please enter a valid Safaricom number");
            return false;
        }else {
            number.setError(null);
            return true;
        }

    }
    public String formatNumber(String number){
        String modified = "-"+number;
        if(modified.contains("-0")){
            return modified.replaceAll("-0","+254");
        }else if(modified.contains("-254")){
            return modified.replaceAll("-254","+254");
        }else if(modified.contains("-+254")){
            return modified;
        }
        return null;
    }
    public boolean validateAmount(TextInputLayout amount){
        String amountInput = amount.getEditText().getText().toString().trim();
        if(amountInput.isEmpty()){
            amount.setError("Amount field can not be empty");
            return false;
        }else if(Integer.parseInt(amountInput) < 100){
            amount.setError("Please enter a value greater than Ksh 100");
            return false;
        }else {
            amount.setError(null);
            return true;
        }

    }

    public void payButtonState(TextView button, ProgressBar progressBar, boolean shouldLoad){
        if(shouldLoad){
            progressBar.setVisibility(View.VISIBLE);
            button.setAlpha((float) 0.5);
            button.setText("");
        }else {
            progressBar.setVisibility(View.INVISIBLE);
            button.setAlpha((float) 1.0);
            button.setText(getContext().getResources().getString(R.string.pay));
        }
    }
}
