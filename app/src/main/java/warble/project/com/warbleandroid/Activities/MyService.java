package warble.project.com.warbleandroid.Activities;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class MyService extends Service {

    MediaPlayer mediaPlayer;
    VideoPath vp=new VideoPath();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mediaPlayer=MediaPlayer.create(getApplicationContext(), Uri.parse(vp.getPath()));
        Log.e("Service", "onStartCommand: "+vp.getDuration());
        mediaPlayer.seekTo(vp.getDuration());
        mediaPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Service", "onDestroy: "+mediaPlayer.getCurrentPosition());
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        vp.setPostDuration(mediaPlayer.getCurrentPosition());
    }
}
