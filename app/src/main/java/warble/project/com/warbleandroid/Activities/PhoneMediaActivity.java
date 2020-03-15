package warble.project.com.warbleandroid.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import warble.project.com.warbleandroid.R;

public class PhoneMediaActivity extends Activity {
    Cursor cursor;
    ListView listView;
    SwipeRefreshLayout swipeRefreshLayout;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    ArrayList<String> videopath;
    ArrayList<String> videotitle;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorBackground));
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cursor.close();
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_phone_media);

        listView = this.findViewById(R.id.ListView);
        swipeRefreshLayout=this.findViewById(R.id.refresh);

        if(checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }

        VideoList();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getApplicationContext(),"Refreshing...",Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        VideoList();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long ID) {
                VideoPath vp = new VideoPath();
                vp.setPath(videopath.get(position));
                VideoPath.setTitle(videotitle.get(position));
                VideoPath.setPosition(position);

                Intent intent = new Intent(getApplicationContext(), VideoPlayer.class);
                startActivity(intent);
            }
        });

    }

    public void VideoList()
    {
        String[] mediaColumns = { MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA, MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE };

        //Cursor for queries
        cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI ,
                mediaColumns, null, null, MediaStore.Video.VideoColumns.TITLE + " ASC");

        ArrayList<VideoViewInfo> videoRows = new ArrayList<>();
        videopath=new ArrayList<>();
        videotitle=new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {

                VideoViewInfo newVVI = new VideoViewInfo();
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID));

                ContentResolver crThumb = getContentResolver();
                BitmapFactory.Options options=new BitmapFactory.Options();
                options.inSampleSize = 1;
                newVVI.thumbPath= MediaStore.Video.Thumbnails.getThumbnail(crThumb, id, MediaStore.Video.Thumbnails.MINI_KIND,
                        options);

                newVVI.filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                newVVI.title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                newVVI.mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));

                File videoFile=new File(newVVI.filePath);
                if(videoFile.length()!=0 && videoFile.exists()) {
                    videopath.add(newVVI.filePath);
                    videotitle.add(newVVI.title);
                    videoRows.add(newVVI);
                }

            } while (cursor.moveToNext());
        }
        listView.setAdapter(new VideoGalleryAdapter(this, videoRows));

    }
}
