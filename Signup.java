package com.csd.shobhit.csd;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class Signup extends AppCompatActivity {

    private EditText nameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    private Button signupButton;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_signup);

        nameEditText = findViewById(R.id.name);
        passwordEditText = findViewById(R.id.password);
        emailEditText = findViewById(R.id.email);
        signupButton = findViewById(R.id.signUp);
        progressBar = findViewById(R.id.signUpProgress);

        mAuth = FirebaseAuth.getInstance();

        setStatusBarColor(R.color.colorWhiteStatus);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarColor(int color) {

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(color));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    @Override
    protected void onStart() {
        super.onStart();

        nameEditText.addTextChangedListener(nameTextWatcher);
        emailEditText.addTextChangedListener(emailTextWatcher);
        passwordEditText.addTextChangedListener(passwordTextWatcher);
        signupButton.setEnabled(false);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void signUpClick(View view) {

        progressBar.setVisibility(View.VISIBLE);
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisLeft) {

            }

            public void onFinish() {

                Log.i("Cliked", "Logged.");
                signUp(nameEditText.getText().toString(), passwordEditText.getText().toString(),
                        emailEditText.getText().toString());
            }
        }.start();
    }

    public void goToLogin(View view){
        finish();
    }

    private void showSnackbar(String message, int time) {

        Snackbar.make(findViewById(R.id.signUpconstraint), message, time)
                .setAction("Action", null).show();
    }

    /* !!--- ----------------------function for user-signup --------------------------------------- --!! */
    private void signUp(final String name, String pass, String email) {

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.INVISIBLE);

                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(name);

                        } else {

                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e) {

                                showSnackbar("Email Already in Use.", 2000);
                            }
                            catch (FirebaseAuthWeakPasswordException e){

                                showSnackbar("Weak Password",3000);
                            }
                            catch (FirebaseNetworkException e) {

                                showSnackbar("No Internet Connectivity.", 2000);
                            }
                            catch (Exception e) {

                                showSnackbar("An Error Occured", 2000);
                                System.out.println(e);
                            }
                        }
                    }
                });
    }

    private void updateUI(String name) {
        Intent intent = new Intent(Signup.this, Profile.class);
        intent.putExtra(EXTRA_MESSAGE, name);
        startActivity(intent);
        finish();
    }
    /* !!--- -------------------------------------------------------------------------------------- --!! */

    private TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            boolean flag = FormValidation.isName(charSequence.toString());
//            if (!flag) {
//                nameEditText.setError("Invalid.");
//                signupButton.setEnabled(false);
//            } else {
//                nameEditText.setError(null);
//                signupButton.setEnabled(true);
//            }
            signupButton.setEnabled(true);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private TextWatcher emailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            boolean flag = FormValidation.isEmailAddress(charSequence.toString());
//            if (!flag) {
//                emailEditText.setError("Invalid.");
//                signupButton.setEnabled(false);
//            } else {
//                emailEditText.setError(null);
//                signupButton.setEnabled(true);
//            }
            signupButton.setEnabled(true);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            boolean flag = FormValidation.isPassword(charSequence.toString());
//            if (!flag) {
//                passwordEditText.setError("Invalid.");
//                signupButton.setEnabled(false);
//            } else {
//                passwordEditText.setError(null);
//                signupButton.setEnabled(true);
//            }
            signupButton.setEnabled(true);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}
