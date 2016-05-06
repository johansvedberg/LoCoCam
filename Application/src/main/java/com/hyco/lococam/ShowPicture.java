package com.hyco.lococam;

import android.app.ActionBar;
import android.app.Activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Location;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;


public class ShowPicture extends Activity {

    private String path;
    private String currentGeofence;
    private Uri myUri;
    private Uri pnguri;
    private Bitmap processedBitmap;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    private String filename;
    private int temperature;
    private String info;
    private String condition;
    private String info2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);
        ActionBar actionBar = getActionBar();
        mBackgroundThread = new HandlerThread("MyHandlerThread");
        mBackgroundThread.start();
        Looper looper = mBackgroundThread.getLooper();
        Handler handler = new Handler(looper);


        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        path = getIntent().getStringExtra("path");
        filename = getIntent().getStringExtra("filename");
        currentGeofence = getIntent().getStringExtra("locationKey");
        temperature = getIntent().getIntExtra("temperature",0);
        condition = getIntent().getStringExtra("condition");




        SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) findViewById(R.id.picture);

        info = currentGeofence;
        info2 = condition + ", " + temperature + "°C";
        myUri = Uri.parse(path);

        processedBitmap = processingBitmap();


        imageView.setImage(ImageSource.bitmap(processedBitmap));
        File mFile = new File(this.getExternalFilesDir(null), filename + "(2).png");
        pnguri = Uri.parse(mFile.toURI().toString());



        ImageSaver imageSaver = new ImageSaver(mFile);

        handler.post(imageSaver);
    }


    private class ImageSaver implements Runnable {


        private final File mFile;

        public ImageSaver(File file) {

            mFile = file;

        }

        @Override
        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);


            FileOutputStream out = null;
            try {
                out = new FileOutputStream(mFile);
                processedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }

    private Bitmap processingBitmap() {
        Bitmap bm1 = null;
        Bitmap newBitmap = null;

        try {
            bm1 = BitmapFactory.decodeStream(
                    getContentResolver().openInputStream(myUri));
            Bitmap.Config config = bm1.getConfig();
            if (config == null) {
                config = Bitmap.Config.ARGB_8888;
            }
            newBitmap = Bitmap.createBitmap(bm1.getWidth(), bm1.getHeight(), config);
            Canvas newCanvas = new Canvas(newBitmap);

            newCanvas.drawBitmap(bm1, 0, 0, null);

            Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintText.setColor(Color.WHITE);
            paintText.setTextSize(300);
            paintText.setStyle(Paint.Style.FILL);
            paintText.setShadowLayer(10f, 10f, 10f, Color.BLACK);

            Rect rectText = new Rect();

            paintText.getTextBounds(info, 0, info.length(), rectText);
            newCanvas.drawText(info,
                    0, rectText.height(), paintText);


            Rect bounds = new Rect();
            paintText.getTextBounds(info2, 0, info2.length(), bounds);
            int x = (newBitmap.getWidth() - bounds.width() - 100) ;
            int y = (newBitmap.getHeight() - bounds.height() + 50);

            newCanvas.drawText(info2, x, y, paintText);

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }


        return newBitmap;
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


    }

    public void share(View view) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/");
        share.putExtra(Intent.EXTRA_STREAM, pnguri);
        startActivity(Intent.createChooser(share, "Share Image"));
    }

    public void delete(View view) {

        //Find a delete function that works

    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Save file");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        startBackgroundThread();
    }

    @Override
    public void onPause() {
        stopBackgroundThread();
        super.onPause();
    }




}




