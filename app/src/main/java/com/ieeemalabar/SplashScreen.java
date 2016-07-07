package com.ieeemalabar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ieeemalabar.models.Comment;
import com.ieeemalabar.models.User;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
                                BackTask bt = new BackTask();
                                bt.execute("http://ieee-malabar.rhcloud.com/ready.php");
                                getUserDetails();
                            }else {
                                Toast.makeText(SplashScreen.this, "No internet access!", Toast.LENGTH_SHORT).show();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 2000);
                            }
                        }
                    });
                } else {
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    //finish();
                }
            }
        }, 2000);
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

    public void getUserDetails(){

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
                        User user = dataSnapshot.getValue(User.class);

                        String name = user.name;
                        String position = user.position;
                        String hubmember = user.hubmember;

                        SharedPreferences settings = getSharedPreferences("com.ieeemalabar", MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("name", name);
                        editor.putString("hubmember", hubmember);
                        editor.putString("position", position);
                        editor.commit();

                        startActivity(new Intent(SplashScreen.this, ActivityMain.class));
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private class BackTask extends AsyncTask<String, Integer, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(String... params) {
            URL url;
            try {
                url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                InputStream is = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                String text = "";
                while ((line = br.readLine()) != null) {
                    text += line;
                }

                Toast.makeText(SplashScreen.this, text, Toast.LENGTH_SHORT).show();

                br.close();

            } catch (Exception e) {
            }
            return null;
        }
    }
}
