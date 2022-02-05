package com.example.herbinance.ui.auth;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.herbinance.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneFragment extends Fragment {
    private EditText etTextFb;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String code;
    private PhoneAuthProvider.ForceResendingToken token;
    private Spinner spinner;
    private FirebaseAuth auth;
    private ConstraintLayout mCLNumber, mCLVerification;
    private EditText etTextVerification;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_phone, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etTextFb = view.findViewById(R.id.etTextFb);
        mCLNumber = view.findViewById(R.id.constraintEntering);
        mCLVerification = view.findViewById(R.id.constraintEnteringVerification);
        Button btnVerify = view.findViewById(R.id.btnVerify);
        etTextVerification = view.findViewById(R.id.verificationEtText);
        spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<String> countryCode = new ArrayAdapter<>(getContext(), R.layout.spinner_item, CountryDetails.countryCodes);
        spinner.setAdapter(countryCode);
        btnVerify.setOnClickListener(v -> {
            String verifcode = etTextVerification.getText().toString().trim();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(code, verifcode);
            signIn(credential);
        });
        view.findViewById(R.id.btnSaveFb).setOnClickListener(v -> requestSms());
        setCallBacks();
    }


    private void setCallBacks() {
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.e("phone", "onVerificationCompleted");
                signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(getActivity(), "Verification Failed " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Log.d(TAG, "onCodeSent:" + s);
                Toast.makeText(getActivity(), "Code Sent",
                        Toast.LENGTH_LONG).show();
                token = forceResendingToken;
                code = s;
            }
        };
    }

    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    close();
                }
            }
        });
    }

    private void signOut() {
        auth.signOut();
    }

    private void requestSms() {
        String spinnerText = spinner.getSelectedItem().toString();
        String phone = etTextFb.getText().toString();

        if (phone.trim().isEmpty()) {
            etTextFb.setError("Provide Phone Number");
            return;
        }
        String phoneNumber = spinnerText + phone;
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(phoneNumber).setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(requireActivity())
                        .setCallbacks(callbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

        mCLNumber.setVisibility(View.INVISIBLE);
        mCLVerification.setVisibility(View.VISIBLE);
    }

    public void close() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigateUp();
    }
}