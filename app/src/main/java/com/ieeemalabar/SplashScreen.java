package com.ieeemalabar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SplashScreen extends Activity {

    String email, password;
    private FirebaseAuth mAuth;
    private static final String TAG = "SplashScreen";
    public static SplashScreen SP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SP = this;

        mAuth = FirebaseAuth.getInstance();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //FirebaseAuth.getInstance().signOut();
        SharedPreferences settings = getSharedPreferences("com.ieeemalabar", MODE_PRIVATE);
        email = settings.getString("user", "");
        password = settings.getString("pass", "");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                if (isEmailValid(email)) {
                    mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                            //pd.hide();
                            if (task.isSuccessful()) {
                                startActivity(new Intent(SplashScreen.this, ActivityMain.class));
                                Toast.makeText(SplashScreen.this, password, Toast.LENGTH_SHORT).show();
                                //finish();
                            }else {
                                Toast.makeText(SplashScreen.this, "No internet access", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    //finish();
                }
            }
        }, 3200);
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
}
