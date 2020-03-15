package warble.project.com.warbleandroid.Utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import warble.project.com.warbleandroid.R;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.Data> {

    @NonNull
    @Override
    public DiscoverAdapter.Data onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_discover_adapter,null);

        return new Data(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoverAdapter.Data data, int i) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class Data extends RecyclerView.ViewHolder {
        VideoView videoView;
        public Data(@NonNull View itemView) {
            super(itemView);

            videoView = itemView.findViewById(R.id.myvideoview);


        }
    }
}
