package com.example.android.light;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private PhoneAuthProvider mAuthProvider;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    EditText username;
    EditText userphone;
    EditText mCode;
    Button login_signup;
    Button mVerify;
    TextView toggle;
    TextView codemsg;

    LinearLayout mLinearLayout;

    private static String TAG;
    String mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TAG = MainActivity.class.getName();

        mAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.user_name);
        userphone = findViewById(R.id.user_phone);
        login_signup = findViewById(R.id.user_login_signup);
        toggle = findViewById(R.id.user_toggle);

        mCode = findViewById(R.id.code);
        mVerify = findViewById(R.id.verify);
        codemsg = findViewById(R.id.codemsg);
        mLinearLayout = findViewById(R.id.verify_layout);


        mAuthProvider = PhoneAuthProvider.getInstance();

        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), SecondActivity.class));
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                Log.d(TAG, "onVerificationCompleted:" + credential);

                String code = credential.getSmsCode();

                PhoneAuthCredential credential1 = PhoneAuthProvider.getCredential(mVerificationId, code);

                signInWithPhoneAuthCredential(credential1);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);

                mVerificationId = verificationId;
                mResendToken = token;

                Toast.makeText(getApplicationContext(), "Code sent", Toast.LENGTH_SHORT).show();

                username.setVisibility(View.GONE);
                userphone.setVisibility(View.GONE);
                login_signup.setVisibility(View.GONE);
                toggle.setVisibility(View.GONE);

                mLinearLayout.setVisibility(View.VISIBLE);

            }
        };
    }

    public void toggle(View view) {

        String text = toggle.getText().toString();

        if (text.equals(getString(R.string.or_login))) {
            toggle.setText(getString(R.string.or_signup));
            login_signup.setText(getString(R.string.login));
        } else {
            toggle.setText(getString(R.string.or_login));
            login_signup.setText(getString(R.string.signup));
        }
    }

    public void signup(View view) {

        String user_phone = userphone.getText().toString();

        Toast.makeText(getApplicationContext(), "Signup called", Toast.LENGTH_SHORT).show();

        mAuthProvider.verifyPhoneNumber(
                user_phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks


    }

    public void verify(View view){

        String code = mCode.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();

                            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                            startActivity(intent);
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

}
