package com.ieeemalabar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ieeemalabar.models.Notification;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewNotification extends AppCompatActivity {

    Activity context;
    ProgressDialog pd;
    String text = "IN";
    EditText mTitleField, mBodyField;
    String title,body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_notification);
        context = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setElevation(0);
        }

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });
    }

    public void sendNotification() {
        mTitleField = (EditText) findViewById(R.id.notif_title);
        mBodyField = (EditText) findViewById(R.id.notif_message);

        title = mTitleField.getText().toString().trim();
        body = mBodyField.getText().toString().trim();

        // Title is required
        if (TextUtils.isEmpty(title)) {
            mTitleField.setError("Required");
            return;
        }
        // Message is required
        if (TextUtils.isEmpty(body)) {
            mBodyField.setError("Required");
            return;
        }

        hideSoftKeyboard(this);
        pd = new ProgressDialog(context);
        pd.setMessage("Sending Notification");
        pd.setCancelable(false);
        pd.setIndeterminate(false);
        pd.show();

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("notifications");

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String date = df.format(c.getTime());

        Notification msg = new Notification(title, body, date);
        mRef.push().setValue(msg);
        send(title);
    }

    public Activity getContext() {
        return context;
    }

    public void send(String title) {
        super.onStart();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        BackTask bt = new BackTask();
        bt.execute("http://ieee-malabar.rhcloud.com/notification.php?message=" + title.replaceAll(" ", "_") + "&uid=" + uid);
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
                while ((line = br.readLine()) != null) {
                    text += line;
                }

                br.close();

            } catch (Exception e) {
                e.printStackTrace();
                if (pd != null) {
                    pd.dismiss();
                }
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            if (pd != null) {
                pd.dismiss();
                if (text.toUpperCase().contains("YES")) {
                    //Toast.makeText(Feedback.this, "Email Sent", Toast.LENGTH_SHORT).show();
                    //finish();
                }
                finish();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {

        mTitleField = (EditText) findViewById(R.id.notif_title);
        mBodyField = (EditText) findViewById(R.id.notif_message);
        title = mTitleField.getText().toString().trim();
        body = mBodyField.getText().toString().trim();

        if (!TextUtils.isEmpty(title) || !TextUtils.isEmpty(body)) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            finish();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to cancel?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener)
                    .setTitle("Cancel Notification")
                    .show();
        } else {
            finish();
        }
    }
}
