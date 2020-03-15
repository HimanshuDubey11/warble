package warble.project.com.warbleandroid.Activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import warble.project.com.warbleandroid.R;
import warble.project.com.warbleandroid.Utils.FeedsAdapter;
import warble.project.com.warbleandroid.Utils.FeedsVideoViewDataAdapter;
import warble.project.com.warbleandroid.Utils.VideoData;

public class FeedsVideoViewData extends AppCompatActivity {


    VideoData videoData;
    VideoView videoView;
    RelativeLayout videoRelativeLayout;
    RecyclerView videoDataRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds_video_view_data);

        videoData = FeedsAdapter.videoData;

        videoView = findViewById(R.id.feedsvideoviewdata);
        videoRelativeLayout = findViewById(R.id.videorelative);
        videoDataRecyclerView = findViewById(R.id.feedsvideoviewrecycle);


        videoView.setVideoURI(Uri.parse(videoData.getVideoUrl()));
        videoView.start();

        videoRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
//
//        videoDataRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        videoDataRecyclerView.setAdapter(new FeedsVideoViewDataAdapter());

    }
}
