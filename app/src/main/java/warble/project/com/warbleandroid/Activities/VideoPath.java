package warble.project.com.warbleandroid.Activities;

import android.util.Log;

public class VideoPath {

    public static String path;
    public static String title;
    private static int position;
    public static int duration;
    public static int postDuration;

    public static String getTitle() {
        return title;
    }

    public static void setTitle(String title) {
        VideoPath.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        VideoPath.path = path;
    }

    public static int getPosition() {
        return position;
    }

    public static void setPosition(int position) {
        VideoPath.position = position;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        VideoPath.duration = duration;
    }

    public int getPostDuration() {
        Log.e("duration", "getPostDuration: "+postDuration);
        return postDuration;
    }

    public void setPostDuration(int postDuration) {
        Log.e("duration", "setPostDuration: "+postDuration);
        VideoPath.postDuration = postDuration;
    }
}
