package com.ieeemalabar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ieeemalabar.models.User;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends Activity {

    ImageView prof_pic;
    private static final int RESULT_LOAD_IMAGE = 1;
    Uri selectedImage;
    Activity context;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    StorageReference imageRef;
    public static Bitmap bmp;
    public String name, college, position, mail, MorC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = this;

        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReferenceFromUrl("gs://project-3576505284407387518.appspot.com/");
        prof_pic = (ImageView) findViewById(R.id.profil_image);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        if (message.equals("details")) {
            findViewById(R.id.profPB).setVisibility(View.GONE);
            String uid = bundle.getString("uid");
            loadUserDetails(uid);
            name = bundle.getString("name");
            college = bundle.getString("college");
            prof_pic.setImageBitmap(PostDetailActivity.profBmp);

        } else if (message.equals("details_error")) {
            findViewById(R.id.profPB).setVisibility(View.GONE);
            String uid = bundle.getString("uid");
            loadUserDetails(uid);
            name = bundle.getString("name");
            college = bundle.getString("college");
            findViewById(R.id.profil_thumb).setVisibility(View.VISIBLE);

        } else if (message.equals("me")) {
            findViewById(R.id.userDetail).setVisibility(View.VISIBLE);
            findViewById(R.id.UserDetailsPB).setVisibility(View.GONE);
            loadImage(FirebaseAuth.getInstance().getCurrentUser().getUid());
            SharedPreferences settings = getSharedPreferences("com.ieeemalabar", MODE_PRIVATE);
            name = settings.getString("name", "");
            college = settings.getString("college", "");
            position = settings.getString("position", "");
            mail = settings.getString("user", "");
            MorC = "M";

        } else if (message.equals("comment")) {
            name = bundle.getString("name");
            String uid = bundle.getString("uid");
            MorC = "C";
            loadImage(uid);
            loadUserDetails(uid);
            //Toast.makeText(Profile.this, uid, Toast.LENGTH_SHORT).show();
        }
        //String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        getWindow().setLayout((int) (width * .8), LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout prof_pic = (LinearLayout) findViewById(R.id.prof_pic);
        TextView prof_name = (TextView) findViewById(R.id.prof_name);
        TextView prof_college = (TextView) findViewById(R.id.prof_college);
        TextView prof_position = (TextView) findViewById(R.id.prof_position);
        TextView prof_mail = (TextView) findViewById(R.id.prof_mail);

        prof_name.setText(name);
        prof_college.setText(college);
        prof_position.setText(position);
        prof_mail.setText(mail);

        TextView mail_icon = (TextView) findViewById(R.id.mail_icon);
        TextView position_icon = (TextView) findViewById(R.id.position_icon);

        prof_pic.getLayoutParams().height = width / 5;
        prof_pic.getLayoutParams().width = width / 5;

        Typeface icon_font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome.ttf");
        Typeface b_font = Typeface.createFromAsset(getAssets(), "fonts/NexaB.otf");
        Typeface n_font = Typeface.createFromAsset(getAssets(), "fonts/NexaL.otf");

        prof_name.setTypeface(b_font);
        prof_mail.setTypeface(b_font);
        prof_position.setTypeface(b_font);
        prof_college.setTypeface(n_font);

        mail_icon.setTypeface(icon_font);
        position_icon.setTypeface(icon_font);

        mail_icon.setText("\uf0e0");
        position_icon.setText("\uf0c0");
    }

    public void loadUserDetails(String uid) {
        FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
                        User user = dataSnapshot.getValue(User.class);

                        TextView prof_college = (TextView) findViewById(R.id.prof_college);
                        TextView prof_position = (TextView) findViewById(R.id.prof_position);
                        TextView prof_mail = (TextView) findViewById(R.id.prof_mail);
                        prof_college.setText(user.college);
                        prof_position.setText(user.position);
                        prof_mail.setText(user.username);

                        findViewById(R.id.userDetail).setVisibility(View.VISIBLE);
                        findViewById(R.id.UserDetailsPB).setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void loadImage(String uid) {
        final long ONE_MEGABYTE = 1024 * 1024;
        imageRef = mStorageRef.child("profile/" + uid + ".jpg");
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                //Toast.makeText(PostDetailActivity.this, "Invalid Name", Toast.LENGTH_SHORT).show()
                bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                prof_pic.setImageBitmap(bmp);

                findViewById(R.id.profPB).setVisibility(View.GONE);

                if (MorC.equals("M")) {
                    findViewById(R.id.edit).setVisibility(View.VISIBLE);
                    prof_pic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                        }
                    });
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                findViewById(R.id.profPB).setVisibility(View.GONE);

                findViewById(R.id.profil_thumb).setVisibility(View.VISIBLE);

                if (MorC.equals("M")) {
                    findViewById(R.id.edit).setVisibility(View.VISIBLE);
                    prof_pic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            prof_pic.setImageURI(selectedImage);
            uploadImage();
        }
    }

    public void uploadImage() {
        imageRef = mStorageRef.child("profile/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg");
        prof_pic.setDrawingCacheEnabled(true);
        prof_pic.buildDrawingCache();
        Bitmap bitmap = prof_pic.getDrawingCache();
        bitmap = getResizedBitmap(bitmap, 256);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                //pd.hide();
                //finish();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                findViewById(R.id.profil_thumb).setVisibility(View.GONE);
            }
        });

        SharedPreferences settings = getSharedPreferences("com.ieeemalabar", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("prof_change", "yes");
        editor.commit();
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
