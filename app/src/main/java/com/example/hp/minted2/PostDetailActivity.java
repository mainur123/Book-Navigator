package com.example.hp.minted2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.drm.DrmStore;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class PostDetailActivity extends AppCompatActivity {

    TextView mTitleTv, mDetailTv;
    ImageView mImageIv;
    Bitmap bitmap;

    Button mBorrowBtn, mSaveBtn, mShareBtn;

    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        //action bar
        ActionBar actionBar = getSupportActionBar();

        //actionbar title
        actionBar.setTitle("Post Detail");
        //set back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //initialize views
        mTitleTv = findViewById(R.id.titleTv);
        mDetailTv = findViewById(R.id.descriptionTv);
        mImageIv = findViewById(R.id.imageView);
        mBorrowBtn = findViewById(R.id.borrowBtn);
        mSaveBtn = findViewById(R.id.saveBtn);
        mShareBtn = findViewById(R.id.shareBtn);

        //get data from intent
        String image = getIntent().getStringExtra("image");
        String title = getIntent().getStringExtra("title");
        String desc = getIntent().getStringExtra("description");

        //set data to views
        mTitleTv.setText(title);
        mDetailTv.setText(desc);
        Picasso.get().load(image).into(mImageIv);



        //borrow btn click handle
        mBorrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PostDetailActivity.this,Done.class);
                startActivity(intent);

            }
        });

        //save btn click handle
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if os is >= marshmallow we need runtime permission to save image
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED){
                        String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //show popup to grant permission
                        requestPermissions(permission, WRITE_EXTERNAL_STORAGE_CODE);
                    }
                    else{
                        //permission already granted, save image
                        saveImage();
                    }
                }
                else {
                    //system os is < marshmallow, save image
                    saveImage();
                }
            }
        });
        //share btn click handle
        mShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareImage();
            }
        });

    }

    private void shareImage() {
        try{
            //get image from imageview as bitmap
            bitmap = ((BitmapDrawable)mImageIv.getDrawable()).getBitmap();
            //get title and description and save in string s
            String s = mTitleTv.getText().toString() + "\n" + mDetailTv.getText().toString();

            File file = new File(getExternalCacheDir(), "sample.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100,fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true,false);
            //intent to share image and text
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(intent.EXTRA_TEXT,s); //put the text
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(Intent.createChooser(intent, "Share via"));
        }
        catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    private void saveImage() {
        //get image from imageview as bitmap
        bitmap = ((BitmapDrawable)mImageIv.getDrawable()).getBitmap();
        //time stamp, for image name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis());
        //path to external storage
        File path = Environment.getExternalStorageDirectory();
        //create folder named "Books"
        File dir = new File(path+"/Books/");
        dir.mkdirs();
        //image name
        String imageName = timeStamp + ".PNG";
        File file = new File(dir,imageName);
        OutputStream out;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Toast.makeText(this, imageName+"saved to"+ dir,Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            //failed saving image
            Toast.makeText(this, e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }

    //handle onBackpressed(go to previous activity)

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case WRITE_EXTERNAL_STORAGE_CODE:{
                //if request code is canceled the result arrays are empty
                if(grantResults.length > 0 && grantResults[0]==
                        PackageManager.PERMISSION_GRANTED){
                    //permission is granted, save image
                    saveImage();
                }
                else{
                    //permission denied
                    Toast.makeText(this, "enable permission to save image", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}

//submited to, submited by
//outline, objectives, introduction, working process, development process, outcomes, future aspects(korte parini but korar iccha ase)
//conclusion-- 8-10 page