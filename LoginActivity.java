package com.csd.shobhit.csd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_b);
        progressBar = findViewById(R.id.loginProgress);

        mAuth = FirebaseAuth.getInstance();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, Profile.class);
            startActivity(intent);
            finish();
        }

        emailEditText.addTextChangedListener(emailTextWatcher);
        passwordEditText.addTextChangedListener(passwordTextWatcher);
        loginButton.setEnabled(false);
        progressBar.setVisibility(View.INVISIBLE);

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

    /* !!--- ----------------------function for user-login ---------------------------------------- --!! */
    public void loginButtonClick(View view) {

        progressBar.setVisibility(View.VISIBLE);
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisLeft) {

            }

            public void onFinish() {

                Log.i("Cliked", "Logged.");
                signIn(emailEditText.getText().toString(), passwordEditText.getText().toString());
            }

        }.start();
    }


    public void goToSignup(View view){
        Intent signupI = new Intent(LoginActivity.this, Signup.class);
        startActivity(signupI);
    }

    private void showSnackbar(String message, int time) {

        Snackbar.make(findViewById(R.id.loginConstraint), message, time)
                .setAction("Action", null).show();
    }

    private void signIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (task.isSuccessful()) {

                            updateUI();
                        } else {

                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e) {

                                showSnackbar("Wrong Password", 2000);
                            } catch (FirebaseAuthInvalidUserException e) {

                                showSnackbar("User Doesn't Exist.", 3000);
                                Intent signupI = new Intent(LoginActivity.this, Signup.class);
                                startActivity(signupI);

                            } catch (FirebaseNetworkException e) {

                                showSnackbar("No Internet Connectivity.", 2000);
                            } catch (Exception e) {

                                showSnackbar("An Error Occured", 2000);
                                System.out.println(e);
                            }
                        }
                    }
                });
    }


    private void updateUI() {
        Intent intent = new Intent(LoginActivity.this, Profile.class);
        startActivity(intent);
        finish();
    }
    /* !!--- -------------------------------------------------------------------------------------- --!! */


    /* !!--- -------------------real time form validation------------------------------------------ --!! */
    private TextWatcher emailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            boolean flag = FormValidation.isEmailAddress(charSequence.toString());
//            if (!flag) {
//                emailEditText.setError("Invalid.");
//                loginButton.setEnabled(false);
//            } else {
//                emailEditText.setError(null);
//                loginButton.setEnabled(true);
//            }
            loginButton.setEnabled(true);
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
//                loginButton.setEnabled(false);
//            } else {
//                passwordEditText.setError(null);
//                loginButton.setEnabled(true);
//            }
            loginButton.setEnabled(true);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

}
