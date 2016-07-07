package com.ieeemalabar.viewholder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ieeemalabar.R;
import com.ieeemalabar.fragment.PostListFragment;
import com.ieeemalabar.models.Post;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;
    public TextView dateView;
    public TextView collegeView;
    public ImageButton starView;
    public TextView numStarsView;
    public TextView bodyView;
    public ImageView feature;
    public RelativeLayout post_edit;

    public StorageReference mStorageRef;
    private FirebaseStorage mStorage;
    public Bitmap bmp;
    StorageReference profRef;
    StorageReference featRef;
    public static boolean setImage = true;

    public PostViewHolder(View itemView) {
        super(itemView);

        post_edit = (RelativeLayout) itemView.findViewById(R.id.post_edit);
        titleView = (TextView) itemView.findViewById(R.id.post_title);
        authorView = (TextView) itemView.findViewById(R.id.post_author);
        dateView = (TextView) itemView.findViewById(R.id.post_date);
        collegeView = (TextView) itemView.findViewById(R.id.collegeView);
        feature = (ImageView) itemView.findViewById(R.id.feature);
        starView = (ImageButton) itemView.findViewById(R.id.star);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        bodyView = (TextView) itemView.findViewById(R.id.post_body);
    }

    public void bindToPost(Post post, View.OnClickListener starClickListener) {

        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReferenceFromUrl("gs://project-3576505284407387518.appspot.com/");
        ImageView prof_pic = (ImageView) itemView.findViewById(R.id.post_author_photo);
        prof_pic.setImageResource(R.drawable.prof_pic);
        loadProfPic(post.uid);

        titleView.setText(post.title.substring(0,1).toUpperCase() + post.title.substring(1));
        authorView.setText(post.author);
        dateView.setText(post.date);
        collegeView.setText(post.college);
        numStarsView.setText(String.valueOf(post.starCount));
        String s = post.body.substring(0, Math.min(post.body.length(), 150));
        s = s.replaceAll("[\\t\\n\\r]", " ");
        bodyView.setText(s + "...");
        feature.setVisibility(View.GONE);
        starView.setOnClickListener(starClickListener);
    }

    public  void loadProfPic(String uid){
        final long ONE_MEGABYTE = 1024 * 1024;
        profRef = mStorageRef.child("profile/" + uid + ".jpg");
        profRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ImageView prof_pic = (ImageView) itemView.findViewById(R.id.post_author_photo);
                prof_pic.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                ImageView prof_pic = (ImageView) itemView.findViewById(R.id.post_author_photo);
                prof_pic.setImageResource(R.drawable.prof_thumb);
            }
        });
    }
}
