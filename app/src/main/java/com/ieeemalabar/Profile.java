package com.ieeemalabar;

import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setElevation(0);
        }

        LinearLayout prof_pic = (LinearLayout) findViewById(R.id.prof_pic);
        TextView prof_name = (TextView) findViewById(R.id.prof_name);
        TextView prof_college = (TextView) findViewById(R.id.prof_college);
        TextView prof_mail = (TextView) findViewById(R.id.prof_mail);
        TextView prof_phone = (TextView) findViewById(R.id.prof_phone);
        TextView prof_place = (TextView) findViewById(R.id.prof_place);

        TextView mail_icon = (TextView) findViewById(R.id.mail_icon);
        TextView phone_icon = (TextView) findViewById(R.id.phone_icon);
        TextView place_icon = (TextView) findViewById(R.id.place_icon);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        //int height = dm.heightPixels;

        prof_pic.getLayoutParams().height = width / 5;
        prof_pic.getLayoutParams().width = width / 5;

        Typeface icon_font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome.ttf");
        Typeface b_font = Typeface.createFromAsset(getAssets(), "fonts/NexaB.otf");
        Typeface n_font = Typeface.createFromAsset(getAssets(), "fonts/NexaL.otf");

        prof_name.setTypeface(b_font);
        prof_college.setTypeface(n_font);
        prof_mail.setTypeface(n_font);
        prof_phone.setTypeface(n_font);
        prof_place.setTypeface(n_font);

        mail_icon.setTypeface(icon_font);
        phone_icon.setTypeface(icon_font);
        place_icon.setTypeface(icon_font);

        mail_icon.setText("\uf0e0");
        phone_icon.setText("\uf098");
        place_icon.setText("\uf041");
    }

    @Override
    public void onBackPressed() {
        //startActivity(new Intent(getApplicationContext(), MainContainer.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.edit_profile:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px * 2;
    }
}
