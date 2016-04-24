package com.hyco.lococam;

import android.app.ActionBar;
import android.app.Activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import android.view.View;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

public class ShowPicture extends Activity {

    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_picture);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        path = getIntent().getStringExtra("path");

        SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) findViewById(R.id.picture);


        Uri myUri = Uri.parse(path);
        imageView.setImage(ImageSource.uri(myUri));

/*
        Picasso.with(this).load(path).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });
          */
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


    }

    public void share(View view) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
        startActivity(Intent.createChooser(share, "Share Image"));
    }

    public void delete(View view) {


    }


}
