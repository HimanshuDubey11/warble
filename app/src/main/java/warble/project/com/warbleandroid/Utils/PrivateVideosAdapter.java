package warble.project.com.warbleandroid.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import warble.project.com.warbleandroid.R;

public class PrivateVideosAdapter extends RecyclerView.Adapter<PrivateVideosAdapter.Video> {


    Context context;
    ArrayList<VideoData> myDataArrayList;
    public static VideoData videoData;
    DatabaseReference reference;

    public PrivateVideosAdapter(Context context, ArrayList<VideoData> myDataArrayList) {
        this.context = context;
        this.myDataArrayList = myDataArrayList;
    }

    @NonNull
    @Override
    public PrivateVideosAdapter.Video onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_private_videos_adapter,null);
        return new PrivateVideosAdapter.Video(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrivateVideosAdapter.Video video, int i) {

        Picasso.get().load(myDataArrayList.get(i).getVideoThumbnail()).fit().centerCrop().into(video.profilevidimage);
        video.profilevidname.setText(myDataArrayList.get(i).getVideoName());

    }

    @Override
    public int getItemCount() {
        return myDataArrayList.size();
    }

    public class Video extends RecyclerView.ViewHolder {
        ImageView profilevidimage;
        TextView profilevidname;
        public Video(@NonNull View itemView) {
            super(itemView);

            profilevidimage = itemView.findViewById(R.id.profilevidimage);
            profilevidname = itemView.findViewById(R.id.profilevidname);

        }
    }
}
