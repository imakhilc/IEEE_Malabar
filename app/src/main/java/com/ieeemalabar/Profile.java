package com.ieeemalabar;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
        LinearLayout level = (LinearLayout) findViewById(R.id.level);
        ImageView backimg = (ImageView) findViewById(R.id.backimg);
        LinearLayout name_align = (LinearLayout) findViewById(R.id.name_align);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        prof_pic.getLayoutParams().height = width / 4;
        prof_pic.getLayoutParams().width = width / 4;
        level.getLayoutParams().height = width / 8;
        backimg.getLayoutParams().height = height / 2;
        name_align.getLayoutParams().height = (width/8) + 10;
    }

    @Override
    public void onBackPressed() {
        //startActivity(new Intent(getApplicationContext(), MainContainer.class));
        finish();
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
}
