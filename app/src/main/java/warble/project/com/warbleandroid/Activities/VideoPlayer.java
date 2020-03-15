package warble.project.com.warbleandroid.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import warble.project.com.warbleandroid.R;


public class VideoPlayer extends AppCompatActivity
{

    VideoView myVideo;
    TextView titleView;
    final VideoPath vp=new VideoPath();
    LinearLayout mainLayout, sizeLayout;
    RelativeLayout mediaUi;
    int audioPlay;
    SharedPreferences.Editor audioPref;
    FrameLayout frameLayout;
    ImageButton ImgBackBtn, playBtn, pauseBtn, rewindBtn, forwardBtn;
    TextView timeView, videoTime;
    private GestureDetectorCompat mDetector;
    int position,position2;
    private SeekBar seekbar;
    CheckBox checkBox;


    @Override
    protected void onStop() {
        super.onStop();
        position2=myVideo.getCurrentPosition();
        if(checkBox.isChecked()) {
            startService(new Intent(getApplicationContext(), MyService.class));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        myVideo.seekTo(position2);
        myVideo.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(checkBox.isChecked())
        {
            stopService(new Intent(getApplicationContext(), MyService.class));
            myVideo.seekTo(vp.getPostDuration());
            myVideo.start();
        }
        else {
            myVideo.seekTo(position2);
            playBtn.setVisibility(View.GONE);
            pauseBtn.setVisibility(View.VISIBLE);
            myVideo.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        position=myVideo.getCurrentPosition();
        vp.setDuration(position);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(checkBox.isChecked())
        {
            stopService(new Intent(getApplicationContext(), MyService.class));
            myVideo.seekTo(vp.getPostDuration());
            myVideo.start();
        }
        else {
            myVideo.seekTo(position);
            myVideo.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoPath.postDuration=0;
        VideoPath.duration=0;
        stopService(new Intent(getApplicationContext(), MyService.class));
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_HEADSETHOOK){
            if(myVideo.isPlaying()){
                myVideo.pause();
                pauseBtn.setVisibility(View.GONE);
                playBtn.setVisibility(View.VISIBLE);
                return true;
            }
            if(!myVideo.isPlaying())
            {
                myVideo.start();
                playBtn.setVisibility(View.GONE);
                pauseBtn.setVisibility(View.VISIBLE);
                seekbar.postDelayed(onEverySecond, 1000);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.videoplayer);

        mainLayout=this.findViewById(R.id.top);
        sizeLayout=this.findViewById(R.id.size_layout);
        ImgBackBtn=this.findViewById(R.id.btn_back);
        frameLayout=findViewById(R.id.frame);
        mediaUi=findViewById(R.id.mediaControls);

        myVideo = findViewById(R.id.videoView);
        titleView=findViewById(R.id.titleview);
        timeView=findViewById(R.id.time);
        seekbar = findViewById(R.id.seekBar);
        videoTime=findViewById(R.id.videoTime);

        playBtn=findViewById(R.id.play);
        pauseBtn=findViewById(R.id.pause);
        rewindBtn=findViewById(R.id.rewind);
        forwardBtn=findViewById(R.id.forward);
        checkBox=findViewById(R.id.checkbox);

        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorBackground));

        SharedPreferences preferences=getApplicationContext().getSharedPreferences("MyPref",0);

        audioPref=preferences.edit();
        audioPref.apply();

        mDetector = new GestureDetectorCompat(this, new Gestures());

        hideSystemUI();

        Log.e("URI", "onCreate: "+vp.getPath());
        Log.e("Video Path", "onCreate: "+vp.getPath());
        Uri uri = Uri.parse(vp.getPath());
        myVideo.setVideoURI(uri);
        playBtn.setVisibility(View.GONE);
        pauseBtn.setVisibility(View.VISIBLE);
        myVideo.start();


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {

           String video=vp.getPath();
            if(video!= null) {
                myVideo.start();
            }

        } else {
            // Request permission from the user
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekbar.removeCallbacks(onEverySecond);
                myVideo.start();
                seekbar.postDelayed(onEverySecond,1000);
                pauseBtn.setVisibility(View.VISIBLE);
                playBtn.setVisibility(View.GONE);
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myVideo.pause();
                playBtn.setVisibility(View.VISIBLE);
                pauseBtn.setVisibility(View.GONE);
            }
        });

        forwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myVideo.seekTo(myVideo.getCurrentPosition()+10000);

                if(playBtn.getVisibility()==View.VISIBLE && !myVideo.isPlaying())
                {
                    pauseBtn.setVisibility(View.VISIBLE);
                    playBtn.setVisibility(View.GONE);
                    myVideo.start();
                    seekbar.postDelayed(onEverySecond, 1000);
                }

                else {
                    myVideo.start();
                    seekbar.postDelayed(onEverySecond, 1000);
                }

                showSystemUI();
            }
        });

        rewindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myVideo.seekTo(myVideo.getCurrentPosition()-10000);

                if(playBtn.getVisibility()==View.VISIBLE && !myVideo.isPlaying())
                {
                    pauseBtn.setVisibility(View.VISIBLE);
                    playBtn.setVisibility(View.GONE);
                    myVideo.start();
                    seekbar.postDelayed(onEverySecond, 1000);
                }
                else {
                    myVideo.start();
                    seekbar.postDelayed(onEverySecond, 1000);
                }

                showSystemUI();
            }
        });


        /*checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("Check", "onCheckedChanged: "+buttonView+" isChecked "+isChecked);
                if(isChecked)
                {
                    audioPref.putInt("play as audio",0);
                    audioPref.commit();
                    Toast.makeText(getApplicationContext(),"You can press Home to enjoy video in background", Toast.LENGTH_LONG).show();
                }
                if(!isChecked)
                {
                    audioPref.putInt("play as audio",1);
                    audioPref.commit();
                    Toast.makeText(getApplicationContext(),"Play as audio feature has been disabled", Toast.LENGTH_LONG).show();
                }
            }
        });*/

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Play as Audio feature has been enabled. " +
                            "You can Press Home Button to enjoy audio in background", Toast.LENGTH_LONG).show();
                    audioPref.putInt("play as audio", 0);
                    audioPref.commit();
                } else if (!checkBox.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Play as Audio feature has been disabled", Toast.LENGTH_LONG).show();
                    audioPref.putInt("play as audio", 1);
                    audioPref.commit();
                }
            }
        });

        myVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                seekbar.setMax(myVideo.getDuration());
                seekbar.postDelayed(onEverySecond, 1000);
            }

        });



        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.removeCallbacks(onEverySecond);
                int currentPosition = myVideo.getCurrentPosition();
                myVideo.seekTo(currentPosition);
                seekBar.postDelayed(onEverySecond,1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBar.removeCallbacks(onEverySecond);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                if(fromUser) {
                    // this is when actually seekbar has been seeked to a new position
                    myVideo.seekTo(progress);
                }
            }
        });

        audioPlay=preferences.getInt("play as audio",0);

        switch (audioPlay)
        {
            case 0:
                checkBox.setChecked(true);
                break;

            case 1:
                checkBox.setChecked(false);
                break;
        }

        myVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d("video", "setOnErrorListener ");
                Toast.makeText(getApplicationContext(),"ERROR PLAYING VIDEO",Toast.LENGTH_LONG).show();
                return true;
            }
        });


        myVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                VideoPath.postDuration=0;
                VideoPath.duration=0;
                finish();
            }
        });
    }

    private Runnable onEverySecond=new Runnable() {

        @Override
        public void run() {

            if(seekbar != null) {
                seekbar.setProgress(myVideo.getCurrentPosition());
            }

            if(myVideo.isPlaying()) {
                int duration=myVideo.getDuration();
                int durationRem=myVideo.getCurrentPosition();
                seekbar.postDelayed(onEverySecond, 1000);
                if(TimeUnit.MILLISECONDS.toHours(duration)==0) {
                    String totalDuration = String.format(Locale.getDefault(), "%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(duration),
                            TimeUnit.MILLISECONDS.toSeconds(duration) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
                    );
                    String currentDuration = String.format(Locale.getDefault(), "%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(durationRem),
                            TimeUnit.MILLISECONDS.toSeconds(durationRem) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(durationRem))
                    );
                    String time = currentDuration + "/" + totalDuration;
                    videoTime.setText(time);
                }
                else
                {
                    String totalDuration = String.format(Locale.getDefault(), "%02d:%02d:%02d",
                            TimeUnit.MILLISECONDS.toHours(duration), TimeUnit.MILLISECONDS.toMinutes(duration),
                            TimeUnit.MILLISECONDS.toSeconds(duration) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
                    );
                    String currentDuration = String.format(Locale.getDefault(), "%02d:%02d:%02d",
                            TimeUnit.MILLISECONDS.toHours(duration), TimeUnit.MILLISECONDS.toMinutes(durationRem),
                            TimeUnit.MILLISECONDS.toSeconds(durationRem) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(durationRem))
                    );
                    String time = currentDuration + "/" + totalDuration;
                    videoTime.setText(time);
                }
            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Uri uri = data.getData();
            try{
                myVideo.setVideoURI(uri);
                myVideo.start();
                hideSystemUI();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

   @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
                // Re-attempt file write
                try {
                    myVideo.start();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
        }
    }


    private void hideSystemUI() {
        mainLayout.setVisibility(LinearLayout.GONE);
        mediaUi.setVisibility(View.GONE);
        titleView.setVisibility(View.GONE);
        sizeLayout.setVisibility(View.GONE);
        timeView.setVisibility(View.GONE);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        SimpleDateFormat format= new SimpleDateFormat("h:mm a",Locale.getDefault());
        String myDate = format.format(new Date());
        timeView.setText(myDate);
        mainLayout.setVisibility(LinearLayout.VISIBLE);
        mediaUi.setVisibility(View.VISIBLE);
        titleView.setVisibility(View.VISIBLE);
        titleView.setText(VideoPath.getTitle());
        sizeLayout.setVisibility(View.VISIBLE);
        timeView.setVisibility(View.VISIBLE);


        final Handler handler = new Handler();
        final Runnable doNextActivity = new Runnable() {
            @Override
            public void run() {
                hideSystemUI();
            }
        };

        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(8000);
                handler.post(doNextActivity);
            }
        }.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        VideoPath.duration=0;
        VideoPath.postDuration=0;
        finish();
    }

    public void onBack(View view)
    {
        VideoPath.duration=0;
        VideoPath.postDuration=0;
        finish();
    }


    public class Gestures extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return super.onDown(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if(myVideo.isPlaying()) {
                hideSystemUI();
                myVideo.pause();
                pauseBtn.setVisibility(View.GONE);
                playBtn.setVisibility(View.VISIBLE);
            }

            else {
                hideSystemUI();
                playBtn.setVisibility(View.GONE);
                pauseBtn.setVisibility(View.VISIBLE);
                seekbar.removeCallbacks(onEverySecond);
                myVideo.start();
                seekbar.postDelayed(onEverySecond,1000);
            }
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

           if(mainLayout.getVisibility()==View.VISIBLE && mediaUi.getVisibility()==View.VISIBLE)
           {
               hideSystemUI();
           }

           else
           {
               showSystemUI();
           }
            return super.onSingleTapConfirmed(e);
        }
    }
}