package warble.project.com.warbleandroid.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import warble.project.com.warbleandroid.Activities.FeedsActivity;
import warble.project.com.warbleandroid.Activities.FeedsVideoViewData;
import warble.project.com.warbleandroid.R;
import warble.project.com.warbleandroid.models.User;

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.Data> {

    Context context;
    ArrayList<VideoData> myDataArrayList;
    public static VideoData videoData;
    DatabaseReference reference;

    public FeedsAdapter(Context context, ArrayList<VideoData> myDataArrayList) {
        this.context = context;
        this.myDataArrayList = myDataArrayList;
    }

    @NonNull
    @Override
    public FeedsAdapter.Data onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_feeds_adapter,null);
        return new Data(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FeedsAdapter.Data data, final int i) {

//        data.imageView.setVideoURI(Uri.parse(myDataArrayList.get(i).getVideoUrl()));

        Picasso.get().load(myDataArrayList.get(i).getVideoThumbnail()).fit().centerCrop().into(data.imageView);

        data.titletext.setText(myDataArrayList.get(i).getVideoName());
        data.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoData = myDataArrayList.get(i);
                view.getContext().startActivity(new Intent(view.getContext(),FeedsVideoViewData.class));
            }
        });
        data.titletext.setText(myDataArrayList.get(i).getVideoName());

        data.songName.setText(myDataArrayList.get(i).getVideoName());
        reference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = reference.orderByChild("uId").equalTo(myDataArrayList.get(i).getuserId());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    User user = postSnapshot.getValue(User.class);

                    data.singerName.setText(user.getuName());
                    Picasso.get().load(user.getuImage()).fit().centerCrop().into(data.relative1profile);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return myDataArrayList.size();
    }

    public class Data extends RecyclerView.ViewHolder {

        ImageView imageView;
        CircleImageView relative1profile;
        TextView titletext, songName, singerName;
        public Data(@NonNull View itemView) {
            super(itemView);
            singerName = itemView.findViewById(R.id.feedssingername);
            imageView = itemView.findViewById(R.id.relative2video);
            titletext = itemView.findViewById(R.id.relative1text);
            songName = itemView.findViewById(R.id.feedssongname);
            relative1profile = itemView.findViewById(R.id.relative1profile);
        }
    }
}
