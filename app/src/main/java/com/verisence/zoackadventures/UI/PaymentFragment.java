package com.verisence.zoackadventures.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.verisence.zoackadventures.BuildConfig;
import com.verisence.zoackadventures.Constants;
import com.verisence.zoackadventures.R;
import com.verisence.zoackadventures.adapters.TransactionsAdapter;
import com.verisence.zoackadventures.models.Destination;
import com.verisence.zoackadventures.models.Payment;
import com.verisence.zoackadventures.models.Transaction;
import com.verisence.zoackadventures.services.PaymentService;
import com.verisence.zoackadventures.utils.DotsIndicatorDecoration;
import com.verisence.zoackadventures.utils.Helpers;

import org.parceler.Parcels;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PaymentFragment extends Fragment implements View.OnClickListener {
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
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    TransactionsAdapter adapter;
    public static final String DATE_FORMAT = "d - M - yyyy";


    AlertDialog.Builder descDialog;

    private Payment mPayment;

    String location;

    public static Fragment newInstance(Payment payment) {
        // Required empty public constructor
        PaymentFragment destinationFragment = new PaymentFragment();
        Bundle args = new Bundle();
        args.putParcelable("payment", Parcels.wrap(payment));
        destinationFragment.setArguments(args);
        return destinationFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPayment = Parcels.unwrap(getArguments().getParcelable("payment"));
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
//        AfricasTalking.initialize("sandbox", BuildConfig.AFRICA_TALKING_API_KEY);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        if(mPayment.getTransactions() != null && mPayment.getTransactions().size() > 0){
            user.setVisibility(View.GONE);
            paymentHistory.setVisibility(View.GONE);
            transactions.setVisibility(View.VISIBLE);

            ArrayList<Transaction> transactionsList = mPayment.getTransactions();
            int remainingAmount = Integer.parseInt(Helpers.removeCommas(mPayment.getAmount().split(" ")[1]));
            for (Transaction t: transactionsList) {
               remainingAmount  = remainingAmount - t.getAmount();
            }
            amountRemaining.setText(String.valueOf(remainingAmount));
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
            transactions.setLayoutManager(layoutManager);
            adapter = new TransactionsAdapter(getContext(),transactionsList);
            transactions.setAdapter(adapter);
        }else {
            user.setText("Hello "+currentUser.getDisplayName()+",");
        }


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
            final Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.payment_dialog);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.roundbcg);
            TextInputLayout cash  = dialog.findViewById(R.id.amount);
            TextInputLayout number = dialog.findViewById(R.id.phoneNumber);
            payBtn = dialog.findViewById(R.id.payBtn);
            payBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!validatePhoneNumber(number) | !validateAmount(cash)){
                        return;
                    }else {
                        ArrayList<Transaction> transactions = mPayment.getTransactions();
                        if(transactions == null){
                            transactions = new ArrayList<>();
                        }
//                        PaymentService payment = AfricasTalking.getService(AfricasTalking.SERVICE_PAYMENT);
//                        /* Set the name of your Africa's Talking payment product */
//                        String productName = "SampleProduct";
//
//                        /* Set the phone number you want to send to in international format */
//                        String phoneNumber = "+254798549950";
//
//                        /* Set The 3-Letter ISO currency code and the checkout amount */
//                        String currencyCode = "KES";
//                        float amount = 100;
//                        HashMap<String, String> metadata = new HashMap<String, String>();
//                        metadata.put("someKey", "someValue");
//                        try {
//                            CheckoutResponse response = payment.mobileCheckout(
//                                    productName, phoneNumber, currencyCode, metadata);
//                            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+response.toString());
//
//                        } catch(Exception ex) {
//                            ex.printStackTrace();
//                        }
                        PaymentService.pay(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Error "+e);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Response "+response);
                            }
                        },100,"+254798549950");

                        Transaction transaction = new Transaction();
                        transaction.setAmount(Integer.parseInt(cash.getEditText().getText().toString()));
                        transaction.setDate(new Date());
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String uid = user.getUid();
                        DatabaseReference savedPaymentRef = FirebaseDatabase
                                .getInstance()
                                .getReference(Constants.FIREBASE_CHILD_TRANSACTIONS).child(uid).child(mPayment.getPushID());
                        transactions.add(transaction);
                        mPayment.setTransactions(transactions);
                        mPayment.setTransactionDate(new Date().toString());
                        savedPaymentRef.setValue(mPayment);

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
}
