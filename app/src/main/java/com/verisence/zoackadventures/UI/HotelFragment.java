package com.verisence.zoackadventures.UI;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

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
import com.verisence.zoackadventures.Mpesa.ApiClient;
import com.verisence.zoackadventures.Mpesa.model.AccessToken;
import com.verisence.zoackadventures.R;
import com.verisence.zoackadventures.models.Hotel;
import com.verisence.zoackadventures.models.Payment;
import com.verisence.zoackadventures.models.Transaction;
import com.verisence.zoackadventures.utils.Helpers;

import org.parceler.Parcels;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.firebase.storage.FirebaseStorage.getInstance;
import static com.verisence.zoackadventures.Constants.KSH;

public class HotelFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.hotelImageView)
    ImageView mImageLabel;
    @BindView(R.id.hotelNameTextView)
    TextView mNameLabel;
    @BindView(R.id.hotelDescTextView)
    TextView mHotelDescription;
    @BindView(R.id.phoneTextView)
    TextView mHotelPhone;
    @BindView(R.id.addressTextView)
    TextView mHotelAddress;
    @BindView(R.id.hotelPrice)
    TextView mHotelPrice;
    @BindView(R.id.bookHotel)
    Button mBookHotel;
    @BindView(R.id.favoriteBtn)
    ImageView favoriteBtn;

    AlertDialog alertDialog;

    private Hotel mHotel;

    public static TextView fromdate;
    public static TextView todate;
    public static TextView top_text;
    public static TextView top_text_two;
    public static Button startBtn;
    public static Button endBtn;
    public static Button adult_add_btn;
    public static Button adult_minus_btn;
    public static TextView adult_count;
    public static Button child_add_btn;
    public static Button child_minus_btn;
    public static TextView child_count;
    public static TextView travelers_count;
    public static TextView totalPrice;
    public static final String DATE_FORMAT = "d - M - yyyy";
    public static TextView payBtn;
    private ApiClient mApiClient;
    private ProgressDialog mProgressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String phoneNumber;
    Helpers helpers;
    private Boolean isFavorite = false;



    public static Fragment newInstance(Hotel hotel) {
        HotelFragment hotelFragment = new HotelFragment();
        Bundle args = new Bundle();
        args.putParcelable("hotel", Parcels.wrap(hotel));
        hotelFragment.setArguments(args);
        return hotelFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHotel = Parcels.unwrap(getArguments().getParcelable("hotel"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotel_list, container, false);
        ButterKnife.bind(this, view);

        Picasso.get().load(mHotel.getImageUrl()).into(mImageLabel);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference savedHotelRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_SAVED_HOTELS)
                .child(uid);


        mNameLabel.setText(mHotel.getName());
        mHotelPrice.setText("Kshs. "+Helpers.numberWithCommas((long)(mHotel.getPrice())));
        mHotelDescription.setText(mHotel.getDescription());
        mHotelPhone.setText(mHotel.getPhone());
        mHotelAddress.setText(mHotel.getAddress());
        mBookHotel.setOnClickListener(this);

        mHotelPhone.setOnClickListener(this);
        mHotelAddress.setOnClickListener(this);
        favoriteBtn.setOnClickListener(this);

        Query query = savedHotelRef;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    if(snapshot != null){
                        String favoriteHotel =""+ snapshot.child("name").getValue();
                        if(favoriteHotel.equals(mHotel.getName())){
                            favoriteBtn.setBackground(getResources().getDrawable(R.drawable.ic_star_black_24dp));
                            isFavorite = true;
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return view;
    }

    @Override
    public void onClick(View v) {

//        if (v == mViewHotels) {
//            Intent i = new Intent(getActivity(), HotelsActivity.class);
//            startActivity(i);
//            ((Activity) getActivity()).overridePendingTransition(0, 0);
//        }
        if (v==mHotelAddress){
            Double lat = mHotel.getLatitude();
            Double longitude = mHotel.getLongitude();
            Uri gmmIntentUri = Uri.parse("geo:" + lat
                    + "," + longitude
                    + "?q=(" + mHotel.getName() + ")");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }

        if (v == mHotelPhone) {
            Intent phoneIntent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:" + mHotel.getPhone()));
            startActivity(phoneIntent);
        }

        if(v == mBookHotel){
//            Toast.makeText(getContext(),String.valueOf(mHotel.getPrice()),Toast.LENGTH_LONG).show();
            final Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.booking_dialog);
//            top_text = (TextView) dialog.findViewById(R.id.top_text);
            top_text_two = (TextView) dialog.findViewById(R.id.top_text_two);
            startBtn = (Button) dialog.findViewById(R.id.start_date_btn);
            endBtn = (Button) dialog.findViewById(R.id.end_date_btn);
            fromdate = (TextView) dialog.findViewById(R.id.fromDate);
            todate = (TextView) dialog.findViewById(R.id.toDate);
            adult_add_btn = (Button)dialog.findViewById(R.id.adult_add_btn);
            adult_minus_btn = (Button) dialog.findViewById(R.id.adult_minus_btn);
            adult_count = (TextView) dialog.findViewById(R.id.adult_count);
            child_add_btn = (Button) dialog.findViewById(R.id.child_add_btn);
            child_minus_btn = (Button) dialog.findViewById(R.id.child_minus_btn);
            child_count = (TextView) dialog.findViewById(R.id.child_count);
//            travelers_count = (TextView) dialog.findViewById(R.id.traveler_count);
            totalPrice = (TextView) dialog.findViewById(R.id.total_price);
            payBtn = (TextView) dialog.findViewById(R.id.payBtn);
            final long[] price = {0};
            final int[] childNumber = {0};
            final int[] adultNumber = {0};
            NumberFormat myFormat = NumberFormat.getInstance();
            myFormat.setGroupingUsed(true);
            mProgressDialog = new ProgressDialog(getContext());
            mApiClient = new ApiClient();
            mApiClient.setIsDebug(true); //Set True to enable logging, false to disable.

            getAccessToken();

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

                        phoneNumber = "" + ds.child("phone").getValue();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showTruitonDatePickerDialog(view);
                }
            });
            endBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showToDatePickerDialog(view);
                }
            });

            adult_add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adultNumber[0] = adultNumber[0] +1;
                    adult_count.setText(travellerNumber(adultNumber[0],"adult"));
                    totalPrice.setText(  KSH + Helpers.numberWithCommas(getPrice(adultNumber[0],childNumber[0])));
                }
            });
            adult_minus_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adultNumber[0] = adultNumber[0] -1;
                    if (adultNumber[0] <= 0) {
                        adultNumber[0] = 0;
                    }
                    adult_count.setText(travellerNumber(adultNumber[0],"adult"));
                    totalPrice.setText(  KSH + helpers.numberWithCommas(getPrice(adultNumber[0],childNumber[0])));
                }
            });

            child_add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    childNumber[0] = childNumber[0] +1;
                    child_count.setText(travellerNumber(childNumber[0],"child"));
                    totalPrice.setText(  KSH + helpers.numberWithCommas(getPrice(adultNumber[0],childNumber[0])));
                }
            });
            child_minus_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    childNumber[0] = childNumber[0] -1;
                    if (childNumber[0] <= 0) {
                        childNumber[0] = 0;
                    }
                    child_count.setText(travellerNumber(childNumber[0],"child"));
                    totalPrice.setText(  KSH + helpers.numberWithCommas(getPrice(adultNumber[0],childNumber[0])));


                }
            });

            payBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Payment payment = new Payment();
                        ArrayList<Transaction> transactions = new ArrayList<>();
                        payment.setAdults((adult_count.getText().toString()));
                        payment.setChildren((child_count.getText().toString()));
                        payment.setArrivalDate(fromdate.getText().toString());
                        payment.setDepartureDate(todate.getText().toString());
                        payment.setTransactionDate(new Date().toString());
                        payment.setHotel(mHotel);
                        payment.setTransactions(transactions);
                        payment.setAmount(totalPrice.getText().toString());

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String uid = user.getUid();
                        DatabaseReference savedPaymentRef = FirebaseDatabase
                                .getInstance()
                                .getReference(Constants.FIREBASE_CHILD_TRANSACTIONS).child(uid);

                        DatabaseReference pushRef = savedPaymentRef.push();
                        String pushId = pushRef.getKey();
                        payment.setPushID(pushId);
                        pushRef.setValue(payment);
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.putExtra(Constants.NAVIGATION,Constants.PAYMENTS_PAGE);
                        startActivity(intent);
                }
            });
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.roundbcg);

            dialog.show();


        }

        if(v == favoriteBtn){

            if(!isFavorite){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                DatabaseReference savedHotelRef = FirebaseDatabase
                        .getInstance()
                        .getReference(Constants.FIREBASE_CHILD_SAVED_HOTELS)
                        .child(uid);

                DatabaseReference pushRef = savedHotelRef.push();
                String pushId = pushRef.getKey();
                mHotel.setPushID(pushId);
                pushRef.setValue(mHotel);
                favoriteBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_black_24dp));
            }



        }
    }

    public static long getDaysBetweenDates(String start, String end) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        Date startDate, endDate;
        long numberOfDays = 0;
        try {
            startDate = dateFormat.parse(start);
            endDate = dateFormat.parse(end);
            numberOfDays = getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numberOfDays;
    }
    private static long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }
    public void showTruitonDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void showToDatePickerDialog(View v) {
        DialogFragment newFragment = new ToDatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog;
            datePickerDialog = new DatePickerDialog(getActivity(),this, year,
                    month,day);

            return datePickerDialog;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        public void onDateSet(DatePicker view, int year, int month , int day) {
            fromdate.setTextColor(getResources().getColor(R.color.black));
            int finalMonth = month+1;
            fromdate.setText(day + " - " + finalMonth +" - " + year);
        }

    }

    public static class ToDatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        // Calendar startDateCalendar=Calendar.getInstance();
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            String getfromdate = fromdate.getText().toString().trim();
            String getfrom[] = getfromdate.split(" - ");
            int year, month, day;
            year = Integer.parseInt(getfrom[2]);
            month = Integer.parseInt(getfrom[1]) - 1;
            day = Integer.parseInt(getfrom[0]);
            final Calendar c = Calendar.getInstance();
            c.set(year, month , day + 1);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            int finalMonth = month+1;
            todate.setText(day + " - " + finalMonth + " - " + year);
            todate.setTextColor(getResources().getColor(R.color.black));



        }
    }
    public void getAccessToken() {
        mApiClient.setGetAccessToken(true);
        mApiClient.mpesaService().getAccessToken().enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {

                if (response.isSuccessful()) {
                    mApiClient.setAuthToken(response.body().accessToken);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {

            }
        });
    }
    public String travellerNumber(int number,String travellerAge){
        String plural = "";
        String singular = "";
        if(travellerAge.equals("adult")){
            plural = "adults";
            singular = "adult";
        }else if(travellerAge.equals("child")){
            plural = "children";
            singular = "child";
        }
        if(number == 1){
            return number + " " + singular;
        }else{
            return number + " " + plural;
        }
    }
    public Long getPrice(int adultNumber, int childNumber){
        return getDaysBetweenDates(fromdate.getText().toString(), todate.getText().toString()) * mHotel.getPrice() * adultNumber + getDaysBetweenDates(fromdate.getText().toString(), todate.getText().toString()) * mHotel.getPrice() * childNumber;
    }
}
