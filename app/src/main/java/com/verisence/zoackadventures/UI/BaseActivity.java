package com.verisence.zoackadventures.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.verisence.zoackadventures.R;
import com.verisence.zoackadventures.utils.dialogs.TermsDialog;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.more_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_terms:
                TermsDialog termsDialog = new TermsDialog(this);
                termsDialog.showTermsPopup(false);
                break;
            case R.id.nav_contact_us:
                TermsDialog contactUsDialog = new TermsDialog(this);
                contactUsDialog.showContactUsPopup();
                break;
            case R.id.nav_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you would like to log out?")
                        .setTitle("Logout Confirmation")
                        .setPositiveButton("Yes, I'm Sure", (DialogInterface.OnClickListener) (v,a) -> logout())
                        .setNegativeButton("No", (DialogInterface.OnClickListener) (v,a) -> builder.create().cancel())
                        .create().show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }
}
