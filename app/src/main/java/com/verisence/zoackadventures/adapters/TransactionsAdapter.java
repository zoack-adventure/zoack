package com.verisence.zoackadventures.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.solver.state.HelperReference;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.verisence.zoackadventures.Constants;
import com.verisence.zoackadventures.R;
import com.verisence.zoackadventures.models.Transaction;
import com.verisence.zoackadventures.utils.Helpers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.MyViewHolder> {
    private ArrayList<Transaction> mTransactions;
    private Context context;
    private View view;
    public static final String DATE_FORMAT = "d - M - yyyy";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView amountValue;
        private TextView date;

        public MyViewHolder(View itemView) {
            super(itemView);
            amountValue = (TextView) itemView.findViewById(R.id.amountValue);
            date = (TextView) itemView.findViewById(R.id.transactionDate);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TransactionsAdapter(Context context, ArrayList<Transaction> transactions) {
        this.context = context;
        this.mTransactions = transactions;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TransactionsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_recycler_item, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if(mTransactions.get(position).getStatus().equalsIgnoreCase("success")){
            SimpleDateFormat dateformat= new SimpleDateFormat("dd-MMM-yyyy");

            String stringDate = "Transaction date: " + dateformat.format( mTransactions.get(position).getDate());

            TextView amount  = holder.amountValue;
            TextView date = holder.date;
            date.setText(stringDate);
            long floatAmount = Long.parseLong(mTransactions.get(position).getValue().split(" ")[1].split("\\.")[0]);
            String commaAmount = Helpers.numberWithCommas(floatAmount);
            amount.setText(commaAmount);

        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mTransactions.size();
    }
}