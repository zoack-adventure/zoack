package com.verisence.zoackadventures.UI;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.verisence.zoackadventures.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = SignUpActivity.class.getSimpleName();
    private static final String ZOACK_TERMS_PREFS = "";
    private static final String TERMS_AND_CONDITIONS = "";
    private FirebaseAuth.AuthStateListener authStateListener;
    private ProgressDialog mAuthProgressDialog;
    private String phone;
    private FirebaseAuth auth;

    @BindView(R.id.signPhone)
    EditText editPhone;
    @BindView(R.id.signEmail) EditText editEmail;
    @BindView(R.id.signPass) EditText editPassW;
    @BindView(R.id.signPassC) EditText editConfirmPassW;
    @BindView(R.id.signButton)
    Button signUpButton;
    @BindView(R.id.linkLog)
    TextView haveAccount;
    Dialog termsDialog;
    Button acceptTerms;
    CheckBox checkBoxAgree;
    Switch switchAgree;


    private Bundle savedInstanceState;

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(ZOACK_TERMS_PREFS, Context.MODE_PRIVATE);

        if (!sharedPreferences.getBoolean(TERMS_AND_CONDITIONS, false)){
            ShowTermsPopup();
        }

    }

    private void ShowTermsPopup() {
        termsDialog.setContentView(R.layout.terms_popup);
        acceptTerms = termsDialog.findViewById(R.id.btnAccept);
        acceptTerms.setEnabled(false);
        acceptTerms.setBackground(this.getResources().getDrawable(R.drawable.round_btn_gray));

//        checkBoxAgree = termsDialog.findViewById(R.id.checkboxAgree);
        switchAgree = termsDialog.findViewById(R.id.switchAgree);

//        checkBoxAgree.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (checkBoxAgree.isChecked()){
//                    acceptTerms.setEnabled(true);
//                    acceptTerms.setBackground(SignUpActivity.this.getResources().getDrawable(R.drawable.round_btn));
//                }else {
//                    acceptTerms.setEnabled(false);
//                    acceptTerms.setBackground(SignUpActivity.this.getResources().getDrawable(R.drawable.round_btn_gray));
//                }
//            }
//        });

        switchAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchAgree.isChecked()){
                    acceptTerms.setEnabled(true);
                    acceptTerms.setBackground(SignUpActivity.this.getResources().getDrawable(R.drawable.round_btn));
                }else {
                    acceptTerms.setEnabled(false);
                    acceptTerms.setBackground(SignUpActivity.this.getResources().getDrawable(R.drawable.round_btn_gray));
                }
            }
        });

        acceptTerms.setOnClickListener(this);

        termsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        termsDialog.show();


    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener!=null){
            auth.removeAuthStateListener(authStateListener);
        }
    }

    private boolean isValidEmail(String email) {
        boolean isGoodEmail =
                (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            editEmail.setError("Please enter a valid email address");
            return false;
        }
        return isGoodEmail;
    }

    private boolean isValidName(String name) {
        if (name.equals("")) {
            editPhone.setError("Please enter your phone number");
            return false;
        }
        return true;
    }

    private boolean isValidPassword(String password, String confirmPassword) {
        if (password.length() < 6) {
            editPassW.setError("Please create a password containing at least 6 characters");
            return false;
        } else if (!password.equals(confirmPassword)) {
            editConfirmPassW.setError("Passwords do not match");
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        termsDialog = new Dialog(this);
        termsDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        createAuthStateListener();
        signUpButton.setOnClickListener(this);

        createAuthProgressDialog();

        haveAccount.setOnClickListener(this);

        editPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    view.setBackgroundResource(R.drawable.focus_border_style);
                }else{
                    view.setBackgroundResource(R.drawable.ricle_text);
                }
            }
        });

        editEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    view.setBackgroundResource(R.drawable.focus_border_style);
                }else{
                    view.setBackgroundResource(R.drawable.ricle_text);
                }
            }
        });

        editPassW.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    view.setBackgroundResource(R.drawable.focus_border_style);
                }else{
                    view.setBackgroundResource(R.drawable.ricle_text);
                }
            }
        });

        editConfirmPassW.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    view.setBackgroundResource(R.drawable.focus_border_style);
                }else{
                    view.setBackgroundResource(R.drawable.ricle_text);
                }
            }
        });
    }

    private void createAuthProgressDialog() {
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("Authenticating with Firebase...");
        mAuthProgressDialog.setCancelable(false);
    }

    private void createAuthStateListener() {
        authStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }

        };
    }

    @Override
    public void onClick(View v) {
        if (v == haveAccount){

            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        if (v == signUpButton){
            createNewUser();
            SharedPreferences sharedPreferences = getSharedPreferences(ZOACK_TERMS_PREFS, Context.MODE_PRIVATE);
        }
        if (v == acceptTerms){
            SharedPreferences preferences = SignUpActivity.this.getSharedPreferences(ZOACK_TERMS_PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean(TERMS_AND_CONDITIONS, true);
            edit.apply();

            termsDialog.dismiss();
        }
    }

    private void createNewUser() {
        phone = editPhone.getText().toString().trim();
        final String name = editPhone.getText().toString().trim();
        final String email = editEmail.getText().toString().trim();
        String password = editPassW.getText().toString().trim();
        String confirmPassword = editConfirmPassW.getText().toString().trim();

        boolean validEmail = isValidEmail(email);
        boolean validName = isValidName(name);
        boolean validPassword = isValidPassword(password, confirmPassword);
        if (!validEmail || !validName || !validPassword) return;

        mAuthProgressDialog.show();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mAuthProgressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Authentication successful");
//                            createFirebaseUserProfile(task.getResult().getUser());

                            FirebaseUser user = auth.getCurrentUser();
                            //get email and uid from user
                            String email = user.getEmail();
                            String uid = user.getUid();
                            //store user info in realtime db using hashmap
                            HashMap<Object, String> hashMap = new HashMap<>();
                            //put info in hashmap
                            hashMap.put("email", email);
                            hashMap.put("uid", uid);
                            hashMap.put("name", "");
                            hashMap.put("phone", phone);
                            hashMap.put("image", "");
                            hashMap.put("cover", "");
                            //firebase db instance
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            //path to store user info named "Users"
                            DatabaseReference reference = database.getReference("Users");
                            //put data within hashmap in db
                            reference.child(uid).setValue(hashMap);

                        } else {
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
//    private void createFirebaseUserProfile(final FirebaseUser user) {
//
//        UserProfileChangeRequest addProfileName = new UserProfileChangeRequest.Builder()
//                .setDisplayName(mName)
//                .build();
//
//        user.updateProfile(addProfileName)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, user.getDisplayName());
//                        }
//                    }
//
//                });
//    }

}
