package com.verisence.zoackadventures.utils.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.verisence.zoackadventures.R;

public class CommunicationDialogs {
    private Context context;
    private Dialog communicationDialog;


    public CommunicationDialogs(Context context) {
        this.context = context;
    }

    public void init(DialogInfo dialogInfo){
        if(communicationDialog!=null && communicationDialog.isShowing()){
            communicationDialog.cancel();
        }
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.default_dialog_layout, null);
        DialogType dialogType = dialogInfo.getDialogType();
        TextView title = dialogView.findViewById(R.id.dialog_title);
        TextView info = dialogView.findViewById(R.id.dialog_info);
        View icon = dialogView.findViewById(R.id.dialog_image);
        Button actionButton = dialogView.findViewById(R.id.action_button);
        TextView actionText = dialogView.findViewById(R.id.action_text);

        switch (dialogType){
//            case SERVICE_UNAVAILABLE:
//                title.setText(context.getResources().getString(R.string.service_unavailable_title));
//                background.setBackground(context.getResources().getDrawable(R.drawable.red_icon_background));
//                icon.setBackground(context.getResources().getDrawable(R.drawable.ic_warning));
//                actionButton.setVisibility(View.GONE);
//                actionText.setText(context.getResources().getString(R.string.dismiss));
//                actionText.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        communicationDialog.cancel();
//                        if(shouldLogout){
//                        Activity activity = (Activity) context;
//                        }
//                    }
//                });
//                break;
//            case UNAUTHORIZED:
//                title.setText(context.getResources().getString(R.string.oops));
//                info.setText(context.getResources().getString(R.string.unauthorized_desc));
//                background.setBackground(context.getResources().getDrawable(R.drawable.red_icon_background));
//                icon.setBackground(context.getResources().getDrawable(R.drawable.ic_warning));
//                actionButton.setVisibility(View.GONE);
//                actionText.setText(context.getResources().getString(R.string.dismiss));
//                actionText.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        communicationDialog.cancel();
//                        Activity activity = (Activity) context;
//                    }
//                });
//                break;
            case NO_INTERNET:
                title.setText(context.getResources().getString(R.string.oops));
                info.setText(context.getResources().getString(R.string.no_internet));
                icon.setBackground(context.getResources().getDrawable(R.drawable.ic_wifi_off));
                actionButton.setVisibility(View.GONE);
                actionText.setText(context.getResources().getString(R.string.dismiss_button));
                actionText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        communicationDialog.cancel();
                    }
                });
                break;
            case SUCCESSFUL_PAYMENT:
                title.setText(context.getResources().getString(R.string.success));
                info.setText(dialogInfo.getInfoText());
                icon.setBackground(context.getResources().getDrawable(R.drawable.ic_tick));
                icon.getBackground().setColorFilter(context.getResources().getColor(R.color.zoack_green_dark),
                        PorterDuff.Mode.SRC_ATOP);
                actionButton.setVisibility(View.VISIBLE);
                actionButton.setVisibility(View.GONE);
                actionText.setText(context.getResources().getString(R.string.dismiss_button));
                actionText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        communicationDialog.cancel();
                    }
                });
                break;
            case TRANSACTION_NOT_PROCESSED:
                title.setText(context.getResources().getString(R.string.oops));
                info.setText(context.getResources().getString(R.string.not_processed));
                icon.setBackground(context.getResources().getDrawable(R.drawable.ic_warning));
                actionButton.setVisibility(View.GONE);
                actionText.setText(context.getResources().getString(R.string.dismiss_button));
                actionText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        communicationDialog.cancel();
                    }
                });
                break;


        }
        openDialog(dialogView,context);
    }
    public void openDialog(View v, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(v);
        builder.setCancelable(false);
        Activity activity = (Activity) context;
        activity.runOnUiThread(new Runnable() {
            public void run() {
                communicationDialog = builder.create();
                communicationDialog.setCancelable(false);
                communicationDialog.setCanceledOnTouchOutside(false);
                communicationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                communicationDialog.show();
            }
        });

    }

}
