package com.unity3d.player;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.unity3d.player.customfonts.MyButton;
import com.unity3d.player.customfonts.MyLiteTextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class UnityPlayerActivity extends Activity {
    protected static UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code

    private String LoadSceneName = "Plugxr";   // don't remove LoadSceneName String

    // Replace with your vuforia database keys
    private String LicenceKeyToUnity = "AWQe4w//////AAAAGThyA+1zHEkdukepCNr5FTxwnTy+oDr7OBJ3Aj1cRuZYlKo23KBgoGSo/XqmXGUW4rWjfJcsvjh625v0/cjqe6COTHyRveA5eQ5dDh/BszGw+W/eQV6gCbubOes5h1eDVWRhJCwUF92PJddFobxs16tLqeLYW2ZBJGcXAaE4vlst5A5HFS+i44fxlWemK50NJ2CeKXXxn6YUVjwf7aU70KdQ+bGa3J1oxwKiGpCRGILyCYgUTBxDRjaq8Fqtsr7wVe9WOEcO8DkPOrC12C9fjAtfGXfFSZqSHG16wCO3cBNJ7Vdcy+w9BLQi4ERzjeHq2hN9m2W5DBiAVMeaq+paNNY+pFWrvLqyqpCJUbl5JVNq";
    private String AccessKeyToUnity = "652f6dac9fe7761cfc50ce5b6397ca12b909679a";
    private String SecretKeyToUnity = "440c396d764f0109534ffe86aebf561b278e19ed";

    private String appId = "Q2KEXU";
    private String deviceType = "Android";

    private FrameLayout frameLayout;
    // private TransparentProgressDialog ringProgressDialog;
    private Bitmap bitmap;
    private LocationManager locationManager;
    private String country;
    private String android_id;
    private double lang;
    private double lat;
    private AppLocationService appLocationService;
    private File videoDirectory;
    private File videoPath;
    private String videoBaseName;


    protected String updateUnityCommandLineArguments(String cmdLine) {
        return cmdLine;
    }

    // Setup activity layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy

        mUnityPlayer = new UnityPlayer(this);
        if (mUnityPlayer.getSettings().getBoolean("hide_status_bar", true)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.unity_layout);

        String cmdLine = updateUnityCommandLineArguments(getIntent().getStringExtra("unity"));
        getIntent().putExtra("unity", cmdLine);


        initView();


        // Get Android Device Id
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);


        appLocationService = new AppLocationService(
                UnityPlayerActivity.this);

        Location gpsLocation = appLocationService
                .getLocation(LocationManager.GPS_PROVIDER);
        if (gpsLocation != null) {
            double latitude = gpsLocation.getLatitude();
            double longitude = gpsLocation.getLongitude();



            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);


            } catch (IOException e) {
                e.printStackTrace();
            }

            String countryName = addresses.get(0).getCountryName();

            getUserDetails(android_id,countryName,deviceType,appId);

        }



        frameLayout.removeAllViews();
        frameLayout.addView(mUnityPlayer);



        mUnityPlayer.requestFocus();
    }




    private void getUserDetails(String android_id, String country, String deviceType, String appid) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<Userdetails> call = api.postUserDetails(android_id,country,"",deviceType,appid);

        call.enqueue(new Callback<Userdetails>() {
            @Override
            public void onResponse(Call<Userdetails> call, Response<Userdetails> response) {


            }

            @Override
            public void onFailure(Call<Userdetails> call, Throwable t) {

            }
        });

    }


    private void initView() {
        frameLayout = (FrameLayout) findViewById(R.id.unity_frame);

    }


    //---Unity scene load---
    public String LoadSceneName(){



        if(LoadSceneName == null){
          //  Log.v("SURYA","CLICK : "+LoadSceneName);
            return "Plugxr";
        }else {
          //  Log.v("SURYA","CLICK : "+LoadSceneName);
            return LoadSceneName;
        }

    }

    // --- Licence Key ---
    public String LicenceKeyToUnity(){

     //   Log.v("SURYA","CLICK : "+LicenceKeyToUnity);
        return LicenceKeyToUnity;
    }

    // --- AccessKey ---
    public String AccessKeyToUnity(){
     //   Log.v("SURYA","CLICK : "+AccessKeyToUnity);
        return AccessKeyToUnity;
    }


    // ---SecretKey---
    public String SecretKeyToUnity(){
    //    Log.v("SURYA","CLICK : "+SecretKeyToUnity);
        return SecretKeyToUnity;
    }

    public String ScreenShotButtonManager(){


        Toast.makeText(UnityPlayer.currentActivity,"True",Toast.LENGTH_SHORT).show();
        return "True";
    }

    // ---SecretKey---
    public String PackageName(){
      //  Log.v("SURYA",getApplicationContext().getPackageName());
        return getApplicationContext().getPackageName();
    }


    // ---ScreenShot---
    public void MoveFile(final String imagepath) { //ScreenShot




        //progressDialog
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                final File file = new File(imagepath);
                // your sd card


                buildFilename();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                bitmap = BitmapFactory.decodeFile(imagepath, options);
                if (bitmap != null) {


                    //saveScreenshot(file);
                    popUpWindow(bitmap, new File(imagepath),videoPath);

                }


            }
        }, 6000);
    }

    private void buildFilename() {

        if (videoDirectory == null) {
            videoDirectory =
                    new File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                    + "/PlugXR");
        }



        if (videoBaseName == null || videoBaseName.isEmpty()) {
            videoBaseName = "Sample";
        }
        videoPath =
                new File(
                        videoDirectory, videoBaseName + Long.toHexString(System.currentTimeMillis()) + ".png");
        File dir = videoPath.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }



    private void popUpWindow(Bitmap bitmap, final File file, final File inputPath) {

        /*if (ringProgressDialog.isShowing())
            ringProgressDialog.dismiss();*/
        File file1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/PlugXR");

        // Log.d("SURYA", "Picture File: " + file1);



        final Dialog dialogexit = new Dialog(UnityPlayer.currentActivity, R.style.Name);
        dialogexit.requestWindowFeature(Window.FEATURE_NO_TITLE) ;
        dialogexit.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialogexit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogexit.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);       //Theme_Material_Light_NoActionBar_Fullscreen
        dialogexit.setContentView(R.layout.layoupic);
        dialogexit.setCancelable(true);


        //saveBitmap(bitmap);



        ImageView imageCapture = (ImageView)dialogexit.findViewById(R.id.imageCapture);

        MyButton share = (MyButton)dialogexit.findViewById(R.id.share);

        imageCapture.setImageURI(Uri.fromFile(file));

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        bitmap = BitmapFactory.decodeFile(String.valueOf(file), options);

        if (bitmap!=null){
            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "surya" , "surya");
        }


        Toast.makeText(UnityPlayerActivity.this,"Image Saved Successfully",Toast.LENGTH_SHORT).show();


        ImageView close = (ImageView)dialogexit.findViewById(R.id.close);

        close.setVisibility(View.VISIBLE);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialogexit.dismiss();
            }
        });



        share.setVisibility(View.GONE);


        Log.v("SURYA",""+videoPath);



        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap bitmap = getBitmapFromView(imageCapture);
                try {
                    FileOutputStream fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    file.setReadable(true, false);
                    final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    intent.setType("image/png");
                    startActivity(Intent.createChooser(intent, "Share image via"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                dialogexit.dismiss();

            }
        });



       /* Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialogexit.dismiss();



            }
        },2000);*/


        dialogexit.show();

    }


    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }





/*
    // When Unity player unloaded move task to background
    @Override public void onUnityPlayerUnloaded() {
        moveTaskToBack(true);
    }

    // When Unity player quited kill process
    @Override public void onUnityPlayerQuitted() {
        Process.killProcess(Process.myPid());
    }*/

    @Override protected void onNewIntent(Intent intent) {
        // To support deep linking, we need to make sure that the client can get access to
        // the last sent intent. The clients access this through a JNI api that allows them
        // to get the intent set on launch. To update that after launch we have to manually
        // replace the intent with the one caught here.
        super.onNewIntent(intent);
        //setIntent(intent);
        mUnityPlayer.newIntent(intent);
    }

    // Quit Unity
    @Override protected void onDestroy ()
    {
        mUnityPlayer.destroy();
        super.onDestroy();
    }

    // Pause Unity
    @Override protected void onPause()
    {
        super.onPause();
        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override protected void onResume()
    {
        super.onResume();
        mUnityPlayer.resume();
    }

    // Low Memory Unity
    @Override public void onLowMemory()
    {
        super.onLowMemory();
        mUnityPlayer.lowMemory();
    }

    // Trim Memory Unity
    @Override public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL)
        {
            mUnityPlayer.lowMemory();
        }
    }

    // This ensures the layout will be correct.
    @Override public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override public boolean onKeyUp(int keyCode, KeyEvent event)     { return mUnityPlayer.injectEvent(event); }
    @Override public boolean onKeyDown(int keyCode, KeyEvent event)   {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            UnityPlayerActivity.this.finish();
        }
        return mUnityPlayer.injectEvent(event); }
    @Override public boolean onTouchEvent(MotionEvent event)          { return mUnityPlayer.injectEvent(event); }
    /*API12*/ public boolean onGenericMotionEvent(MotionEvent event)  { return mUnityPlayer.injectEvent(event); }


    public void ShowWebView(String url){

        LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        View view = inflater.inflate(R.layout.layout_openwebview, null);
        final BottomSheetDialog bottomSheetDialogChangeProfile = new BottomSheetDialog(UnityPlayerActivity.this);
        bottomSheetDialogChangeProfile.setContentView(view);

        WebView webView = (WebView)bottomSheetDialogChangeProfile.findViewById(R.id.webView);

        ImageView close = (ImageView)bottomSheetDialogChangeProfile.findViewById(R.id.close);

        MyLiteTextView urlText = (MyLiteTextView)bottomSheetDialogChangeProfile.findViewById(R.id.text);


        urlText.setText(url);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogChangeProfile.dismiss();
            }
        });

        webView.loadUrl(url);

        bottomSheetDialogChangeProfile.show();

    }




}
