package com.verisence.zoackadventures.utils.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.verisence.zoackadventures.R;
import com.verisence.zoackadventures.UI.SignUpActivity;

public class TermsDialog {

    public TermsDialog(Context context) {
        mContext = context;
    }

    private static final String ZOACK_TERMS_PREFS = "";
    private static final String TERMS_AND_CONDITIONS = "";
    Dialog termsDialog;
    Button acceptTerms;
    Switch switchAgree;
    Context mContext;
    RelativeLayout web, email, phone;


    public void showTermsPopup(boolean showSwitch) {
        termsDialog = new Dialog(mContext);
        termsDialog.setContentView(R.layout.terms_popup);
        acceptTerms = termsDialog.findViewById(R.id.btnAccept);

        acceptTerms.setBackground(mContext.getResources().getDrawable(R.drawable.round_btn_gray));


        switchAgree = termsDialog.findViewById(R.id.switchAgree);
        if(showSwitch){
            switchAgree.setVisibility(View.VISIBLE);
            acceptTerms.setEnabled(false);
            acceptTerms.setVisibility(View.VISIBLE);
        }else {
            switchAgree.setVisibility(View.GONE);
            acceptTerms.setEnabled(true);
            acceptTerms.setVisibility(View.GONE);
        }
        switchAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchAgree.isChecked()){
                    acceptTerms.setEnabled(true);
                    acceptTerms.setBackground(mContext.getResources().getDrawable(R.drawable.round_btn));
                }else {
                    acceptTerms.setEnabled(false);
                    acceptTerms.setBackground(mContext.getResources().getDrawable(R.drawable.round_btn_gray));
                }
            }
        });

        acceptTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = mContext.getSharedPreferences(ZOACK_TERMS_PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = preferences.edit();
                edit.putBoolean(TERMS_AND_CONDITIONS, true);
                edit.apply();

                termsDialog.dismiss();
            }
        });

        termsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        termsDialog.show();


    }
    public void showContactUsPopup() {
        termsDialog = new Dialog(mContext);
        termsDialog.setContentView(R.layout.contact_us_popup);

        web =  termsDialog.findViewById(R.id.contactWeb);
        email =  termsDialog.findViewById(R.id.contactEmail);
        phone =  termsDialog.findViewById(R.id.contactPhone);


        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:+254746079970"));
                mContext.startActivity(phoneIntent);
            }
        });

        web.setOnClickListener(v -> {
            Intent webIntent = new Intent(Intent.ACTION_VIEW);
            webIntent.setData(Uri.parse("http://www.zoackadventures.co.ke"));
            mContext.startActivity(webIntent);
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","info@zoackadventures.co.ke", null));
                mContext.startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });


        termsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        termsDialog.show();


    }
}
