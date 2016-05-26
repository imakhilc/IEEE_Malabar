package com.ieeemalabar.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ieeemalabar.ActivityMain;
import com.ieeemalabar.R;

/**
 * Created by AKHIL on 26-Apr-16.
 * Created by AKHIL on 26-Apr-16.
 */
public class SignUp extends Fragment {

    private FirebaseAuth mAuth;
    private Toast mToastText;
    Spinner mCollegeField;
    Button mSignUpButton;
    EditText mNameField,mEmailField,mIeeeField,mPasswordField;
    ProgressDialog pd;
    CheckBox check;
    Boolean tick = true;
    private DatabaseReference mDatabase;
    private static final String TAG = "SignUp";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.signup,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mNameField = (EditText) getView().findViewById(R.id.fullname1);
        mEmailField = (EditText) getView().findViewById(R.id.email1);
        mCollegeField = (Spinner) getView().findViewById(R.id.college1);
        mIeeeField = (EditText) getView().findViewById(R.id.ieee1);
        mPasswordField = (EditText) getView().findViewById(R.id.password1);
        check = (CheckBox) getActivity().findViewById(R.id.checkBox1);
        mSignUpButton = (Button) getView().findViewById(R.id.register);

        Typeface b_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/normalone.ttf");
        mNameField.setTypeface(b_font);
        mEmailField.setTypeface(b_font);
        mIeeeField.setTypeface(b_font);
        mPasswordField.setTypeface(b_font);
        mSignUpButton.setTypeface(b_font);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.colleges, R.layout.college_list);
        mCollegeField.setAdapter(adapter);

        pd = new ProgressDialog(getActivity());
        pd.setCancelable(false);
        mToastText = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);

        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        }

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tick) {
                    mPasswordField.setInputType(InputType.TYPE_CLASS_TEXT);
                    mPasswordField.setSelection(mPasswordField.getText().length());
                    tick = false;
                } else {
                    mPasswordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mPasswordField.setSelection(mPasswordField.getText().length());
                    tick = true;
                }
            }
        });
    }

    private void displayMessage(final String message) {
        mToastText.setText(message);
        mToastText.show();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    private void signUp() {
        //Log.d(TAG, "signUp");
        if (!validateForm()) {
            return;
        }
        hideSoftKeyboard(getActivity());
        pd.setMessage("Creating Account");
        pd.show();
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                        pd.hide();

                        if (task.isSuccessful()) {
                            displayMessage("Registration success");
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            displayMessage("Registration Failed");
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = user.getEmail().toLowerCase();
        String name = mNameField.getText().toString().toUpperCase();
        String college = mCollegeField.getSelectedItem().toString();
        String ieee = mIeeeField.getText().toString();

        // Write new user
        writeNewUser(user.getUid(), username, name, college, ieee);

        // Go to MainActivity
        startActivity(new Intent(getActivity(), ActivityMain.class));
        getActivity().finish();
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mEmailField.getText().toString())||!isEmailValid(mEmailField.getText().toString())) {
            mEmailField.setError("Invalid email id");
            result = false;
        } else {
            mEmailField.setError(null);
        }

        if (mPasswordField.getText().toString().length()<6) {
            mPasswordField.setError("Password should contain at least 6 characters");
            result = false;
        } else {
            mPasswordField.setError(null);
        }

        if (mNameField.getText().toString().length()<4) {
            mNameField.setError("Enter your full name");
            result = false;
        } else {
            mNameField.setError(null);
        }

        if (mIeeeField.getText().toString().length()!=8) {
            mIeeeField.setError("Invalid ID");
            result = false;
        } else {
            mIeeeField.setError(null);
        }

        return result;
    }

    private void writeNewUser(String userId, String username, String name, String college, String ieee) {
        User user = new User(username,name,college,ieee);
        mDatabase.child("users").child(userId).setValue(user);
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    @IgnoreExtraProperties
    public class User {

        public String username;
        public String name;
        public String college;
        public String ieee;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String username, String name, String college, String ieee) {
            this.username = username;
            this.name = name;
            this.college = college;
            this.ieee = ieee;
        }

        public User(String username) {
            this.username = username;
        }

    }
}